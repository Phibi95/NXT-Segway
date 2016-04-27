package main;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

/*
 * Thread that checks for Obstacles and stops driving if there is one.
 * @Author Daniel
 */
public class ObstacleChecker extends Thread{
	
	
	UltrasonicSensor sonic;
	Segway segway;
	int lastvalue =0;
	boolean running;
	public ObstacleChecker(Segway segway){
		this.segway=segway;
		running=true;
	}
	public void run(){
		sonic = new UltrasonicSensor(SensorPort.S3);
		int sonicDistance=0;
		while(running){
			sonicDistance=sonic.getDistance();
			lastvalue=sonicDistance;
			if(sonicDistance<=17){
				segway.stopMovement();
				segway.pause();
			}
		}
	}
	
	public void kill(){
		running=false;
	}
	
	public int getValue(){
		return lastvalue;
	}
	
	
}
