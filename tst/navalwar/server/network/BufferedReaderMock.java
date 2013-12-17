package navalwar.server.network;

import java.io.BufferedReader;
import java.io.Reader;

public class BufferedReaderMock extends BufferedReader {
	public BufferedReaderMock(Reader arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public String readLineCreateMsg() {
		return "CreateMsg";
	}
}
