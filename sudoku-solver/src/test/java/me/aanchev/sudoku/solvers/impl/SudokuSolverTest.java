package me.aanchev.sudoku.solvers.impl;

import me.aanchev.sudoku.model.NotableCell;
import me.aanchev.sudoku.model.SudokuGrid;
import me.aanchev.sudoku.solvers.SudokuSolver;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

@RunWith(Theories.class)
public abstract class SudokuSolverTest {

    @DataPoints("sudokuGrids")
    public static List<String> sudokuGrids = asList(
            String.join("\n",
                    "",
                    "   39  1 ",
                    "5 1    4 ",
                    "9  7  5  ",
                    "6 253  7 ",
                    "    7   8",
                    "7  8  9 3",
                    "8 3 1  9 ",
                    " 9 2 6  7",
                    "4    3 61"
            ),
            String.join("\n",
                    "",
                    "1    97  ",
                    " 9     2 ",
                    "  786  5 ",
                    " 1  7   5",
                    "   5 2   ",
                    "5   9  6 ",
                    " 2  534  ",
                    " 3     7 ",
                    "  86    2"
            ),
            String.join("\n",
                    "",
                    " 593  7  ",
                    " 1  5    ",
                    " 7  816  ",
                    "4  7  3  ",
                    "  7   8  ",
                    "  3  5  4",
                    "  817  6 ",
                    "    9  3 ",
                    "  6  849 "
            )
    );

    @Test
    @Theory
    public void testPullingSampleSudokuGame(@FromDataPoints("sudokuGrids") String inputGrid) {
        SudokuGrid grid = SudokuGrid.sudokuGrid(inputGrid);

        getSolver().solve(grid);

        assertGridIsComplete(grid);
    }

    protected abstract SudokuSolver getSolver();


    public static void assertGridIsComplete(SudokuGrid grid) {
        List<Integer> values = new ArrayList<>(grid.getWidth());
        grid.possibleValues().forEach(values::add);
        sort(values);

        boolean allComplete = Stream.of(
                IntStream.range(0, grid.getHeight()).mapToObj(grid::getRow),
                IntStream.range(0, grid.getWidth()).mapToObj(grid::getColumn),
                grid.tiles()
        ).flatMap(s -> s)
                .map(domain -> domain.map(NotableCell::get).filter(Objects::nonNull).sorted().collect(toList()))
                .allMatch(values::equals);

        assertTrue(allComplete);
    }
}
