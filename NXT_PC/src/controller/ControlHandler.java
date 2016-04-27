package controller;

import gui.ControlWindow;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import comm.Communication;
import cmd.PathCMD;

/**
 * This class handles the inputs and manages the Bluetooth-connection
 * 
 * @author Justin
 *
 */

public class ControlHandler implements RemoteController, PathController {
	// TODO
	public Communication comm;
	private ControlWindow ctrlWin;

	public ControlHandler(ControlWindow ctrlWin) {
		this.ctrlWin = ctrlWin;
		comm = new Communication(ctrlWin);
	}

	public void setRobotPosition(int x, int y) {
		ctrlWin.setRobotPosition(x, y);
	}

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
	public void setSpeed(int speed) {
		comm.changeSpeed(speed);
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
		// comm = new Communication(ctrlWin);
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

	@Override
	/**
	 * 
	 * @param commands the commands for the path
	 * format of one command: {cmd,val}, both int
	 * 
	 */
	public void startPath(ArrayList<PathCMD> commands) {
		// TODO uploading balken

		comm.uploadPath(commands);
		comm.startPath();
	}

	public void disconnect() {
		comm.disconnect();
	}

	public void exporttofile() {
		comm.savetofile();
	}
	
	public void openExcel(){
		try {
			Runtime.getRuntime().exec("cmd /C start \"\" \"/autoexcel.bat\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendtoServer() {
		comm.sendtoServer();
	}

	public void resumePath() {
		comm.resumePath();
	}

	public void pausePath() {
		comm.pausePath();
	}

}
