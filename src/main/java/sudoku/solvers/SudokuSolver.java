package sudoku.solvers;

import sudoku.model.SudokuGrid;

import java.util.stream.Stream;

public interface SudokuSolver {
    void solve(SudokuGrid grid);

    Stream<SudokuMove> play(SudokuGrid grid);
}