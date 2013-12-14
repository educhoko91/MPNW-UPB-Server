package navalwar.server.gameengine.exceptions;

import navalwar.server.gameengine.units.Unit;

public class PlaceNotFreeToPlaceUnitException extends Exception {
	
	private Unit u;
	private int row;
	private int col;

	public PlaceNotFreeToPlaceUnitException(Unit u, int row, int col) {
		this.u = u;
		this.row = row;
		this.col = col;
	}
	
	public String toString() {
		return super.toString() + " --- unit:" + u.getClass().getName() + " row:" + row + " col:" + col + "\n";
	}

}
