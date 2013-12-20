package navalwar.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import navalwar.server.gameengine.GameEngineModule;
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

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareEverythingForTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({NetworkRequest.class,DataOutputStream.class})
public class NetworkRequestTest {

	private Socket socketMock,socketMock2;
	private BufferedReader brMock,brMock2;
	private NetworkRequest req,req2;
	private DataOutputStream outToClientMock,outToClientMock2;
	private IGameEngineModule gameMock;
	
	@Before
	public void setup() throws IOException{
		socketMock = new SocketMock(); 
		socketMock2 = new SocketMock();
		brMock = new BufferedReaderMock(new InputStreamReader(socketMock.getInputStream()));
		outToClientMock = PowerMock.createMock(DataOutputStream.class);
		outToClientMock2 = PowerMock.createMock(DataOutputStream.class);
		IGameEngineModule game = GameEngineModule.getInstance();
		brMock2 = PowerMock.createMock(BufferedReader.class);
		req = new NetworkRequest(socketMock, game);
		req2 = new NetworkRequest(socketMock2, game);
		req.setBufferedReader(brMock);
		req2.setBufferedReader(brMock2);
		req.setOutToClient(outToClientMock);
		req2.setOutToClient(outToClientMock2);
		((BufferedReaderMock) brMock).setGame(game);
		gameMock = PowerMock.createMock(GameEngineModule.class);
	}
	/*
	 * Testing sobre los metodos del Network request en integracion con el gameEngine
	 * especificamente testea el crear una guerra, unirse a ella y tratar de iniciarla
	 * 
	 * GrayBox test
	 * integration test
	 * 
	 * Si un cliente inicia una guerra siendo el unico ejercito en dicha guerra, la guerra no inicia
	 * 
	 * Expected that outToClientMock wryteBytes("STARTERROR")
	 */
	
