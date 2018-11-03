package sudoku.solvers.impl;

import sudoku.grid.GridCell;
import sudoku.model.NotableCell;
import sudoku.model.SudokuGrid;
import sudoku.model.SudokuGrid.SudokuCell;
import sudoku.solvers.SudokuMove;
import sudoku.solvers.SudokuSolver;

import java.util.BitSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static sudoku.utils.StreamExtensions.findExactlyOne;
import static sudoku.utils.StreamExtensions.iterate;

public class SimpleSudokuSolver implements SudokuSolver {


    @Override
    public SudokuGrid solve(SudokuGrid grid) {
        apply(grid, playthrough(grid));
        return grid;
    }

    @Override
    public Stream<SudokuMove> playthrough(SudokuGrid grid) {
        Stream.Builder<SudokuMove> plan = Stream.builder();

        notePotential(grid, plan);

        while (true) {
            if (markLoners(grid, plan)) {
                continue;
            }

            if (markObliged(grid, plan)) {
                continue;
            }

            break;
        }

        return plan.build();
    }

    protected void apply(SudokuGrid grid, Stream<SudokuMove> playthrough) {
        playthrough
                .filter(m -> !m.isNote())
                .forEach(m -> grid.getCell(m.getRow(), m.getColumn()).set(m.getValue()));
    }


    protected void notePotential(SudokuGrid grid, Stream.Builder<SudokuMove> plan) {
        grid.forEach((row, col) -> cell -> {
            if (cell.isEmpty()) {
                BitSet seen = new BitSet();
                grid.getSeen(row, col).map(NotableCell::get).filter(Objects::nonNull).forEach(seen::set);
                grid.possibleValues().forEach(v -> {
                    if (!seen.get(v)) {
                        plan.accept(expressPotential(cell, v));
                        cell.makeNote(v);
                    }
                });
            }
        });
    }


    protected boolean markLoners(SudokuGrid grid, Stream.Builder<SudokuMove> plan) {
        boolean[] hasChanged = {false};

        grid.forEach((y, x) -> cell -> {
            if (cell.isEmpty()) {
                if (cell.notesSize() == 1) {
                    int v = cell.getNotes().findFirst()
                            .orElseThrow(() -> new IllegalStateException("Notes size lied"));
                    mark(grid, plan, cell, v);
                    hasChanged[0] = true;
                }
            }
        });

        return hasChanged[0];
    }

    protected boolean markObliged(SudokuGrid grid, Stream.Builder<SudokuMove> plan) {
        for (int v : grid.possibleValues()) {
            Stream<Stream<SudokuCell>> domains = Stream.concat(Stream.concat(
                    IntStream.range(0, grid.getHeight())
                            .mapToObj(grid::getRow),
                    IntStream.range(0, grid.getWidth())
                            .mapToObj(grid::getColumn)),
                    grid.tiles()
            );

            for (Stream<SudokuCell> domain : iterate(domains)) {
                Optional<SudokuCell> obliged = findObliged(v, domain);
                if (obliged.isPresent()) {
                    mark(grid, plan, obliged.get(), v);
                    return true;
                }
            }
        }
        return false;
    }

    private Optional<SudokuCell> findObliged(int value, Stream<SudokuCell> cells) {
        return findExactlyOne(cells.filter(c -> c.isEmpty() && c.hasNote(value)));
    }


    private void mark(SudokuGrid grid, Stream.Builder<SudokuMove> plan, SudokuCell cell, int v) {
        cell.set(v);
        plan.accept(expressMarking(cell, v));
        grid.getSeen(cell.getRow(), cell.getColumn())
                .filter(c -> c.hasNote(v))
                .forEach(c -> {
                    c.clearNote(v);
                    plan.accept(expressImpossibility(c, v));
                });
    }


    protected SudokuMove expressMarking(GridCell cell, Integer value) {
        return new SudokuMove(cell.getRow(), cell.getRow(), value, false);
    }

    protected SudokuMove expressPotential(GridCell cell, Integer value) {
        return new SudokuMove(cell.getRow(), cell.getColumn(), value, true);
    }

    protected SudokuMove expressImpossibility(GridCell cell, Integer value) {
        return new SudokuMove(cell.getRow(), cell.getColumn(), -value, true);
    }


