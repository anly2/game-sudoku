package sudoku.model;

import java.util.List;

public interface Grid<ELEM> extends Iterable<ELEM> {
	public int getWidth();
	public int getHeight();
	
	public ELEM getCell(int row, int column);
	public List<ELEM> getRow(int row);
	public List<ELEM> getColumn(int column);
	public List<ELEM> getCells();
}
