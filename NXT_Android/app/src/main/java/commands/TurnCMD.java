package commands;

public class TurnCMD extends PathCMD{
	public TurnCMD(int value){
		this.setValue(value);
	}

	public String getType() {
		return "Turn";
	}
}
