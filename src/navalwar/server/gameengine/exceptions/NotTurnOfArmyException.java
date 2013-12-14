package navalwar.server.gameengine.exceptions;

public class NotTurnOfArmyException extends Exception {

	private int armyID;
	
	public NotTurnOfArmyException(int armyID) {
		this.armyID = armyID;
	}
	
	public int getArmyID() { return armyID; }
	
	public String toString() {
		return super.toString() + " armyID: " + armyID + "\n";
	}


}
