package comm;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cmd.DriveCMD;
import cmd.TurnCMD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.LCD;
import main.PathThread;
import main.Segway;

/**
 * The Bluetooth Thread needs to be started once and stops only if User calls .stop.
 * We have a Master -> Slave Relation. Slave = Roboter. Master = PC/Handy.
 * @author Philipp
 *
 */
public class BluetoothThread extends Thread{
	NXTConnection connection = null;
	Segway segway;
	boolean running, disconnected, DEBUG, livecont;
	String mode;
	byte paketcounter = 0b1;
	int timeout = 0, line =0;
	DataInputStream dis=null;
    DataOutputStream dos=null;
    CommunicationProtokoll CommProt;
    long TimeStamp, TimeStampdiff;
    byte[] b;
    public PathThread PathThread;
	
	/**
	 * @author Philipp
	 * @param segway The Heart of the Segway. Be carefull!
	 * @param DEBUG set true if you want the Display to show the Debug Messages of the Bluetooth-Module
	 */
	public BluetoothThread(Segway segway, boolean DEBUG){
		this.segway= segway;
		this.running=true;
		this.disconnected = true;
		this.DEBUG = DEBUG;
		this.mode = "send";
		this.CommProt = new CommunicationProtokoll();
		this.TimeStamp=0;
		this.livecont=false;
		this.PathThread = new PathThread(segway);
	}
	
	/**
	 * The main part of the Bluetooth Thread
	 * @author Philipp
	 */
	public void run(){
	    while(running){
	    	if(disconnected){
	    		connection = Bluetooth.waitForConnection();
	    //		connection.setIOMode(NXTConnection.RAW);
	    		this.disconnected=false;
	    		this.livecont=false;
	    		paketcounter = 0b1;
	    		print("Successfully Connected");
		    	dis = connection.openDataInputStream();
			    dos = connection.openDataOutputStream();
			    TimeStampdiff=System.currentTimeMillis();
			    this.mode = "send";
	    	}
		    //Jetzt muss entweder gesendet oder empfangen werden also ganz einfach
	    	//ein switch
	    	try{
	    		switch(this.mode){
			    	case "send":
			    		sendData();
//			    		print("waiting for ACK");
				    	//Checking Answer of Master
				    	byte recieved = dis.readByte();
				    	while(recieved!=0b01111110&&timeout!=400&&recieved!=0b01111100){
				    		timeout++;
//				    		printclear();
//				    		print("waiting for ACK");
//				    		print("Test: " + recieved);
//				    		print("Timeout:"+ timeout);
				    		recieved=dis.readByte();
				    	}
				    	//This ACK makes the Slave listen to the Master
				    	if(recieved==0b01111100){
				    		this.mode="recieve";
				    		print("Changing mode to");
				    		print("RECIEVE");
				    	} else if (recieved==0b01111110){ //This ACK makes the Slave sending Data to the Master
				    		this.mode="send";
				    	}
//				    	print("Recieved ACK:");
			    		break;
			    	case "recieve":
			    		if(livecont){
//			    			listenDirect();
			    			listenData();
			    		} else {
			    			listenData();
			    		}
			    		break;
		    	}
	    	}  catch (IOException ioe) {
				printclear();
				print("Write Exception/Timout");
				//Try to restart Connection:
				print("Trying to reconnect");
				print("Connection");
				this.disconnected=true;
		      }
	    }
	}
	
	/**
	 * Stopps the Bluetooth thread. DO NOT USE!!
	 * @author Philipp
	 */
	public void stop(){
		print("Bluetooth-Mod down");
		this.running=false;
	}
	
