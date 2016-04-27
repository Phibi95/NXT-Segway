package main;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.util.Delay;

/*
 * author: pascal, phillipp
 */

public class EmergencyThread extends Thread{
	static NXTMotor left, right;
	static NXTMotor led ;
	static boolean stop = false;
	
	public void run(){
		right = new NXTMotor(MotorPort.A);
		left = new NXTMotor(MotorPort.B);
		led = new NXTMotor(MotorPort.C);
		stop = false;
		left.setPower(0);
		right.setPower(0);
		while(!stop) {
			//3 times slow 
			for (int x=0;x<3;x++){
				for(int i=0;i<=100;i++){
					led.setPower(i);
					if(stop){
						return;
					}
					Delay.msDelay(5);
				}
			}
			
			for (int x=0;x<3;x++){
				for(int i=0;i<=100;i++){
					led.setPower(i);
					if(stop){
						return;
					}
					Delay.msDelay(10);
				}
			}
			
			for (int x=0;x<3;x++){
				for(int i=0;i<=100;i++){
					led.setPower(i);
					if(stop){
						return;
					}
					Delay.msDelay(5);
				}
			}
			//System.out.println("HILFE!!");
			led.setPower(0);
			Delay.msDelay(400);
		}
		
	}
	
	public void stop(){
		stop=true;
		led.setPower(0);
	}

	public boolean getStop(){
		return stop;
	}
}
