package navalwar.server.network;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
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
	
	@Before
	public void setup() throws IOException{
		socketMock = EasyMock.createMock(Socket.class);
	}
	/*
	 * Test de integracion que se enfoca en probar que de recibir datos correctos para crear una
	 * guerra 
	 */
	
	@Test
	public void createGameTest() throws IOException{
		req = new NetworkRequest(socketMock, GameEngineModule.getInstance());
		
		
	}

}
