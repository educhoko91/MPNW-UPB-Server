package navalwar.server.network;

public class ArmyWarEntry {

	private int warID;
	private int armyID;
	
	public ArmyWarEntry(int warID, int armyID){
		this.armyID = armyID;
		this.warID = warID;
	}
	
	public int getArmyID(){
		return armyID;
	}
	public int getWarID(){
		return warID;
	}
	
	@Override
	public boolean equals(Object a){
		
		if(a instanceof ArmyWarEntry){
			ArmyWarEntry e = (ArmyWarEntry) a;
			if(this.warID == e.warID && this.armyID == e.armyID){
				return true;
			}
			else{
				return false;
			}
		}
		else
			return false;
	}
	
}
