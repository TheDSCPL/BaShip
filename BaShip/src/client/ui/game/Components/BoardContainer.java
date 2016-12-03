/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import pt.up.fe.lpro1613.sharedlib.structs.*;

/**
 *
 * @author luisp
 */
public class BoardContainer extends javax.swing.JPanel {

    /**
     * Creates new form BoardContainer
     */
    public BoardContainer() {
        initComponents();
        this.left = board.left;
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public final boolean left;
    
    private boolean waiting;
    public void setOpponentWaiting(boolean waiting)
    {
        this.waiting = waiting;
        if(waiting && !(board instanceof WaitingOnOpponentPanel))
        {
            board = new WaitingOnOpponentPanel(board.left);
            board.revalidate();
            board.repaint();
            this.revalidate();
            this.repaint();
        }
        else if(!waiting && !(board instanceof Board))
        {
            board = new Board(left);
            board.revalidate();
            board.repaint();
            this.revalidate();
            this.repaint();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private client.ui.game.Components.SuperBoard board;
    // End of variables declaration//GEN-END:variables

    public void updateBoard(UIInfo info) {
        board.update(info);
    }
}
