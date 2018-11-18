package me.aanchev.sudoku.solvers.modular;

import me.aanchev.sudoku.model.SudokuGrid;
import me.aanchev.sudoku.solvers.SudokuMove;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Map.entry;

@FunctionalInterface
public interface Tactic {
    /**
     * Give an estimate of the appropriateness of this tactic, and a callback to execute it.
     *
     * @param grid the sudoku grid for which to estimate and potentially perform
     * @return a pair of an estimate of appropriateness, and a callback to execute the tactic.
     */
    Map.Entry<Integer, Supplier<Stream<SudokuMove>>> apply(SudokuGrid grid);


    static Tactic eagerTactic(Function<SudokuGrid, ? extends Collection<SudokuMove>> tactic) {
        return grid -> {
            Collection<SudokuMove> moves = tactic.apply(grid);
            return entry(moves.size(), moves::stream);
        };
    }
}
