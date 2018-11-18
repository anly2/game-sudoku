package me.aanchev.sudoku.solvers.modular;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.aanchev.sudoku.model.SudokuGrid;
import me.aanchev.sudoku.solvers.SudokuMove;
import me.aanchev.sudoku.solvers.SudokuSolver;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
public class CompositeSudokuSolver implements SudokuSolver {

    @NonNull
    private List<Tactic> tactics;

    /* Solver contract */

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

}
