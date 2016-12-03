/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import java.awt.*;
import static pt.up.fe.lpro1613.sharedlib.constants.BoardK.*;
import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;
import pt.up.fe.lpro1613.sharedlib.utils.Matrix;

/**
 *
 * @author luisp
 */
public final class Board extends javax.swing.JPanel {

    private static boolean nextSide = true;

    public Board() {
        this(nextSide);
        nextSide = !nextSide;
    }

    public Board(boolean left) {
        initComponents();

        final int ACTUAL_BOARD_SIZE = BOARD_SIZE + 1;

        setLayout(new GridLayout(ACTUAL_BOARD_SIZE, ACTUAL_BOARD_SIZE));

        final Matrix<Block> grid = new Matrix<>(ACTUAL_BOARD_SIZE, ACTUAL_BOARD_SIZE, null);
        for (int i = 0; i < ACTUAL_BOARD_SIZE * ACTUAL_BOARD_SIZE; i++) {
            final int x = i % ACTUAL_BOARD_SIZE;
            final int y = (int) (i / ACTUAL_BOARD_SIZE);
            Coord gridCoord = new Coord(x, y);
            Coord blockCoord = new Coord(x - (left ? 1 : 0), y - 1);
            Block block;
            if (y == 0) //Columns labels
            {
                int offset = x - (left ? 1 : 0);
                String label = (offset < 0 || offset >= BOARD_SIZE) ? (" ") : ("" + (char) ('A' + offset));    //if the coordinates of this iteration are the coordinates of the empty corner, create it. otherwise create another label with the next char sequence
                block = new Block(blockCoord, left, label);
            } else if (x == (left ? 0 : BOARD_SIZE)) //Rows labels
            {
                String label = (y == 0) ? (" ") : ("" + y);    //if the coordinates of this iteration are the coordinates of the empty corner, create it. otherwise create another label with the next char sequence
                block = new Block(blockCoord, left, label);
            } else //clickable Block
            {
                block = new Block(blockCoord, left);
            }
            add(block); //puts the Block in the grid
            grid.set(gridCoord, block);
        }
        this.grid = grid.getUnmodifiableMatrix();

        this.left = left;
    }

    private final Matrix<Block> grid;
    private final boolean left;

    private final Block get(Coord c) {
        return grid.get(new Coord(c.x + (left ? 1 : 0), c.y + 1));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setRequestFocusEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    void update(BoardUIInfo info) {
        // TODO: LUIS
        info.board.forEach((c, v) -> {
            get(c).setSquareFill(v);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
