package sudoku.old;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

class VCell extends JLabel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	public static Font font = new Font("Tahoma", 0, 20);

	protected Cell cell;
	protected Color backgroundColor = Color.lightGray;
	
	public VCell (Cell cell) {
		super ();
		this.cell = cell;
		
		setSize(22, 22);
	    setPreferredSize(new Dimension(40,40));
	    
	    setHorizontalAlignment(SwingConstants.CENTER);
	    setVerticalAlignment(SwingConstants.CENTER);
		
		setBackground(backgroundColor);
		setOpaque(true);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setFont(font);
		
		this.addMouseListener (this);
		
		redraw();
	}

	
	public void redraw () {
		super.repaint();
		
		if (cell == null) return;
		
		if (cell.isSet())
			setText (""+cell.get());
		else
			setText(" ");
		
		Integer[] noted = (cell.getNoted()).toArray(new Integer[0]);
		Arrays.sort(noted);
		setToolTipText (join (noted, " "));
	}
	
	private String join (Integer[] numbers, String delimiter) {
		String str = "";
		
		for (Integer n: numbers)
			str += " " + n;
		
		if (str.length() > 0)
			str= str.substring(delimiter.length());
		
		return str;
	}
	
	
	public void highlight (Color color) {
		setBackground (color);
		redraw();
	}
	public void unhighlight () {
		setBackground (backgroundColor);
		redraw();
	}


	
	
	@Override
	public void mouseClicked (MouseEvent e) {
		highlight (Color.red);
	}


	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
	}


	@Override
	public void mousePressed(MouseEvent e) {	
	}


	@Override
	public void mouseReleased(MouseEvent e) {		
	}
}