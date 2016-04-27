package comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android_app.Fragment_Infos;
import commands.PathCMD;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

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
	ArrayList<Float> Angle = new ArrayList<Float>();

	ArrayList<Float> Offset = new ArrayList<Float>();
	ArrayList<Float> GyroSpeed = new ArrayList<Float>();
	ArrayList<Float> MotorSpeed = new ArrayList<Float>();
	ArrayList<Long> Timestamp = new ArrayList<Long>();
//	List<byte[]> Befehle = Collections.synchronizedList(new ArrayList<byte[]>());
	static CopyOnWriteArrayList<byte[]> Befehle = new CopyOnWriteArrayList<byte[]>();
	ArrayList<Float> Tacho = new ArrayList<Float>();
	ArrayList<Float> PwrLeft = new ArrayList<Float>();
	ArrayList<Float> PwrRight = new ArrayList<Float>();
	ArrayList<LogType> Log = new ArrayList<LogType>();
	boolean crit = false;
	volatile AtomicBoolean execute=new AtomicBoolean(false);

	HttpCall http;

	public Communication(){

	}

	public void init() {
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
	}

    NXTInfo[] nxtInfo;

	public void run(){
		CommProt = new CommunicationProtokoll();

		try {
            nxtComm.open(new NXTInfo(2, "Rita", "00:16:53:0F:DA:7C"));
            dis=nxtComm.getInputStream();
            dos=nxtComm.getOutputStream();

			connected();
			System.out.println("CONNECTED BITCHES");
			try {
				int i;
				byte[] b;
				while(running){
//					System.out.println("Waiting for Data");
					b = new byte[150];
					i=dis.read(b);
//					System.out.println("Recieved Data"+i);
					weiterverarbeiten(b);
//					System.out.println("Anzahl bytes:"+i);
					//Checking Befehlsarray
					if(Befehle.size() > 0){
						System.out.println("Bin drin");
						//Befehle abarbeiten
						dos.write(0b01111100);
						System.out.println("Changing Mode to SEND");
						dos.flush();
						System.out.println("Recieved ACK");
						
//						ArrayList<byte[]> hilfee = (ArrayList<byte[]>) Befehle.clone();
						synchronized(Befehle){
							Iterator<byte[]> iterator = Befehle.iterator(); 
							while (iterator.hasNext()){
								if(iterator==null){
									Befehle.clear();
									break;
								}
								byte[] message = iterator.next();
								System.out.println("Sending cmd");
								dos.write(message);
								dos.flush();
								System.out.println("waiting for ack");
								//wait for ACK
								b = new byte[1];
								dis.read(b);
								Befehle.remove(message);
								System.out.println("Success");
							}
						}
						//setKBackgroundgreen();
						//Befehle.clear();
						
						//Wieder auf Empfangen wechseln:
						byte[] sendingcont = new byte[3];
						byte flag = 0x7E;
						sendingcont[0]=flag;
						sendingcont[1]=0b1110;
						sendingcont[2]=0x20;
						dos.write(sendingcont);
//						System.out.println("wrinting 0x20");
						dos.flush();
//						System.out.println("Success");
//						System.out.println("waiting for ack");
						//wait for ACK
						b = new byte[1];
						dis.read(b);
					} else {
						dos.write(0b01111110);
//						System.out.println("wrinting 0b011111110");
						dos.flush();
//						System.out.println("Recieved ACK");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				disconnected();
				nxtComm.open(new NXTInfo(2, "Rita", "00:16:53:0F:DA:7C"));
				dis=nxtComm.getInputStream();
				dos=nxtComm.getOutputStream();
				System.out.println("CONNECTED BITCHES");
			}
		} catch (Exception e) {
			e.printStackTrace();
			disconnected();
		}
	}

	private void connected() {
		running = true;
	}

	private void disconnected() {
		running = false;
		Befehle.clear();
	}

	public void disconnect() {
		if (!running) {
			System.out.println("ERROR: Trying to send Values but not connected!!");
		} else {
			System.out.println("Disconnect:Daten in Befehlskette schreiben.");
			addSimpleCMD((byte) 0x10);
		}
	}

	public void sendKvalues() {
		if (!running) {
			System.out
					.println("ERROR: Trying to send Values but not connected!!");
		} else {
			// First check if there is already a update comd in the cmd array
			byte befehl = 0x40;
//			ArrayList<byte[]> hilfe = (ArrayList<byte[]>) Befehle.clone();
			for (byte[] cmd : Befehle) {
				if (cmd[3] == befehl) {
					Befehle.remove(cmd);
					break;
				}
			}
			double[] daten = new double[4];
			byte paketnum = 0b111;
			/*
			daten[0] = Double.parseDouble(ctrlWin.angField.getText());
			daten[1] = Double.parseDouble(ctrlWin.angvelField.getText());
			daten[2] = Double.parseDouble(ctrlWin.speedField.getText());
			daten[3] = Double.parseDouble(ctrlWin.posField.getText());*/
			byte[] sending = CommProt
					.encode(befehl, daten, (long) 12, paketnum);
			System.out.println("Daten in Befehlskette schreiben.");
			Befehle.add(sending);
		}
	}

	public void startLeft(){
		System.out.println("Start left!");
		addSimpleCMD((byte)0x11);
	}
	
	public void stopLeft(){
		System.out.println("Stop left!");
		addSimpleCMD((byte)0x12);
	}
	
	public void startRight(){
		System.out.println("Start right!");
		addSimpleCMD((byte)0x13);
	}
	
	public void stopRight(){
		System.out.println("Stop right!");
		addSimpleCMD((byte)0x14);
	}
	
	public void startForward(){
		System.out.println("Start forward!");
		addSimpleCMD((byte)0x15);
	}
	
	public void stopForward(){
		System.out.println("Stop forward!");
		addSimpleCMD((byte)0x16);
	}
	
	public void startBackward(){
		System.out.println("Start backward!");
		addSimpleCMD((byte)0x17);
	}
	
	public void stopBackward(){
		System.out.println("Stop backward!");
		addSimpleCMD((byte) 0x18);
	}
	
	public void turnabsolut(int value){
		System.out.println("Turn absolut: "+ value +" Degree!");
		byte befehl = 0x19;
		byte paketnum = 0b111;
		double[] daten = new double[1];
		daten[0] = value;
		byte[] sending = CommProt.encode(befehl, daten, (long)12, paketnum);
		//System.out.println("Daten in Befehlskette schreiben.");
		Befehle.add(sending);
	}
	
	public void turnrelativ(int value){
		System.out.println("Turn relativ: "+ value +" Degree!");
		byte befehl=0;
		if(value<0){
			befehl = 0x25;
		} else {
			befehl = 0x24;
		}
		byte paketnum = 0b111;
		double[] daten = new double[1];
		daten[0] = value;
		byte[] sending = CommProt.encode(befehl, daten, (long)12, paketnum);
		//System.out.println("Daten in Befehlskette schreiben.");
		Befehle.add(sending);
	}
	
	public void uploadPath(ArrayList<PathCMD> patharray){
		for (PathCMD cmd : patharray) {
			double[] values = new double[2];
			switch(cmd.getType()){
				case "Turn":
					values[0]=cmd.getValue();
					values[1]=0;
					break;
				case "Drive":
					values[0]=0;
					values[1]=cmd.getValue();
					break;
			}
			byte befehl= 0x31;
			byte paketnum = 0b10;
			byte[] sending = CommProt
					.encode(befehl, values, (long) 12, paketnum);
			System.out.println("Path in Befehlskette schreiben.");
			Befehle.add(sending);
		}
	}
	
	public void startPath(){
		System.out.println("Start PATH!");
		addSimpleCMD((byte)0x33);
	}
	
	public void pausePath(){
		System.out.println("Pause PATH!");
		addSimpleCMD((byte)0x34);
	}
	
	public void resumePath(){
		System.out.println("Resume PATH!");
		addSimpleCMD((byte)0x36);
	}
	
	public void stopPath(){
		System.out.println("Stop PATH!");
		addSimpleCMD((byte) 0x36);
	}
	
	private void addSimpleCMD(byte befehl){

		byte[] sendingcont = new byte[3];
		byte flag = 0x7E;
		sendingcont[0]=flag;
		sendingcont[1]=0b1110;
		sendingcont[2]=befehl;
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

	}

	private void weiterverarbeiten(byte[] b) throws ArrayEmptyException {
		if (b == null) {
			throw new ArrayEmptyException();
		} else if (b.length == 0) {
			throw new ArrayEmptyException();
		}
		paketnr = b[1];
		double[] daten = CommProt.decode(b);
		Timestamp.add((long) daten[0]);
		Angle.add((float) daten[1]);
		
		Offset.add((float) daten[6]);
		
		//TODO UPDATE THESE!
		MotorSpeed.add((float) daten[1]);
		GyroSpeed.add((float) daten[2]);
		Tacho.add((float) daten[3]);
		PwrLeft.add((float) daten[6]);
		PwrRight.add((float) daten[7]);

		LogType hilf = new LogType();
		hilf.add((long) daten[0], "Timestamp");
		hilf.add((float) daten[1], "Angle");
		hilf.add((float) daten[2], "Angularvelo");
		hilf.add((float) daten[3], "MotorPos Left");
		hilf.add((float) daten[4], "MotorPos Right");
		hilf.add((float) daten[5], "MotorSpeed Left");
		hilf.add((float) daten[5], "MotorSpeed Right");
		hilf.add((float) daten[6], "Offset");
		hilf.add((float) daten[7], "UltraSonic");
		hilf.add((long) daten[8], "X");
		hilf.add((long) daten[9], "Y");
		hilf.add((int) daten[10], "Akku");
		Log.add(hilf);

		HttpCall call = new HttpCall("12345");
		call.add(hilf);
		call.excutePost(call.toJson());

        /*
        TODO in einzelne Klasse ausgliedern so dass ich hier nix aenndern muss
        */


		//ANGLE
		Fragment_Infos.setAngle("" + daten[1]);
		//Angularvelo
		Fragment_Infos.setAngle_vel("" + daten[2]);
		//MotorPos Left
		//ctrlWin.label_2.setText("" + daten[3]);
		//MotorPos Right
		//ctrlWin.label_3.setText("" + daten[4]);
		//MotorSpeed Left
		//MotorSpeed Right
		//ctrlWin.label_5.setText("" + daten[5]);
		//Offset
		Fragment_Infos.setOffset(""+daten[6]);
		//UltraSonic
		//ctrlWin.label_7.setText("" + daten[7]);
		//X and Y
		//ctrlWin.label_8.setText("X: " + daten[8] + " Y: " + daten[9]);
		Fragment_Infos.setPosX(""+daten[8]);
		Fragment_Infos.setPosY("" + daten[9]);
		//Akku
		//ctrlWin.label_13.setText("" + daten[10]);
		//System.out.println(Arrays.toString(daten));
	}

	public void sendtoServer() {
		http = new HttpCall("0123456");
		http.addLogList(Log);
		http.excutePost(http.toJson());
	}

	public void changeSpeed(int value) {
		System.out.println("Changing the Speed to: " + value);
		double[] values = new double[1];
		values[0] = value;
		byte befehl = 0x50;
		byte paketnum = 0b10;
		byte[] sending = CommProt.encode(befehl, values, (long) 12, paketnum);
		Befehle.add(sending);
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

	public ArrayList<Float> getAngle() {
		return Angle;
	}

	public void setAngle(ArrayList<Float> angle) {
		Angle = angle;
	}

	public ArrayList<Float> getOffset() {
		return Offset;
	}

	public void setOffset(ArrayList<Float> offset) {
		Offset = offset;
	}

	public ArrayList<Float> getGyroSpeed() {
		return GyroSpeed;
	}

	public void setGyroSpeed(ArrayList<Float> gyroSpeed) {
		GyroSpeed = gyroSpeed;
	}

	public ArrayList<Float> getMotorSpeed() {
		return MotorSpeed;
	}

	public void setMotorSpeed(ArrayList<Float> motorSpeed) {
		MotorSpeed = motorSpeed;
	}

	public ArrayList<Long> getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(ArrayList<Long> timestamp) {
		Timestamp = timestamp;
	}

	public List<byte[]> getBefehle() {
		return Befehle;
	}

	public ArrayList<Float> getTacho() {
		return Tacho;
	}

	public void setTacho(ArrayList<Float> tacho) {
		Tacho = tacho;
	}

	public ArrayList<Float> getPwrLeft() {
		return PwrLeft;
	}

	public void setPwrLeft(ArrayList<Float> pwrLeft) {
		PwrLeft = pwrLeft;
	}

	public ArrayList<Float> getPwrRight() {
		return PwrRight;
	}

	public void setPwrRight(ArrayList<Float> pwrRight) {
		PwrRight = pwrRight;
	}

}
class ArrayEmptyException extends Exception {

	/**
		 * 
		 */
	private static final long serialVersionUID = -7676458867416946036L;

}