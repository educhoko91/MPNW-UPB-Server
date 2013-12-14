package navalwar.server.gameengine.info;

import java.util.List;

public interface IWarInfo {

	public int getWarID();
	public String getName(); 
	public String getDesc(); 
	public int getNumArmies();
	public int getNumFightingArmies(); 
	public List<Integer> getArmiesIDs(); 

}
