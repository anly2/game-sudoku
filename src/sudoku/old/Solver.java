package sudoku.old;

import java.util.ArrayList;

public class Solver {
	protected Grid grid;
	protected Express express;
	
	public Solver (Grid grid) {
		this.grid = grid;
		this.express = new Express(new VGrid(grid));
	}
	public Solver (VGrid grid) {
		this.grid = grid.getGrid();
		this.express = new Express (grid);
	}
	
	public void solve () {
		markInitial();
		
		notePotential();
		
		while (true) {
			try {
				//Mark holes
				if (markHoles())
					continue;
			
				//Mark loners
				if (markLoners())
					continue;
			
				//Mark lines
				if (markLines())
					continue;
				
				//<- Bridge
				
				//Make Trials
				if (makeTrial())
					continue;
			}
			catch (Inconsistency e) {
				if (undoTrial())
					continue;
				
				throw new Inconsistency ("Puzzle appears to be unsolvable!");
			}
			
			break;
		}
		
		express.clean_all();
		express.victory();
	}

	
	public void markInitial () {
		for (Cell cell: grid) 
			if (cell.isSet())
				express.initial (cell);
	}
	
	
	public void notePotential () {
		for (int v = 1; v <= 9; v++)
			notePotential (v); //expresses notes
	}
	
	public void notePotential (int value) {
		/*
		 * - Optimized -
		 * 
		 * Could easily be:
		 * <code>
		 * 	for (Cell cell : grid.cells)
		 * 		if (!cell.sees(value))
		 * 			cell.note(value);
		 * </code>
		 */
		
		for (int n=0; n<9; n++) {
			Cells reg = grid.getReg(n);
			
			if (reg.contains (value))
				continue;
			
			for (Cell cell: reg) {
				if (cell.isSet())
					continue;
				
				//If the cell sees the value
				if (grid.getSeen(cell, false).contains (value))
					continue;
				
				cell.note(value);
				express.note(cell, value);
			}
		}
	}

	public boolean markHoles () {
		boolean hasChanged = false;
		
		for (Cell cell: grid) {
			if (cell.isSet())
				continue;
			
			ArrayList<Integer> noted = cell.getNoted();
			
			if (noted.size() > 1)
				continue;
			
			if (noted.size() == 0)
				throw new Inconsistency();
			
			int v = noted.get(0);
			mark (cell, v);
			express.hole (cell, v);
			hasChanged = true;
		}
		
		return hasChanged;
	}
	
	public boolean markLoners () {
		boolean hasChanged = false;

		for (int n=0; n<9; n++) {
			Cells reg = this.grid.getReg(n);
			
			for (int v=1; v<=9; v++) {
				Cells marked = reg.getMarked (v);
				
				if (marked.size() != 1)
					continue;
				
				Cell cell = marked.get(0);
				mark (cell, v);
				express.loner (cell, v);
				hasChanged = true;
			}
		}
		
		return hasChanged;
	}
	
