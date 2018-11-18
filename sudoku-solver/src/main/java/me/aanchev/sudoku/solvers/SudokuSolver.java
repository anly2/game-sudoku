package me.aanchev.sudoku.solvers;

import me.aanchev.sudoku.model.SudokuGrid;

import java.util.stream.Stream;

public interface SudokuSolver {
    default void solve(SudokuGrid grid) {
        play(grid);
    }

    Stream<SudokuMove> play(SudokuGrid grid);
}