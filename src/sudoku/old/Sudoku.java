package sudoku.old;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Sudoku {
	/**
	 * The generation of the actual sudoku puzzle is from third party programs.
	 * It is inconvenient but manual input is the simplest way for now.
	 */
	
	//public static String defaultRaw = "9 2 5   3 43278     89    16   3  7 2 5   4 9 1  9   63    19     46935 7   8 2 4"; //easy
	//public static String defaultRaw = "  7 5 8            2 4 6 5 6 52 37 4 92 8 56 7 41 53 9 7 3 2 9            3 1 6  "; //medium
	//public static String defaultRaw = "        2  82 6 5 7254           439    6    852           2876 3 8 91  4        "; //hard
	//public static String defaultRaw = " 2   4 9   7 1   48     6   89 625             354 17   8     29   3 8   7 9   1 "; //expert
	//public static String defaultRaw = "   7   2  7   59    4  1 6  9   7  23       86  2   7  4 3  1    69   8  1   4   "; //expert
	public static String defaultRaw = "5    1   4  72     92  6 8   3 6 17       2  21 3 786    98 7   7   2  4  9   5  ";
	
	public static void main (String[] args) {
		String raw = (args.length <= 0)? defaultRaw : args[0];
		
		try {
			Grid grid = new Grid (raw);
			VGrid vgrid = new VGrid (grid);
			
			new Sudoku (vgrid);
			
			Solver solver = new Solver (vgrid);
			solver.solve(); //changes grid
		}
		catch (InvalidGridRaw e) { //custom exception
			System.err.println(e);
		}
	}

	
	public static final int windowWidth = 250;
	public static final int windowHeight = 260;
	
	private JFrame window;
	private JPanel document;
	
	public Sudoku (Grid grid) {
		this (new VGrid(grid));
	}
	public Sudoku (VGrid vis) {
		window = new JFrame();

		//make sure the program exits when the frame closes
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Sudoku Solver");
		window.setSize(windowWidth,windowHeight);

		//This will center the JFrame in the middle of the screen
		window.setLocationRelativeTo(null);
		
		document = new JPanel ();
		document.add(vis);
		//draw (grid);
		

		window.getContentPane().add(document);
        window.setVisible(true);
	}
	
	public void draw (Grid grid) {
		document.add(new VGrid(grid));
	}
}