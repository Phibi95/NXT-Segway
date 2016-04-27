package comm;

import gui.ControlWindow;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cmd.PathCMD;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;


/**
 * The Class which handles the Communication
 * @author Philipp
 *
 */
public class Communication extends Thread {
	CommunicationProtokoll CommProt = new CommunicationProtokoll();
	boolean running = true;
	boolean critical = false;
	InputStream dis;
	OutputStream dos;
	static NXTComm nxtComm;
	Communication comm;

	public byte paketnr = 0b0;

	// Lets do some logging of the DATA
	CopyOnWriteArrayList<byte[]> Befehle = new CopyOnWriteArrayList<byte[]>();
	ArrayList<LogType> Log = new ArrayList<LogType>();
	private ControlWindow ctrlWin;
	boolean crit = false;

	HttpCall http;
	
	/**
	 * @author Philipp
	 */
	public Communication(ControlWindow ctrlWin) {
		this.ctrlWin = ctrlWin;
	}
	
	/**
	 * @author Philipp
	 */
	public void init() {
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
		HttpThread thread = new HttpThread(this);
		thread.run();
		http = new HttpCall("012345");
	}
	
	/**
	 * @author Philipp
	 */
	public void run() {
		CommProt = new CommunicationProtokoll();
		try {
			NXTInfo[] nxtInfo = nxtComm.search("Rita");
			if (nxtInfo == null) {
				throw new NXTCommException();
			} else if (nxtInfo.length == 0) {
				throw new NXTCommException();
			}
			// jButton1.setText("Found Device");
			nxtComm.open(nxtInfo[0]);
			connected();
			dis = nxtComm.getInputStream();
			dos = nxtComm.getOutputStream();
			try {
				int i;
				byte[] b;
				while (running) {
					// System.out.println("Waiting for Data");
					b = new byte[150];
					i = dis.read(b);
					// System.out.println("Recieved Data");
					updateLabels(b);
					// System.out.println("Anzahl bytes:"+i);
					// Checking Befehlsarray
					if (Befehle.isEmpty() == false) {
						// Befehle abarbeiten
						dos.write(0b01111100);
						System.out.println("Changing Mode to SEND");
						dos.flush();
						System.out.println("Recieved ACK");

						// ArrayList<byte[]> hilfee = (ArrayList<byte[]>)
						// Befehle.clone();
						synchronized (Befehle) {
							Iterator<byte[]> iterator = Befehle.iterator();
							while (iterator.hasNext()) {
								byte[] message = iterator.next();
								System.out.println("Sending cmd");
								dos.write(message);
								dos.flush();
								System.out.println("waiting for ack");
								// wait for ACK
								b = new byte[1];
								dis.read(b);
								if (message[2] == 0x10) {
									disconnected();
									return;
								}
								Befehle.remove(message);
								System.out.println("Success");
							}
						}
						setKBackgroundgreen();
						// Befehle.clear();

						// Wieder auf Empfangen wechseln:
						byte[] sendingcont = new byte[3];
						byte flag = 0x7E;
						sendingcont[0] = flag;
						sendingcont[1] = 0b1110;
						sendingcont[2] = 0x20;
						dos.write(sendingcont);
						// System.out.println("wrinting 0x20");
						dos.flush();
						// System.out.println("Success");
						// System.out.println("waiting for ack");
						// wait for ACK
						b = new byte[1];
						dis.read(b);
					} else {
						dos.write(0b01111110);
						// System.out.println("wrinting 0b011111110");
						dos.flush();
						// System.out.println("Recieved ACK");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				disconnected();
			}
		} catch (NXTCommException e) {
			e.printStackTrace();
			disconnected();
		}
	}
	
	/**
	 * @author Philipp
	 */
	private void connected() {
		running = true;
		ctrlWin.btnVerbinden.setEnabled(!running);
		ctrlWin.btnVerbinden.setText("Connected!");
		ctrlWin.tabbedPane.setEnabledAt(1, true);
		ctrlWin.tabbedPane.setEnabledAt(2, true);
		// ctrlWin.tabbedPane.setEnabledAt(3, true);
	}
	
	/**
	 * @author Philipp
	 */
	private void disconnected() {
		running = false;
		ctrlWin.btnVerbinden.setEnabled(!running);
		ctrlWin.btnVerbinden.setText("Connect to Device");
		ctrlWin.tabbedPane.setEnabledAt(1, false);
		ctrlWin.tabbedPane.setEnabledAt(2, false);
		// ctrlWin.tabbedPane.setEnabledAt(3, false);
		Befehle.clear();
		try {
			this.dis.close();
			this.dos.close();
			nxtComm.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Philipp
	 */
	public void disconnect() {
		if (!running) {
			System.out
					.println("ERROR: Trying to send Values but not connected!!");
		} else {
			System.out.println("Disconnect:Daten in Befehlskette schreiben.");
			addSimpleCMD((byte) 0x10);
		}
	}
	
	/**
	 * @author Philipp
	 */
	public void sendKvalues() {
		if (!running) {
			System.out
					.println("ERROR: Trying to send Values but not connected!!");
		} else {
			// First check if there is already a update comd in the cmd array
			byte befehl = 0x40;
			// ArrayList<byte[]> hilfe = (ArrayList<byte[]>) Befehle.clone();
			for (byte[] cmd : Befehle) {
				if (cmd[3] == befehl) {
					Befehle.remove(cmd);
					break;
				}
			}
			double[] daten = new double[4];
			byte paketnum = 0b111;
			daten[0] = Double.parseDouble(ctrlWin.angField.getText());
			daten[1] = Double.parseDouble(ctrlWin.angvelField.getText());
			daten[2] = Double.parseDouble(ctrlWin.speedField.getText());
			daten[3] = Double.parseDouble(ctrlWin.posField.getText());
			setKBackgroundyellow();
			byte[] sending = CommProt
					.encode(befehl, daten, (long) 12, paketnum);
			System.out.println("Daten in Befehlskette schreiben.");
			Befehle.add(sending);
		}
	}
	
	/**
	 * @author Philipp
	 */
	private void setKBackgroundgreen() {
		ctrlWin.angField.setBackground(Color.GREEN);
		ctrlWin.angvelField.setBackground(Color.GREEN);
		ctrlWin.speedField.setBackground(Color.GREEN);
		ctrlWin.posField.setBackground(Color.GREEN);
		ctrlWin.angField.setOpaque(true);
		ctrlWin.angvelField.setOpaque(true);
		ctrlWin.speedField.setOpaque(true);
		ctrlWin.posField.setOpaque(true);
	}
	
	/**
	 * @author Philipp
	 */
	private void setKBackgroundyellow() {
		ctrlWin.angField.setBackground(Color.YELLOW);
		ctrlWin.angvelField.setBackground(Color.YELLOW);
		ctrlWin.speedField.setBackground(Color.YELLOW);
		ctrlWin.posField.setBackground(Color.YELLOW);
		ctrlWin.angField.setOpaque(true);
		ctrlWin.angvelField.setOpaque(true);
		ctrlWin.speedField.setOpaque(true);
		ctrlWin.posField.setOpaque(true);
	}
	
	/**
	 * @author Philipp
	 */
	public void startLeft() {
		System.out.println("Start left!");
		addSimpleCMD((byte) 0x11);
	}

	/**
	 * @author Philipp
	 */
	public void stopLeft() {
		System.out.println("Stop left!");
		addSimpleCMD((byte) 0x12);
	}
	
	/**
	 * @author Philipp
	 */
	public void startRight() {
		System.out.println("Start right!");
		addSimpleCMD((byte) 0x13);
	}
	
	/**
	 * @author Philipp
	 */
	public void stopRight() {
		System.out.println("Stop right!");
		addSimpleCMD((byte) 0x14);
	}
	
	/**
	 * @author Philipp
	 */
	public void startForward() {
		System.out.println("Start forward!");
		addSimpleCMD((byte) 0x15);
	}
	
	/**
	 * @author Philipp
	 */
	public void stopForward() {
		System.out.println("Stop forward!");
		addSimpleCMD((byte) 0x16);
	}
	
	/**
	 * @author Philipp
	 */
	public void startBackward() {
		System.out.println("Start backward!");
		addSimpleCMD((byte) 0x17);
	}
	
	/**
	 * @author Philipp
	 */
	public void stopBackward() {
		System.out.println("Stop backward!");
		addSimpleCMD((byte) 0x18);
	}
	
	/**
	 * @author Philipp
	 * @param value
	 */
	public void turnabsolut(int value) {
		System.out.println("Turn absolut: " + value + " Degree!");
		byte befehl = 0x19;
		byte paketnum = 0b111;
		double[] daten = new double[1];
		daten[0] = value;
		byte[] sending = CommProt.encode(befehl, daten, (long) 12, paketnum);
		// System.out.println("Daten in Befehlskette schreiben.");
		Befehle.add(sending);
	}
	
	/**
	 * @author Philipp
	 * @param value
	 */
	public void turnrelativ(int value) {
		System.out.println("Turn relativ: " + value + " Degree!");
		byte befehl = 0;
		if (value < 0) {
			befehl = 0x25;
		} else {
			befehl = 0x24;
		}
		byte paketnum = 0b111;
		double[] daten = new double[1];
		daten[0] = value;
		byte[] sending = CommProt.encode(befehl, daten, (long) 12, paketnum);
		// System.out.println("Daten in Befehlskette schreiben.");
		Befehle.add(sending);
	}
	/**
	 * @author Philipp
	 * @param patharray
	 */
	public void uploadPath(ArrayList<PathCMD> patharray) {
		for (PathCMD cmd : patharray) {
			double[] values = new double[2];
			switch (cmd.getType()) {
			case "Turn":
				values[0] = cmd.getValue();
				values[1] = 0;
				break;
			case "Drive":
				values[0] = 0;
				values[1] = cmd.getValue();
				break;
			}
			byte befehl = 0x31;
			byte paketnum = 0b10;
			byte[] sending = CommProt.encode(befehl, values, (long) 12,
					paketnum);
			System.out.println("Path in Befehlskette schreiben.");
			Befehle.add(sending);
		}
	}
	
	/**
	 * @author Philipp
	 * @param value
	 */
	public void changeSpeed(int value) {
		System.out.println("Changing the Speed to: " + value);
		double[] values = new double[1];
		values[0] = value;
		byte befehl = 0x50;
		byte paketnum = 0b10;
		byte[] sending = CommProt.encode(befehl, values, (long) 12, paketnum);
		Befehle.add(sending);
	}
	
	/**
	 * @author Philipp
	 */
	public void startPath() {
		System.out.println("Start PATH!");
		addSimpleCMD((byte) 0x33);
	}
	
	/**
	 * @author Philipp
	 */
	public void pausePath() {
		System.out.println("Pause PATH!");
		addSimpleCMD((byte) 0x34);
	}
	
	/**
	 * @author Philipp
	 */
	public void resumePath() {
		System.out.println("Resume PATH!");
		addSimpleCMD((byte) 0x36);
	}
	
	/**
	 * @author Philipp
	 */
	public void stopPath() {
		System.out.println("Stop PATH!");
		addSimpleCMD((byte) 0x36);
	}
	
	/**
	 * @author Philipp
	 * @param befehl
	 */
	private void addSimpleCMD(byte befehl) {
		byte[] sendingcont = new byte[3];
		byte flag = 0x7E;
		sendingcont[0] = flag;
		sendingcont[1] = 0b1110;
		sendingcont[2] = befehl;
		Befehle.add(sendingcont);
	}

	/**
	 * @author Philipp
	 */
	public void startDirektControl() {
		Befehle.clear();
		/*
		 * LIVE 0x10 - Versetzt den Roboter in den Live Modus, sodass er von PC
		 * oder Android gesteuert werden kann
		 */
	}

	/**
	 * @author Philipp
	 */
	public void sendForward(int value) {
		/*
		 * LIVEDIR 0x11 2Byte, Wertebereich: 0 – 360 Grad Wenn der Roboter in
		 * DirectControl ist, setzt dieser ihn in eine neue Richtung
		 */

		/*
		 * LIVEFRC 0x12 2Byte Wertebereich: 0-255Cm/s Wenn der Roboter in
		 * DirectControl ist, setzt dieser ihn auf eine neue Geschwindigkeit die
		 * er ab jetzt benutzt
		 */

		/*
		 * LIVEDES 0x13 2Byte Wertebereich: 0-255 Cm Wenn der Roboter in
		 * DirectControl ist, laesst dieser ihn mit der gespeicherten
		 * Geschwindigkeit die uebermittelte Distanz fahren
		 */

	}

	private void updateLabels(byte[] b) throws ArrayEmptyException {
		if (b == null) {
			throw new ArrayEmptyException();
		} else if (b.length == 0) {
			throw new ArrayEmptyException();
		}
		paketnr = b[1];
		double[] daten = CommProt.decode(b);

		LogType hilf = new LogType();
		//ANGLE
		//MotorPos Left
		//MotorPos Right
		//MotorSpeed
		//GyroSpeed
		//Offset
		//UltraSonic
		//X and Y
		//Akku
		synchronized (hilf) {
			hilf.add((long) daten[0], "Timestamp");
			hilf.add((float) daten[1], "Angle");
			hilf.add((float) daten[2], "MotorPos Left");
			hilf.add((float) daten[3], "MotorPos Right");
			hilf.add((float) daten[4], "MotorSpeed");
			hilf.add((float) daten[5], "GyroSpeed");
			hilf.add((float) daten[6], "Offset");
			hilf.add((float) daten[7], "UltraSonic");
			hilf.add((long) daten[8], "X");
			hilf.add((long) daten[9], "Y");
			hilf.add((int) daten[10], "Akku");
			synchronized (Log) {
				Log.add(hilf);
				this.http.add(hilf);
				String test = this.http.toJson();
				this.http.excutePost(test);
				this.http.clear();
			}
		}
		// ANGLE
		ctrlWin.label.setText("" + daten[1]);
		// Angularvelo
		ctrlWin.label_1.setText("" + daten[2]);
		// MotorPos Left
		ctrlWin.label_2.setText("" + daten[3]);
		// MotorPos Right
		ctrlWin.label_3.setText("" + daten[4]);
		// MotorSpeed Left
		ctrlWin.label_4.setText("" + daten[4]);
		// MotorSpeed Right
		ctrlWin.label_5.setText("" + daten[5]);
		// Offset
		ctrlWin.label_6.setText("" + daten[6]);
		// UltraSonic
		ctrlWin.label_7.setText("" + daten[7]);
		// X and Y
		ctrlWin.label_8.setText("X: " + daten[8] + " Y: " + daten[9]);
		// Akku
		ctrlWin.label_13.setText("" + daten[10]);

		ctrlWin.ctrlHandler.setRobotPosition((int) daten[8], (int) daten[9]);
		
		
		// HttpCall a = new HttpCall("12345");
		// a.add(hilf);
		// System.out.println(Arrays.toString(b));
		// System.out.println(Arrays.toString(daten));
		// System.out.println(daten[1] / 100);
	}

	public void savetofile() {
		/*System.out.println("Writing Offset to File");
		ArrayList<Float> hilf_float;
		ArrayList<Long> hilf_long;
		try {
			PrintStream writer = new PrintStream(new File("offset.txt"));
			hilf_float = (ArrayList<Float>) Offset.clone();
			for (Float value : hilf_float) {
				writer.println(Float.toString(value));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Writing Angle to File");
		try {
			PrintStream writer = new PrintStream(new File("angle.txt"));
			hilf_float = (ArrayList<Float>) Angle.clone();
			for (Float value : hilf_float) {
				writer.println(Float.toString(value));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Writing MotorSpeed to File");
		try {
			PrintStream writer = new PrintStream(new File("motorspeed.txt"));
			hilf_float = (ArrayList<Float>) MotorSpeed.clone();
			for (Float value : hilf_float) {
				writer.println(Float.toString(value));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Writing Timestamp to File");
		try {
			PrintStream writer = new PrintStream(new File("timestamp.txt"));
			hilf_long = (ArrayList<Long>) Timestamp.clone();
			for (Long value : hilf_long) {
				writer.println(Long.toString(value));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Writing Gyrospeed to File");
		try {
			PrintStream writer = new PrintStream(new File("gyro.txt"));
			hilf_float = (ArrayList<Float>) GyroSpeed.clone();
			for (Float value : hilf_float) {
				writer.println(Float.toString(value));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}// end of savetofile

	public void sendtoServer() {
		http = new HttpCall("0123456");
		http.addLogList(Log);
		http.excutePost(http.toJson());
	}

	public CommunicationProtokoll getCommProt() {
		return CommProt;
	}

	public void setCommProt(CommunicationProtokoll commProt) {
		CommProt = commProt;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	public InputStream getDis() {
		return dis;
	}

	public void setDis(InputStream dis) {
		this.dis = dis;
	}

	public OutputStream getDos() {
		return dos;
	}

	public void setDos(OutputStream dos) {
		this.dos = dos;
	}

	public static NXTComm getNxtComm() {
		return nxtComm;
	}

	public static void setNxtComm(NXTComm nxtComm) {
		Communication.nxtComm = nxtComm;
	}

	public Communication getComm() {
		return comm;
	}

	public void setComm(Communication comm) {
		this.comm = comm;
	}

	public byte getPaketnr() {
		return paketnr;
	}

	public void setPaketnr(byte paketnr) {
		this.paketnr = paketnr;
	}

	

	public List<byte[]> getBefehle() {
		return Befehle;
	}
}

class ArrayEmptyException extends Exception {

	/**
		 * 
		 */
	private static final long serialVersionUID = -7676458867416946036L;

}