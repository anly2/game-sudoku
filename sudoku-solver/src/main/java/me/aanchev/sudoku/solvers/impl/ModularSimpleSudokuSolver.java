package me.aanchev.sudoku.solvers.impl;

import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import me.aanchev.sudoku.grid.GridCell;
import me.aanchev.sudoku.model.NotableCell;
import me.aanchev.sudoku.model.SudokuGrid;
import me.aanchev.sudoku.solvers.SudokuMove;
import me.aanchev.sudoku.solvers.SudokuSolver;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static me.aanchev.sudoku.solvers.impl.CompositeSudokuSolver.Tactic.eagerTactic;
import static me.aanchev.sudoku.utils.StreamExtensions.findExactlyOne;
import static me.aanchev.sudoku.utils.StreamExtensions.iterate;

@Slf4j
public class ModularSimpleSudokuSolver {
    @Delegate
    private SudokuSolver base;

    public ModularSimpleSudokuSolver() {
        this.base = new CompositeSudokuSolver(asList(
                eagerTactic(this::notePotential),
                eagerTactic(this::markLoners),
                eagerTactic(this::markObliged),
                eagerTactic(this::markLines)
        ));
    }


    protected Collection<SudokuMove> notePotential(SudokuGrid grid) {
        Stream.Builder<SudokuMove> plan = Stream.builder();

        for (SudokuGrid.SudokuCell cell : grid) {
            if (cell.notesSize() > 0) {
                return emptySet();
            }

            if (cell.isEmpty()) {
                BitSet seen = new BitSet();
                grid.getSeen(cell.getRow(), cell.getColumn())
                        .map(NotableCell::get).filter(Objects::nonNull).forEach(seen::set);
                grid.possibleValues().forEach(v -> {
                    if (!seen.get(v) && !cell.hasNote(v)) {
                        plan.accept(expressPotential(cell, v));
                        cell.makeNote(v);
                    }
                });
            }
        }

        return plan.build().collect(toList());
    }

    protected Collection<SudokuMove> markLoners(SudokuGrid grid) {
        Stream.Builder<SudokuMove> plan = Stream.builder();

        grid.forEach((y, x) -> cell -> {
            if (cell.isEmpty()) {
                if (cell.notesSize() == 1) {
                    int v = cell.getNotes().findFirst()
                            .orElseThrow(() -> new IllegalStateException("Notes size lied"));

                    log.info("Found a cell noted solely " + v + ": " + cell);
                    mark(grid, plan, cell, v);
                }
            }
        });

        return plan.build().collect(toList());
    }

    protected Collection<SudokuMove> markObliged(SudokuGrid grid) {
        Stream.Builder<SudokuMove> plan = Stream.builder();

        topLoop:
        for (int v : grid.possibleValues()) {
            Stream<Stream<SudokuGrid.SudokuCell>> domains = Stream.concat(Stream.concat(
                    IntStream.range(0, grid.getHeight())
                            .mapToObj(grid::getRow),
                    IntStream.range(0, grid.getWidth())
                            .mapToObj(grid::getColumn)),
                    grid.tiles()
            );

            for (Stream<SudokuGrid.SudokuCell> domain : iterate(domains)) {
                Optional<SudokuGrid.SudokuCell> obliged = findObliged(v, domain);
                if (obliged.isPresent()) {
                    log.info("Found a cell obliged to " + v + ": " + obliged.get());
                    mark(grid, plan, obliged.get(), v);
                    break topLoop;
                }
            }
        }

        return plan.build().collect(toList());
    }

    private Optional<SudokuGrid.SudokuCell> findObliged(int value, Stream<SudokuGrid.SudokuCell> cells) {
        return findExactlyOne(cells.filter(c -> c.isEmpty() && c.hasNote(value)));
    }

    protected Collection<SudokuMove> markLines(SudokuGrid grid) {
        Stream.Builder<SudokuMove> plan = Stream.builder();

        topLoop:
        for (int v : grid.possibleValues()) {
            for (Stream<SudokuGrid.SudokuCell> tile : iterate(grid.tiles())) {
                Set<SudokuGrid.SudokuCell> cells = tile.filter(c -> c.isEmpty() && c.hasNote(v))
                        .limit(4).collect(toSet());

                if (cells.size() < 2 || cells.size() > 3) {
                    continue;
                }

                boolean somethingChanged;

                // Horizontal lines
                somethingChanged = findExactlyOne(cells.stream().map(GridCell::getRow).distinct())
                        .map(grid::getRow)
                        .map(affected -> affected
                                .filter(c -> c.isEmpty() && c.hasNote(v) && !cells.contains(c))
                                .peek(c -> {
                                    c.clearNote(v);
                                    plan.accept(expressImpossibility(c, v));
                                })
                                .count())
                        .map(changedCount -> changedCount > 0)
                        .orElse(false);

                if (somethingChanged) {
                    break topLoop;
                }

                // Vertical lines
                somethingChanged = findExactlyOne(cells.stream().map(GridCell::getColumn).distinct())
                        .map(grid::getColumn)
                        .map(affected -> affected
                                .filter(c -> c.isEmpty() && c.hasNote(v) && !cells.contains(c))
                                .peek(c -> {
                                    c.clearNote(v);
                                    plan.accept(expressImpossibility(c, v));
                                })
                                .count())
                        .map(changedCount -> changedCount > 0)
                        .orElse(false);

                if (somethingChanged) {
                    break topLoop;
                }
            }
        }

        return plan.build().collect(toList());
    }


    protected void mark(SudokuGrid grid, Stream.Builder<SudokuMove> plan, SudokuGrid.SudokuCell cell, int v) {
        cell.set(v);
        plan.accept(expressMarking(cell, v));
        grid.getSeen(cell.getRow(), cell.getColumn())
                .filter(c -> c.hasNote(v))
                .forEach(c -> {
                    c.clearNote(v);
                    plan.accept(expressImpossibility(c, v));
                });
    }

    protected SudokuMove expressMarking(GridCell cell, Integer value) {
        log.info("Marking cell: " + cell);
        return new SudokuMove(cell.getRow(), cell.getRow(), value, false);
    }

    protected SudokuMove expressPotential(GridCell cell, Integer value) {
        log.info("Noting potential for " + value + " in cell: " + cell);
        return new SudokuMove(cell.getRow(), cell.getColumn(), value, true);
    }

    protected SudokuMove expressImpossibility(GridCell cell, Integer value) {
        log.info("Erasing note for " + value + " from cell: " + cell);
        return new SudokuMove(cell.getRow(), cell.getColumn(), -value, true);
    }

}