    /* UNCOVERTED --

    private void p() {
        notePotential();

        while (true) {
            try {
                //Mark holes
                if (markHoles())
                    continue;

                //Mark loners
                if (markLoners())
                    continue;

                //Mark lines
                if (markLines())
                    continue;

                //<- Bridge

                //Make Trials
                if (makeTrial())
                    continue;
            } catch (Inconsistency e) {
                if (undoTrial())
                    continue;

                throw new Inconsistency("Puzzle appears to be unsolvable!");
            }

            break;
        }

        express.clean_all();
        express.victory();
    }


    public boolean markLoners() {
        boolean hasChanged = false;

        for (int n = 0; n < 9; n++) {
            Cells reg = this.grid.getReg(n);

            for (int v = 1; v <= 9; v++) {
                Cells marked = reg.getMarked(v);

                if (marked.size() != 1)
                    continue;

                Cell cell = marked.get(0);
                mark(cell, v);
                express.loner(cell, v);
                hasChanged = true;
            }
        }

        return hasChanged;
    }

    public boolean markLines() {
        boolean hasChanged = false;

        for (int n = 0; n < 9; n++) {
            Cells reg = this.grid.getReg(n);

            for (int v = 1; v <= 9; v++) {
                Cells marked = reg.getMarked(v);

                if (marked.size() < 2)
                    continue;


                //Look for horizontal lines
                boolean hasHLine = true;
                int row = -1;

                for (Cell m : marked) {
                    CellPosition pos = m.getParent().find(m);

                    if (row == -1) {
                        row = pos.row();
                        continue;
                    }

                    if (row == pos.row())
                        continue;

                    hasHLine = false;
                    break;
                }


                //Look for horizontal lines
                boolean hasVLine = true;
                int col = -1;

                for (Cell m : marked) {
                    CellPosition pos = m.getParent().find(m);

                    if (col == -1) {
                        col = pos.col();
                        continue;
                    }

                    if (col == pos.col())
                        continue;

                    hasVLine = false;
                    break;
                }


                //Handle the line
                if (hasHLine || hasVLine) {
                    Cells affected = ((hasHLine) ? this.grid.getRow(row) : this.grid.getCol(col));
                    affected.remove(marked);

                    Cells changed = new Cells();

                    for (Cell cell : affected)
                        if (cell.clearNote(v))
                            changed.add(cell);

                    if (changed.size() > 0) {
                        express.line(marked, v, changed);
                        hasChanged = true;
                        continue;
                    }
                }
            }
        }
        return hasChanged;
    }


    ArrayList<GridSave> trials = new ArrayList<GridSave>();

    public boolean makeTrial() {
        //Pick a cell
        GridSave trial = new GridSave(grid);

        //If all cells are set then puzzle is complete
        if (trial.size() == 0)
            return false; //no change


        //Pick a cell to "expriment" with
        CellClone chosenClone = trial.first();

        //there shouldn't be cells with no notes, and cells with 1 note should be handled in markHoles()
        if (chosenClone.getNoted().size() < 2)
            throw new Inconsistency();

        //Pick the digit to try with
        int v = chosenClone.getNoted().get(0);

        //Clear the note at that cell
        chosenClone.clearNote(v);

        //Mark the cell in the original
        Cell cell = grid.getCell(chosenClone.getPosition().index());
        mark(cell, v);

        //Express the change
        express.trial(cell, v);

        //Save the trial
        trials.add(0, trial); //push

        //Signal the change
        return true;
    }

    public boolean undoTrial() {
        if (trials.size() == 0)
            return false;

        GridSave trial = trials.get(0);
        trial.load();
        trials.remove(0);

        return true;
    }


    public Cells mark(Cell cell, int value) {
        cell.set(value);

        Cells seen = cell.getSeen();

        for (Cell s : seen)
            s.clearNote(value);

        return seen;
    }


    public VGrid getVisual() {
        return this.express.getVisual();
    }
}


class GridSave {
    protected ArrayList<CellClone> array;
    protected Grid original;

    public GridSave() {
        this.array = new ArrayList<CellClone>();
        this.original = null;
    }

    public GridSave(Grid grid) {
        this();
        this.original = grid;

        for (Cell cell : grid)
            if (!cell.isSet())
                add(cell);
    }

    public void add(Cell cell) {
        add(new CellClone(cell));
    }

    public void add(CellClone cell) {
        //Effectively sorts the array of CellClones by noted.size()
        int i;
        for (i = 0; i < this.array.size(); i++)
            if (this.array.get(i).getNoted().size() > cell.getNoted().size())
                break;

        this.array.add(i, cell);
    }

    public CellClone get(int i) {
        return this.array.get(i);
    }

    public CellClone first() {
        return get(0);
    }

    public void load() {
        if (this.original == null)
            throw new RuntimeException("No target grid specified to load onto!");

        load(this.original);
    }

    public void load(Grid grid) {
        for (CellClone clone : this.array) {
            Cell cell = grid.getCell(clone.getPosition().index());

            cell.set(clone.get());
            cell.clearAll();

            for (int value : clone.getNoted())
                cell.note(value);

            cell.setParent(clone.getParent());
        }
    }


    public int size() {
        return array.size();
    }
}

class CellClone extends Cell {
    protected CellPosition position;

    public CellClone(Cell cell) {
        super(cell.get());
        //super.set(cell.get());

        for (int value : cell.getNoted())
            super.note(value);

        Grid parent = cell.getParent();
        super.setParent(parent);
        this.position = parent.find(cell);
    }

    public String toString() {
        String ser = "";

        ser += get() < 0 ? " " : get();

        ser += "{";

        for (int n : super.getNoted())
            ser += "" + n + ", ";

        ser = ser.substring(0, ser.length() - 2) + "}";

        return ser;
    }

    public CellPosition getPosition() {
        return this.position;
    }

    public CellPosition position() {
        return getPosition();
    }

}
    */

}
