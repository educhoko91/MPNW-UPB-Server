package navalwar.server.network;

import navalwar.server.gameengine.IGameEngineModule;
import navalwar.server.gameengine.IGameEngineModule.ShotCodes;

/**
 * This interface must be implemented by Network module of the server.
 * It provides methods that enable the game engine module of the server to
 * notify the network module of events such as the start of a war (by someone),
 * the turn of an army, the turn timeout, the attack by an army, or the end of
 * the war.
 * 
 * @author Alfredo Villalba
 *
 */
public interface IServerNetworkModule {

	public void bindGameEngineModule(IGameEngineModule game);
	public int startWar(int warID);
	public int turnArmy(int warID, int armyID);
	public int turnArmyTimeout(int warID, int armyID) throws InterruptedException;
	public int armyKicked(int warID, int armyID);
	public int endWar(int warID, int winnerArmyID);
	public void armyJoined(int warID, int armyID, String armyName);
	public void broadcastShot(int warID, int attackerID, int attackedID, int x,
			int y, ShotCodes code);
	
}
