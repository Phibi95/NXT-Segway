package main;
import navigation.Coordinates;
import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.GyroSensor;
import lejos.util.Delay;

/*
 * author: Pascal, Daniel, Philipp
 */

public class Segway extends Thread{
	

	final GyroSensor gyro;
	final NXTMotor LEFT_MOTOR, RIGHT_MOTOR, LED;
	ObstacleChecker checker;
	
	//constants
	final static int DELAY = 1;
	final static int SPEED_PARAMETER = 55;
	final static double CRITICAL_DISTANCE=17;
	
	//booleans for control
	boolean balance=true;
	boolean forward=false;
	boolean goRight = false;
	boolean goLeft = false;
	boolean backward = false;
	boolean init=true;
	
	//angle
	double angle;
	double rawAngle;
	
	//distance 
	float sonicDistance=0;
	
	//degree
	private double posDegree;
	double degLeft, degRight; 
	boolean remote;
	
	//shit
	int tachoTarget=0;
	int offsetLeft=0;
	int offsetRight=0;
	int posOffsetL = 0;
	int posOffsetR = 0;
	int range;

	
	//tacho
	double tachoTargetLeft=0;
	double tachoTargetRight=0;
	int oldTacho, tacho;
	
	int[] tachoArrayRight = new int[12];
	int[] tachoArrayLeft = new int[12];
	
	//time
	double timeDiff;
	long timestamp;
	long timestamp2;
	long[] timeArray = new long[12];
	int pointer = 0;

	//offset
	double offset = 0;
	double alpha=0.0001;
	
	//power
	int pwrRight=0;
	int pwrLeft=0;

	//speed
	
	int speed=40;
	//handler
	BalanceHandler handler;

	//speed
	double gyroSpeed=0;
	double motorSpeedR=0;
	double motorSpeedL=0;
	
	//amount of measurements for the calibration
	final int MEASUREMENTS = 1000;
	
	boolean running = true;
	boolean DEBUG_MODE;
	
	WaitThread waiter;
	Coordinates position;
	
	/** This is the heart of the Robot. This Thread is running and calculating all the time.
	 * @author Philipp, Pascal, Daniel, Markus
	 * @param handler
	 * @param alpha
	 * @param DEBUG
	 */
	
	public Segway(BalanceHandler handler, double alpha, boolean DEBUG){
		
		gyro = new GyroSensor(SensorPort.S4);
		
		RIGHT_MOTOR = new NXTMotor(MotorPort.A);
		LEFT_MOTOR = new NXTMotor(MotorPort.B);
		LED = new NXTMotor(MotorPort.C);
		
		LEFT_MOTOR.resetTachoCount();
		RIGHT_MOTOR.resetTachoCount();
		
		gyro.setOffset(0);

		LEFT_MOTOR.setPower(0);
		RIGHT_MOTOR.setPower(0);
		
		
		this.handler = handler;
		this.alpha = alpha;
		this.DEBUG_MODE = DEBUG;
		
		for(int i= 0; i<tachoArrayLeft.length; i++) {
			tachoArrayLeft[i]=0;
			tachoArrayRight[i]=0;
			timeArray[i]=0;
		}
		
		waiter= new WaitThread(500);
	}
	
	/*
	 * Initiliazes all the values and resets them and a new start
	 */
	
	public void init(){
		//enter critical section
		init=true;
		
		//calibration
		LCD.clear();
		LCD.drawString("Put down Robot", 1, 2);
		LCD.drawString("& press button.", 1, 3);
		Button.waitForAnyPress();
		Delay.msDelay(1000);
		LCD.clear();
		LCD.drawString("Calibration", 1, 3);
	
		//offset calculation
		double gesamt = 0;
		gyro.setOffset(0);
		for (int i = 0; i < MEASUREMENTS; i++) {
			gesamt += gyro.getAngularVelocity();
		}
		offset = (gesamt / MEASUREMENTS)+1;
		
		LCD.clear();
		LCD.drawString("Calibration", 1, 2);
		LCD.drawString("done!", 1, 3);
	
		//reset power values
		LEFT_MOTOR.setPower(0);
		RIGHT_MOTOR.setPower(0);
		LEFT_MOTOR.resetTachoCount();
		RIGHT_MOTOR.resetTachoCount();
		
		//reset power
		pwrLeft=0;
		pwrRight=0;
		
		//reset motorspeed
		motorSpeedL=0;
		motorSpeedR=0;
		
		//reset angle values
		rawAngle=0.0; 
		gyroSpeed=0.0;
		angle = 0.0;
		
		
		//time and tacho
		for(int i= 0; i<tachoArrayLeft.length; i++) {
			tachoArrayLeft[i]=0;
			tachoArrayRight[i]=0;
			timeArray[i]=0;
		}
		
		sonicDistance=0;
		speed=30;
		tachoTargetLeft=0;
		tachoTargetRight=0;
		degLeft=80;
		degRight=0;
		posDegree=0;
		goRight=false;
		goLeft=false;
		forward=false;
		
		//end calibrations
		lejos.nxt.Sound.beep();
		LCD.clear();
		LCD.drawString("Put robot up", 1, 3);
		LCD.drawString("and press", 1, 4);
		LCD.drawString("any button.", 1, 5);
		Button.waitForAnyPress();
		LCD.clear();
		
		timestamp2 = System.nanoTime();
		timestamp = timestamp2;
		
		position = new Coordinates();
		
		//end critical section
		init=false;
		balance=true;
		//start a new obstaclechecker
		
				if(checker!=null)checker.kill();
				checker = new ObstacleChecker(this);
				checker.start();
				
	}
	
