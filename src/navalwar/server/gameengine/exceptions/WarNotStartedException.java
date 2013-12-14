package navalwar.server.gameengine.exceptions;

public class WarNotStartedException extends Exception {

	private int warID;
	
	public WarNotStartedException(int warID) {
		this.warID = warID;
	}
	
	public String toString() {
		return super.toString() + " --- warID: " + warID + "\n";
	}

}
