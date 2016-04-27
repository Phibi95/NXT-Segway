package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cmd.DriveCMD;
import cmd.PathCMD;
import cmd.TurnCMD;
import controller.ControlHandler;

/**
 * GUI Class
 * 
 * @author Justin, Philipp
 *
 */
//@formatter:off
////==============================================================================////
/*
List for setting parameters
angle=0
angular velocity=1
position motor left=2
position motor right=3
speed motor left=4
speed motor right=5
offset=6
ultrasonic distance=7
position robot=8
K_ANGLE=9
K_ANGULARVEL=10
K_MOTORSPEED=11
K_MOTORPOS=12
battery=13
 */
////==============================================================================////
//@formatter:on
public class ControlWindow extends JFrame {
	public ControlHandler ctrlHandler;
	private static final long serialVersionUID = 9020688998090208425L;
	private JPanel contentPane;
	private JLabel label_14;
	private JLabel lblNewLabel_2;
	private JLabel lblWert;
	private JLabel lblWert_1;
	private JLabel lblNewLabel;
	public JLabel label;
	private JLabel lblKwinkel;
	private Component lblNewLabel_1;
	public JLabel label_1;
	private JLabel lblKwinkelgeschw;
	private JLabel lblMotorPosLinks;
	public JLabel label_2;
	private Component lblKmotorspeed;
	private JLabel lblMotorPosRechts;
	public JLabel label_3;
	private JLabel lblKmotorpos;
	private Component lblMotorGeschwLinks;
	public JLabel label_4;
	private JLabel lblAkku;
	public JLabel label_13;
	private JLabel lblMotorGeschwRechts;
	public JLabel label_5;
	private JLabel lblOffset;
	public JLabel label_6;
	private JLabel lblUltraschall;
	public JLabel label_7;
	private Component lblPositionImRaum;
	public JLabel label_8;
	private JLabel label_15;
	private JLabel lblGertAuswhlen;
	public JButton btnVerbinden;

	private JLabel label_16;
	public JTextField angField;
	public JTextField angvelField;
	public JTextField speedField;
	public JTextField posField;
	public JTabbedPane tabbedPane;
	private JButton btnVor;
	private JButton btnZurck;
	private JButton btnNewButton;
	private JButton btnLinks_1;
	private GPanel panelDirectCont;
	private JSlider slider;
	private JButton btnDrehen;
	private JButton btnAbsDrehen;
	private JLabel lblFahre;
	private JTextField textField;
	private JTextField angleText;
	private boolean d_pressed;
	private boolean s_pressed;
	private boolean a_pressed;
	private boolean w_pressed;
	private JList<String> list;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	protected boolean path;
	private ArrayList<PathCMD> commands = new ArrayList<PathCMD>();
	private JLabel lblLive;
	public Cross panel;
	protected int i;
	boolean pathPaused = false;

