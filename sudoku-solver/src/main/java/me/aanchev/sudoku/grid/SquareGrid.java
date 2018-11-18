package me.aanchev.sudoku.grid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class SquareGrid<ELEM> implements Grid<ELEM> {

    /* Properties */

    private int size;
    private Object[] cells;


    /* Constructors */

    public SquareGrid(int size) {
        this.size = size;
        this.cells = new Object[size * size];
    }


    /* Accessors */

    public int getWidth() {
        return size;
    }

    public int getHeight() {
        return size;
    }


    /* Internal accessors */

    protected int getIndex(int row, int column) {
        assert (row >= 0 && row < size) : "Row index out of bounds: 0 </= " + row + " </ " + size;
        assert (column >= 0 && column < size) : "Column index out of bounds: 0 </= " + column + " </ " + size;

        return row * size + column;
    }


    /* Grid accessors */

    public ELEM setCell(int row, int column, ELEM element) {
        int index = getIndex(row, column);
        @SuppressWarnings("unchecked")
        ELEM prev = (ELEM) cells[index];
        cells[index] = element;
        return prev;
    }

    @SuppressWarnings("unchecked")
    public ELEM getCell(int row, int column) {
        return (ELEM) cells[getIndex(row, column)];
    }


    /* Convenience contracts */

    @SuppressWarnings("unchecked")
    public void forEach(BiFunction<Integer, Integer, Consumer<? super ELEM>> action) {
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                action.apply(row, col).accept((ELEM) cells[getIndex(row, col)]);
            }
        }
    }


    @SuppressWarnings("unchecked")
    public Stream<ELEM> stream() {
        return (Stream<ELEM>) Arrays.stream(cells);
    }

    @Override
    public Iterator<ELEM> iterator() {
        return stream().iterator();
    }
}
