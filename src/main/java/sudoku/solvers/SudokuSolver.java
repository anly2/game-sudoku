package sudoku.solvers;

import sudoku.model.concrete.SudokuGrid;

public class SudokuSolver {
	
	private SudokuGrid grid;
	
	public SudokuSolver(SudokuGrid grid) {
		this.grid = grid;
	}
	
	public static SudokuSolver solve(SudokuGrid sudokuGrid) {
		SudokuSolver solver = new SudokuSolver(sudokuGrid);
		solver.solve();
		return solver;
	}
	
	
	
	public void solve() {
	}
}
