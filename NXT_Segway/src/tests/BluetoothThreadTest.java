package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import comm.BluetoothThread;

public class BluetoothThreadTest {
	@Test
	public void testDoubletoShort() {
		double testwert= 999.99;
		double result = DoubletoShort(testwert);
		//assertEquals("Double to Short funktioniert nicht",99999, result,0);
		result=FloattoShort(DoubletoFloat(testwert));
		assertEquals("Double->Float->Short",9999, result,0);
	}

	@Test
	public void testDoubletoFloat() {
		double testwert= 999.9;
		float result = DoubletoFloat(testwert);
		assertEquals("Double to Short funktioniert nicht",9999, result,0);
	}

	@Test
	public void testFloattoShort() {
		float testwert= 9999;
		short result = FloattoShort(testwert);
		assertEquals("Double to Short funktioniert nicht",9999, result,0);
	}

	@Test
	public void testShorttoByte() {
		short testwert= 9999;
		short result = FloattoShort(testwert);
		assertEquals("Double to Short funktioniert nicht",9999, result,0);
	}
	
	public static short DoubletoShort(double value){
		float temp1 = (float) value;
		temp1 = temp1*10;
		short result = (short) temp1;
		return result;
	}
	
	public static float DoubletoFloat(double value){
		return (float) (value*10);
	}
	
	public static short FloattoShort(float value){
		return (short) (value);
	}
	
	public static byte[] ShorttoByte(short value){
		byte[] result = new byte[2];
		result[0]=(byte)(value & 0xff);
		result[0]=(byte)((value >> 8) & 0xff);
		return result;
	}

}
