package me.aanchev.sudoku.solvers.impl.simple;

import me.aanchev.sudoku.solvers.SudokuSolver;
import me.aanchev.sudoku.solvers.impl.SudokuSolverTest;

public class SimpleSudokuSolverTest extends SudokuSolverTest {

    @Override
    protected SudokuSolver getSolver() {
        return new SimpleSudokuSolver();
    }
}
