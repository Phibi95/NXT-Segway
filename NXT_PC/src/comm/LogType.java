package comm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Philipp
 *
 */
public class LogType {
	Date time;
	Long nano;
	
	Map<String,Long> ListLong = new HashMap<String,Long>();
	Map<String,Float> ListFloat = new HashMap<String,Float>();
	Map<String,Double> ListDouble = new HashMap<String,Double>();
	Map<String,Integer> ListInteger = new HashMap<String,Integer>();
	
	/**
	 * @author Philipp
	 */
	public LogType(){
		time = new Date();
		nano = System.nanoTime();
	}
	
	/**
	 * @author Philipp
	 * @param date
	 */
	public LogType(Date date){
		time = date;
	}
	
	/**
	 * @author Philipp
	 * @param value
	 * @param name
	 */
	public void add(Long value, String name){
		ListLong.put(name, value);
	}
	
	/**
	 * @author Philipp
	 * @param value
	 * @param name
	 */
	public void add(Integer value, String name){
		ListInteger.put(name, value);
	}
	
	/**
	 * @author Philipp
	 * @param value
	 * @param name
	 */
	public void add(Double value, String name){
		ListDouble.put(name, value);
	}
	
	/**
	 * @author Philipp
	 * @param value
	 * @param name
	 */
	public void add(Float value, String name){
		ListFloat.put(name, value);
	}
	
	/*
	 * "0": {
				"X" : -30,
				"Y" : -14,
				"GYROSPEED" : 642,
				"MOTORPOSLEFT" : 6024,
				"MOTORPOSRIGHT" : 5231,
				"OFFSET" : 849,
				"ANGLE" : 28,
				"TIMESTAMP" : 0
				}
	 */
	
	/**
	 * @author Philipp
	 * @return
	 */
	public String toJSON(){
		SimpleDateFormat ft =  new SimpleDateFormat ("yyyy.MM.dd-hh:mm:ss:SSS");
		String jsonItem = "\t\t\""+ft.format(time)+":"+nano+"\": {\n";
		String praefix = "\t\t\t";
		//Double
		String[] hilf = MaptoJson(ListDouble);
		for (String string : hilf) {
			jsonItem = jsonItem + praefix + string +",\n";
		}
		
		//Float
		hilf = MaptoJson(ListFloat);
		for (String string : hilf) {
			jsonItem = jsonItem + praefix + string +",\n";
		}
		
		//Integer
		hilf = MaptoJson(ListInteger);
		for (String string : hilf) {
			jsonItem = jsonItem + praefix + string +",\n";
		}
		
		//Long
		hilf = MaptoJson(ListLong);
		int x = hilf.length;
		for (String string : hilf) {
			if(x<=1){
				jsonItem = jsonItem + praefix + string +"\n";
			} else {
				jsonItem = jsonItem + praefix + string +",\n";
			}
			x--;
		}
		jsonItem = jsonItem +"\t\t}";
		return jsonItem;
	}
	
	/**
	 * @author Philipp
	 * @param list
	 * @return
	 */
	private String[] MaptoJson(Map list){
		String[] result = new String[list.size()];
		int i=0;
		Iterator it = list.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        result[i] = "\""+ pair.getKey().toString().toUpperCase() + "\"" + " : " + "\"" + pair.getValue() + "\"";
	        it.remove(); // avoids a ConcurrentModificationException
	        i++;
	    }
	    return result;
	}
	
	public String toString(){
		return"";
	}
	
}
