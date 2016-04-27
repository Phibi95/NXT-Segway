package gui;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * Panel for remote control
 * 
 * @author Justin
 *
 */
public class GPanel extends JPanel implements ActionListener {

	private int circ_x, circ_y;
	private boolean circ_pressed = false;
	protected static final int CIRCLESIZE = 25;
	private static final long serialVersionUID = 1L;

	public GPanel() {
		this.setFocusable(true);

	}

	/**
	 * The
	 * 
	 * @param keyCode
	 */
	protected void onButtonPressed(int keyCode) {
		switch (keyCode) {
		case 0:		//forward
			System.out.println("FW");
			break;	
		case 1:		//right
			System.out.println("R");
			break;
		case 2:		//backward
			System.out.println("BW");
			break;
		case 3:		//left
			System.out.println("L");
			break;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// g.drawOval(100, 70, 100, 100);
		// g.drawOval(circ_x, circ_y, CIRCLESIZE, CIRCLESIZE);
		if (circ_pressed) {
			g.fillOval(150, 150, CIRCLESIZE, CIRCLESIZE);
			repaint();
		}

	}

	protected void handlePress(int x, int y) {
		circ_pressed = false;
		int res_x = getPosX() - x;
		int res_y = getPosY() - y;
		// if the press was made within the circle
		if (Math.sqrt(res_x * res_x + res_y * res_y) < CIRCLESIZE / 2) {
			circ_pressed = true;
		}
	}

	/**
	 * Is triggered when the user wants to move the circle
	 * 
	 * @param x
	 *            x-coordinate of the desired middle position of the circle
	 * @param y
	 *            y-coordinate of the desired middle position of the circle
	 */
	protected void setPos(int x, int y) {
		this.circ_x = x - (CIRCLESIZE / 2);
		this.circ_y = y - (CIRCLESIZE / 2);
	}

	/**
	 * 
	 * @return x-coordinate of the circle's middle point
	 */
	public int getPosX() {
		return this.circ_x + (CIRCLESIZE / 2);
	}

	/**
	 * 
	 * @return y-coordinate of the circle's middle point
	 */
	public int getPosY() {
		return this.circ_y + (CIRCLESIZE / 2);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