	@Test
	public void createGameJoinAndStartTest() throws IOException{
		
		outToClientMock.writeBytes("WarIDMsg"+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes(""+EasyMock.anyInt()+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes("ArmyIDMsg"+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes("armyID:"+EasyMock.anyInt()+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes("STARTERROR"+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes("Code:-101"+'\n');
		PowerMock.expectLastCall();
		outToClientMock.close();
		PowerMock.expectLastCall();
		PowerMock.replay(outToClientMock);
		req.run();
		
		PowerMock.verify(outToClientMock);
		
		
	}
	/*
	 * Test realizado con el fin de cubrir el metodo list (White Box)
	 * test unitario
	 * Test unitario que simula el correcto listado de las guerras en un servidor,
	 * el oraculo espera que el test no lance excepciones
	 */
	@Test
	public void listTest() throws IOException{
		req.setGameEngine(gameMock);
		req.setBufferedReader(brMock2);
		EasyMock.expect(brMock2.readLine()).andReturn("ListMsg"+'\n');
		List<Integer> l = new ArrayList<Integer>();
		l.add(150);
		EasyMock.expect(gameMock.getWarsList()).andReturn(l);
		outToClientMock.writeBytes("GamesMsg"+'\n');
		EasyMock.expectLastCall();
		outToClientMock.writeBytes(""+l.size()+'\n');
		EasyMock.expectLastCall();
		IWarInfo infoMock = EasyMock.createMock(WarInfo.class);
		EasyMock.expect(gameMock.getWarInfo(150)).andReturn(infoMock);
		EasyMock.expect(infoMock.getWarID()).andReturn(150);
		EasyMock.expect(infoMock.getName()).andReturn("Test");
		outToClientMock.writeBytes("warID:150"+'\n');
		EasyMock.expectLastCall();
		outToClientMock.writeBytes("warName:Test"+'\n');
		EasyMock.expectLastCall();
		EasyMock.expect(brMock2.readLine()).andReturn("DISCONNECT"+'\n');
		outToClientMock.close();
		PowerMock.expectLastCall();
		brMock2.close();
		EasyMock.expectLastCall();
		EasyMock.replay(gameMock);
		EasyMock.replay(brMock2);
		PowerMock.replay(outToClientMock);
		EasyMock.replay(infoMock);
		req.run();
		EasyMock.verify(gameMock);
		EasyMock.verify(brMock2);
		EasyMock.verify(infoMock);
		PowerMock.verify(outToClientMock);
	}
	
	/*
	 * Test cuya finalidad es simular el acceso de 2 clientes al modulo red
	 */
	
	@Test
	public void TwoClientsTest() throws IOException, WarDoesNotExistException, WarAlreadyFinishedException, WarAlreadyStartedException, InvalidUnitNameException, PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException{
		
		req.setGameEngine(gameMock);
		req2.setGameEngine(gameMock);
		
		BufferedReader brMock3 = EasyMock.createMock(BufferedReader.class);
		req.setBufferedReader(brMock3);
		
		EasyMock.expect(brMock3.readLine()).andReturn("CreateWarMsg"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("warName:Test"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("warDesc:DescTest"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("JOIN"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("warID:150"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("armyName:TestArmy"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("size:1"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("Unit:Plane"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("X:5"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("Y:5"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("StartMsg"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("warID:150"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("armyID:1"+'\n');
		EasyMock.expect(brMock3.readLine()).andReturn("DISCONNECT"+'\n');
		brMock3.close();
		EasyMock.expectLastCall();
		
		outToClientMock.writeBytes("WarIDMsg"+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes("150"+'\n');
		PowerMock.expectLastCall();
		
		outToClientMock.writeBytes("ArmyIDMsg"+'\n');
		PowerMock.expectLastCall();
		outToClientMock.writeBytes("armyID:1");
		PowerMock.expectLastCall();
		outToClientMock.close();
		PowerMock.expectLastCall();
		
		outToClientMock2.writeBytes("GamesMsg"+'\n');
		PowerMock.expectLastCall();
		outToClientMock2.writeBytes("1"+'\n');
		PowerMock.expectLastCall();
		outToClientMock2.writeBytes("warID:150"+'\n');
		PowerMock.expectLastCall();
		outToClientMock2.writeBytes("warName:Test"+'\n');
		PowerMock.expectLastCall();
		outToClientMock2.writeBytes("ArmyIDMsg"+'\n');
		PowerMock.expectLastCall();
		outToClientMock2.writeBytes("armyID:2");
		PowerMock.expectLastCall();
		
		
		EasyMock.expect(brMock2.readLine()).andReturn("ListMsg"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("JOIN"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("warID:150"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("armyName:TestArmy2"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("size:1"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("Unit:Plane"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("X:2"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("Y:2"+'\n');
		EasyMock.expect(brMock2.readLine()).andReturn("DISCONNECT"+'\n');
		brMock2.close();
		EasyMock.expectLastCall();
		
		IWarInfo infoMock = EasyMock.createMock(WarInfo.class);
		EasyMock.expect(infoMock.getWarID()).andReturn(150);
		EasyMock.expect(infoMock.getName()).andReturn("Test");
		
		List<Integer> l = new ArrayList<Integer>();
		l.add(150);
		EasyMock.expect(gameMock.createWar("Test", "DescTest")).andReturn(150);
		EasyMock.expect(gameMock.getWarsList()).andReturn(l);
		EasyMock.expect(gameMock.getWarInfo(150)).andReturn(infoMock);
		
		List<UnitAndPlace> units1= new ArrayList<UnitAndPlace>();
		UnitAndPlace unitMock = EasyMock.createMock(UnitAndPlace.class);
		units1.add(unitMock);
		EasyMock.expect(gameMock.regArmy(150, "TestArmy1", units1)).andReturn(150);	
		List<UnitAndPlace> units2= new ArrayList<UnitAndPlace>();
		UnitAndPlace unitMock2 = EasyMock.createMock(UnitAndPlace.class);
		units1.add(unitMock2);
		EasyMock.expect(gameMock.regArmy(150, "TestArmy2", units2)).andReturn(150);	
		List<Integer> l2 = new ArrayList<Integer>();
		l2.add(1);
		l2.add(2);
		EasyMock.expect(gameMock.getArmies(150)).andReturn(l2);
		gameMock.startWar(150);
		EasyMock.expectLastCall();
		
		EasyMock.replay(gameMock);
		EasyMock.replay(brMock3);
		EasyMock.replay(brMock2);
		PowerMock.replay(outToClientMock);
		PowerMock.replay(outToClientMock2);
		EasyMock.replay(unitMock);
		EasyMock.replay(unitMock2);
		EasyMock.replay(infoMock);
		
		Thread t = new Thread(req);
		Thread t2 = new Thread(req2);
		t.start();
		t2.start();
		//req.run();req2.run();
		EasyMock.verify(gameMock);
		EasyMock.verify(brMock3);
		EasyMock.verify(brMock2);
		PowerMock.verify(outToClientMock);
		PowerMock.verify(outToClientMock2);
		EasyMock.verify(unitMock);
		EasyMock.verify(unitMock2);
		EasyMock.verify(infoMock);
	}

}
