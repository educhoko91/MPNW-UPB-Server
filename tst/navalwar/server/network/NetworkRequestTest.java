package navalwar.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.IGameEngineModule;

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

	private Socket socketMock;
	private BufferedReader brMock;
	private NetworkRequest req;
	private DataOutputStream outToClientMock;
	
	@Before
	public void setup() throws IOException{
		socketMock = new SocketMock(); 
		brMock = new BufferedReaderMock(new InputStreamReader(socketMock.getInputStream()));
		outToClientMock = PowerMock.createMock(DataOutputStream.class);
	}
	/*
	 * GrayBox test
	 * integration test
	 * 
	 * Si un cliente inicia una guerra siendo el unico ejercito en dicha guerra, la guerra no inicia
	 * 
	 * Expected that outToClientMock wryteBytes("STARTERROR")
	 */
	
	@Test
	public void createGameTest() throws IOException{
		IGameEngineModule game = GameEngineModule.getInstance();
		req = new NetworkRequest(socketMock, game);
		req.setBufferedReader(brMock);
		req.setOutToClient(outToClientMock);
		((BufferedReaderMock) brMock).setGame(game);
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

}
