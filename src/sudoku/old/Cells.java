package sudoku.old;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Decorator class for ArrayList<Cell>
 */
class Cells implements Iterable<Cell> {
	protected ArrayList<Cell> array;
	
	public Cells () {
		this.array = new ArrayList<Cell>();
	}
	public Cells (ArrayList<Cell> cells) {
		this.array = cells;
	}
	public Cells (Cell cell) {
		this ();
		this.array.add(cell);
	}
	
	public boolean add (Cell cell) {
		return this.array.add(cell);
	}
	public void add (Cell cell, int index) {
		this.array.add(index, cell);
	}
	public void add (int index, Cell cell) {
		this.array.add(index, cell);
	}

	public void add (Cells cells) {
		add (cells, false);
	}
	public void add (Cells cells, boolean allowDuplicates) {
		for (Cell s: cells.array)
			if (allowDuplicates || !this.contains(s))
				this.add(s);
	}
	
	public void remove (int i) {
		this.array.remove (i);
	}
	public void remove (Cell cell) {
		this.array.remove (cell);
	}
	public void remove (Cells cells) {
		for (Cell cell: cells)
			remove (cell);
	}
	
	public Cell get (int i) {
		return this.array.get(i);
	}
	public Cells getMarked (int value) {
		Cells marked = new Cells ();
		
		for (Cell cell: this.array)
			if (cell.hasNote (value))
				marked.add(cell);
		
		return marked;
	}
	public int indexOf (Cell cell) {
		return this.array.indexOf(cell);
	}
	
	public boolean contains (Cell cell) {
		return this.array.contains(cell);
	}
	public boolean contains (int value) {
		for (Cell cell: this.array)
			if (cell.get() == value)
				return true;
		
		return false;
	}
	
	public int size () {
		return this.array.size();
	}
	public Iterator<Cell> iterator() {
		return this.array.iterator();
	}
}