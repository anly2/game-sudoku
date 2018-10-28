package sudoku.model;

import java.util.List;

public interface NotableCell<VALUE> {

    boolean hasNote(VALUE value);

    void setNote(VALUE value, boolean flag);

    void makeNote(VALUE value);

    void clearNote(VALUE value);

    List<VALUE> getNotes();
}
