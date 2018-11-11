package me.aanchev.sudoku.solvers;

import me.aanchev.sudoku.model.SudokuGrid;

import java.util.stream.Stream;

public interface SudokuSolver {
    void solve(SudokuGrid grid);

    Stream<SudokuMove> play(SudokuGrid grid);
}