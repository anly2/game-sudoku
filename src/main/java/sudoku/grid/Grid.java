package sudoku.grid;

public interface Grid<ELEM> extends Iterable<ELEM> {
    int getWidth();

    int getHeight();

    ELEM getCell(int row, int column);

    ELEM setCell(int row, int column, ELEM element);
}
