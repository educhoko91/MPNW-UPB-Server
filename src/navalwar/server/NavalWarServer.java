package navalwar.server;

import java.io.IOException;

import navalwar.server.gameengine.GameEngineModule;
import navalwar.server.gameengine.IGameEngineModule;
import navalwar.server.network.IServerNetworkModule;
import navalwar.server.network.ServerNetworkModule;

public class NavalWarServer {
	
	public static void main(String[] args) throws IOException {
		IGameEngineModule game = GameEngineModule.getInstance();
		IServerNetworkModule net = ServerNetworkModule.getInstance();
		
		game.bindNetModule(net);
		net.bindGameEngineModule(game);
		((ServerNetworkModule) net).receiveConnections();
	}

}
