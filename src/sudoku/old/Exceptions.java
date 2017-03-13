package sudoku.old;

import java.util.ArrayList;

class InvalidGridRaw extends Exception {
	private static final long serialVersionUID = 1L;
	
	protected String raw;
	
	public InvalidGridRaw () {
		raw = null;
	}
	
	public InvalidGridRaw (String raw) {
		this();
		this.raw = raw;
	}
	
	public String getLocalizedMessage () {
		String msg = "";
		
		msg += "Invalid sudoku grid raw source!";
				
		if (this.raw != null)
			msg += "\n Given source: |"+this.raw+"|\n";
		
		return msg;
	}
}


class CellNotFound extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	protected Cells collection;
	protected Cell cell;
	
	public CellNotFound () {
		super ("Cell not found in the given collection");
	}

	public CellNotFound (Cells collection, Cell cell) {
		this.collection = collection;
		this.cell = cell;
	}
	public CellNotFound (Cell cell, Cells collection) {
		this (collection, cell);
	}
	
	public CellNotFound (ArrayList<Cell> collection, Cell cell) {
		this (new Cells(collection), cell);
	}
	public CellNotFound (Cell cell, ArrayList<Cell> collection) {
		this (collection, cell);
	}
}

class Inconsistency extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public Inconsistency () {
		super ("Encountered an indication of an error.");
	}
	public Inconsistency (String message) {
		super (message);
	}
}