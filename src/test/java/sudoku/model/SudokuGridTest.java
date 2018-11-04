package sudoku.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

public class SudokuGridTest {

    @Test
    public void getTile_works() {
        int size = 3 * 3;
        SudokuGrid grid = SudokuGrid.sudokuGrid(IntStream.range(0, size * size).boxed().collect(Collectors.toList()));

        assertThat(grid.getTile(0, 0)
                .map(NotableCell::get)
                .collect(Collectors.toList()))
                .isEqualTo(asList(0, 1, 2, 9, 10, 11, 18, 19, 20));

        assertThat(grid.getTile(3, 4)
                .map(NotableCell::get)
                .collect(Collectors.toList()))
                .isEqualTo(asList(30, 31, 32, 39, 40, 41, 48, 49, 50));

        assertThat(grid.getTile(8, 8) //(y: size-1, x: size-1)
                .map(NotableCell::get)
                .collect(Collectors.toList()))
                .isEqualTo(asList(60, 61, 62, 69, 70, 71, 78, 79, 80));
    }

    @Test
    public void getSeen_works() {
        int size = 3 * 3;
        SudokuGrid grid = SudokuGrid.sudokuGrid(IntStream.range(0, size * size).boxed().collect(Collectors.toList()));

        assertThat(grid.getSeen(0, 0)
                .map(NotableCell::get)
                .collect(Collectors.toList()))
                .isEqualTo(asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 18, 27, 36, 45, 54, 63, 72, 10, 11, 19, 20));

        assertThat(grid.getSeen(3, 4)
                .map(NotableCell::get)
                .collect(Collectors.toList()))
                .isEqualTo(asList(27, 28, 29, 30, 32, 33, 34, 35, 4, 13, 22, 40, 49, 58, 67, 76, 39, 41, 48, 50));

    }

    //    @Test
    public void printGrid() {
        int size = 3 * 3;
        SudokuGrid grid = SudokuGrid.sudokuGrid(IntStream.range(0, size * size).boxed().collect(Collectors.toList()));

        Integer[][] result = new Integer[size][size];
        grid.forEach((y, x) -> c -> result[y][x] = c.get());

        System.out.println(asString(result));
    }

    public static String asString(Integer[][] grid) {
        return Arrays.stream(grid)
                .map(row -> Arrays.stream(row)
                        .map(cell -> cell == null ? "  " : String.format("%2d", cell))
                        .collect(joining(","))
                )
                .collect(joining("\n"));
    }
}