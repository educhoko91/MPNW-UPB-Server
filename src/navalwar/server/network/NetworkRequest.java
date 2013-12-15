package navalwar.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import navalwar.server.gameengine.IGameEngineModule;
import navalwar.server.gameengine.info.IWarInfo;
import navalwar.server.gameengine.info.WarInfo;

public class NetworkRequest implements Runnable,IServerNetworkModule {
	IGameEngineModule game;
	Socket s;
	
	public NetworkRequest (Socket s,IGameEngineModule game){
		this.s = s;
		this.game = game;
	}
	
	@Override
	public void run(){
		try {
			System.out.println("Connection established with host : " + s.getInetAddress() + "port: "+ s.getPort());
			listener();
		}catch(Exception e){
			if(e instanceof SocketException ){
				System.out.println("Connection loss");
			}
			else{
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void listener() throws IOException{
		while(true){
			String inputLine;
			InputStreamReader is = new InputStreamReader(this.s.getInputStream());
			BufferedReader br = new BufferedReader(is);
			
			inputLine = br.readLine();
			StringTokenizer tokenizer = new StringTokenizer(inputLine);
			String request= tokenizer.nextToken();
			System.out.println("LLamado por: " + s.getInetAddress());
			System.out.println("Mensaje : "+ inputLine);
			switch(request){
				case "ListMsg" :
					handleWarListMsg();
					break;
				case "CreateWarMsg":
					creatingWar(br);
					break;
				case "StartMsg":
					System.out.println("game.startWar(warID)");
					break;
				case "MovementMsg":
					System.out.println("game.handleShot()");
					break;
				case "SurrenderMsg":
					System.out.println("game.QuitArmy(armyID,warID)");
					break;
				case "ExitMsg":
					System.out.println("game.ExitArmy(armyID,warID)");
					break;
				case "JOIN":
					System.out.println("game.JOIN(warID)");
					break;
				default :
					break;
			}
	
		}
	}
	
	private void creatingWar(BufferedReader br) throws IOException{
		String warNameMsg,warDescMsg,warName,warDesc;
		warNameMsg = br.readLine();
		warDescMsg = br.readLine();
		StringTokenizer nameTokenizer = new StringTokenizer(warNameMsg);
		StringTokenizer descTokenizer = new StringTokenizer(warDescMsg);
		nameTokenizer.nextToken(":");
		warName = nameTokenizer.nextToken();
		descTokenizer.nextToken(":");
		warDesc = descTokenizer.nextToken();
		System.out.println("warName = "+ warName);
		System.out.println("warDescription = "+ warDesc);
		game.createWar(warName, warDesc);
	}

	private void handleWarListMsg() throws IOException{
		List<Integer> warIDs = game.getWarsList();
		List<IWarInfo> wars = new ArrayList<IWarInfo>();
		DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
		String header ="GamesMsg"+'\n';
		outToClient.writeBytes(header);
		String numberOfWars = ""+ warIDs.size() + '\n';
		outToClient.writeBytes(numberOfWars);
		System.out.println("mando header");
		for(Integer i : warIDs){
			IWarInfo info = game.getWarInfo(i);
			String register = info.getWarID()+";"+info.getName()+";"+info.getDesc()+";"+info.getNumArmies()+'\n';
			outToClient.writeBytes(register);
		}
		System.out.println("termino de mandar lista");
		
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
