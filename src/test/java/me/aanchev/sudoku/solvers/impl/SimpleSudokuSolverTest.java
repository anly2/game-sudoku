package me.aanchev.sudoku.solvers.impl;

import me.aanchev.sudoku.model.SudokuGrid;
import org.junit.Test;

import static me.aanchev.sudoku.solvers.utils.SolverTestUtils.assertGridIsComplete;

public class SimpleSudokuSolverTest {
    @Test
    public void testSampleSudokuGame() {
        SudokuGrid grid = SudokuGrid.sudokuGrid(String.join("\n",
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
        ));

        new SimpleSudokuSolver().solve(grid);

        assertGridIsComplete(grid);

//        Integer[][] result = new Integer[9][9];
//        grid.forEach((y, x) -> c -> result[y][x] = c.get());
//
//        assertThat(asString(result)).isEqualTo(asString(new Integer[][]{
//                {2, 3, 8, 3, 9, 5, 7, 1, 6},
//                {5, 7, 1, 6, 2, 8, 3, 4, 9},
//                {9, 3, 6, 7, 4, 1, 5, 8, 2},
//                {6, 8, 2, 5, 3, 9, 1, 7, 4},
//                {3, 5, 9, 1, 7, 4, 6, 2, 8},
//                {7, 1, 4, 8, 6, 2, 9, 5, 3},
//                {8, 6, 3, 4, 1, 7, 2, 9, 5},
//                {1, 9, 5, 2, 8, 6, 4, 3, 7},
//                {4, 2, 7, 9, 5, 3, 8, 6, 1}
//        }));
    }

}
