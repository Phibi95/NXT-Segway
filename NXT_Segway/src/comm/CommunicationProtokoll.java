package comm;
import java.lang.Double;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Philipp
 *
 */
public class CommunicationProtokoll {
	
	Map<Integer,String[]> befehle= new HashMap<Integer,String[]>();;
	
	public CommunicationProtokoll(){
		Integer key;
		String[] value;
		key=0x11;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x12;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x13;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x14;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x15;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x16;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x17;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x18;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x19;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x20;
		value=new String[]{"D","F","I","I","F","F","F","I","I","I","S"};
		befehle.put(key, value);
		
		key=0x24;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x25;
		value=new String[]{"I"};
		befehle.put(key, value);
		
		key=0x31;
		value=new String[]{"I","I"};
		befehle.put(key, value);
		
		key=0x40;
		value=new String[]{"F","F","F","F"};
		befehle.put(key, value);
		
		key=0x50;
		value=new String[]{"I"};
		befehle.put(key, value);
	}
	
	/**
	 * Encodes the values you want to send.
	 * @author Philipp
	 * @param befehl CMD in Hexadecimal
	 * @param values Array of values you want to send. (DOUBLE)
	 * @param time long current timestamp
	 * @param paketnum byte the packet number
	 * @return
	 */
	public byte[] encode(byte befehl, double[] values, long time, byte paketnum){
		Byte flag = 0x7E;
		int headerlength = 3;
		byte[] hilf;
		int i = 0;
		if(befehle.containsKey((int)befehl)){
			String[] encodearray = befehle.get((int)befehl);
			int hilfarraypos = 3;
			int valuearraypos = 0;
			int sizeofencodedarray = 0;
			byte[] temp;
			
			//First calculate the Size of the result byte array...
			for (String string : encodearray) {
				switch(string){
				case "L":
				case "D":
					sizeofencodedarray +=8;
					break;
				case "F":
				case "I":
					sizeofencodedarray +=4;
					break;
				case "S":
					sizeofencodedarray +=2;
					break;
				}
			}
			
			//create the array
			hilf = new byte[sizeofencodedarray+3];
			hilf[0] =flag;
			hilf[1] =paketnum;
			hilf[2] =befehl;
			for (String string : encodearray) {
				switch(string){
					case "L":
						long value_l = (long)values[valuearraypos];
						temp = writeLong(value_l);
						for (byte b : temp) {
							hilf[hilfarraypos]=b;
							hilfarraypos++;
						}
						break;
					case "D":
						double value_d = (double)values[valuearraypos];
						temp = writeDouble(value_d);
						for (byte b : temp) {
							hilf[hilfarraypos]=b;
							hilfarraypos++;
						}
						break;
					case "F":
						float value_f = (float)values[valuearraypos];
						temp = writeFloat(value_f);
						for (byte b : temp) {
							hilf[hilfarraypos]=b;
							hilfarraypos++;
						}
						break;
					case "I":
						int value_i = (int)values[valuearraypos];
						temp = writeInt(value_i);
						for (byte b : temp) {
							hilf[hilfarraypos]=b;
							hilfarraypos++;
						}
						break;
					case "S":
						short value_s = (short)values[valuearraypos];
						temp = writeShort(value_s);
						for (byte b : temp) {
							hilf[hilfarraypos]=b;
							hilfarraypos++;
						}
						break;
				}
				valuearraypos++;
			}
		} else {
			return null;
		}
		return hilf;
	}

	/**
	 * Decodes the values you got to an Array of Doubles
	 * @author Philipp
	 * @param message The message you want to decode
	 * @return The message is decoded and the header is already escaped
	 * 
	 */
		
