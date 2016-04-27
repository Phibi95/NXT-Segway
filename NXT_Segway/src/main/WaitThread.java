package main;

import lejos.util.Delay;

/**
 * Lets the Segway wait without killing the balancing
 * @author Daniel
 *
 */

public class WaitThread extends Thread{

	int time;
	
	WaitThread(int time){
		this.time=time;
	}
	
	public boolean wait=false;
	boolean noKill=true;
	@Override
	public void run() {
		wait=true;
		Delay.msDelay(time);
		wait=false;
		while(noKill){}
	}
	
	public void kill(){
		noKill=false;
	}

}
