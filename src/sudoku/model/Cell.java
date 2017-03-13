package sudoku.model;

public interface Cell<VALUE> {
	public VALUE get();
	public void set(VALUE value);
	public boolean isEmpty();
}
