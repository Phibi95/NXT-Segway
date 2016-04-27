package comm;

import java.util.ArrayList;

/**
 * 
 * @author Jacky, Justin
 * 
 *
 */
public class HttpThread extends Thread {
	private HttpCall call = new HttpCall("12345");
	private String UserID = "USER";
	private String SID = "";
	LogType log = new LogType();
	Communication c;
	Long time;
	ArrayList<LogType> temp;

	public HttpThread(Communication c) {
		this.c = c;
		time = System.currentTimeMillis();
	}

	@Override
	public void run() {
		if (System.currentTimeMillis() - time > 50) {
			synchronized (c.Log) {
				call.addLogList(c.Log);
				call.excutePost(call.toJson());
				c.Log.clear();
			}
		}
	}
}
