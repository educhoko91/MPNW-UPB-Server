package navalwar.server.gameengine.exceptions;

public class WarDoesNotExistException extends Exception {

	private int warID;
	
	public WarDoesNotExistException(int warID) {
		this.warID = warID;
	}
	
	public String toString() {
		return super.toString() + " warID: " + warID + "\n";
	}
}
