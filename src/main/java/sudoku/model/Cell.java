package sudoku.model;

public interface Cell<VALUE> {
	VALUE get();

	void set(VALUE value);

	boolean isEmpty();
}
