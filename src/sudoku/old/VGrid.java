package sudoku.old;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JPanel;

class VGrid extends JPanel {
	private static final long serialVersionUID = 1L;
	protected HashMap<Cell, VCell> map;
	protected Grid grid;

	public VGrid (Grid grid) {
		super();
		this.map = new HashMap<Cell, VCell>();
		
		this.grid = grid;
		
		setSize(200, 200);
		setPreferredSize(new Dimension(200,200));
		
		setBackground(Color.gray);
		setLayout(null);
		
		for (Cell cell: grid.cells)
			add (cell);
	}
	
	public void add (Cell cell) {
		VCell vis = new VCell (cell);
		
		CellPosition pos;
		try {
			pos = this.grid.find(cell);
		}
		catch (CellNotFound e) {
			throw new RuntimeException ("Cell not found in this grid?! Something is wrong for sure.");
		}
		
		//the + is for the region delimiter borders
		int x = pos.col()*21 + ((int)pos.col()/3)*5; 
		int y = pos.row()*21 + ((int)pos.row()/3)*5;
		vis.setLocation(x, y);
		
		this.map.put (cell, vis);
		this.add(vis);
	}

	
	public void repaint () {
		super.repaint();
		
		if (map == null) return;
		
		for (VCell cell: map.values())
			cell.redraw();
	}
	
	
	public Grid getGrid () {
		return this.grid;
	}

	public VCell getVisual (Cell cell) {
		return this.map.get(cell);
	}
}