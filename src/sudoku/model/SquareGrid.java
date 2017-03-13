package sudoku.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SquareGrid<ELEM> implements Grid<ELEM> {
	
	/* Properties */
	
	private int size;
	private List<ELEM> cells;
	
	
	/* Constructors */
	
	public SquareGrid() {
		this(9);
	}
	
	public SquareGrid(int size) {
		this.size = size;
		this.cells = new ArrayList<ELEM>(size*size);
	}


	/** Grid contract **/
	
	/* Accessors */
	
	public int getWidth() {
		return size;
	}
	public int getHeight() {
		return size;
	}

	
	/* Internal accessors */
	
	protected ELEM getCell(int index) {
		//if (index < 0 || index >= cells.size())
		//	throw new IndexOutOfBoundsException();
		
		return cells.get(index);
	}
	
	protected int getIndex(int row, int column) {
		if (row < 0 || row >= size)
			throw new IndexOutOfBoundsException("Row index out of bounds: 0 </= " + row + " </ " + size);
		
		if (column < 0 || column >= size)
			throw new IndexOutOfBoundsException("Column index out of bounds: 0 </= " + column + " </ " + size);
		
		return row * size + column;
	}

	
	/* Grid accessors */
	
	@Override
	public ELEM getCell(int row, int column) {
		return getCell(getIndex(row, column));
	}

	@Override
	public List<ELEM> getRow(int row) {
		List<ELEM> r = new ArrayList<ELEM>(size);
		
		for (int i=0; i<size; i++)
			r.add(getCell(row, i));
		
		return r;
	}

	@Override
	public List<ELEM> getColumn(int column) {
		List<ELEM> r = new ArrayList<ELEM>(size);
		
		for (int i=0; i<size; i++)
			r.add(getCell(i, column));
		
		return r;
	}

	@Override
	public List<ELEM> getCells() {
		return cells;
	}

	
	/* Convenience contracts */

	@Override
	public Iterator<ELEM> iterator() {
		return cells.iterator();
	}
}
