package me.aanchev.sudoku.solvers.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.aanchev.sudoku.model.SudokuGrid;
import me.aanchev.sudoku.solvers.SudokuMove;
import me.aanchev.sudoku.solvers.SudokuSolver;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.Map.entry;

@RequiredArgsConstructor
public class CompositeSudokuSolver implements SudokuSolver {

    @NonNull
    private List<Tactic> tactics;

    /* Solver contract */

    @Override
    public void solve(SudokuGrid grid) {
        long performedMoves = play(grid).count();
        if (performedMoves == 0) {
            throw new IllegalStateException("Failed to solve the sudoku");
        }
    }

    @Override
    public Stream<SudokuMove> play(SudokuGrid grid) {
        @SuppressWarnings("unchecked")
        Supplier<Stream<SudokuMove>>[] bestTactic = new Supplier[]{null};
        return Stream.iterate(
                Stream.ofNullable((SudokuMove) null),
                moves -> {
                    bestTactic[0] = tactics.stream()
                            .map(t -> t.apply(grid))
                            .filter(e -> e.getKey() > 0)
                            .max(comparing(Entry::getKey))
                            .map(Entry::getValue)
                            .orElse(null);
                    return bestTactic[0] != null;
                },
                moves -> bestTactic[0].get()
        ).flatMap(s -> s);
    }

    @FunctionalInterface
    public interface Tactic {
        /**
         * Give an estimate of the appropriateness of this tactic, and a callback to execute it.
         *
         * @param grid the sudoku grid for which to estimate and potentially perform
         * @return a pair of an estimate of appropriateness, and a callback to execute the tactic.
         */
        Entry<Integer, Supplier<Stream<SudokuMove>>> apply(SudokuGrid grid);


        static Tactic eagerTactic(Function<SudokuGrid, ? extends Collection<SudokuMove>> tactic) {
            return grid -> {
                Collection<SudokuMove> moves = tactic.apply(grid);
                return entry(moves.size(), moves::stream);
            };
        }
    }
}
