package navalwar.server.gameengine;

public class UnitObject {

	String unitName;
	int x;
	int y;
	
	public UnitObject(String name,int x, int y){
		unitName = name;
		this.x =x;
		this.y =y;
	}
	
	public String getName(){
		return unitName;
	}
	public int getX()
	{
		return x;
	}
	public int getY(){
		return y;
	}
}
