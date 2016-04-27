package comm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/***
 * 
 * @author Philipp, Markus
 *
 */
public class HttpCall {
	
	String UserID = "999999";
	String SID = "TEST";
	String lastJson;
	ArrayList<LogType> LogArray = new ArrayList<LogType>();
	
	/**
	 * @author Philipp
	 * @param UserID
	 */
	public HttpCall(String UserID){
		this.UserID = UserID;
		this.SID = "0123aersdf40";
		lastJson="";
	}
	
	/**
	 * @author Philipp
	 * @param LogType
	 */
	public void add(LogType log){
		LogArray.add(log);
	}
	
	/**
	 * @author Philipp
	 * @param LogType
	 */
	public void addLogList(ArrayList<LogType> list){
		LogArray=(ArrayList<LogType>) list.clone();
	}
	
	/**
	 * @author Philipp
	 */
	public void clear(){
		LogArray.clear();
	}
	
	/*
	 * $itemJson = '{
	  "user_id" : "1000",
	  "sid": "921347843e4",
	  "hash" : "",
		"values": {';
	 * $itemJson = $itemJson.'
			"'.$u.'": {
				"X" : '.mt_rand(-100,100).',
				"Y" : '.mt_rand(-100,100).',
				"GYROSPEED" : '.mt_rand(-900,900).',
				"MOTORPOSLEFT" : '.mt_rand(0,10000).',
				"MOTORPOSRIGHT" : '.mt_rand(0,10000).',
				"OFFSET" : '.mt_rand(800.25,870.99).',
				"ANGLE" : '.mt_rand(-45,45).',
				"TIMESTAMP" : '.$u.'
				}';
	for ($i = $_GET['I_START']; $i < $_GET['I_END']; $i++) {
		$itemJson = $itemJson.',
			"'.$i.'": {
				"X" : '.mt_rand(-100,100).',
				"Y" : '.mt_rand(-100,100).',
				"GYROSPEED" : '.mt_rand(-900,900).',
				"MOTORPOSLEFT" : '.mt_rand(0,10000).',
				"MOTORPOSRIGHT" : '.mt_rand(0,10000).',
				"OFFSET" : '.mt_rand(800.25,870.99).',
				"ANGLE" : '.mt_rand(-45,45).',
				"TIMESTAMP" : '.$i.'
				}';
	}
	$itemJson = $itemJson.'
		}
    }';
	 */
	
	
	/**
	 * @author Philipp
	 * @return
	 */
	public String toJson(){
		String jsonItem = "{\n"+
				"\t\"user_id\" : \""+UserID+"\",\n"+
				"\t\"sid\": \""+SID+"\",\n"+
				"\t\"hash\" : \"\",\n"+
				"\t\"values\": {\n";
		int x = LogArray.size();
		for (LogType log : LogArray) {
			if(x<=1){
				jsonItem = jsonItem + log.toJSON() +"\n";
			} else {
				jsonItem = jsonItem + log.toJSON() +",\n";
			}
			x--;
		}
		
		jsonItem = jsonItem + "\t}\n"+
					"}";
		lastJson = jsonItem;
		return jsonItem;
	}
	
	/**
	 * @author Philipp
	 */
	public String excutePost(){
		return this.excutePost("http://zezation.me/NXT/recieve.php","");
	}
	
	/**
	 * @author Philipp
	 */
	public String excutePost(String json){
		return this.excutePost("http://zezation.me/NXT/recieve.php",json);
	}
	
	/**
	 * @author Philipp
	 */
	private String excutePost(String targetURL, String json){
		String urlParameters="";
		if(json.equals("")){
			//first create the Json String:
			urlParameters = this.toJson();
		} else {
			urlParameters = json;
		}
		System.out.println(urlParameters);
		byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
		int    postDataLength = postData.length;
		String request        = targetURL;
		URL url;
		try {
			System.out.println("Starting request!");
			url = new URL( request );
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput( true );
			conn.setInstanceFollowRedirects( false );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/json"); 
			conn.setRequestProperty( "charset", "utf-8");
			conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
			conn.setUseCaches( false );
			try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
				System.out.println("Writing");
			   wr.write( postData );
			   wr.flush();
			}
			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        for ( int c = in.read(); c != -1; c = in.read() ){
	        	System.out.print((char)c);
	        }
		} catch (Exception io){
			io.printStackTrace();
		}
		return "";
	}
}