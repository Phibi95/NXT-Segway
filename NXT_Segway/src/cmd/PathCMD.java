package cmd;

/**
 * 
 * @author Philipp
 *
 */
public abstract class PathCMD {
	private int value;
	public abstract String getType();
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