	public double[] decode(byte[] message){
		double[] result = new double[1];
		int befehl= message[2];
		
		if(befehle.containsKey((int)befehl)){
			String[] decodearray = befehle.get((int)befehl);
			int resultarraypos = 0;
			int messagearraypos = 3;
			//First calculate the Size of the result double array...
			int sizeofdecodedarray = decodearray.length;
			
			//create the array
			result = new double[sizeofdecodedarray];
			for (String string : decodearray) {
				switch(string){
					case "L":
						byte[] value_l = new byte[8];
						for (int x = 0; x < 8; x++) {
							value_l[x]=message[messagearraypos];
							messagearraypos++;
						}
						
						result[resultarraypos] = (double)readLong(value_l);
						resultarraypos++;
						break;
					case "D":
						byte[] value_d = new byte[8];
						for (int x = 0; x < 8; x++) {
							value_d[x]=message[messagearraypos];
							messagearraypos++;
						}
						
						result[resultarraypos] = readDouble(value_d);
						resultarraypos++;
						break;
					case "F":
						byte[] value_f = new byte[4];
						for (int x = 0; x < 4; x++) {
							value_f[x]=message[messagearraypos];
							messagearraypos++;
						}
						
						result[resultarraypos] = (double)readFloat(value_f);
						resultarraypos++;
						break;
					case "I":
						byte[] value_i = new byte[4];
						for (int x = 0; x < 4; x++) {
							value_i[x]=message[messagearraypos];
							messagearraypos++;
						}
						
						result[resultarraypos] = (double)readInt(value_i);
						resultarraypos++;
						break;
					case "S":
						byte[] value_s = new byte[2];
						for (int x = 0; x < 2; x++) {
							value_s[x]=message[messagearraypos];
							messagearraypos++;
						}
						
						result[resultarraypos] = (double)readShort(value_s);
						resultarraypos++;
						break;
				}
			}
			return result;
		} else {
			return result;
		}
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */	
	public byte[] writeByte(double data){
		byte[] result = new byte[1];
		result[0] = (byte)data;
		return result;
	}
		
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public byte[] writeChar(char data) {
		byte[] result = new byte[2];
		result[0]=((byte) ((data >>> 8) & 0xFF));
		result[1]=((byte) ((data >>> 0) & 0xFF));
		return result;
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public byte[] writeShort(int data) {
		byte[] result = new byte[2];
		result[0]=((byte) ((data >>> 8) & 0xFF));
		result[1]=((byte) ((data >>> 0) & 0xFF));
		return result;
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public byte[] writeInt(int data) {
		byte[] result = new byte[4];
		result[0]=((byte) ((data >>> 24) & 0xFF));
		result[1]=((byte) ((data >>> 16) & 0xFF));
		result[2]=((byte) ((data >>> 8) & 0xFF));
		result[3]=((byte) ((data >>> 0) & 0xFF));
		return result;
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public byte[] writeLong(long data) {
		byte[] result = new byte[8];
		result[0]=((byte) ((data >>> 56) & 0xFF));
		result[1]=((byte) ((data >>> 48) & 0xFF));
		result[2]=((byte) ((data >>> 40) & 0xFF));
		result[3]=((byte) ((data >>> 32) & 0xFF));

		result[4]=((byte) ((data >>> 24) & 0xFF));
		result[5]=((byte) ((data >>> 16) & 0xFF));
		result[6]=((byte) ((data >>> 8) & 0xFF));
		result[7]=((byte) ((data >>> 0) & 0xFF));
		return result;
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public byte[] writeFloat(float data) {
		return writeInt(Float.floatToIntBits(data));
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public byte[] writeDouble(double data) {
		return writeLong(Double.doubleToLongBits(data));
	}
	
	/**
	 * Read char represented as two bytes.
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public char readChar(byte[] data) {
		return (char) readUnsignedShort(data);
	}

	/**
	 * Read short represented as two bytes.
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public short readShort(byte[] data) {
		return (short) readUnsignedShort(data);
	}
	
	/**
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public int readUnsignedShort(byte[] data){
		int b1, b2;

		b1 = data[0] & 0xff;
		b2 = data[1] & 0xff;

		return ((b1 << 8) + b2);
	}

	/**
	 * Read int represented as four bytes.
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public int readInt(byte[] data) {
		return (((data[0] & 0xff) << 24) + ((data[1] & 0xff) << 16) + ((data[2] & 0xff) << 8) + ((data[3] & 0xff)));
	}

	/**
	 * Read long represented as eight bytes.
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public long readLong(byte[] data) {
		return (((long)readInt(data) << 32) + readInt(data));
	}
	
	/**
	 * Read float represented as four bytes.
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public double readFloat(byte[] data) {
		return Float.intBitsToFloat(readInt(data));
	}
	
	/**
	 * Read double represented as eight bytes.
	 * @author Philipp
	 * @param data The Data you want to convert to an Array of Bytes
	 * @return an Array of Bytes
	 */
	public double readDouble(byte[] data) {
		return Double.longBitsToDouble(readLong(data));
	}
}
