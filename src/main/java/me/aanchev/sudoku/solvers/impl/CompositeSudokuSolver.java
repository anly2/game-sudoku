package me.aanchev.sudoku.solvers.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.aanchev.sudoku.model.SudokuGrid;
import me.aanchev.sudoku.solvers.SudokuMove;
import me.aanchev.sudoku.solvers.SudokuSolver;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
        play(grid);
    }

    @Override
    public Stream<SudokuMove> play(SudokuGrid grid) {
        Tactic[] bestTactic = {null};
        return Stream.iterate(
                Stream.ofNullable((SudokuMove) null),
                moves -> {
                    bestTactic[0] = tactics.stream()
                            .map(t -> entry(t, t.estimateAppropriateness(grid)))
                            .filter(e -> e.getValue() > 0)
                            .max(comparing(Map.Entry::getValue))
                            .map(Map.Entry::getKey)
                            .orElse(null);
                    return bestTactic[0] != null;
                },
                moves -> bestTactic[0].play(grid)
        ).flatMap(s -> s);
    }

    public interface Tactic {
        int estimateAppropriateness(SudokuGrid grid);

        Stream<SudokuMove> play(SudokuGrid grid);


        static Tactic tryFirstTactic(Function<SudokuGrid, List<SudokuMove>> tactic) {
            return new Tactic() {
                private List<SudokuMove> moves;

                @Override
                public int estimateAppropriateness(SudokuGrid grid) {
                    this.moves = tactic.apply(grid);
                    return moves.size();
                }

                @Override
                public Stream<SudokuMove> play(SudokuGrid grid) {
                    //not thread-safe (is grid the same as the one estimateAppr was called with?)
                    return moves.stream();
                }
            };
        }
    }
}
