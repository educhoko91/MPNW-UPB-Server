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
import navalwar.server.gameengine.UnitAndPlace;
import navalwar.server.gameengine.exceptions.InvalidUnitNameException;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.exceptions.WarAlreadyFinishedException;
import navalwar.server.gameengine.exceptions.WarAlreadyStartedException;
import navalwar.server.gameengine.exceptions.WarDoesNotExistException;
import navalwar.server.gameengine.info.IWarInfo;
import navalwar.server.gameengine.info.WarInfo;

public class NetworkRequest implements Runnable{
	private IGameEngineModule game;
	private Socket s;
	private DataOutputStream outToClient;
	private BufferedReader br;
	
	public NetworkRequest (Socket s,IGameEngineModule game) throws IOException{
		this.s = s;
		this.game = game;
		outToClient = new DataOutputStream(this.s.getOutputStream());
		br = new BufferedReader(new InputStreamReader(this.s.getInputStream())); 
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
		System.out.println("Cerrando thread");
		
	}
	
	private void listener() throws IOException, WarDoesNotExistException, WarAlreadyFinishedException, WarAlreadyStartedException, InvalidUnitNameException, PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException{
		boolean alive = true;
		while(alive){
			String inputLine;
			inputLine = br.readLine();
			StringTokenizer tokenizer = new StringTokenizer(inputLine);
			String request= tokenizer.nextToken();
			/////////////////////////////////////////////////////////
			System.out.println("LLamado por: " + s.getInetAddress());
			System.out.println("Mensaje : "+ inputLine);
			/////////////////////////////////////////////////////////
			switch(request){
				case "ListMsg" :
					handleWarListMsg();
					break;
				case "CreateWarMsg":
					creatingWar();
					break;
				case "StartMsg":
					handleStart();
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
					handleJoin();
					break;
				case "DISCONNECT":
					br.close();
					outToClient.close();
					s.close();
					alive = false;
					break;
				default :
					break;
			}
	
		}
	}
	
	private void creatingWar() throws IOException{
		String warNameMsg,warDescMsg,warName,warDesc;
		warNameMsg = br.readLine();
		warDescMsg = br.readLine();
		StringTokenizer nameTokenizer = new StringTokenizer(warNameMsg);
		StringTokenizer descTokenizer = new StringTokenizer(warDescMsg);
		nameTokenizer.nextToken(":");
		warName="";
		while(nameTokenizer.hasMoreTokens())
			warName += nameTokenizer.nextToken();
		descTokenizer.nextToken(":");
		warDesc="";
		while(descTokenizer.hasMoreTokens())
			warDesc += descTokenizer.nextToken();
		//////////////////////////////////////////////////
		System.out.println("warName = "+ warName);
		System.out.println("warDescription = "+ warDesc);
		//////////////////////////////////////////////////
		if(warName.isEmpty()){
			System.out.println("Nombre null en la partida, fallo al crear");
			outToClient.writeBytes("CREATINGERROR"+'n');
			outToClient.writeBytes("Code:-100"+'n');
		}
		else{
			int warID = game.createWar(warName, warDesc);
			String WarIDMsg = "WarIDMsg"+'\n';
			outToClient.writeBytes(WarIDMsg);
			String response = ""+warID+'\n';
			outToClient.writeBytes(response);
		}
	}

	private void handleWarListMsg() throws IOException{
		List<Integer> warIDs = game.getWarsList();
		String header ="GamesMsg"+'\n';
		outToClient.writeBytes(header);
		String numberOfWars = ""+ warIDs.size() + '\n';
		outToClient.writeBytes(numberOfWars);
		///////////////////////////////////
		System.out.println("mando header");
		///////////////////////////////////
		for(Integer i : warIDs){
			IWarInfo info = game.getWarInfo(i);
			String warID="warID:"+info.getWarID()+'\n';
			String warName="warName:"+info.getName()+'\n';
			outToClient.writeBytes(warID);
			outToClient.writeBytes(warName);
		}
		///////////////////////////////////////////////
		System.out.println("termino de mandar lista");
		///////////////////////////////////////////////
	}
	
	private void handleJoin() throws IOException, WarDoesNotExistException, WarAlreadyFinishedException, WarAlreadyStartedException, InvalidUnitNameException, PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException{
		String WarIDMsg,ArmyNameMsg,SizeMsg;
		List<UnitAndPlace> units = new ArrayList<UnitAndPlace>();
		WarIDMsg = br.readLine();
		ArmyNameMsg = br.readLine();
		SizeMsg = br.readLine();
		StringTokenizer warIDTokenizer = new StringTokenizer(WarIDMsg);
		StringTokenizer armyNameTokenizer = new StringTokenizer(ArmyNameMsg);
		StringTokenizer SizeTokenizer = new StringTokenizer(SizeMsg);
		warIDTokenizer.nextToken(":");
		int warID = Integer.parseInt(warIDTokenizer.nextToken());
		armyNameTokenizer.nextToken(":");
		String armyName = armyNameTokenizer.nextToken();
		SizeTokenizer.nextToken(":");
		//////////////////////////////////////////////////////
		System.out.println("WarID: "+warID);
		System.out.println("War: "+ game.getWarInfo(warID).getName());
		System.out.println("ArmyName: "+ armyName);
		System.out.println("Units:");
		//////////////////////////////////////////////////////
		int size = Integer.parseInt(SizeTokenizer.nextToken());
		for(int i=0 ; i<size ;i++){
			String unitName,UnitMsg,XMsg,YMsg;
			int x,y; 
			UnitMsg = br.readLine();
			XMsg = br.readLine();
			YMsg = br.readLine();
			StringTokenizer unitTokenizer = new StringTokenizer(UnitMsg);
			StringTokenizer XTokenizer = new StringTokenizer(XMsg);
			StringTokenizer YTokenizer = new StringTokenizer(YMsg);
			unitTokenizer.nextToken(":");
			unitName = unitTokenizer.nextToken();
			XTokenizer.nextToken(":");
			x = Integer.parseInt(XTokenizer.nextToken());
			YTokenizer.nextToken(":");
			y = Integer.parseInt(YTokenizer.nextToken());
			units.add(new UnitAndPlace(unitName, x, y));
			///////////////////////////////////////////////////
			System.out.println("Unit: "+unitName);
			System.out.println("X: " + x);
			System.out.println("Y: " + y);
			/////////////////////////////////////////////////////
		}
		int armyID = game.regArmy(warID, armyName, units);
		outToClient.writeBytes("ArmyIDMsg"+'\n');
		outToClient.writeBytes("armyID:"+armyID+'\n');
	}
	
	private void handleStart() throws IOException{
		String warIDMsg,armyIDMsg;
		warIDMsg = br.readLine();
		armyIDMsg = br.readLine();
		StringTokenizer warIDTokenizer = new StringTokenizer(warIDMsg);
		StringTokenizer armyIDTokenizer = new StringTokenizer(armyIDMsg);
		warIDTokenizer.nextToken(":");
		int warID = Integer.parseInt(warIDTokenizer.nextToken());
		int armyID = Integer.parseInt(armyIDTokenizer.nextToken());
		try {
			game.startWar(warID);
		} catch (WarDoesNotExistException e) {
			System.out.println("inicindo una guerra que no existe");
			e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			System.out.println("la guerra ya ha sido iniciada");
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			System.out.println("la guerra ya ha terminado");
			e.printStackTrace();
		}
	}
	


}
