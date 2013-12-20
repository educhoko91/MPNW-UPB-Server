package navalwar.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map.Entry;

import navalwar.server.gameengine.IGameEngineModule;

public class EngineRequest implements IServerNetworkModule{

	private IGameEngineModule game;
	

	public EngineRequest(IGameEngineModule game){
		this.game = game;
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
		
		List<Integer> armies = game.getArmies(warID);
		ArmyWarEntry armywar = null;
		for(Integer i : armies){
			for(Entry<ArmyWarEntry, String> e : ServerNetworkModule.armyWarIpMap.entrySet()){
				armywar = e.getKey();
				if(armywar.getArmyID()==i && armywar.getWarID()==warID){
					break;
				}
			}
			String ip = ServerNetworkModule.armyWarIpMap.get(armywar);
			Socket s = ServerNetworkModule.ipSocketMap.get(ip);
			try {
				DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
				outToClient.writeBytes("NextTurnMsg"+'\n');
				outToClient.writeBytes("nextTurn:"+armyID+'\n');
				System.out.println("enviado siguiente turno: "+armyID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