	/*
	 * The Thread for calculating all the values and balance/drive
	 */
	int posR = 0;
	int posL = 0;
	int oldR =0;
	int oldL =0;
	int distance=0;
	double motorSpeed;
	
	public void run(){
		while(true){			
			//refresh the time
			long timeStamp2 = System.nanoTime(); // in ns
			int cycleTime  = (int) ((timeStamp2 - timestamp)/1000.0); // in ms
			timeDiff = cycleTime;
			timestamp = timeStamp2;
			
			
			
			Delay.msDelay(DELAY);
			oldR=posR;
			oldL=posL;
			posR = RIGHT_MOTOR.getTachoCount();
			posL = LEFT_MOTOR.getTachoCount();
			//integrate the angle
			
			motorSpeedR = (posR - tachoArrayRight[pointer])/((timestamp - timeArray[pointer])/1000000000.0);
			motorSpeedL = (posL - tachoArrayLeft[pointer])/((timestamp - timeArray[pointer])/1000000000.0);
			motorSpeed = (motorSpeedR+motorSpeedL)/2;
			
			tacho=(posR+posL)/2;
			
			tachoArrayLeft[pointer] = posL;
			tachoArrayRight[pointer] = posR;
			timeArray[pointer] = timestamp;
			pointer = (++pointer) % timeArray.length;
			
			//save the actual angle
			rawAngle= gyro.getAngularVelocity();
			gyroSpeed = handler.gyroSpeed(rawAngle, offset);
			// EMAOFFSET Calculation
			offset = alpha * rawAngle + (1 - alpha) * offset;
			
			angle = handler.integrate(angle, cycleTime, gyroSpeed);

			//print the values
			//statusOutput();
			
			if(forward) forward();
			if(backward) backward();
			if(goRight) right();
			if(goLeft) left();		
			if(balance) balance();
			if(balance) updatePower();
		}
	}
	
	/*
	 * Functions that control the Segway
	 * 
	 *  authors: Pascal & Daniel
	 */
	
	/*
	 * calculates the power for balancing and checks for emergency
	 */
	
	public void balance(){
		if(angle >= 70 || angle <=-70) {
			LEFT_MOTOR.setPower(0);
			RIGHT_MOTOR.setPower(0);
			balance=false;
			checker.kill();
			TaskManager.emergency();
			statusOutput();
			}
		pwrRight = handler.calcMotorPower(gyroSpeed, angle, tacho-tachoTargetRight, motorSpeed);
		pwrLeft = handler.calcMotorPower(gyroSpeed, angle, tacho-tachoTargetLeft, motorSpeed);
	}
	
	/*
	 * Updates the Power for the different motors
	 */
	
	public void updatePower(){
		if(posR-posOffsetR > posL-posOffsetL) {
			pwrRight-=3;
			pwrLeft+=3;
		}
		else if(posR-posOffsetR < posL-posOffsetL) {
			pwrRight+=3;
			pwrLeft-=3;
		}
		LEFT_MOTOR.setPower(pwrLeft+offsetLeft);
		RIGHT_MOTOR.setPower(pwrRight+offsetRight);
	}

	
	
	/**
	 * Function to drive forward, using an offset on the motorposition. Drives in several 500 degree parts
	 * @author: Pascal, Daniel
	 */

	
	public void forward(){	
		//calculate driven distance
		tachoTarget+=(posR-oldR);
		distance+=(posR-oldR);
		
		//stop if robot has reached his target
		if(tachoTarget>=range){
			//lejos.nxt.Sound.twoBeeps(); //Beeps kills the robot, dont do it
			stopForward();
			return;
		}
		
		//wait so the tacho iteration is not too high
		if(waiter.wait) return;
		waiter.kill();
		//increase tacho so the robot does drive too compensate the sudden tacho change
		tachoTargetRight+=30+speed;		
		tachoTargetLeft+=30+speed;
		//start the wait
		waiter=new WaitThread(250);
		waiter.start();
	}
		
