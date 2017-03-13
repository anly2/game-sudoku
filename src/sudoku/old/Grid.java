package sudoku.old;

import java.util.Iterator;

public class Grid implements Iterable<Cell>{
	protected Cells cells;
	
	public Grid (String raw) throws InvalidGridRaw {		
		this.fill(raw); //throws InvalidGridRaw
	}
	
	public void fill (String raw) throws InvalidGridRaw {
		raw = raw.replaceAll("[^0-9 ]", "");
		
		if (raw.length() != 81)
			throw new InvalidGridRaw (raw);
		
		this.cells = new Cells();
				
		//Rolls and cols are handled otherwise in the "getters"
		for (int i=0; i<81; i++)
			this.cells.add (i, new Cell (raw.substring(i, i+1), this));
	}
	
	
	public Cells getRow (int i) {
		if (i < 0 || i >= 9)
			throw new RuntimeException ("Invalid row index!");
		
		Cells row = new Cells();
		
		for (int j=0; j<9; j++)
			row.add (getCell (i, j));
		
		return row;
	}
	public Cells getCol (int j) {
		if (j < 0 || j >= 9)
			throw new RuntimeException ("Invalid column index!");
		
		Cells col = new Cells();
		
		for (int i=0; i<9; i++)
			col.add (getCell (i, j));
		
		return col;
	}	
	public Cells getReg (int n) {
		if (n < 0 || n >= 9)
			throw new RuntimeException ("Invalid region index!");

		Cells reg = new Cells();
		
		int sr = (int)(n/3) * 3;
		int sc = (n%3) * 3;
		
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				reg.add (getCell (sr+i, sc+j));
		
		return reg;
	}
	public Cells getSq  (int n) {
		return getReg (n);
	}

	public Cell getCell (int row, int col) {
		if (row < 0 || row >= 9)
			throw new RuntimeException ("Invalid row index!");
		if (col < 0 || col >= 9)
			throw new RuntimeException ("Invalid column index!");
		
		return getCell (row*9 + col);
	}
	public Cell getCell (int index) {
		if (index < 0 || index >= this.cells.size())
			throw new RuntimeException ("Invalid cell index!");
		
		return this.cells.get (index);
	}
	
	public Cells getSeen (Cell cell) throws RuntimeException {
		return getSeen (cell, true);
	}
	public Cells getSeen (Cell cell, boolean includeRegion) throws RuntimeException {
		//Locate
		CellPosition pos;
		try {
			pos = find (cell);
		}
		catch (CellNotFound e) {
			throw new RuntimeException ("Saught Cell not found on this grid!");
		}
		
		//Merge
		Cells seen = new Cells();

		seen.add (getRow (pos.row()));
		seen.add (getCol (pos.col()));
		
		if (includeRegion)
			seen.add (getReg (pos.reg()));

		//Exclude the cell itself
		seen.remove(cell);
		
		//Return
		return seen;
	}

	public CellPosition find (Cell cell) throws CellNotFound {
		int i = find (cell, this.cells);
		
		return new CellPosition (i);
		
	}
	public int find (Cell cell, Cells collection) throws CellNotFound {
		if (cell == null)
			throw new RuntimeException ("Saught cell is null!");
		
		int i = collection.indexOf(cell);
		
		if (i < 0)
			throw new CellNotFound(collection, cell);
		
		return i;
	}
	
	public Iterator<Cell> iterator () {
		return cells.iterator();
	}
	
	public String print () {
		String str = "";
		
		for (int i=0; i<cells.size(); i++) {
			if (i%9 == 0)
				str += "\n";
			
			str += cells.get(i).toString();
		}
		
		System.out.println(str);
		return str;
	}
}