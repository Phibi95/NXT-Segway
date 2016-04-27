package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import comm.HttpCall;
import comm.LogType;

/**
 * 
 * @author Philipp
 *
 */
public class HttpcallTest {
	
	static HttpCall http;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		http = new HttpCall("123438"); 
	}

	@Test
	public void testHttpcall() {
		this.http = new HttpCall("123438"); 
	}

	@Test
	public void testToJson() {
		float minf = 800.0f;
		float maxf = 870.0f;
		int maxi = 100;
		int mini = -100;
		Random rand = new Random();
		int randomInt = 0;
		float randomFloat = 0f;
		for(long i=0;i<10;i++){
			LogType log = new LogType();
			randomInt = rand.nextInt((maxi - mini) + 1) + mini;
			log.add(randomInt, "X");
			randomInt = rand.nextInt((maxi - mini) + 1) + mini;
			log.add(randomInt, "Y");
			randomInt = rand.nextInt((900 - -900) + 1) + -900;
			log.add(randomInt, "GyroSpeed");
			
			minf = 800.0f;
			maxf = 870.0f;
			randomFloat = rand.nextFloat() * (maxf - minf) + minf;
			log.add(randomFloat, "Offset");
			
			minf = -45.0f;
			maxf = 45.0f;
			randomFloat = rand.nextFloat() * (maxf - minf) + minf;
			log.add(randomFloat, "Angle");
			
			/*
			 * "X" : '.mt_rand(-100,100).',
				"Y" : '.mt_rand(-100,100).',
				"GYROSPEED" : '.mt_rand(-900,900).',
				"MOTORPOSLEFT" : '.mt_rand(0,10000).',
				"MOTORPOSRIGHT" : '.mt_rand(0,10000).',
				"OFFSET" : '.mt_rand(800.25,870.99).',
				"ANGLE" : '.mt_rand(-45,45).'
			 */
			
			log.add(i,"TimeStamp");
			http.add(log);
		}
		
		String test = this.http.toJson();
		
//		System.out.println(test);
		
		System.out.println(this.http.excutePost(test));
	}

}
