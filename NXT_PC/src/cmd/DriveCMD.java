package cmd;

/**
 * 
 * @author Philipp
 *
 */
public class DriveCMD extends PathCMD{
	
	public DriveCMD(int val){
		this.setValue(val);
	}
	
	public String getType() {
		return "Drive";
	}
}
