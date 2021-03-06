/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import javax.swing.GroupLayout;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.UIInfo;

/**
 *
 * @author luisp
 */
public class BoardContainer extends javax.swing.JPanel {

    /**
     * Creates new form BoardContainer
     */
    public BoardContainer() {
        this(true);
    }

    public BoardContainer(boolean left) {
        this.left = left;
        initComponents();
        shipsPreview = left ? new ShipsPreviewLeft() : new ShipsPreviewRight();
        setOpponentWaiting(!left);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        board = new client.ui.game.Components.Board();
        shipsPreviewLeft1 = new client.ui.game.Components.ShipsPreviewLeft();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(shipsPreviewLeft1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(shipsPreviewLeft1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public final boolean left;

    private boolean waiting;

    public final void setOpponentWaiting(boolean waiting) {
        this.waiting = waiting;
        if (waiting && !(board instanceof WaitingOnOpponentPanel)) {
            board = new WaitingOnOpponentPanel(board.left);
        }
        else if (!waiting && !(board instanceof Board)) {
            board = new Board(left);
        }

        repaintBoard();
    }

    //private SuperBoard previousBoard = null;
    private void repaintBoard() {
        this.removeAll();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        final javax.swing.GroupLayout.Alignment horizontalAlignment = left ? GroupLayout.Alignment.TRAILING : GroupLayout.Alignment.LEADING;

        if (!waiting) {
            //if (shipsPreview == null) {
            shipsPreview = left ? new ShipsPreviewLeft() : new ShipsPreviewRight();
            //}
            /*
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(shipsPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(shipsPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );*/

            layout.setHorizontalGroup(
                    layout.createParallelGroup(horizontalAlignment)
                    .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(shipsPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(10, 10, 10)
                            .addComponent(shipsPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

        }
        else {
            shipsPreview = null;

            /*layout.setHorizontalGroup(
                layout.createParallelGroup(horizontalAlignment)
                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(92, 92, 92))
            );*/
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(board, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addGap(47, 47, 47)
                            .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(51, 51, 51))
            );
        }

        //previousBoard = board;
    }

    private ShipsPreview shipsPreview;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private client.ui.game.Components.SuperBoard board;
    private client.ui.game.Components.ShipsPreviewLeft shipsPreviewLeft1;
    // End of variables declaration//GEN-END:variables

    private int updateId = 0;

    public void updateBoard(UIInfo info) {
        board.update(info);
        if (shipsPreview != null && info instanceof BoardUIInfo) {
            BoardUIInfo buii = (BoardUIInfo) info;
            for (int n = 1; n <= 4; n++) //shipSizes
            {
                for (int i = 1; i <= (5 - n); i++) //shipNumber
                {
                    for (int j = 1; j <= n; j++) //blockNumber
                    {
                        final BoardUIInfo.SquareFill sf = buii.getBottomInfo(n, i - 1, j - 1);
                        shipsPreview.getPreviewBlock(n, i, j).setSquareFill(sf);
                    }
                }
            }
            updateId++;
        }
    }
}
