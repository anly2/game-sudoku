package sudoku.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sudoku.grid.GridCell;
import sudoku.grid.SquareGrid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class SudokuGrid extends SquareGrid<SudokuGrid.SudokuCell> {
    @Getter
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
                super.setCell(row, col, new SudokuCell(row, col));
            }
        }
    }


    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public class SudokuCell extends NotableCell implements GridCell {
        @Getter
        private int row, column;

        @Override
        public String toString() {
            return String.format("(At row=%d, col=%d, %s)", row, column, super.toString());
        }
    }

    @Override
    public SudokuCell setCell(int row, int column, SudokuCell element) {
        throw new UnsupportedOperationException("The Sudoku Grid should be immutable. " +
                "Mutate individual cells if necessary.");
    }


    public Iterable<Integer> possibleValues() {
        return () -> IntStream.rangeClosed(1, getWidth()).iterator();
    }

    public Stream<Stream<SudokuCell>> tiles() {
        Stream.Builder<Stream<SudokuCell>> tiles = Stream.builder();
        for (int row = 0; row < tileSize; row++) {
            for (int col = 0; col < tileSize; col++) {
                tiles.accept(getTile(row * tileSize, col * tileSize));
            }
        }
        return tiles.build();
    }

    public Stream<SudokuCell> getTile(int y, int x) {
        int i = Math.floorDiv(y, tileSize);
        int j = Math.floorDiv(x, tileSize);

        Stream.Builder<SudokuCell> tile = Stream.builder();
        for (int row = 0; row < tileSize; row++) {
            for (int col = 0; col < tileSize; col++) {
                tile.accept(getCell(i + row, j + col));
            }
        }
        return tile.build();
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
        return Stream.concat(
                Stream.concat(getRow(row), getColumn(column)),
                getTile(row, column)
                        .filter(cell -> cell.getRow() != row && cell.getColumn() != column)
        );
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
