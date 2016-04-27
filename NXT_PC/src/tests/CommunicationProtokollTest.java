package tests;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import comm.CommunicationProtokoll;

/**
 * 
 * @author Philipp
 *
 */
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
		byte[] result = this.CommProt.writeDouble(99.00);
		System.out.println(Integer.toBinaryString(result[0]));
		System.out.println(Integer.toBinaryString(result[1]));
		System.out.println(Integer.toBinaryString(result[2]));
		System.out.println(Integer.toBinaryString(result[3]));
		System.out.println(Integer.toBinaryString(result[4]));
		System.out.println(Integer.toBinaryString(result[5]));
		System.out.println(Integer.toBinaryString(result[6]));
		System.out.println(Integer.toBinaryString(result[7]));
		
		System.out.println(""+ 99 +": "+Arrays.toString(this.CommProt.writeDouble(99.00)));
		System.out.println("TEST : "+CommProt.readDouble(this.CommProt.writeDouble(99.00)));
		fail("Not yet implemented");
	}

	@Test
	public void testFromInttoByte() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromBytetoDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testFromBytetoFloat() {
		fail("Not yet implemented");
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
	public void testEncode(){
		byte befehl = 0x40;
    	double[] daten = new double[13];
    	long TimeStamp=System.currentTimeMillis();
    	daten[0]=TimeStamp;
    	daten[1]=0.1;
    	daten[2]= 0.001;
    	daten[3]=1;
    	daten[4]= 874.68;
    	daten[5]= -70.56;
    	daten[6]=12;
    	daten[7]=0;
    	
    	byte paketcounter=0b11;
		byte[] sendingData = CommProt.encode(befehl, daten, TimeStamp ,paketcounter);
		System.out.println(Arrays.toString(sendingData));
		double[] decoded = CommProt.decode(sendingData);
		System.out.println(Arrays.toString(decoded));
	}

}
