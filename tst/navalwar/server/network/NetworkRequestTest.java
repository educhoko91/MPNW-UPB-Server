package navalwar.server.network;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.IGameEngineModule;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class NetworkRequestTest {

	private Socket socketMock;
	private BufferedReader brMock;
	private NetworkRequest req;
	private DataOutputStream outToClientMock;
	
	@Before
	public void setup() throws IOException{
		socketMock = new SocketMock(); 
		brMock = new BufferedReaderMock(new InputStreamReader(socketMock.getInputStream()));
		outToClientMock = EasyMock.createNiceMock(DataOutputStream.class);
	}
	/*
	 * Test de integracion que se enfoca en probar que de recibir datos correctos para crear una
	 * guerra siempre creara una guerra correcta
	 * 
	 * Expected no haya excepciones
	 */
	
	@Test
	public void createGameTest() throws IOException{
		req = new NetworkRequest(socketMock, GameEngineModule.getInstance());
		req.setBufferedReader(brMock);
		req.setOutToClient(outToClientMock);
		outToClientMock.writeBytes("WarIDMsg"+'\n');
		outToClientMock.writeBytes(""+EasyMock.anyInt()+'\n');
		EasyMock.replay(outToClientMock);
		req.run();
		
	}

}
