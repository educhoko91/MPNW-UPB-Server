package navalwar.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

import navalwar.server.gameengine.IGameEngineModule;

public class EngineRequest implements IServerNetworkModule{

	private IGameEngineModule game;
	private Socket s;

	public EngineRequest(IGameEngineModule game, Socket s){
		this.game = game;
		this.s = s;
	}
	


	@Override
	public void bindGameEngineModule(IGameEngineModule game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int startWar(int warID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int turnArmy(int warID, int armyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int turnArmyTimeout(int warID, int armyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int armyKicked(int warID, int armyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int endWar(int warID, int winnerArmyID) {
		// TODO Auto-generated method stub
		return 0;
	}

}
