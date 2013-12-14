package navalwar.server.gameengine.exceptions;

public class WarAlreadyFinishedException extends Exception {

	private int warID;
	
	public WarAlreadyFinishedException(int warID) {
		this.warID = warID;
	}
	
	public String toString() {
		return super.toString() + " warID: " + warID + "\n";
	}


}
