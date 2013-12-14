package navalwar.server.gameengine.units;

import navalwar.server.gameengine.Army;


public class Tank extends Unit {
	
	public Tank() {
		super();
		
		int[][] shape =
			{
				{Unit.BLOCK, Unit.BLOCK, Unit.BLOCK}
			};
		this.shape = shape;
		
		this.numRows = shape.length;
		this.numCols = shape[0].length;
		
		int[][] shots = new int[numRows][numCols];
		for(int i = 0; i < numRows; i++)
			for(int j = 0; j < numCols; j++)
				shots[i][j] = 0;
		this.shots = shots;
		
		numBlocks = computeNumBlocks();
	
	}

}
