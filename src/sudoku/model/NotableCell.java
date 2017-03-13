package sudoku.model;

import java.util.List;

public interface NotableCell<VALUE> {

	public boolean hasNote(VALUE value);
	public void setNote(VALUE value, boolean flag);
	
	public void makeNote(VALUE value);
	public void clearNote(VALUE value);
	
	public List<VALUE> getNotes();
}
