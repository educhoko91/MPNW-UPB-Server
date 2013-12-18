package navalwar.server.gameengine;

public class UnitAndPlace {

	private String unitName;
	private int row;
	private int col;
	
	public UnitAndPlace(String unitName, int row, int col) {
		this.unitName = unitName;
		this.row = row;
		this.col = col;
	}
	
	public String getUnitName() { return unitName; }
	public int getRow() { return row; }
	public int getCol() { return col; }
}
