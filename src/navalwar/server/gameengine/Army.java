package navalwar.server.gameengine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import navalwar.server.gameengine.IGameEngineModule.ShotCodes;
import navalwar.server.gameengine.exceptions.PlaceNotFreeToPlaceUnitException;
import navalwar.server.gameengine.exceptions.UnitCoordinatesOutsideMatrixException;
import navalwar.server.gameengine.units.Unit;
import navalwar.server.gameengine.units.Unit.StatusUnit;


public class Army {
	
	public static final int EMPTY_CELL = 0;
	public static final int UNIT_ADDED = 101;

	public static final int ARMY_MATRIX_NUM_COLS = 10;  // num cols of battle matrix
	public static final int ARMY_MATRIX_NUM_ROWS = 10;  // num rows of battle matrix

	
	private int armyID;
	private String name;
	private int numRows;
	private int numCols;
	private int[][] matrix;
	private Map<Integer, Unit> units;
	private int numDestroyedUnits;
	

	public Army(String name) {
		this.name = name;
		this.numRows = ARMY_MATRIX_NUM_ROWS;
		this.numCols = ARMY_MATRIX_NUM_COLS;
		matrix = new int[numRows][numCols];
		units = new HashMap<Integer, Unit>();
		numDestroyedUnits = 0;
	}
	
	
	public void setArmyID(int armyID) { this.armyID = armyID; }

	public int getArmyID() { return armyID;	}
	public String getName() { return name; }
	public int getNumUnits() { return units.size(); }
	public int getNumDestroyedUnits() { return numDestroyedUnits; }
	
	
	public int addUnit(Unit u, int row, int col) throws PlaceNotFreeToPlaceUnitException, UnitCoordinatesOutsideMatrixException {
		
		int[][] shape = u.getShape();
		
		// check if outside matrix
		if ((row < 0) || (col < 0) ||
				((row + u.getNumRows() - 1) >= Army.ARMY_MATRIX_NUM_ROWS) ||
				((col + u.getNumCols() - 1) >= Army.ARMY_MATRIX_NUM_COLS))
			throw new UnitCoordinatesOutsideMatrixException(u, row, col);

		// check if there is place to add unit
		boolean free = true;
		for(int i = 0; i < u.getNumRows(); i++) {
			if (!free) break;
			for(int j = 0; j < u.getNumCols(); j++) {

				// cell is not free
				if ((shape[i][j] == Unit.BLOCK) &&
						(matrix[row+i][col+j] != Army.EMPTY_CELL)) {
					free = false;
					break;
				}
			}
		}

		// not possible to place unit
		if (!free) throw new PlaceNotFreeToPlaceUnitException(u, row, col);
		
		// otherwise place unit
		for(int i = 0; i < u.getNumRows(); i++) {
			for(int j = 0; j < u.getNumCols(); j++) {
				if (shape[i][j] == Unit.BLOCK)
					matrix[row+i][col+j] = u.getID();
			}
		}
		units.put(u.getID(), u);
		u.setField(this, row, col);
		
		return UNIT_ADDED;
	}
	
	
	public ShotCodes handleShot(int enemyID, int row, int col) {

		// shot in unit
		if ((matrix[row][col] != EMPTY_CELL) &&
				(matrix[row][col] > 0)) {

			int unitID = matrix[row][col];
			Unit u = units.get(unitID);
			
			matrix[row][col] *= -1;
			int relRow = row - u.getRow();
			int relCol = col - u.getCol();
			u.handleShot(relRow, relCol);

			if (u.isDestroyed()) {
				numDestroyedUnits++;
				
				// checkf if all units of army were destroyed
				if (numDestroyedUnits == units.size())
					return ShotCodes.SHOT_IN_UNIT_AND_DESTROYED_AND_ARMY_DESTROYED;
				
				return ShotCodes.SHOT_IN_UNIT_AND_DESTROYED;
			}
			else return ShotCodes.SHOT_IN_UNIT_BUT_STILL_OPERATIONAL;
			
		}
		
		// shot in free cell or already shoten unit
		if (matrix[row][col] == EMPTY_CELL) return ShotCodes.SHOT_IN_EMPTY_CELL;
		return ShotCodes.SHOT_IN_ALREADY_SHOTEN_UNIT;
		
	}

}
