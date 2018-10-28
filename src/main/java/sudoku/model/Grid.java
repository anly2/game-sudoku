package sudoku.model;

import java.util.List;

public interface Grid<ELEM> extends Iterable<ELEM> {
    int getWidth();

    int getHeight();

    ELEM getCell(int row, int column);

    List<ELEM> getRow(int row);

    List<ELEM> getColumn(int column);

    List<ELEM> getCells();
}