	public void startForward(int range) {
		if(backward) return;
		forward = true;
		this.range=handler.getDriveDegrees(range); 
		distance=0;
		//lejos.nxt.Sound.beep();
		tachoTarget=0;
	}
	
	public void stopForward() {
		stopMovement();
		forward = false;
	}
	
	public void startBackward(int range) {
		if(backward) return;
		backward=true;
		this.range=handler.getDriveDegrees(range);
		//this.range=range;
		distance=0;
		//lejos.nxt.Sound.beep();
		tachoTarget=0;
	}
	
	public void backward(){
		//calculate driven distance
		tachoTarget+=(oldR-posR);
		distance-=(oldR-posR);
		
		//stop if robot has reached his target
		if(tachoTarget>=range){
			//lejos.nxt.Sound.twoBeeps(); //Beeps kills the robot, dont do it
			backward=false;
			return;
		}
		
		//wait so the tacho iteration is not too high
		if(waiter.wait) return;
		waiter.kill();
		//increase tacho so the robot does drive too compensate the sudden tacho change
		tachoTargetRight-=(50+speed);		
		tachoTargetLeft-=(50+speed);
		//start the wait
		waiter=new WaitThread(300);
		waiter.start();
	}
	
	public void stopBackward(){
		backward = false;
		stopMovement();
	}

	/*
	 * takes a value between 0-100 and changes it to a working speed offset 
	 */
	
	public void setSpeed(int inputSpeed){
		if(inputSpeed>100)inputSpeed=100;
		if(inputSpeed<0)inputSpeed=0;
		speed = (inputSpeed*SPEED_PARAMETER)/100;
		//speed = inputSpeed;
	}
	
	
	/*
	 * goes to absolute degree
	 * 
	 * takes degree from 0 to 360, add exception here
	 */
	
	public void startDegreeAbsolut(int degree){
		if(degree <= 0) degree =0;
		if(degree >=360) degree=360;		
		double tempDegree = getPosDegree() % 360;
		double targetDegree = (tempDegree-degree)%360;
		if(targetDegree<0)targetDegree=360+targetDegree;
		if(targetDegree<180)startLeft((int)targetDegree);
		else startRight(360-(int)targetDegree);
	}
	
	/*
	 * starts right turn
	 * 
	 * input: degree
	 */
	public void startRight(int degree){
		if(goRight) return;
		if(goLeft) return;
		degRight=posDegree+degree;
		tachoTargetLeft-=150;
		goRight=true;
	}
	
	public void right(){
		posDegree-=handler.getTurnDegrees(posR-oldR);
		posDegree+=handler.getTurnDegrees(posL-oldL);
		if(degRight<=0.958*posDegree) stopRight();
	}
	
	public void stopRight(){
		tachoTargetLeft+=150;
		goRight=false;
		stopMovement();
	}
	
	/*
	 * starts left turn
	 * 
	 * input:degree
	 */
	
	public void startLeft(int degree){
		if(goLeft) return;
		if(goRight) return;
		degLeft=posDegree-degree;
		tachoTargetRight-=150;
		goLeft=true;
	}

	public void left(){
		posDegree-=handler.getTurnDegrees(posR-oldR);
		posDegree+=handler.getTurnDegrees(posL-oldL);
		if(degLeft>=0.958*posDegree) stopLeft();
	}
	
	public void stopLeft(){
		tachoTargetRight+=150;
		goLeft=false;
		stopMovement();
	}
	
	public  synchronized void  stopMovement(){
		//lejos.nxt.Sound.beep();
		posOffsetL=posL;
		posOffsetR=posR;
		position.updateCoordiantes(distance, posDegree);
		distance=0;
		stopRemote();
	}
	
	public void checkObstacle(double sensorValue){
		if(sensorValue<CRITICAL_DISTANCE){
			stopMovement();
			goLeft=false;
			goRight=false;
			forward=false;
			backward=false;
		}
	}
	public void startRemote(){
		remote=true;
	}
	
	public void stopRemote(){
		remote=false;
	}
	
	boolean[] pause=new boolean[4];
	
	public  synchronized void pause(){
		pause[0]=forward;
		pause[1]=goLeft;
		pause[2]=goRight;
		pause[3]=backward;
		forward=false;
		goLeft=false;
		goRight=false;
		backward=false;
	}
	
	public void resume(){
		forward=pause[0];
		goLeft=pause[1];
		goRight=pause[2];
		backward=pause[3];
	}
	
	/*
	 * stops this tread
	 */
	public void stop(){
		this.running=false;
	}

	/**
	 * For development only... Writes the values. Is only active if DEBUG_MODE is set to true
	 */
	
