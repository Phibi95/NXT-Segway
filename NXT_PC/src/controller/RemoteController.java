package controller;

/**
 * Basic interface for the remote control
 * 
 * @author Justin
 *
 */
public interface RemoteController {
	/**
	 * 
	 */
	public void forwardAction();

	public void backwardAction();

	public void leftAction();

	public void rightAction();

	public void stop();

	void rotateAbsolute(int degrees);

	void rotateRelative(int degrees);

	void setSpeed(int speed);


}