	// end attributes

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager
								.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ControlWindow frame = new ControlWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.a
	 */
	public ControlWindow() {
		listModel = new DefaultListModel<String>();
		ctrlHandler = new ControlHandler(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 793, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		/*
		 * Create a container panel with tabs for switching between connection,
		 * control,...
		 */
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				handleKeyRelease(e.getKeyCode());

			}
		});

		contentPane.add(tabbedPane);

		// Connection panel
		JPanel panelConnect = new JPanel();
		// Remote control panel
		panelDirectCont = new GPanel();
		this.setVisible(true);
		panelDirectCont.requestFocusInWindow();
		tabbedPane.addTab("Connect", null, panelConnect, null);
		tabbedPane.addTab("Control", null, panelDirectCont, null);

		btnVor = new JButton("FORWARD");
		btnVor.setBounds(125, 30, 89, 23);
		btnVor.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// do some stuff

				handleKeyPress(KeyEvent.VK_W);
				tabbedPane.requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleKeyRelease(KeyEvent.VK_W);
				tabbedPane.requestFocus();
			}

		});
		panelDirectCont.setLayout(null);
		panelDirectCont.add(btnVor);

		btnLinks_1 = new JButton("LEFT");
		btnLinks_1.setBounds(51, 58, 73, 23);
		btnLinks_1.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// do some stuff

				handleKeyPress(KeyEvent.VK_A);
				tabbedPane.requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleKeyRelease(KeyEvent.VK_A);
				tabbedPane.requestFocus();
			}
		});

		panelDirectCont.add(btnLinks_1);

		btnNewButton = new JButton("RIGHT");
		btnNewButton.setBounds(215, 58, 89, 23);
		btnNewButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// do some stuff

				handleKeyPress(KeyEvent.VK_D);
				tabbedPane.requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleKeyRelease(KeyEvent.VK_D);
				tabbedPane.requestFocus();
			}
		});
		panelDirectCont.add(btnNewButton);

		btnZurck = new JButton("BACK");
		btnZurck.setBounds(125, 86, 89, 23);
		btnZurck.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				// do some stuff

				handleKeyPress(KeyEvent.VK_S);
				tabbedPane.requestFocus();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleKeyRelease(KeyEvent.VK_S);
				tabbedPane.requestFocus();
			}
		});
		panelDirectCont.add(btnZurck);

		slider = new JSlider(JSlider.HORIZONTAL, -360, 360, 0);
		slider.setBounds(1, 201, 200, 47);
		slider.setMajorTickSpacing(90);
		slider.setMinorTickSpacing(15);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		panelDirectCont.add(slider);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				angleText.setText("" + slider.getValue());

			}

		});

		btnDrehen = new JButton("relative");
		btnDrehen.setBounds(210, 201, 85, 23);
		btnDrehen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int angle = 0;
				try {
					angle = Integer.parseInt(angleText.getText());
				} catch (NumberFormatException exc) {
					angle = 0;
					// TODO show popup
				}
				if (angle != 0) {
					if (!path) {
						handleRotation(angle, false);
					} else {
						handlePathRotation(angle, false);
					}
				}
			}
		});
		panelDirectCont.add(btnDrehen);

		btnAbsDrehen = new JButton("absolute");
		btnAbsDrehen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int angle = 0;
				try {
					angle = Integer.parseInt(angleText.getText());
				} catch (NumberFormatException exc) {
					angle = 0;
					// TODO show popup
				}
				if (angle != 0) {
					if (!path) {
						handleRotation(angle, true);
					} else {
						handlePathRotation(angle, true);
					}
				}
			}
		});
		btnAbsDrehen.setBounds(210, 229, 85, 23);
		panelDirectCont.add(btnAbsDrehen);

		lblFahre = new JLabel("Drive");
		lblFahre.setBounds(0, 287, 46, 14);
		panelDirectCont.add(lblFahre);

		textField = new JTextField();
		textField.setBounds(38, 284, 86, 20);
		panelDirectCont.add(textField);
		textField.setColumns(10);

		JLabel lblCm = new JLabel("cm ");
		lblCm.setBounds(133, 287, 46, 14);
		panelDirectCont.add(lblCm);

		JButton btnNewButton_1 = new JButton("forward");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int distance = 0;
				try {
					distance = Integer.parseInt(textField.getText());
				} catch (NumberFormatException exc) {
					distance = 0;
					// TODO show popup
				}
				if (distance != 0) {
					handlePathForward(distance);
				}

			}
		});
		btnNewButton_1.setBounds(206, 283, 89, 23);
		panelDirectCont.add(btnNewButton_1);

		JLabel lblDehen = new JLabel("Rotate");
		lblDehen.setBounds(10, 177, 46, 14);
		panelDirectCont.add(lblDehen);

		JButton btnRckwrts = new JButton("backward");
		btnRckwrts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int distance = 0;
				try {
					distance = Integer.parseInt(textField.getText());
				} catch (NumberFormatException exc) {
					distance = 0;
					// TODO show popup
				}
				if (distance != 0) {
					handlePathBackward(distance);
				}

			}
		});
		btnRckwrts.setBounds(206, 313, 89, 23);
		panelDirectCont.add(btnRckwrts);

		angleText = new JTextField();
		angleText.setBounds(51, 173, 86, 20);
		panelDirectCont.add(angleText);
		angleText.setColumns(10);

		JButton btnPfadAngeben = new JButton("Enter path");
		btnPfadAngeben.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enterPathMode();
			}
		});
		btnPfadAngeben.setBounds(336, 11, 114, 23);
		panelDirectCont.add(btnPfadAngeben);

		JSeparator separator = new JSeparator();
		separator.setBounds(1, 164, 303, 2);
		panelDirectCont.add(separator);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(336, 61, 114, 77);
		panelDirectCont.add(scrollPane);

		list = new JList<String>(listModel);

		scrollPane.setViewportView(list);

		JButton btnRemote = new JButton("Remote");
		btnRemote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enterLiveMode();
			}
		});
		btnRemote.setBounds(10, 11, 114, 23);
		panelDirectCont.add(btnRemote);

		JButton btnStart = new JButton("Start");

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startPath();
			}
		});
		btnStart.setBounds(336, 145, 85, 23);
		panelDirectCont.add(btnStart);

		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				listModel.clear();
				commands.clear();
			}
		});
		btnStop.setBounds(523, 145, 85, 23);
		panelDirectCont.add(btnStop);

		JButton btnDel = new JButton("DELETE");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteItem();
			}
		});
		btnDel.setBounds(460, 58, 86, 23);
		panelDirectCont.add(btnDel);

		panel = new Cross();
		panel.setBounds(336, 174, 416, 404);
		panelDirectCont.add(panel);
		// panel.addPoint(1, 1);
		// panel.addPoint(-1, -1);
		// panel.addPoint(4, 4);
		// panel.addPoint(-4, 4);

		lblLive = new JLabel("LIVE");
		lblLive.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLive.setBounds(468, 12, 89, 14);
		panelDirectCont.add(lblLive);

		JButton btnPause = new JButton("Pause");
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (pathPaused) {
					ctrlHandler.resumePath();
					pathPaused = false;
				} else {
					pathPaused = true;
					ctrlHandler.pausePath();
				}
			}
		});
		btnPause.setBounds(431, 145, 85, 23);
		panelDirectCont.add(btnPause);

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(324, 17, 2, 386);
		panelDirectCont.add(separator_1);

		JButton button = new JButton("CLEAR");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				commands.clear();
				listModel.clear();
			}
		});
		button.setBounds(460, 115, 86, 23);
		panelDirectCont.add(button);

		JSlider slider_1 = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 0);
		slider_1.setPaintTicks(true);
		slider_1.setPaintLabels(true);
		slider_1.setMajorTickSpacing(10);
		slider_1.setBounds(42, 115, 200, 47);

		panelDirectCont.add(slider_1);

		JLabel lblS = new JLabel("Speed");
		lblS.setBounds(10, 124, 46, 14);
		panelDirectCont.add(lblS);

		JButton btnSet = new JButton("SET");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlHandler.setSpeed(slider_1.getValue());
			}
		});
		btnSet.setBounds(246, 115, 58, 23);
		panelDirectCont.add(btnSet);
		panel.setCoords(0, 0);
		lblGertAuswhlen = new JLabel("Choose device:");

		// comboBox for choosing between different NXT-devices
		JComboBox<String> comboBox = new JComboBox<String>();

		// Connection panel layout
		btnVerbinden = new JButton("Connect");

		JButton btnTrennen = new JButton("Disconnect");
		btnTrennen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlHandler.disconnect();
				tabbedPane.requestFocus();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panelConnect);
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(Alignment.TRAILING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addComponent(lblGertAuswhlen)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		comboBox,
																		GroupLayout.PREFERRED_SIZE,
																		233,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		ComponentPlacement.UNRELATED)
																.addGroup(
																		gl_panel.createParallelGroup(
																				Alignment.LEADING)
																				.addComponent(
																						btnTrennen,
																						GroupLayout.PREFERRED_SIZE,
																						99,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						btnVerbinden))))
								.addContainerGap(202, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblGertAuswhlen)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(
														comboBox,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(btnVerbinden))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnTrennen)
								.addContainerGap(251, Short.MAX_VALUE)));
		panelConnect.setLayout(gl_panel);
		// Parameter view panel
		JPanel panelParam = new JPanel();
		panelParam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		tabbedPane.addTab("Watch", null, panelParam, null);
		panelParam.setLayout(null);

		lblNewLabel_2 = new JLabel("Name");
		lblNewLabel_2.setBounds(40, 0, 27, 14);
		panelParam.add(lblNewLabel_2);

		lblWert = new JLabel("Value");
		lblWert.setBounds(133, 0, 39, 14);
		panelParam.add(lblWert);

		label_16 = new JLabel("             ");
		label_16.setBounds(164, 0, 39, 14);
		panelParam.add(label_16);

		label_14 = new JLabel("Name");
		label_14.setBounds(282, 0, 27, 14);
		panelParam.add(label_14);

		lblWert_1 = new JLabel("Value");
		lblWert_1.setBounds(378, 0, 35, 14);
		panelParam.add(lblWert_1);

		lblNewLabel = new JLabel("Angle");
		lblNewLabel.setBounds(0, 22, 31, 14);
		panelParam.add(lblNewLabel);

		label = new JLabel("0.000000");
		label.setBounds(133, 22, 86, 14);
		panelParam.add(label);

		lblKwinkel = new JLabel("K_ANGLE");
		lblKwinkel.setBounds(249, 22, 50, 14);
		panelParam.add(lblKwinkel);

		lblNewLabel_1 = new JLabel("angular vel.");
		lblNewLabel_1.setBounds(0, 47, 71, 14);

		angField = new JTextField();
		angField.setBounds(347, 19, 86, 20);
		angField.setText("0.68");
		panelParam.add(angField);
		angField.setColumns(10);
		panelParam.add(lblNewLabel_1);

		label_1 = new JLabel("0.000000");
		label_1.setBounds(133, 47, 86, 14);
		panelParam.add(label_1);

		lblKwinkelgeschw = new JLabel("K_ANGULARVEL");
		lblKwinkelgeschw.setBounds(249, 47, 93, 14);
		panelParam.add(lblKwinkelgeschw);

		lblMotorPosLinks = new JLabel("Motor pos. left");
		lblMotorPosLinks.setBounds(0, 72, 78, 14);

		angvelField = new JTextField();
		angvelField.setBounds(347, 44, 86, 20);
		angvelField.setText("7.9");
		angvelField.setColumns(10);
		panelParam.add(angvelField);
		panelParam.add(lblMotorPosLinks);

		label_2 = new JLabel("0.000000");
		label_2.setBounds(133, 72, 86, 14);
		panelParam.add(label_2);

		lblKmotorspeed = new JLabel("K_MOTORSPEED");
		lblKmotorspeed.setBounds(249, 72, 80, 14);
		panelParam.add(lblKmotorspeed);

		lblMotorPosRechts = new JLabel("Motor pos. right");
		lblMotorPosRechts.setBounds(0, 97, 88, 14);

		speedField = new JTextField();
		speedField.setBounds(347, 69, 86, 20);
		speedField.setText("0.114");
		speedField.setColumns(10);
		panelParam.add(speedField);
		panelParam.add(lblMotorPosRechts);

		label_3 = new JLabel("0.000000");
		label_3.setBounds(133, 97, 86, 14);
		panelParam.add(label_3);

		lblKmotorpos = new JLabel("K_MOTORPOS");
		lblKmotorpos.setBounds(249, 97, 69, 14);
		panelParam.add(lblKmotorpos);

		lblMotorGeschwLinks = new JLabel("Motor speed left");
		lblMotorGeschwLinks.setBounds(0, 119, 98, 14);

		posField = new JTextField();
		posField.setBounds(347, 94, 86, 20);
		posField.setText("0.17");
		posField.setColumns(10);
		panelParam.add(posField);
		panelParam.add(lblMotorGeschwLinks);

		label_4 = new JLabel("0.000000");
		label_4.setBounds(133, 119, 86, 14);
		panelParam.add(label_4);

		lblAkku = new JLabel("Battery");
		lblAkku.setBounds(249, 119, 60, 14);
		panelParam.add(lblAkku);

		label_13 = new JLabel("0.000000");
		label_13.setBounds(367, 119, 46, 14);
		panelParam.add(label_13);

		lblMotorGeschwRechts = new JLabel("Motor speed right");
		lblMotorGeschwRechts.setBounds(0, 138, 108, 14);
		panelParam.add(lblMotorGeschwRechts);

		label_5 = new JLabel("0.000000");
		label_5.setBounds(133, 138, 86, 14);
		panelParam.add(label_5);

		lblOffset = new JLabel("Offset");
		lblOffset.setBounds(0, 157, 31, 14);
		panelParam.add(lblOffset);

		label_6 = new JLabel("0.000000");
		label_6.setBounds(133, 157, 86, 14);
		panelParam.add(label_6);

		lblUltraschall = new JLabel("Ultrasonic dist.");
		lblUltraschall.setBounds(0, 176, 74, 14);
		panelParam.add(lblUltraschall);

		label_7 = new JLabel("0.000000");
		label_7.setBounds(133, 176, 86, 14);
		panelParam.add(label_7);

		lblPositionImRaum = new JLabel("Position");
		lblPositionImRaum.setBounds(0, 195, 80, 14);
		panelParam.add(lblPositionImRaum);

		label_8 = new JLabel("0.000000");
		label_8.setBounds(133, 195, 86, 14);
		panelParam.add(label_8);

		label_15 = new JLabel(" ");
		label_15.setBounds(52, 214, 3, 14);
		panelParam.add(label_15);

		// Refresh button for parameter
		JButton btnAktualisieren = new JButton("SET");
		btnAktualisieren.setBounds(397, 293, 65, 23);
		btnAktualisieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlHandler.comm.sendKvalues();
				System.out.println("Sending");
			}
		});
		panelParam.add(btnAktualisieren);

		JButton btnExportToFile = new JButton("Export to file");
		btnExportToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlHandler.exporttofile();
			}
		});
		btnExportToFile.setBounds(85, 293, 108, 23);
		panelParam.add(btnExportToFile);

		JButton btnOpenInExcel = new JButton("Open in Excel");
		btnOpenInExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlHandler.openExcel();
			}
		});
		btnOpenInExcel.setBounds(201, 293, 108, 23);
		panelParam.add(btnOpenInExcel);
		JButton btnNewButton_2 = new JButton("Send to Server");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ctrlHandler.sendtoServer();
			}
		});
		btnNewButton_2.setBounds(85, 327, 108, 23);
		panelParam.add(btnNewButton_2);
		btnVerbinden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1_ActionPerformed(evt);
			}
		});
		// tabbedPane.setEnabledAt(1, false);
		// tabbedPane.setEnabledAt(2, false);
		// tabbedPane.setEnabledAt(3, false);

	}

	protected void test() {
		// TODO Auto-generated method stub
		// crosstester c = new crosstester();
		// c.init(panel);
		// c.start();

	}

	/**
	 * Deletes an item from the path list. The item is the selected item.
	 */
	protected void deleteItem() {

		int index = list.getSelectedIndex();
		if (index != -1) {
			listModel.remove(index);
			commands.remove(index);
		}
	}

	/**
	 * Sets the robot's position to be displayed in the window. The coordinates
	 * are relative to the center (in centimeters). E.g. (-20,-20) means 20cm
	 * left and 20cm forward relative to the starting point.
	 * 
	 * @param x
	 *            x-coordinate relative to starting point
	 * @param y
	 *            y-coordinate relative to starting point
	 */
	public void setRobotPosition(int x, int y) {
		panel.setRobot(x, y);
	}

	protected void enterLiveMode() {
		this.path = false;
		lblLive.setText("LIVE");
	}

	protected void handlePathBackward(int i) {
		commands.add(new DriveCMD(-i));
		listModel.addElement(i + "cm backward");

	}

	protected void handlePathForward(int i) {
		commands.add(new DriveCMD(i));
		listModel.addElement(i + "cm forward");

	}

	/**
	 * Controlhandler starts the path. If path is empty, do nothing
	 */
	protected void startPath() {
		if (commands.size() > 0) {
			ctrlHandler.startPath(commands);
			pathPaused = false;
		} else {
			// TODO
			// INFO: NO PATH
		}

	}

	/**
	 * 
	 * @param degrees
	 * @param absolute
	 */
	protected void handlePathRotation(int degrees, boolean absolute) {
		String abs = "relative";
		if (absolute) {
			abs = "absolute";
		}
		listModel.addElement(degrees + "° " + abs);
		commands.add(new TurnCMD(degrees));
	}

	protected void enterPathMode() {
		this.path = true;
		lblLive.setText("PATH");
	}

	protected void handleKeyRelease(int keyCode) {
		int source = tabbedPane.getSelectedIndex();
		if (source == 1) // If the currently selected pane is the second one
		{
			switch (keyCode) {
			case KeyEvent.VK_W:
				ctrlHandler.stopForwardAction();
				// panelDirectCont.onButtonPressed(0);
				w_pressed = false;
				break;
			case KeyEvent.VK_A:
				ctrlHandler.stopLeftAction();
				// panelDirectCont.onButtonPressed(3);
				a_pressed = false;
				break;
			case KeyEvent.VK_S:
				ctrlHandler.stopBackwardAction();
				// panelDirectCont.onButtonPressed(2);
				s_pressed = false;
				break;
			case KeyEvent.VK_D:
				ctrlHandler.stopRightAction();
				// panelDirectCont.onButtonPressed(1);
				d_pressed = false;
				break;
			}
		}

	}

	protected void setParam(float parseFloat, float parseFloat2,
			float parseFloat3, float f) {

	}
	
	public void jButton1_ActionPerformed(ActionEvent evt) {
		btnVerbinden.setEnabled(false);
		btnVerbinden.setText("Connecting...");
		ctrlHandler.connect();
		tabbedPane.requestFocus();
	} // end of jButton1_ActionPerformed

	public void jButton3_ActionPerformed(ActionEvent evt) {
		ctrlHandler.comm.sendKvalues();
		System.out.println("Sending");
	} // end of jButton3_ActionPerformed

	/**
	 * Handle the key presses triggered from the remote control panel
	 * 
	 * @param e
	 *            the key int
	 */
	protected void handleKeyPress(int e) {
		int source = tabbedPane.getSelectedIndex();
		if (source == 1) // If the currently selected pane is the second one
		{
			switch (e) {
			case KeyEvent.VK_W:
				if (!w_pressed) {
					ctrlHandler.forwardAction();
					panelDirectCont.onButtonPressed(0);
					w_pressed = true;
				}

				break;
			case KeyEvent.VK_A:
				if (!a_pressed) {
					ctrlHandler.leftAction();
					panelDirectCont.onButtonPressed(3);
					a_pressed = true;
				}

				break;
			case KeyEvent.VK_S:
				if (!s_pressed) {
					ctrlHandler.backwardAction();
					panelDirectCont.onButtonPressed(2);
					s_pressed = true;
				}

				break;
			case KeyEvent.VK_D:
				if (!d_pressed) {
					ctrlHandler.rightAction();
					panelDirectCont.onButtonPressed(1);
					d_pressed = true;
				}

				break;
			}
		}
	}

	/**
	 * Handles the rotation in live mode.
	 *
	 * @param degrees
	 *            the angle to rotate
	 * @param absolute
	 *            is it an absolute angle?
	 * 
	 */
	protected void handleRotation(int degrees, boolean absolute) {
		if (absolute) {
			ctrlHandler.rotateAbsolute(degrees);
		} else {
			ctrlHandler.rotateRelative(degrees);
		}
	}
}
