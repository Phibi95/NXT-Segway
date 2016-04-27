package main;

import java.util.ArrayList;

import cmd.PathCMD;

/**
 * 
 * @author Philipp
 *
 */

public class PathThread extends Thread{
	Segway segway;
	private ArrayList<PathCMD> befehle;
	PathCMD currentCMD;
	public boolean running=false;
	public boolean isFinished=false;
	
	public PathThread(Segway seg){
		this.segway=seg;
		befehle = new ArrayList<PathCMD>();
		this.running=false;
	}
	
	public void addCMD(PathCMD cmd) throws PathIsRunning{
		if(running){
			throw new PathIsRunning();
		} else {
			befehle.add(cmd);
		}
	}
	
	public void deleteCMD(PathCMD cmd) throws PathIsRunning{
		if(running){
			throw new PathIsRunning();
		} else {
			befehle.remove(cmd);
		}
	}
	
	public void stopPath(){
		running=false;
		isFinished=true;
		befehle.clear();
	}
	
	/**
	 * Executes a Path in its order and clears it afterwards
	 * 
	 * authors: Pascal, Daniel
	 * 
	 * @see java.lang.Thread#run()
	 */
	
	public void run(){
		lejos.nxt.Sound.beep();
		running=true;
		for(int i=0; i<befehle.size(); i++){
			//get current command
			PathCMD currentCMD = befehle.get(i);
			//check for the type			
			if(currentCMD.getType().equals("Drive")){
				//if its positive it should go forward
				if(currentCMD.getValue()>=0){
					segway.startForward(currentCMD.getValue());
				}
				else segway.startBackward(-currentCMD.getValue());
			}
			//is a turn command
			else {
				if(currentCMD.getValue()>=0){
					segway.startRight(currentCMD.getValue());
				}
				else{
					segway.startLeft(-currentCMD.getValue());
				}
			}
			segway.startRemote();
			while(segway.remote){
				if(!running) return;
			}
		}
		isFinished=true;
	}	
}

/**
 * 
 * @author Philipp
 *
 */
class PathIsRunning extends Exception{
	
}
/**
 * 
 * @author Philipp
 *
 */
class PathListIsEmpty extends Exception{
	
}