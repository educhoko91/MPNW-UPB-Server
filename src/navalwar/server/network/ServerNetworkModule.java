package navalwar.server.network;

import java.util.HashMap;

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.IGameEngineModule;
import navalwar.server.gameengine.War;

public class ServerNetworkModule implements IServerNetworkModule {

	
	IGameEngineModule game = null;
	
	
	//--------------------------------------------
	// Constructors & singleton pattern
	//--------------------------------------------

	private ServerNetworkModule() {
		// TODO complete module contructor
	}
	
	private static ServerNetworkModule instance = null;
	public static ServerNetworkModule getInstance() {
		if (instance == null) instance = new ServerNetworkModule();
		return instance;
	}
	
	//--------------------------------------------
	// IServerNetworkModule methods
	//--------------------------------------------

	public void bindGameEngineModule(IGameEngineModule game) {
		this.game = game;
	}

	public int startWar(int warID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int turnArmy(int warID, int armyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int turnArmyTimeout(int warID, int armyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int armyKicked(int warID, int armyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int endWar(int warID, int winnerArmyID) {
		// TODO Auto-generated method stub
		return 0;
	}



}
