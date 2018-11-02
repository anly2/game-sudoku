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

    public void foreachTile(GridConsumer consumer) {
        for (int row = 0; row < tileSize; row++) {
            for (int col = 0; col < tileSize; col++) {
                consumer.accept(row * tileSize, col * tileSize);
            }
        }
    }

    public void foreachCellInTile(int y, int x, CellConsumer consumer) {
        foreachCellInTile(y, x, gridConsumer(consumer));
    }

    public void foreachCellInTile(int y, int x, GridConsumer consumer) {
        int i = Math.floorDiv(y, tileSize);
        int j = Math.floorDiv(x, tileSize);

        for (int row = 0; row < tileSize; row++) {
            for (int col = 0; col < tileSize; col++) {
                consumer.accept(i + row, j + col);
            }
        }
    }


    public void foreachCellInRow(int row, CellConsumer consumer) {
        foreachCellInRow(row, gridConsumer(consumer));
    }

    public void foreachCellInRow(int row, GridConsumer consumer) {
        for (int col = 0; col < getWidth(); col++) {
            consumer.accept(row, col);
        }
    }


    public void foreachCellInColumn(int col, CellConsumer consumer) {
        foreachCellInColumn(col, gridConsumer(consumer));
    }

    public void foreachCellInColumn(int col, GridConsumer consumer) {
        for (int row = 0; row < getHeight(); row++) {
            consumer.accept(row, col);
        }
    }


    public void foreachSeen(int row, int col, CellConsumer consumer) {
        foreachSeen(row, col, gridConsumer(consumer));
    }

    public void foreachSeen(int row, int col, GridConsumer consumer) {
        foreachCellInRow(row, consumer);
        foreachCellInColumn(col, consumer);
        foreachCellInTile(row, col, (y, x) -> {
            if (y != row && x != col) {
                consumer.accept(y, x);
            }
        });
    }


    @FunctionalInterface
    public interface GridConsumer {
        void accept(int row, int col);
    }

    @FunctionalInterface
    public interface CellConsumer {
        void accept(int row, int col, SudokuCell cell);
    }

    private GridConsumer gridConsumer(CellConsumer consumer) {
        return (y, x) -> consumer.accept(y, x, getCell(y, x));
    }

    public static Stream<SudokuCell> asStream(Consumer<CellConsumer> looper) {
        Stream.Builder<SudokuCell> cells = Stream.builder();
        looper.accept((y, x, c) -> cells.accept(c));
        return cells.build();
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
