package sudoku.old;

import java.awt.Color;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Express {
    protected VGrid grid;
    protected ArrayList<Expression> effects;

    public static final int em = 1000; // base speed in ms
    protected static int markNoteDelay = (1*em/10);
    protected static int markDenoteDelay = (1*em/5);
    protected static int markHoleDelay = 5*em;
    protected static int markLonerDelay = 5*em;
    protected static int markLineDelay = 5*em;
    protected static int markBridgeDelay = 5*em;
    protected static int markTrialDelay = 5*em;

    protected static boolean hardDenote = false;


    protected int countInitial = 0;
    protected int countNotes = 0;
    protected int countDenotes = 0;
    protected int countHoles = 0;
    protected int countLoners = 0;
    protected int countLines = 0;
    protected int countBridges = 0;
    protected int countTrials = 0;


    public Express (VGrid grid) {
        this.grid = grid;
        effects = new ArrayList<Expression>();
    }


    /* Initial */

    public void initial (Cell cell) {
        VCell vis = grid.getVisual (cell);

        vis.backgroundColor = new Color (170, 170, 170);
        vis.setBackground(vis.backgroundColor);

        countInitial++;
    }


    /* Note */

    public void note (Cell cell, int value) {
        clean_all();
        mark_note (cell, value);

        reg_effect ("note", new Cells (cell));
        countNotes++;
        wait (markNoteDelay);
    }

    @SuppressWarnings("unused")
    private void mark_note (Cells cells, int value) {
        for (Cell cell: cells)
            mark_note (cell, value);
    }
    private void mark_note (Cell cell, int value) {
        VCell vis = grid.getVisual (cell);

        vis.setBackground (Color.pink);
        vis.setText (""+value);
    }

    private void clean_note (Cells cells) {
        for (Cell cell: cells)
            clean_note (cell);
    }
    private void clean_note (Cell cell) {
        VCell vis = grid.getVisual (cell);

        vis.setBackground(vis.backgroundColor);
        vis.redraw();
    }


    /* DeNote (clear note) */

    public void denote (Cell cell, int value) {
        if (!hardDenote && cell.isSet())
            return;

        //secondary effect -> no "clear_all();"
        mark_denote (cell, value);

        reg_effect ("denote", new Cells (cell));
        countDenotes++;
        wait (markDenoteDelay);
    }

    @SuppressWarnings("unused")
    private void mark_denote (Cells cells, int value) {
        for (Cell cell: cells)
            mark_denote (cell, value);
    }
    private void mark_denote (Cell cell, int value) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.redraw();

        vis.setBackground (Color.red);
        vis.setText (""+value);
    }

    private void clean_denote (Cells cells) {
        for (Cell cell: cells)
            clean_denote (cell);
    }
    private void clean_denote (Cell cell) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground(vis.backgroundColor);
        vis.redraw();
    }


    /* Hole */

    public void hole (Cell cell, int value) {
        clean_all();
        mark_hole (cell, value);

        reg_effect ("hole", new Cells(cell));
        countHoles++;
        wait (markHoleDelay);
    }

    @SuppressWarnings("unused")
    private void mark_hole (Cells cells, int value) {
        for (Cell cell: cells)
            mark_hole (cell, value);
    }
    private void mark_hole (Cell cell, int value) {
        VCell vis = this.grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (Color.cyan);
        vis.redraw();

        Cells seen = cell.getSeen();
        for (Cell s: seen)
            denote (s, value);
    }

    private void clean_hole (Cells cells) {
        for (Cell cell: cells)
            clean_hole (cell);
    }
    private void clean_hole (Cell cell) {
        VCell vis = this.grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (vis.backgroundColor);
    }


    /* Loner */

    public void loner (Cell cell, int value) {
        clean_all();
        mark_loner (cell, value);

        reg_effect ("loner", new Cells(cell));
        countLoners++;
        wait (markLonerDelay);
    }

    @SuppressWarnings("unused")
    private void mark_loner (Cells cells, int value) {
        for (Cell cell: cells)
            mark_loner (cell, value);
    }
    private void mark_loner (Cell cell, int value) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (Color.green);
        vis.redraw();

        Cells seen = cell.getSeen();

        for (Cell s: seen)
            denote (s, value);
    }

    private void clean_loner (Cells cells) {
        for (Cell cell: cells)
            clean_loner (cell);
    }
    private void clean_loner (Cell cell) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (vis.backgroundColor);
    }


    /* Line */

    public void line (Cells cells, int value) {
        line (cells, value, null);
    }
    public void line (Cells cells, int value, Cells changed) {
        clean_all();
        mark_line (cells, value);

        reg_effect ("line", cells);
        countLines++;

        if (changed != null) {
            for (Cell cell: changed)
                denote (cell, value);
        }

        wait (markLineDelay);
    }

    private void mark_line (Cells cells, int value) {
        for (Cell cell: cells)
            mark_line (cell, value);
    }
    private void mark_line (Cell cell, int value) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (Color.yellow);
        vis.setText (""+value);
    }

    private void clean_line (Cells cells) {
        for (Cell cell: cells)
            clean_line (cell);
    }
    private void clean_line (Cell cell) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (vis.backgroundColor);
        vis.redraw();
    }



    /* Bridge */

    public void bridge (Cells cells) {
        clean_all();
        mark_bridge (cells);

        reg_effect ("bridge", cells);
        countBridges++;
        wait (markBridgeDelay);
    }

    private void mark_bridge (Cells cells) {
        for (Cell cell: cells)
            mark_bridge (cell);
    }
    private void mark_bridge (Cell cell) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (Color.yellow);
    }

    private void clean_bridge (Cells cells) {
        for (Cell cell: cells)
            clean_bridge (cell);
    }
    private void clean_bridge (Cell cell) {
        VCell vis = grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (vis.backgroundColor);
    }


    /* Trial */

    public void trial (Cell cell, int value) {
        clean_all();
        mark_trial (cell, value);

        reg_effect ("trial", new Cells(cell));
        countTrials++;
        wait (markTrialDelay);
    }

    @SuppressWarnings("unused")
    private void mark_trial (Cells cells, int value) {
        for (Cell cell: cells)
            mark_hole (cell, value);
    }
    private void mark_trial (Cell cell, int value) {
        VCell vis = this.grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (Color.orange);
        vis.redraw();

        Cells seen = cell.getSeen();
        for (Cell s: seen)
            denote (s, value);
    }

    private void clean_trial (Cells cells) {
        for (Cell cell: cells)
            clean_trial (cell);
    }
    private void clean_trial (Cell cell) {
        VCell vis = this.grid.getVisual (cell);

        if (vis == null)
            return;

        vis.setBackground (vis.backgroundColor);
    }


    /* Cleaning */

    private void reg_effect (String type, Cells affected) {
        Expression e = new Expression(type, affected);

        effects.add(0, e);
    }

    private void clean_last () {
        if (effects.size() == 0)
            return;

        clean (effects.get(0)); //removes the effects entry
    }

    private void clean (Expression e) {
        if (e == null || e.affected() == null || e.type() == null)
            return;

        if (e.is("hole"))
            clean_hole (e.affected());

        if (e.is("loner"))
            clean_loner (e.affected());

        if (e.is("line"))
            clean_line (e.affected());

        if (e.is("bridge"))
            clean_bridge (e.affected());

        if (e.is("trial"))
            clean_trial (e.affected());

        if (e.is("note"))
            clean_note (e.affected());

        if (e.is("denote"))
            clean_denote (e.affected());

        effects.remove(e);
    }

    public void clean_all () {
        while (effects.size() > 0)
            clean_last();
    }


    /* Victory */

    public void victory () {
        String msg = "Congratulations! \n The puzzle is complete!";
        String title = "Puzzle is complete!";

        msg += "\n\n";
        msg += "I:"+countInitial+" N:"+countNotes+" D:"+countDenotes+" H:"+countHoles+" Lo:"+countLoners+" Li:"+countLines+" B:"+countBridges+" T:"+countTrials;

        JOptionPane.showMessageDialog(grid, msg, title, JOptionPane.PLAIN_MESSAGE);
    }


    /* General */

    public void wait (int delay) {
        try {
            Thread.sleep (delay);
        }
        catch (InterruptedException e) {}
    }


    public VGrid getVisual () {
        return this.grid;
    }
}


class Expression {
    protected String type;
    protected Cells affected;

    public Expression (String type, Cells affected) {
        this.type = type.toLowerCase();
        this.affected = affected;
    }

    public boolean is (String type) {
        if (this.type == null)
            return (type == null);

        type = type.toLowerCase();

        return this.type.equals(type);
    }

    public Cells affected () {
        return getAffected();
    }
    public Cells getAffected () {
        return this.affected;
    }

    public String type () {
        return this.getType();
    }
    public String getType () {
        return this.type;
    }
}