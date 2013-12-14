package navalwar.server.gameengine.exceptions;

public class WarAlreadyStartedException extends Exception {

	private int warID;
	
	public WarAlreadyStartedException(int warID) {
		this.warID = warID;
	}
	
	public String toString() {
		return super.toString() + " warID: " + warID + "\n";
	}

}
