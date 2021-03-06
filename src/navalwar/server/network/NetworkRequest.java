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

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.IGameEngineModule;
import navalwar.server.gameengine.IGameEngineModule.ShotCodes;
import navalwar.server.gameengine.UnitAndPlace;
import navalwar.server.gameengine.exceptions.ArmyTurnTimeoutException;
import navalwar.server.gameengine.exceptions.InvalidUnitNameException;
import navalwar.server.gameengine.exceptions.NotTurnOfArmyException;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.exceptions.WarAlreadyFinishedException;
import navalwar.server.gameengine.exceptions.WarAlreadyStartedException;
import navalwar.server.gameengine.exceptions.WarDoesNotExistException;
import navalwar.server.gameengine.exceptions.WarNotStartedException;
import navalwar.server.gameengine.info.ArmyInfo;
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
					handleShot();
					break;
				case "SurrenderMsg":
					handleSurrender();
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
			outToClient.writeBytes("CREATINGERROR"+'\n');
			outToClient.writeBytes("Code:-100"+'\n');
			
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
	
	private void handleJoin() throws IOException{
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
		//System.out.println("WarID: "+warID);
		//System.out.println("War: "+ game.getWarInfo(warID).getName());
		//System.out.println("ArmyName: "+ armyName);
		//System.out.println("Units:");
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
		int armyID;
		try {
			armyID = game.regArmy(warID, armyName, units);
			outToClient.writeBytes("ArmyIDMsg"+'\n');
			outToClient.writeBytes("armyID:"+armyID+'\n');
			
		
			/*
			 * este if se realizo con finalidades del test, ya que al no existir una verdadera conexion 
			 * el SocketMock no se comunicara con todos los clientes
			 * ademas de ser parte de algo q no se busca probar aun; en un futuro si se aplicarian 
			 * test sobre este
			 */
			if(!(s instanceof SocketMock)){
				((GameEngineModule) game).net.armyJoined(warID,armyID,armyName);
				ServerNetworkModule.putArmyWarIpMap(new ArmyWarEntry(warID, armyID), s.getInetAddress().getHostAddress());
				updateEnemies(warID,armyID);
			}
			
			
		} catch (WarDoesNotExistException e) {
			outToClient.writeBytes("JOINERROR"+'n');
			outToClient.writeBytes("Code:-103"+'n');
			//e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			outToClient.writeBytes("JOINERROR"+'n');
			outToClient.writeBytes("Code:-103"+'n');
			//e.printStackTrace();
		} catch (WarAlreadyStartedException e) {
			outToClient.writeBytes("JOINERROR"+'n');
			outToClient.writeBytes("Code:-103"+'n');
			//e.printStackTrace();
		} catch (InvalidUnitNameException e) {
			outToClient.writeBytes("JOINERROR"+'n');
			outToClient.writeBytes("Code:-103"+'n');
			//e.printStackTrace();
		} catch (PlaceNotFreeToPlaceUnitException e) {
			outToClient.writeBytes("JOINERROR"+'n');
			outToClient.writeBytes("Code:-103"+'n');
			//e.printStackTrace();
		} catch (UnitCoordinatesOutsideMatrixException e) {
			outToClient.writeBytes("JOINERROR"+'n');
			outToClient.writeBytes("Code:-103"+'n');
			//e.printStackTrace();
		}
		
		
		
	}
	
	private void handleStart() throws IOException{
		String warIDMsg,armyIDMsg;
		warIDMsg = br.readLine();
		armyIDMsg = br.readLine();
		StringTokenizer warIDTokenizer = new StringTokenizer(warIDMsg);
		StringTokenizer armyIDTokenizer = new StringTokenizer(armyIDMsg);
		warIDTokenizer.nextToken(":");
		int warID = Integer.parseInt(warIDTokenizer.nextToken());
		armyIDTokenizer.nextToken(":");
		int armyID = Integer.parseInt(armyIDTokenizer.nextToken());
		try {
			//corregido con el test de integracion:
			if(game.getArmies(warID).size()<=1){
				outToClient.writeBytes("STARTERROR"+'\n');
				outToClient.writeBytes("Code:-101"+'\n');
			}
			else{
				game.startWar(warID);
				getTurn(warID);
			}
			
		} catch (WarDoesNotExistException e) {
			System.out.println("inicindo una guerra que no existe");
			outToClient.writeBytes("STARTERROR"+'\n');
			outToClient.writeBytes("Code:-101"+'\n');
		
		} catch (WarAlreadyStartedException e) {
			System.out.println("la guerra ya ha sido iniciada");
			outToClient.writeBytes("STARTERROR"+'\n');
			outToClient.writeBytes("Code:-101"+'\n');
			
		} catch (WarAlreadyFinishedException e) {
			System.out.println("la guerra ya ha terminado");
			outToClient.writeBytes("STARTERROR"+'\n');
			outToClient.writeBytes("Code:-101"+'\n');
			
		}
		
		
		
	}
	
	private void getTurn(int warID){
		try {
			int armyID = game.getNextTurn(warID);
			((GameEngineModule) game).net.turnArmy(warID, armyID);
		} catch (WarDoesNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WarAlreadyFinishedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WarNotStartedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void handleShot() throws IOException{
		String warIDMSg,attackerIDMsg,attackedIDMsg,XMsg,YMsg;
		warIDMSg = br.readLine();
		attackerIDMsg = br.readLine();
		attackedIDMsg = br.readLine();
		XMsg = br.readLine();
		YMsg = br.readLine();
		StringTokenizer warIDTokenizer = new StringTokenizer(warIDMSg);
		StringTokenizer attackerIDTokenizer = new StringTokenizer(attackerIDMsg);
		StringTokenizer attackedIDTokenizer = new StringTokenizer(attackedIDMsg);
		StringTokenizer XTokenizer = new StringTokenizer(XMsg);
		StringTokenizer YTokenizer = new StringTokenizer(YMsg);
		warIDTokenizer.nextToken(":");
		attackerIDTokenizer.nextToken(":");
		attackedIDTokenizer.nextToken(":");
		XTokenizer.nextToken(":");
		YTokenizer.nextToken(":");
		int warID = Integer.parseInt(warIDTokenizer.nextToken());
		int attackerID = Integer.parseInt(attackerIDTokenizer.nextToken());
		int attackedID = Integer.parseInt(attackedIDTokenizer.nextToken());
		int x = Integer.parseInt(XTokenizer.nextToken());
		int y = Integer.parseInt(YTokenizer.nextToken());
		try {
			ShotCodes code = game.handleShot(warID, attackerID, attackedID, x, y);
			((GameEngineModule) game).net.broadcastShot(warID, attackerID, attackedID, x, y,code);
			
			
		} catch (WarDoesNotExistException e) {
			outToClient.writeBytes("ERRORSHOT"+'\n');
			outToClient.writeBytes("Code:-106"+'\n');
		} catch (ArmyTurnTimeoutException e) {
			outToClient.writeBytes("ERRORSHOT"+'\n');
			outToClient.writeBytes("Code:-106"+'\n');
		} catch (NotTurnOfArmyException e) {
			outToClient.writeBytes("ERRORSHOT"+'\n');
			outToClient.writeBytes("Code:-106"+'\n');
		} catch (WarNotStartedException e) {
			outToClient.writeBytes("ERRORSHOT"+'\n');
			outToClient.writeBytes("Code:-106"+'\n');
		} catch (WarAlreadyFinishedException e) {
			outToClient.writeBytes("ERRORSHOT"+'\n');
			outToClient.writeBytes("Code:-106"+'\n');
		}
	}
	
	private void updateEnemies(int warID, int armyID) throws IOException{
		List<Integer> enemies = game.getArmies(warID);
		outToClient.writeBytes("EnemyListMsg"+'\n');
		outToClient.writeBytes(""+(enemies.size()-1)+'\n');
		for(Integer i : enemies){
			if(i==armyID)
				continue;
			ArmyInfo info = (ArmyInfo) game.getArmyInfo(warID, i);
			outToClient.writeBytes("armyID:"+info.getArmyID()+'\n');
			outToClient.writeBytes("armyName:"+info.getName()+'\n');
			
		}
	}
	
	private void handleSurrender(){
		
	}
	
	//Estos metodos sirven para los test
	public void setBufferedReader(BufferedReader br){
		this.br = br;
	}
	
	public void setOutToClient(DataOutputStream ds){
		outToClient = ds;
	}

	public void setGameEngine(IGameEngineModule game){
		this.game = game;
	}

}