 	public boolean markLines () {
		boolean hasChanged = false;
		
		for (int n=0; n<9; n++) {
			Cells reg = this.grid.getReg(n);
			
			for (int v=1; v<=9; v++) {
				Cells marked = reg.getMarked (v);
				
				if (marked.size() < 2)
					continue;
				

				//Look for horizontal lines
				boolean hasHLine = true;
				int row = -1;
				
				for (Cell m: marked) {
					CellPosition pos = m.getParent().find(m);
					
					if (row == -1) {
						row = pos.row();
						continue;
					}
					
					if (row == pos.row())
						continue;
					
					hasHLine = false;
					break;
				}
				
				
				//Look for horizontal lines
				boolean hasVLine = true;
				int col = -1;
				
				for (Cell m: marked) {
					CellPosition pos = m.getParent().find(m);
					
					if (col == -1) {
						col = pos.col();
						continue;
					}
					
					if (col == pos.col())
						continue;
					
					hasVLine = false;
					break;
				}
				
				
				//Handle the line
				if (hasHLine || hasVLine) {						
					Cells affected = ((hasHLine)? this.grid.getRow(row) : this.grid.getCol(col));
					affected.remove (marked);
					
					Cells changed = new Cells();
					
					for (Cell cell: affected)
						if (cell.clearNote(v))
							changed.add (cell);

					if (changed.size() > 0) {
						express.line (marked, v, changed);
						hasChanged = true;
						continue;
					}
				}
			}
		}
		return hasChanged;
	}
	
 	
 	ArrayList<GridSave> trials = new ArrayList<GridSave>();
 	public boolean makeTrial () {
 		//Pick a cell
 		GridSave trial = new GridSave(grid);
 		
 		//If all cells are set then puzzle is complete
 		if (trial.size() == 0)
 			return false; //no change
 		
 		
 		//Pick a cell to "expriment" with
 		CellClone chosenClone = trial.first();
 		
 		//there shouldn't be cells with no notes, and cells with 1 note should be handled in markHoles()
 		if (chosenClone.getNoted().size() < 2)
 			throw new Inconsistency(); 
 		
 		//Pick the digit to try with
 		int v = chosenClone.getNoted().get(0);
 		
 		//Clear the note at that cell
 		chosenClone.clearNote(v);
 		
 		//Mark the cell in the original
 		Cell cell = grid.getCell(chosenClone.getPosition().index());
 		mark (cell, v);
 		
 		//Express the change
 		express.trial (cell, v);
 		
 		//Save the trial
 		trials.add(0, trial); //push
 		
 		//Signal the change
 		return true;
 	}
 	
 	public boolean undoTrial () { 		
 		if (trials.size() == 0)
 			return false;
 		
 		GridSave trial = trials.get(0);
 		trial.load ();
 		trials.remove(0);
 		
 		return true;
 	}
 	
	
	public Cells mark (Cell cell, int value) {
		cell.set(value);
		
		Cells seen = cell.getSeen();
		
		for (Cell s: seen) 
			s.clearNote(value);
		
		return seen;
	}
	
	
	public VGrid getVisual () {
		return this.express.getVisual();
	}
}


class GridSave {
	protected ArrayList<CellClone> array;
	protected Grid original;
	
	public GridSave () {
		this.array = new ArrayList<CellClone>();
		this.original = null;
	}
	
	public GridSave (Grid grid) {
		this ();
		this.original = grid;
		
		for (Cell cell: grid)
			if (!cell.isSet())
				add (cell);
	}
	
	public void add (Cell cell) {
		add (new CellClone (cell));
	}
	
	public void add (CellClone cell) {
		//Effectively sorts the array of CellClones by noted.size()
		int i;
		for (i=0; i<this.array.size(); i++)
			if (this.array.get(i).getNoted().size() > cell.getNoted().size())
				break;
		
		this.array.add (i, cell);
	}
	
	public CellClone get (int i) {
		return this.array.get(i);
	}
	public CellClone first () {
		return get(0);
	}
	
	public void load () {
		if (this.original == null)
			throw new RuntimeException ("No target grid specified to load onto!");
		
		load (this.original);
	}
	public void load (Grid grid) {
		for (CellClone clone: this.array) {
			Cell cell = grid.getCell(clone.getPosition().index());
			
			cell.set(clone.get());
			cell.clearAll();

			for (int value: clone.getNoted())
				cell.note(value);
			
			cell.setParent(clone.getParent());
		}
	}
	
	
	public int size () {
		return array.size();
	}
}

class CellClone extends Cell {
	protected CellPosition position;
	
	public CellClone (Cell cell) {
		super (cell.get());
		//super.set(cell.get());
		
		for (int value: cell.getNoted())
			super.note(value);
		
		Grid parent = cell.getParent();
		super.setParent(parent);
		this.position = parent.find(cell);
	}
	
	public String toString () {
		String ser = "";

		ser += get()<0? " " : get();
		
		ser += "{";
		
		for (int n: super.getNoted())
			ser += ""+n+", ";
		
		ser = ser.substring(0, ser.length()-2) + "}";
		
		return ser;
	}
	
	public CellPosition getPosition () {
		return this.position;
	}
	public CellPosition position() {
		return getPosition();
	}
}



