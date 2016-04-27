package main;

import cmd.DriveCMD;
import cmd.TurnCMD;
import comm.BluetoothThread;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

/**
 * The TaskManager is the Main Thread that coordinates the switch between Driving, Balancing, Turning.
 * @author Philipp, Pascal, Daniel
 *
 */

public class TaskManager {

	// Constants
		
	final static double K_ANGULAR_VELO = 0.33; //0.4 Angular Velocity
	final static double K_ANGLE = 8;//10 Angle
	final static double K_MOTORPOS =0.18;//0.2;// Motorposition
	final static double K_MOTORSPEED =0.18;//0.2 Motorspeed
	
	final static double ALPHA = 0.0005; //Alpha for EMA	
	final static int DELAY = 300; //delay for the menu
	
	
	static int menuPoint = 1;
	static EmergencyThread emergency;
	static BalanceHandler handler;
	static Segway segway;
	
	public static String state = "calibrate";
	
	public void start(){
		//TODO Loading K Values from File
		
		handler = new BalanceHandler(K_ANGULAR_VELO, K_ANGLE, K_MOTORPOS, K_MOTORSPEED);
		
		segway = new Segway(handler,ALPHA, false);
		BluetoothThread bluetooth = new BluetoothThread(segway,false);
		bluetooth.start();
		
		segway.init();
		segway.start();
		
		emergency = new EmergencyThread();


		
		Delay.msDelay(3000);
//		segway.startForward(50);
//		Delay.msDelay(10000);
//		segway.setSpeed(0);
//		segway.startForward(50);
//		Delay.msDelay(10000);
//		segway.setSpeed(100);
//		segway.startForward(50);

//		segway.startForward(300);
//		segway.startBackward(2000);
//		segway.startRight(180);
//		segway.startLeft(180);
		

//		try {
//			bluetooth.PathThread.addCMD(new DriveCMD(300));
//			bluetooth.PathThread.addCMD(new TurnCMD(90));
//			bluetooth.PathThread.addCMD(new DriveCMD(300));
//			bluetooth.PathThread.addCMD(new TurnCMD(-30));
//			bluetooth.PathThread.addCMD(new DriveCMD(300));
//			bluetooth.PathThread.addCMD(new TurnCMD(-60));
//			bluetooth.PathThread.addCMD(new DriveCMD(300));
//			
//		} catch (PathIsRunning e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		bluetooth.PathThread.start();
//		 


	}
	
	
	static boolean endEmergency = false;
	
	public static void emergency(){
		endEmergency=false;
		while(true){

			if(!emergency.getStop()){
				emergency = new EmergencyThread();
				emergency.start();
			}
			showBrickManager();
			if(endEmergency)return;
			LCD.clear();
		}
	}

		
	public static void restart(){
		emergency.stop();
		LCD.clear();		
		endEmergency=true;
		state="restart";
		segway.init();	
		segway.start();
	}
	
	public static void reset(){
		emergency.stop();
		LCD.clear();				
		segway.init();
		endEmergency=true;
		state="reset";
		handler.restart();
		segway.start();
		
	}
		/*
		 * shows the brick manager to edit values on the fly
		 */
		
		public static void showBrickManager() {
			while(true){
				showData();

				if(state.equals("reset")) break;
				if(state.equals("restart")) break;
				if(state.equals("exit")) break;
				
				if(Button.ENTER.isDown()) {
					while(Button.ENTER.isDown());
					editMenuPoint(menuPoint);
				}
				else if(Button.RIGHT.isDown()){
					menuPoint++;
					if(menuPoint>=7) menuPoint=7;
					while(Button.RIGHT.isDown());
				} 
				else if(Button.LEFT.isDown()){
					menuPoint--;
					if(menuPoint<=1) menuPoint=1;
					while(Button.LEFT.isDown());
				}
			}
		}
		
		/*
		 * prints lots of the changeable data
		 */

		public static void showData() {
			LCD.clear();
			LCD.drawString("X", 1, menuPoint);
			LCD.drawString("Reset", 2, 5);
			LCD.drawString("Neustart", 2, 6);
			LCD.drawString("Exit", 2, 7);
			LCD.drawString("K1: "+ handler.getK_ANGULAR_VELO(), 2, 1);
			LCD.drawString("K2: " + handler.getK_ANGLE(), 2, 2);
			LCD.drawString("K3: " + handler.getK_MOTORPOS(), 2, 3);
			LCD.drawString("K4: " + handler.getK_MOTORSPEED(), 2, 4);
			Delay.msDelay(200);
		}
		
		/*
		 * For editing a menu point by pressing a button
		 */
		
		static void editMenuPoint(int menuPoint) {
			switch(menuPoint){
				case 1:
					while(Button.ESCAPE.isUp()){
						if(Button.RIGHT.isDown()) {
							handler.setK_ANGULAR_VELO(handler.getK_ANGULAR_VELO()+0.01);
							Delay.msDelay(DELAY);
						}
						else if(Button.LEFT.isDown()) {
							handler.setK_ANGULAR_VELO(handler.getK_ANGULAR_VELO()-0.01);
							Delay.msDelay(DELAY);
						}
						showData();
					}
					break;
				case 2:
					while(Button.ESCAPE.isUp()){
						if(Button.RIGHT.isDown()) {
							handler.setK_ANGLE(handler.getK_ANGLE()+0.1);
							Delay.msDelay(DELAY);
						}
						else if(Button.LEFT.isDown()) {
							handler.setK_ANGLE(handler.getK_ANGLE()-0.1);
							Delay.msDelay(DELAY);
						}
						showData();
					}
					break;
				case 3:
					while(Button.ESCAPE.isUp()){
						if(Button.RIGHT.isDown()) {
							handler.setK_MOTORPOS(handler.getK_MOTORPOS()+0.01);
							Delay.msDelay(DELAY);
						}
						else if(Button.LEFT.isDown()) {
							handler.setK_MOTORPOS(handler.getK_MOTORPOS()-0.01);
							Delay.msDelay(DELAY);
						}
						showData();
					}
					break;
				case 4:
					while(Button.ESCAPE.isUp()){
						if(Button.RIGHT.isDown()) {
							handler.setK_MOTORSPEED(handler.getK_MOTORSPEED()+0.01);
							Delay.msDelay(DELAY);
						}
						else if(Button.LEFT.isDown()) {
							handler.setK_MOTORSPEED(handler.getK_MOTORSPEED()-0.01);
							Delay.msDelay(DELAY);
						}
						showData();
					}
					break;
				case 5:
					reset();
					break;
				case 6:
					restart();
					break;
				case 7:
					state=("exit");
					break;
			}
		}
		
}
