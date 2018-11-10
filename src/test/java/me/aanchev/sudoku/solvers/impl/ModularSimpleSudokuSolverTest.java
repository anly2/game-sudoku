package me.aanchev.sudoku.solvers.impl;

import me.aanchev.sudoku.model.SudokuGrid;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.List;

import static java.util.Arrays.asList;
import static me.aanchev.sudoku.solvers.utils.SolverTestUtils.assertGridIsComplete;

@RunWith(Theories.class)
public class ModularSimpleSudokuSolverTest {

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

        new ModularSimpleSudokuSolver().solve(grid);

        assertGridIsComplete(grid);
    }

}
