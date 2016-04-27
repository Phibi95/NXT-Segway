package tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import comm.CommunicationProtokoll;

public class CommunicationProtokollTest {
	static CommunicationProtokoll CommProt;
	
	@BeforeClass
	public static void init(){
		CommProt = new CommunicationProtokoll();
	}

	@Test
	public void testFromShorttoByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromFloattoByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromDoubletoByte() {
		double testvalue = 99.00;
		boolean showprint=false;
		byte[] result = this.CommProt.writeDouble(testvalue);
		for (byte b : result) {
			if(showprint)System.out.println(Integer.toBinaryString(b));
		}
//		System.out.println(Integer.toBinaryString(result[0]));
//		System.out.println(Integer.toBinaryString(result[1]));
//		System.out.println(Integer.toBinaryString(result[2]));
//		System.out.println(Integer.toBinaryString(result[3]));
//		System.out.println(Integer.toBinaryString(result[4]));
//		System.out.println(Integer.toBinaryString(result[5]));
//		System.out.println(Integer.toBinaryString(result[6]));
//		System.out.println(Integer.toBinaryString(result[7]));
		
		if(showprint)System.out.println(""+ testvalue +": "+Arrays.toString(this.CommProt.writeDouble(testvalue)));
		if(showprint)System.out.println("TEST : "+CommProt.readDouble(this.CommProt.writeDouble(testvalue)));
		if(showprint)fail("Check chat.");
	}

	@Test
	public void testFromInttoByte() {
		int testvalue = 255;
		boolean showprint=false;
		byte[] result = this.CommProt.writeInt(testvalue);
		for (byte b : result) {
			if(showprint)System.out.println(Integer.toBinaryString(b));
		}
		if(showprint)System.out.println(""+ testvalue +": "+Arrays.toString(this.CommProt.writeInt(testvalue)));
		if(showprint)System.out.println("TEST : "+CommProt.readInt(this.CommProt.writeInt(testvalue)));
	}

	@Test
	public void testFromBytetoDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromBytetoFloat() {
		float testvalue = 1203.09f;
		boolean showprint=false;
		byte[] result = this.CommProt.writeFloat(testvalue);
		for (byte b : result) {
			if(showprint)System.out.println(Integer.toBinaryString(b));
		}
		if(showprint)System.out.println(""+ testvalue +": "+Arrays.toString(this.CommProt.writeFloat(testvalue)));
		if(showprint)System.out.println("TEST : "+CommProt.readFloat(this.CommProt.writeFloat(testvalue)));
	}

	@Test
	public void testFromBytetoInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromBytetoShort() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testEncode0x20(){
		boolean showprint = true;
		if(showprint)System.out.println("Testing 0x20 encoding!");
		byte befehl = 0x20;
    	double[] daten = new double[9];
    	long TimeStamp=System.currentTimeMillis();
    	/*
		 * daten[0]= TimeStamp;						//LONG
		 * daten[1]= segway.getMotorspeed();		//FLOAT
		 * daten[2]= segway.getGyroSpeed();			//FLOAT
		 * daten[3]= segway.getTacho();				//FLOAT
		 * daten[4]= segway.getOffset();			//FLOAT
		 * daten[5]= segway.getAngle();				//FLOAT
		 * daten[6]= segway.getPwrLeft();			//SHORT
		 * daten[7]= segway.getPwrRight();			//SHORT
		 * daten[8]= segway.getAkkuspannung();		//SHORT
		 */
    	daten[0]=TimeStamp;
    	daten[1]=0.1;
    	daten[2]= 0.001;
    	daten[3]=1;
    	daten[4]= 874.68;
    	daten[5]= -70.56;
    	daten[6]=12;
    	daten[7]=122;
    	daten[8]=20;
    	
    	byte paketcounter=0b11;
		byte[] sendingData = CommProt.encode(befehl, daten, TimeStamp ,paketcounter);
		if(showprint)System.out.println("Length: " + sendingData.length);
		if(showprint)System.out.println(Arrays.toString(sendingData));
		double[] decoded = CommProt.decode(sendingData);
		if(showprint)System.out.println(Arrays.toString(daten));
		if(showprint)System.out.println(Arrays.toString(decoded));
	}
	
	@Test
	public void testEncode0x40(){
		boolean showprint = true;
		if(showprint)System.out.println("Testing 0x40 encoding!");
		byte befehl = 0x40;
		double[] daten = new double[4];
    	long TimeStamp=System.currentTimeMillis();
    	daten[0]= 0.111f;
    	daten[1]= 0.5f;
    	daten[2]= 7.12f;
    	daten[3]= 1;
    	
    	byte paketcounter=0b11;
		byte[] sendingData = CommProt.encode(befehl, daten, TimeStamp ,paketcounter);
		if(showprint)System.out.println(Arrays.toString(sendingData));
		double[] decoded = CommProt.decode(sendingData);
		if(showprint)System.out.println(Arrays.toString(daten));
		if(showprint)System.out.println(Arrays.toString(decoded));
	}
	
	@Test
	public void testEncode0x31(){
		boolean showprint = true;
		if(showprint)System.out.println("Testing 0x31 encoding!");
		byte befehl = 0x31;
		double[] daten = new double[2];
    	long TimeStamp=System.currentTimeMillis();
    	daten[0]= 360;
    	daten[1]= 255;
    	
    	byte paketcounter=0b11;
		byte[] sendingData = CommProt.encode(befehl, daten, TimeStamp ,paketcounter);
		if(showprint)System.out.println(Arrays.toString(sendingData));
		double[] decoded = CommProt.decode(sendingData);
		if(showprint)System.out.println(Arrays.toString(daten));
		if(showprint)System.out.println(Arrays.toString(decoded));
	}

}
