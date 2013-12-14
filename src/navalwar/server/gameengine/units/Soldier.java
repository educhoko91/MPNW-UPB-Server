package navalwar.server.gameengine.units;

public class Soldier extends Unit {

	public Soldier() {
		super();
		
		int[][] shape =
			{
				{Unit.BLOCK}
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
