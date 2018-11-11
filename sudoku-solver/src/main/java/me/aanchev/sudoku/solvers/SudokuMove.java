package me.aanchev.sudoku.solvers;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class SudokuMove {
    private int row;
    private int column;
    private int value;
    private boolean note = false;
}