	private void statusOutput() {
		LCD.drawString("posDeg: " + posDegree, 1, 1);
		LCD.drawString("posX: " + getPosX(), 1, 2);
		LCD.drawString("posY: " + getPosY(), 1, 3);
		LCD.drawString("TL: " + posL, 1, 4);
		LCD.drawString("TR: " + posR, 1, 5);
		LCD.drawString("offset: " + offset, 1, 6);
	}

	//Getter and Setter
		public int getMotorCountLeft(){
			return LEFT_MOTOR.getTachoCount();
		}
		
		public int getMotorCountRight(){
			return RIGHT_MOTOR.getTachoCount();
		}
		
		public void setPowerRightMotor(int power){
			RIGHT_MOTOR.setPower(power);
		}
		
		public void setPowerLeftMotor(int power){
			LEFT_MOTOR.setPower(power);
		}
		public void disableDebug() {
			DEBUG_MODE=false;
		}
		
		public void enableDebug() {
			DEBUG_MODE=true;
		}
	
		public boolean isBalancing() {
			return balance;
		}
	
		public double getAngle() {
			return angle;
		}
		
		public void setAngle(double angle){
			this.angle= angle;
		}

		public double getOffset() {
			return offset;
		}

		public void setOffset(double offset){		
			this.offset = offset;
		}

		public double getAlpha() {
			return alpha;
		}

		public void setAlpha(double alpha) {
			if(alpha > 1 || alpha < 0) throw new RuntimeException();
			this.alpha = alpha;
		}

		public int getPwr() {
			return pwrLeft;
		}

		public void setPwr(int pwr) {
			pwrLeft = pwr;
			pwrRight = pwr;
		}

		public double getTacho() {
			return tachoArrayLeft[0];
		}

		public double getGyroSpeed() {
			return gyroSpeed;
		}

		public double getMotorspeed() {
			return (motorSpeedR+motorSpeedL)/2;
		}

		public void setLight(int power) {
			LED.setPower(power);
		}
		
		public int getLight() {
			return LED.getPower();
		}
		
		public int getPwrRight() {
			return pwrRight;
		}

		public void setPwrRight(int pwrRight) {
			this.pwrRight = pwrRight;
		}

		public int getPwrLeft() {
			return pwrLeft;
		}

		public void setPwrLeft(int pwrLeft) {
			this.pwrLeft = pwrLeft;
		}
		
		public float getRawAngle(){
			return gyro.getAngularVelocity();
		}
		
		public float getAkkuspannung(){
			return Battery.getVoltage();
		}
		
		//BalanceHanlder Getter and Setter
		
		//Getter and Setter
		public double getK_ANGULAR_VELO() {
			return this.handler.getK_ANGULAR_VELO();
		}


		public void setK_ANGULAR_VELO(double k_ANGULAR_VELO) {
			this.handler.setK_ANGULAR_VELO(k_ANGULAR_VELO);
		}


		public double getK_ANGLE() {
			return this.handler.getK_ANGLE();
		}


		public void setK_ANGLE(double k_ANGLE) {
			this.handler.setK_ANGLE(k_ANGLE);
		}


		public double getK_MOTORPOS() {
			return this.handler.getK_MOTORPOS();
		}


		public void setK_MOTORPOS(double k_MOTORPOS) {
			this.handler.setK_MOTORPOS(k_MOTORPOS);
		}


		public double getK_MOTORSPEED() {
			return this.handler.getK_MOTORSPEED();
		}


		public void setK_MOTORSPEED(double k_MOTORSPEED) {
			this.handler.setK_MOTORSPEED(k_MOTORSPEED);
		}

		public double getPosDegree() {
			return posDegree;
		}

		public void setPosDegree(double posDegree) {
			this.posDegree = posDegree;
		}
		
		
		public synchronized boolean isForward() {
			return forward;
		}

		public void setForward(boolean forward) {
			this.forward = forward;
		}

		public synchronized boolean isGoRight() {
			return goRight;
		}

		public void setGoRight(boolean goRight) {
			this.goRight = goRight;
		}

		public synchronized boolean isGoLeft() {
			return goLeft;
		}

		public void setGoLeft(boolean goLeft) {
			this.goLeft = goLeft;
		}

		public boolean isBackward() {
			return backward;
		}

		public void setBackward(boolean backward) {
			this.backward = backward;
		}
		
		public int getPosX(){
			if(position==null) return 0;
			return position.getPosX();
		}
		
		public int getPosY(){
			if(position==null) return 0;
			return position.getPosY();
		}
		
		public int getUltrasonicValue(){
			if(this.checker==null){
				return 0;
			} else {
				return this.checker.getValue();
			}
		}
}
