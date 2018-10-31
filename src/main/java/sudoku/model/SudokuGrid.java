package sudoku.model;

import sudoku.grid.SquareGrid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

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


    /* Static initializers */

    public static SudokuGrid sudokuGrid(String data) {
        return sudokuGrid(data.split("[^\\d \t]*"));
    }

    public static SudokuGrid sudokuGrid(String... values) {
        return sudokuGrid(Arrays.stream(values)
                .map(v -> v == null || "".equals(v.trim()) ? null : Integer.valueOf(v))
                .collect(toList()));
    }

    public static SudokuGrid sudokuGrid(List<Integer> values) {
        SudokuGrid grid = new SudokuGrid((int) sqrt(sqrt(values.size())));
        Iterator<Integer> it = values.iterator();
        grid.forEach((row, col) -> cell -> ofNullable(it.next()).ifPresent(cell::set));
        return grid;
    }
}
