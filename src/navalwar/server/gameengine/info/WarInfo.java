package navalwar.server.gameengine.info;

import java.util.ArrayList;
import java.util.List;

import navalwar.server.gameengine.Army;
import navalwar.server.gameengine.War;

public class WarInfo implements IWarInfo {
	
	private int warID;
	private String name;
	private String desc;
	private int numArmies;
	private int numFightingArmies;
	private List<Integer> armiesIDs;
	
	public WarInfo(War war) {
		warID = war.getWarID();
		name = war.getName();
		desc = war.getDesc();
		numArmies = war.getNumArmies();
		numFightingArmies = war.getNumFightingArmies();
		armiesIDs = new ArrayList<Integer>();
		for(Army army : war.getArmies()) armiesIDs.add(army.getArmyID());
	}

	public int getWarID() { return warID; }
	public String getName() { return name; }
	public String getDesc() { return desc; }
	public int getNumArmies() { return numArmies; }
	public int getNumFightingArmies() {	return numFightingArmies; }
	public List<Integer> getArmiesIDs() { return armiesIDs; }

}
