package navalwar.server.gameengine.units;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import navalwar.server.gameengine.Army;


public abstract class Unit {


	public static final int EMPTY = 0;
	public static final int BLOCK = 1;
	private static final int MIN_UNIT_ID = 1000;
	private static final int MAX_UNIT_ID = 9999;

	protected int unitID;
	protected Army field;
	protected int row;
	protected int col;
	
	protected int[][] shape;
	protected int numRows;
	protected int numCols;
	protected int numBlocks;
	protected int numShotedBlocks;
	
	protected int[][] shots;
	
	public enum StatusUnit { INTACT, SHOTED, DESTROYED };
	private StatusUnit status;

	private static Map<Integer, Unit> units = new HashMap<Integer, Unit>();
	
	private static Random rnd = new Random(System.currentTimeMillis());
	
	public Unit() {

		// unit not belong to any field
		row = -1;
		col = -1;
		field = null;
		numShotedBlocks = 0;
		status = StatusUnit.INTACT;

		// assign unique ID to unit
		unitID = MIN_UNIT_ID + rnd.nextInt(MAX_UNIT_ID - MIN_UNIT_ID + 1);
		while (units.containsKey(unitID)) unitID = MIN_UNIT_ID + rnd.nextInt(MAX_UNIT_ID - MIN_UNIT_ID + 1);

		// keep unit in map
		units.put(unitID, this);
	}
	
	
	protected int computeNumBlocks() {
		int numBlocks = 0;
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				if (shape[i][j] == Unit.BLOCK) numBlocks++;
			}
		}
		return numBlocks;
	}
	
	public static Unit getUnit(int unitID) {
		return units.get(unitID);
	}

	public int getID() { return unitID; }
	public int[][] getShape() { return shape; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public void setField(Army field, int row, int col) {
		this.field = field;
		this.row = row;
		this.col = col;
	}

	public void handleShot(int row, int col) {
		
		// keep track of number of shots
		shots[row][col]++;
		
		// check if a block has been shoted for first time
		if ((shape[row][col] == Unit.BLOCK) &&
				(shots[row][col] == 1)) {
			numShotedBlocks++;
			status = StatusUnit.SHOTED;
		}
		
		// check if the unit is destroyed
		if (numShotedBlocks >= numBlocks) {
			status = StatusUnit.DESTROYED;
		}
		
	}

	public int getRow() { return row; }
	public int getCol() { return col; }

	public boolean isIntact() { return status == StatusUnit.INTACT; }
	public boolean isShoted() { return status == StatusUnit.SHOTED; }
	public boolean isDestroyed() { return status == StatusUnit.DESTROYED; }
	


}
