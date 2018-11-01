package sudoku.model;

import java.util.BitSet;
import java.util.stream.IntStream;

public class SudokuCell {

    /* Properties */

    private Integer value; //avoid constant boxing/unboxing
    private BitSet notes;


    /* Construtors */

    public SudokuCell(Integer value) {
        this(value, 9);
    }

    public SudokuCell(Integer value, int size) {
//        assert value <= 0 && value > size: "Value (" + value + ") is not valid for the given Sudoku Grid size";

        this.value = value;
        this.notes = new BitSet(size);
    }


    /* Accessors */

    public Integer get() {
        return value;
    }

    public void set(Integer value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return (value == null);
    }


    /* Notes */

    public boolean hasNote(Integer value) {
        return notes.get(value);
    }

    public void makeNote(Integer value) {
        notes.set(value);
    }

    public void clearNote(Integer value) {
        notes.clear(value);
    }

    public int notesSize() {
        return notes.cardinality();
    }

    public IntStream getNotes() {
        return notes.stream();
    }
}
