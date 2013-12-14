package navalwar.server.gameengine.info;

import navalwar.server.gameengine.Army;

public class ArmyInfo implements IArmyInfo {

	public int armyID;
	public String name;
	public int numUnits;
	public int numShotedUnits;
	public int numDestroyedUnits;

	public ArmyInfo(Army army) {
		armyID = army.getArmyID();
		name = army.getName();
		numUnits = army.getNumUnits();
		numDestroyedUnits = army.getNumDestroyedUnits();
	}
	
	public int getArmyID() { return armyID;	}
	public String getName() { return name; }
	public int getNumUnits() { return numUnits; }
	public int getNumShotedUnits() { return numShotedUnits;	}
	public int getNumDestroyedUnits() { return numDestroyedUnits; }
	
}
