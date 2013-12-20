package navalwar.server.network;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;

import navalwar.server.gameengine.IGameEngineModule;

public class BufferedReaderMock extends BufferedReader {
	
	private int selector = 0; 
	private IGameEngineModule game;
	private int warID = -100;
	private int armyID = -10;
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
			case 3:
				selector++;
				return "JOIN";
			case 4:
				selector++;
				List<Integer> wars = game.getWarsList();
				
				for(Integer i : wars){
					warID = i;
					break;
				}
				return "warID:"+warID;
			case 5: 
				selector++;
				return "ArmyName:TestArmy";
			case 6:
				selector++;
				return "sizeMsg:1";
			case 7:
				selector++;
				return "Unit:Plane";
			case 8:
				selector++;
				return "X:4";
			case 9:
				selector++;
				return "Y:4";
			case 10:
				selector++;
				return "StartMsg";
			case 11:
				selector++;
				return "warID:"+warID;
			case 12:
				selector++;
				List<Integer> armies = game.getArmies(warID);
				for(Integer i : armies){
					armyID = i;
				}
				return "armyID:"+armyID;
			default:
				selector++;
				return "DISCONNECT";
		}
		
	}
	
	public void setSelector(int n){
		selector = n;
	}
	
	public void setGame(IGameEngineModule game){
		this.game=game;
	}
	
	public int getwarID(){
		return warID;
	}
}
