package sudoku.model.concrete;

import sudoku.model.NotableCell;

import java.util.ArrayList;
import java.util.List;

public class SudokuCell implements sudoku.model.Cell<Integer>, NotableCell<Integer> {
	public static final int DEFAULT_GRID_SIZE = 9;
	
	/* Properties */
	
	private Integer value; //avoid constant boxing/unboxing
	private boolean[] notes;
	
	
	/* Construtors */

	public SudokuCell(Integer value) {
		this(value, DEFAULT_GRID_SIZE);
	}
	
	public SudokuCell(Integer value, int size) {
		checkValue(value, size);
		
		this.value = value;
		this.notes = new boolean[size];
	}
	
	
	public void checkValue(Integer value, int size) {
		if (value < 0 || value >= size)
			throw new IndexOutOfBoundsException("Value ("+value+") is not valid for the given Sudoku Grid size");
	}
	
	
	/* Accessors */
	
	@Override
	public Integer get() {
		return value;
	}

	@Override
	public void set(Integer value) {
		this.value = value;
	}

	@Override
	public boolean isEmpty() {
		return (value == null);
	}

	
	/* Notes */

	public boolean hasNote(Integer value) {
		checkValue(value, notes.length);
		
		return notes[value];
	}
	
	public void setNote(Integer value, boolean flag) {
		checkValue(value, notes.length);
		
		notes[value] = flag;
	}
	
	
	public void makeNote(Integer value) {
		setNote(value, true);
	}
	
	public void clearNote(Integer value) {
		setNote(value, true);
	}
	
	
	public List<Integer> getNotes() {
		List<Integer> notes = new ArrayList<Integer>();
		
		for (int i=0; i<this.notes.length; i++)
			if (hasNote(i))
				notes.add(i);
		
		return notes;
	}
}
