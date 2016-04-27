package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

/**
 * Panel for coordinates
 * 
 * @author Justin, Jacky
 *
 */
public class Cross extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int CIRCSIZE = 2; // RADIUS
	private static final int CIRCDIA = 2 * CIRCSIZE;
	private static final int MAX_SIZE_HIST = 500;
	private Boolean lock = false;
	int robotX, robotY;
	// The unscaled real-sized coordinates
	List<int[]> rawPoints = (List<int[]>) Collections
			.synchronizedList(new ArrayList<int[]>());
	// ArrayList<int[]> rawPoints = new ArrayList<int[]>();
	// The drawable coordinates
	ArrayList<int[]> drawPoints = new ArrayList<int[]>();
	// half resolution in centimeters
	private int resX = 20;
	private int resY = 20;
	private int robotRawX, robotRawY;
	private BasicStroke pathStroke = new BasicStroke(3);
	// List for the robot's way history

	List<int[]> rawWay = (List<int[]>) Collections
			.synchronizedList(new ArrayList<int[]>());
	private ArrayList<int[]> drawWay = new ArrayList<int[]>();

	private BasicStroke wayStroke = new BasicStroke(1);

	/**
	 * Custom resolution
	 * 
	 * @param resX
	 * @param resY
	 */
	public Cross(int resX, int resY) {
		this.resX = resX;
		this.resY = resY;
	}

	/**
	 * Default constructor
	 */
	public Cross() {

	}

	// /==============================DRAWING STUFF==================///
	@Override
	public void paintComponent(Graphics oG) {
		super.paintComponent(oG);
		Graphics2D g = (Graphics2D) oG;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		drawCross(g);
		drawPath(g, drawPoints, false);
		drawPath(g, drawWay, true);
		drawRobot(g);

		repaint();
	}

	/**
	 * Draws the robot.
	 * 
	 * @param g
	 *            Graphics to draw
	 */
	private void drawRobot(Graphics g) {
		g.drawOval(this.robotX, this.robotY, CIRCDIA, CIRCDIA);
	}

	/**
	 * Draws the waypoints and connects them.
	 * 
	 * @param g
	 *            Graphics to draw
	 */
	private void drawPath(Graphics2D g, ArrayList<int[]> points, boolean way) {
		synchronized (rawWay) {

			int x1, y1;
			int x2, y2;
			if (points.size() > 1) {
				for (int i = 0; i < points.size() - 1; i++) {
					if (!way) {
						g.setColor(Color.RED);
						g.setStroke(pathStroke);
					} else {
						g.setColor(Color.orange);
						g.setStroke(wayStroke);
					}

					x1 = points.get(i)[0];
					y1 = points.get(i)[1];
					x2 = points.get(i + 1)[0];
					y2 = points.get(i + 1)[1];

					g.drawLine(x1, y1, x2, y2);
					if (!way)
						drawX(x1, y1, g);
				}
				drawX(points.get(points.size() - 1)[0],
						points.get(points.size() - 1)[1], g);
			}

		}
	}

	/**
	 * Draws a waypoint.
	 * 
	 * @param x
	 *            x-coord
	 * @param y1
	 *            y-coord
	 * @param g
	 *            graphics
	 */
	private void drawX(int x, int y, Graphics2D g) {

		g.setColor(Color.BLACK);
		g.fillRect(x - 4, y - 4, 8, 8);

	}

	/**
	 * Draws the cross (axes).
	 * 
	 * @param g
	 *            Graphics to draw
	 */
	private void drawCross(Graphics g) {
		g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2,
				this.getHeight());
		g.drawLine(0, this.getHeight() / 2, this.getWidth(),
				this.getHeight() / 2);

	}

	/**
	 * Insert a point
	 * 
	 * @param x
	 *            x-coord
	 * @param y
	 *            y-coord
	 * @return point array
	 */

	// /==============================MATH STUFF==================///
	protected int[] pointToArray(int x, int y) {
		int[] res = new int[2];
		res[0] = x;
		res[1] = y;
		return res;
	}

	/**
	 * Scale a point to fit into the screen
	 * 
	 * @param pX
	 *            raw coords of the point
	 * @param pY
	 *            raw coords of the point (not inverted yet)
	 * @return
	 */
	public int[] scalePoint(int pX, int pY) {
		int[] p = new int[2];
		// add the half resolution
		p[0] = pX + resX;
		p[1] = -pY + resY;

		p[0] = (int) (this.getWidth() * (p[0] / (resX * 2d)));
		p[1] = (int) (this.getHeight() * (p[1] / (resY * 2d)));
		return p;
	}

	/**
	 * Scales all points so they fit on the screen. (Instead of clipping)
	 */
	protected void scaleAllPoints() {
		// Find the most outer point and set this distance to middle as
		// resolution
		synchronized (rawWay) {

			int highest = 0;
			for (int[] p : rawPoints) {
				if (Math.abs(p[0]) > highest) {
					highest = Math.abs(p[0]); // x-coord
				}
				if (Math.abs(p[1]) > highest) {
					highest = Math.abs(p[1]); // y-coord
				}
			}
			for (int[] p : rawWay) {
				if (Math.abs(p[0]) > highest) {
					highest = Math.abs(p[0]); // x-coord
				}
				if (Math.abs(p[1]) > highest) {
					highest = Math.abs(p[1]); // y-coord
				}
			}
			if (Math.abs(this.robotRawX) > highest) {
				highest = Math.abs(this.robotRawX);
			} else if (Math.abs(this.robotRawY) > highest) {
				highest = Math.abs(this.robotRawY);
			}

			highest += 2; // leave some space
			resX = highest;
			resY = highest;
			drawPoints.clear();
			for (int[] r : rawPoints) {
				drawPoints.add(scalePoint(r[0], r[1]));
			}
			drawWay.clear();
			for (int[] r : rawWay) {
				drawWay.add(scalePoint(r[0], r[1]));
			}
			int[] robotScale = scalePoint(robotRawX, robotRawY);
			setCoords(robotScale[0], robotScale[1]);

		}
	}

	/**
	 * Sets the circle's center point of the current position
	 * 
	 * @param x
	 *            x-coordinate relative to center
	 * @param y
	 *            y-coordinate relative to center
	 */
	public void setCoords(int x, int y) {
		this.robotX = (x - CIRCSIZE);
		this.robotY = (y - CIRCSIZE);
	}

	/**
	 * Update robot position
	 * 
	 * @param x
	 * @param y
	 */
	public void setRobot(int x, int y) {
		this.robotRawX = x;
		this.robotRawY = y;

		addWaypoint(pointToArray(x, y));

		scaleAllPoints();

	}

	// TODO might not be safe
	/**
	 * Delete a point from the list
	 * 
	 * @param index
	 * @return
	 */
	public boolean deletePoint(int index) {
		try {
			this.rawPoints.remove(index);
		} catch (Exception e) {
			return false;
		}
		scaleAllPoints();
		return true;

	}

	private void addWaypoint(int[] point) {
		synchronized (rawWay) {
			rawWay.add(point);
			if (rawWay.size() > MAX_SIZE_HIST) {
				rawWay.remove(0);
			}
		}
		// if (rawWay.size() > MAX_SIZE_HIST) {
		// rawWay.remove(0);
		// }
	}

	/**
	 * Add a path point
	 * 
	 * @param x
	 * @param y
	 */
	public void addPoint(int x, int y) {
		this.rawPoints.add(pointToArray(x, y));
		scaleAllPoints();
	}
}