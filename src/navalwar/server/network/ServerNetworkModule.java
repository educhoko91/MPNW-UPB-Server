package navalwar.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.IGameEngineModule;
import navalwar.server.gameengine.IGameEngineModule.ShotCodes;
import navalwar.server.gameengine.War;

public class ServerNetworkModule implements IServerNetworkModule {

	
	IGameEngineModule game = null;
	ServerSocket ws;
	public static ConcurrentHashMap<String, Socket>ipSocketMap= new ConcurrentHashMap<String,Socket>();
	public static ConcurrentHashMap<ArmyWarEntry, String>armyWarIpMap= new ConcurrentHashMap<ArmyWarEntry,String>();
	
	public static void putArmyWarIpMap(ArmyWarEntry entry, String ip){
		armyWarIpMap.put(entry, ip);
	}
	
	//--------------------------------------------
	// Constructors & singleton pattern
	//--------------------------------------------

	private ServerNetworkModule() throws IOException {
		// TODO complete module contructor
		ws = new ServerSocket(6789);
		
	}
	
	private static ServerNetworkModule instance = null;
	public static ServerNetworkModule getInstance() throws IOException {
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

	public void receiveConnections() throws IOException{
		EngineRequest engineNet = new EngineRequest(game);
		game.bindNetModule(engineNet);
		
		while(true){
			Socket connectionSocket = ws.accept();
			ipSocketMap.put(connectionSocket.getInetAddress().getHostAddress(), connectionSocket);
			System.out.println(connectionSocket.getInetAddress().getHostAddress());
			NetworkRequest newConnection = new NetworkRequest(connectionSocket,game);
			Thread thread = new Thread(newConnection);
			thread.start();
		}
	}

	@Override
	public void armyJoined(int warID, int armyID, String armyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastShot(int warID, int attackerID, int attackedID, int x,
			int y,ShotCodes code) {
		// TODO Auto-generated method stub
		
	}


}
