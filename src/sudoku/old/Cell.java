package sudoku.old;

import java.util.ArrayList;

public class Cell {
	protected int value;
	protected ArrayList<Integer> noted;
	protected Grid parent;
	
	public Cell (String value) {
		this.noted = new ArrayList<Integer>(); 
		this.value = normalize (value);
		this.parent = null;
	}	
	public Cell (String value, Grid parent) {
		this (value);
		setParent (parent);
	}
	
	public Cell (int value) {
		this.noted = new ArrayList<Integer>();
		this.value = normalize (value);
		this.parent = null;
	}	
	public Cell (int value, Grid parent) {
		this (value);
		setParent (parent);
	}

	
	public int normalize (String value) {
		try {
			int val = new Integer (value);
			
			return normalize (val);
		}
		catch (NumberFormatException e) {
			return -1;
		}
	}
	public int normalize (int value) {
		if (value <= 0 || value >= 10)
			return -1;
		
		return value;
	}


	public void setParent (Grid grid) {
		this.parent = grid;
	}
	public Grid getParent () {
		return this.parent;
	}

	public void set (String value) {
		this.value = normalize (value);
		clearAll(); //Clear all noted
	}
	public void set (int value) {
		this.value = normalize (value);
		clearAll(); //Clear all noted
	}
	public void clear () {
		set (" "); //Lets (set & normalize) handle "blank"
	}
	public int get () {
		return this.value;
	}
	public boolean isSet () {
		return (get() > 0 && get() <= 9);
	}
	
	public void note (int value) {
		if (value <= 0 || value >= 10)
			return;
		
		if (this.noted.contains(value))
			return;
		
		this.noted.add(value);
	}
	public boolean clearNote (int value) {
		if (!this.noted.contains(value))
			return false;
		
		this.noted.remove((Integer)value);
		return true;
	}
	public void clearAll () {
		this.noted = new ArrayList<Integer>();	
	}
	public ArrayList<Integer> getNoted () {
		return this.noted;
	}
	public boolean hasNote (int value) {
		return (this.noted.contains(value));
	}
	
	public String toString () {
		String ser = "";
		
		ser += " "; //modifier
		ser += get()<0? " " : get();
		
		//noted?
		
		return ser;
	}

	
	public boolean sees (int value) {
		if (this.parent == null)
			throw new RuntimeException ("Cell not assigned to a grid!");
		
		value = normalize (value);
		
		if (this.parent.getSeen(this).contains(value))
			return true;
		
		return false;
	}
	
	public Cells seen () {
		return this.getSeen();
	}
	public Cells getSeen () {
		if (this.parent == null)
			throw new RuntimeException ("Cell not assigned to a grid!");
		
		return this.getParent().getSeen(this);
	}
}
