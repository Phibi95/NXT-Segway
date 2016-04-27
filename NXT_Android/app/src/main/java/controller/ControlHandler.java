package controller;

import java.util.ArrayList;
import comm.Communication;
import commands.PathCMD;

/**
 * This class handles the inputs and manages the Bluetooth-connection
 * 
 * @author Justin
 * @author Pascal, fitting the Android App
 *
 */

public class ControlHandler implements controller.RemoteController, controller.PathController {
	public Communication comm;

	public ControlHandler() {
		comm = new Communication();
	}

	public void setRobotPosition(int x, int y) {	}

	@Override
	public void forwardAction() {
		comm.startForward();
	}

	@Override
	public void backwardAction() {
		comm.startBackward();
	}

	@Override
	public void leftAction() {
		comm.startLeft();
	}

	@Override
	public void rightAction() {
		comm.startRight();
	}

	@Override
	public void setSpeed(int i) {
		comm.changeSpeed(i);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateAbsolute(int degrees) {
		comm.turnabsolut(degrees);
	}

	@Override
	public void rotateRelative(int degrees) {
		comm.turnrelativ(degrees);
	}

	public void connect() {
		comm = new Communication();
		comm.init();
		comm.start();
	}

	public void stopForwardAction() {
		comm.stopForward();
	}

	public void stopLeftAction() {
		comm.stopLeft();
	}

	public void stopBackwardAction() {
		comm.stopBackward();
	}

	public void stopRightAction() {
		comm.stopRight();
	}

	/**
	 * 
	 * @param commands the commands for the path
	 * format of one command: {cmd,val}, both int
	 * 
	 */
	public void startPath(ArrayList<PathCMD> commands) {
		comm.uploadPath(commands);
		System.out.println("Uploaded Path!");
		comm.startPath();
		System.out.println("Start Path!");
	}

	public void disconnect() {
		comm.disconnect();
	}

	
	public void exporttofile(){
		
	}

	public boolean isConnected(){
		return comm.isRunning();
	}

	public void pausePath() {
		comm.pausePath();
	}

	public void resumePath() {
		comm.resumePath();
	}

}
