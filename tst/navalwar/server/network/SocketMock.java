package navalwar.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMock extends Socket{

	public SocketMock() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public InputStream getInputStream(){
		return new InputStreamMock();	
	}

	public OutputStream getOutputStream(){
		return new OutputStreamMock(); 
	}
	
}
