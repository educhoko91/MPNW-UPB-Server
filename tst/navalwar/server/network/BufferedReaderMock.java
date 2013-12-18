package navalwar.server.network;

import java.io.BufferedReader;
import java.io.Reader;

public class BufferedReaderMock extends BufferedReader {
	
	private int selector = 0; 
	
	public BufferedReaderMock(Reader arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public String readLine() {
		switch(selector){
			case 0:
				selector++;
				return "CreateWarMsg";
			case 1:
				selector++;
				return "warName:Test";
			case 2:
				selector++;
				return "warDesc:DescTest";
			default:
				selector++;
				return "DISCONNECT";
		}
		
	}
	
	public void setSelector(int n){
		selector = n;
	}
	
	
	
	
}
