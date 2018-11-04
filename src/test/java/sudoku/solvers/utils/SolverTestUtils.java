package sudoku.solvers.utils;

import sudoku.model.NotableCell;
import sudoku.model.SudokuGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.sort;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertTrue;

public class SolverTestUtils {
    public static void assertGridIsComplete(SudokuGrid grid) {
        List<Integer> values = new ArrayList<>(grid.getWidth());
        grid.possibleValues().forEach(values::add);
        sort(values);

        boolean allComplete = Stream.of(
                IntStream.range(0, grid.getHeight()).mapToObj(grid::getRow),
                IntStream.range(0, grid.getWidth()).mapToObj(grid::getColumn),
                grid.tiles()
        ).flatMap(s -> s)
                .map(domain -> domain.map(NotableCell::get).filter(Objects::nonNull).sorted().collect(toList()))
                .allMatch(values::equals);

        assertTrue(allComplete);
    }
}
