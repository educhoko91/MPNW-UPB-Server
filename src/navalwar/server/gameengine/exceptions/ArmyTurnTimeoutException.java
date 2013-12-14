package navalwar.server.gameengine.exceptions;

public class ArmyTurnTimeoutException extends Exception {
	
	private int armyID;

	public ArmyTurnTimeoutException(int armyID) {
		this.armyID = armyID;
	}
	
	public int getArmyID() { return armyID; }
	
	public String toString() {
		return super.toString() + " armyID: " + armyID + "\n";
	}


}
