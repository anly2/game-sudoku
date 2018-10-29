package sudoku.model;

import sudoku.grid.SquareGrid;

import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuGrid extends SquareGrid<SudokuCell> {
    private int tileSize;

    public SudokuGrid() {
        this(3);
    }

    public SudokuGrid(int tileSize) {
        super(tileSize * tileSize);
        this.tileSize = tileSize;
        init();
    }

    protected void init() {
        assert getWidth() == getHeight() : "Not a square grid!";
        int size = getHeight();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                setCell(row, col, new SudokuCell(null, size));
            }
        }
    }


    public void forPossibleValues(Consumer<Integer> consumer) {
        IntStream.rangeClosed(1, getWidth()).boxed().forEach(consumer);
    }

    public Stream<SudokuCell> getTile(int row, int column) {
        int i = Math.floorDiv(row, tileSize);
        int j = Math.floorDiv(column, tileSize);
        return IntStream.range(i, i + tileSize).boxed()
                .flatMap(y -> IntStream.range(j, j + tileSize)
                        .mapToObj(x -> getCell(y, x)));
    }

    public Stream<SudokuCell> getRow(int row) {
        return IntStream.range(0, getWidth())
                .mapToObj(col -> getCell(row, col));
    }

    public Stream<SudokuCell> getColumn(int column) {
        return IntStream.range(0, getHeight())
                .mapToObj(row -> getCell(row, column));
    }

    public Stream<SudokuCell> getSeen(int row, int column) {
        return Stream.of(getTile(row, column), getRow(row), getColumn(column))
                .flatMap(s -> s);
    }
}
