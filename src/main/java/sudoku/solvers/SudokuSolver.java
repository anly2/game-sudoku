package sudoku.solvers;

import sudoku.model.SudokuGrid;

import java.util.stream.Stream;

public interface SudokuSolver {
    SudokuGrid solve(SudokuGrid grid);

    Stream<SudokuMove> playthrough(SudokuGrid grid);
}