	/**
	 * This is the function that handels the recieved data.
	 * @author Philipp
	 * @throws IOException
	 */
	private void listenData() throws IOException {
//		printclear();
//		print("waiting for Input");
		b = new byte[200];
		//Checking Signal of Master
    	int recieved = dis.read(b);
    	double[] message = new double[1];
    	
    	byte flag = b[0];
    	byte paketnr = b[1];
    	byte befehl = b[2];
    	
    	switch(befehl){
    		case 0x01:
    			print("Disconnecting");
    			connection.close();
    			this.disconnected=true;
    			return;
    		case 0x10:
    			//Changing to direct driving mode
    			print("Recieved Direct");
    			print("Controll");
    			livecont=true;
    			directcont();
    			break;
			/*
			 * LIVELEFT	0x11	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, dreh dich links
			 */
			case 0x11:
				print("START DIRLEFT");
				directcont();
				this.segway.startLeft(Integer.MAX_VALUE);
				break;
			/*
			 * LIVELEFT	0x12	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, STOP dreh dich links
			 */
			case 0x12:
				print("STOP DIRLEFT");
				directcont();
				this.segway.stopLeft();
				break;
			/*
			 * LIVERIGHT	0x13	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, dreh dich rechts
			 */
			case 0x13:
				print("START DIRRIGHT");
				directcont();
				this.segway.startRight(Integer.MAX_VALUE);
				break;
			/*
			 * LIVERIGHT	0x14	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, STOP dreh dich rechts
			 */
			case 0x14:
				print("STOP DIRRIGHT");
				directcont();
				this.segway.stopRight();
				break;
			/*
			 * LIVEFOR	0x15	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, fahr vorwaerts
			 */
			case 0x15:
				print("START DIRFOR");
				directcont();
				this.segway.startForward(3000); //pascal-this is faster than another if check
				break;
			/*
			 * LIVEFOR	0x16	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, STOP fahr vorwaerts
			 */
			case 0x16:
				print("STOP DIRFOR");
				directcont();
				this.segway.stopForward();
				break;
			/*
			 * LIVEFOR	0x17	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, fahr rueckwaerts
			 */	
			case 0x17:
				print("START DIRBACK");
				directcont();
				this.segway.startBackward(3000);
				break;
			/*
			 * LIVEFOR	0x18	2Byte, 
			 * Wertebereich: 
			 * Wenn der Roboter in DirectControl ist, STOP fahr rueckwaerts
			 */	
			case 0x18:
				print("STOP DIRBACK");
				directcont();
				this.segway.stopBackward();
				break;
			/*
			 * Turn	0x19	4Byte, 
			 * Wertebereich: INT
			 * Dreh dich absolut!
			 */	
			case 0x19:
				print("Turn");
				message = CommProt.decode(b);
				this.segway.startDegreeAbsolut((int)message[0]);
				break;
			/*
			 * Changing back to Send mode
			 */
			case 0x20:
    			print("Changing mode to");
    			print("SEND");
    			this.mode="send";
    			break;
			/*
			 * LIVEFRC	0x23	2Byte
			 * Wertebereich: 0-255Cm/s
			 * TODO
			 */
			case 0x23:
				print("Changing FRC");
				message = CommProt.decode(b);
//    				this.segway.setLIVEFRC((float) message[0]);
				break;
			//TODO WAS IST DAS ? :D ueberbleibsel von wann anders
			case 0x24:
    			message=CommProt.decode(b);
    			this.segway.startRight((int)message[0]);
    			break;
    		case 0x25:
    			message=CommProt.decode(b);
    			this.segway.startLeft((int)message[0]);
    			break;
    		/*****
    		 * PATH funktionen
    		 */
    			
    		/*
    		 * Changing to the PathMode TODO
    		 * FIXME brauchen wir das?
    		 */
    		case 0x30:
    			
    			break;
    			
    		/*
    		 * Decode Path Value
    		 */
    		case 0x31:
    			print("Recieved Path");
    			message=CommProt.decode(b);
    			if(PathThread.isFinished) PathThread = new PathThread(segway);
    			if (message[0]==0){
    				try {
						PathThread.addCMD(new DriveCMD((int)message[1]));
						print("Added Path");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			} else if (message[1]==0){
    				try {
						PathThread.addCMD(new TurnCMD((int)message[0]));
						print("Added Path");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			break;
    		case 0x32:
    			
    			
    			break;
    		case 0x33:
				try {
					print("Start Path");
					if(!PathThread.running) PathThread.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			break;
    		case 0x34:
    			segway.pause();
    			break;
    		case 0x35:
    			segway.resume();
    			break;
    		case 0x36:
    			PathThread.stopPath();
    			break;
			/*
			 * SETKAVALUES	0x40	4*4Byte
			 * Setzt die K1-K4 Werte
			 */	
    		case 0x40:
    			message = CommProt.decode(b);
    			//K1
    			this.segway.setK_ANGULAR_VELO(message[0]);
    			print("Setting k1");
    			print(""+message[0]);
    			//K2
    			this.segway.setK_ANGLE(message[1]);
    			print("Setting k2");
    			print(""+message[1]);
    			//K3
    			this.segway.setK_MOTORPOS(message[2]);
    			print("Setting k3");
    			print(""+message[2]);
    			//K4
    			this.segway.setK_MOTORSPEED(message[3]);
    			print("Setting k4");
    			print(""+message[3]);
    			print("Finished");
    			break;
    		case 0x50:
    			message = CommProt.decode(b);
    			this.segway.setSpeed((int)message[0]);
    			print("Set Segwayspeed");
    			print(""+message[0]);
    			break;
    	}
    	//SEND ACK
    	dos.write(0b01111100);
    	dos.flush();
	}
	
	private void directcont() {
		if(this.PathThread.isAlive()){
			this.segway.pause();
		}
	}

	/**
	 * Sending logging values
	 * @author Philipp
	 * @throws IOException
	 */
	private void sendData() throws IOException{
//    	printclear();
//    	print("Sending Data");
    	//byte flag = 0x7E;
    	if(paketcounter==0b01111111){
    		paketcounter=0b1;
    	}
    	byte befehl = 0x20;
    	double[] daten = new double[11];
    	TimeStamp=System.currentTimeMillis()-TimeStampdiff;
    	//ANGLE
		//MotorPos Left
		//MotorPos Right
		//MotorSpeed
		//GyroSpeed
		//Offset
		//UltraSonic
		//X and Y
		//Akku
    	daten[0]= TimeStamp;
    	daten[1]= segway.getAngle();
    	daten[2]= segway.getMotorCountLeft();
    	daten[3]= segway.getMotorCountRight();
    	daten[4]= segway.getMotorspeed();
    	daten[5]= segway.getGyroSpeed();
    	daten[6]= segway.getOffset();
    	daten[7]= segway.getUltrasonicValue();
    	daten[8]= segway.getPosX();
    	daten[9]= segway.getPosY();
    	daten[10]= segway.getAkkuspannung();
//    	try {
//			sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	print("Trying encode");
    	byte[] sendingData = CommProt.encode(befehl, daten, TimeStamp ,paketcounter);
    	
    	for (byte b : sendingData) {
			dos.write(b);
		}
    	dos.flush();
    	paketcounter++;
    	timeout = 0;
//    	print("Success");
	}
	
	/**
	 * Just for debugging 
	 * @author Philipp
	 * @param string
	 */
	public void print(String string) {
		if(DEBUG){
			if(line==8){
				line=0;
				printclear();
			}
			LCD.drawString(string,0,line);
			line++;
		}
	}
	
	/**
	 * Just for debugging 
	 * @author Philipp
	 */
	private void printclear() {
		if(DEBUG){
			LCD.clear();
			line=0;
		}
	}
}