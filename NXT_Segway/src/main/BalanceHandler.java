package main;

/*
 * author:Pascal
 */

public class BalanceHandler {
	
	double K_ANGULAR_VELO; // Angular Velocity
	double K_ANGLE; // Angle
	double K_MOTORPOS; // Motorposition
	double K_MOTORSPEED; // Motorspeed
	
	double backup_angular_velo, backup_angle, backup_motorpos, backup_motorspeed;

	public BalanceHandler(double k_angular_velo, double k_angle, double k_motorpos, double k_motorspeed){
		this.K_ANGULAR_VELO = k_angular_velo;
		this.K_ANGLE = k_angle;
		this.K_MOTORPOS = k_motorpos;
		this.K_MOTORSPEED = k_motorspeed;
		
		backup_angular_velo = k_angular_velo;
		backup_angle = k_angle;
		backup_motorpos = k_motorpos;
		backup_motorspeed = k_motorspeed;
	}
	
	/*
	 * gets a difference in Tachocount and returns the driven degrees if only one wheel moves
	 * 
	 * first value is wheel diameter, second one is 2*wheel distacne; formula is in wiki
	 * @author Daniel
	 */
	
	public double getTurnDegrees(double tachoDifference){
		return (tachoDifference*8.4)/35;
	}
	
	/*
	 * gets the degree number you need for driving an input of centimeters
	 */
	
	public int getDriveDegrees(int centimeters){
		return (int) (centimeters/(8.4*Math.PI)*360);
	}

	/**
	 * Calculates current speed of the motor
	 * 
	 * @return current motor speed in degrees/second
	 */

	public double motorSpeed(double tacho, double oldTacho, double timeDiff) {
		return (tacho - oldTacho) / timeDiff;
	}

	/**
	 * This method calculates the MotorPower with the given Parameters
	 */

	public int calcMotorPower(double gyroSpeed, double angle, double tacho, double motorSpeed){
		return (int) (K_ANGULAR_VELO * gyroSpeed + K_ANGLE * angle + K_MOTORPOS * tacho + K_MOTORSPEED * motorSpeed);
	}

	/**
	 * This method calculates the current angle
	 */

	public double integrate(double angle, double timeDiff, double gyroSpeed) {
		angle += (timeDiff/1000000)*gyroSpeed;
		return angle;
	}

	/**
	 * This method calculates the true gyro speed (without offset)
	 * 
	 * @return current gyro speed
	 */
	public double gyroSpeed(double rawAngle, double offset) {
		return rawAngle - offset;
	}
	/*
	 * Restart Method, that resets the values to original
	 */
	public void restart() {
		this.K_ANGULAR_VELO = backup_angular_velo;
		this.K_ANGLE = backup_angle;
		this.K_MOTORPOS = backup_motorpos;
		this.K_MOTORSPEED = backup_motorspeed;
	}
	
	//Getter and Setter
	public double getK_ANGULAR_VELO() {
		return K_ANGULAR_VELO;
	}


	public void setK_ANGULAR_VELO(double k_ANGULAR_VELO) {
		K_ANGULAR_VELO = k_ANGULAR_VELO;
	}


	public double getK_ANGLE() {
		return K_ANGLE;
	}


	public void setK_ANGLE(double k_ANGLE) {
		K_ANGLE = k_ANGLE;
	}


	public double getK_MOTORPOS() {
		return K_MOTORPOS;
	}


	public void setK_MOTORPOS(double k_MOTORPOS) {
		K_MOTORPOS = k_MOTORPOS;
	}


	public double getK_MOTORSPEED() {
		return K_MOTORSPEED;
	}


	public void setK_MOTORSPEED(double k_MOTORSPEED) {
		K_MOTORSPEED = k_MOTORSPEED;
	}

}
