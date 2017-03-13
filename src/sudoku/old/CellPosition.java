package sudoku.old;

class CellPosition {
	protected int row;
	protected int col;
	
	public CellPosition (int i) {
		this.col = i%9;
		this.row = (int)i/9;
	}
	public CellPosition (int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int row () {
		return this.row;
	}
	public int col () {
		return this.col;
	}
	
	public int reg () {
		return ((int)(row()/3)*3 + (int)(col()/3));
	}
	public int ind () {
		return (this.row*9 + this.col);
	}
	public int index () {
		return this.ind();
	}
}