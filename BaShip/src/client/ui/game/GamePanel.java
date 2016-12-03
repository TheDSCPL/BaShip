/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game;

import client.ui.game.Components.*;
import java.awt.*;
import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.GameUIInfo;

public final class GamePanel extends javax.swing.JPanel {

    /**
     * Creates new form GamePanel
     */
    public GamePanel() {
        initComponents();
        
        /*final JLayeredPane leftPane = new JLayeredPane();
        leftPane.setLayer(new Board(true),JLayeredPane.DEFAULT_LAYER);
        final JLayeredPane rightPane = new JLayeredPane();
        rightPane.setLayer(new Board(false),JLayeredPane.DEFAULT_LAYER);*/
        
        leftBoard = new BoardContainer();
        rightBoard = new BoardContainer();
        
        add(new TopBar(), BorderLayout.PAGE_START);
        add(leftBoard, BorderLayout.LINE_START);
        add(new GameChat(), BorderLayout.CENTER);
        add(rightBoard, BorderLayout.LINE_END);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    public void updateGameScreen(GameUIInfo info) {
        System.out.println("received info:" + info);
        // TODO: LUIS
    }

    public void updateBoardInfo(BoardUIInfo info) {
        System.out.println("" + info);
        System.out.println(leftBoard);
        System.out.println(rightBoard);
        
        if (info.leftBoard) {
            leftBoard.updateBoard(info);
        }
        else {
            rightBoard.updateBoard(info);
        }
    }
    
    private BoardContainer leftBoard;
    private BoardContainer rightBoard;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
