/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game;

import client.ui.game.Components.*;
import java.awt.*;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;

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
        leftBoard = new BoardContainer(true);
        rightBoard = new BoardContainer(false);
        topBar = new TopBar();
        gameChat = new GameChat();

        add(topBar, BorderLayout.PAGE_START);
        add(leftBoard, BorderLayout.LINE_START);
        add(gameChat, BorderLayout.CENTER);
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
        topBar.setFirstPlayer(info.titleLeft);
        topBar.setSecondPlayer(info.titleRight);
        topBar.setTurn(info.player1Turn, info.player2Turn);

        rightBoard.setOpponentWaiting(!info.showRightBoard);

        // Only the Boards that are WaitingOnOpponentPanel will perform any action 
        leftBoard.updateBoard(info);
        rightBoard.updateBoard(info);
    }

    public void updateBoardInfo(BoardUIInfo info) {
        if (info.leftBoard) {
            leftBoard.updateBoard(info);
        }
        else {
            rightBoard.updateBoard(info);
        }
    }

    private final BoardContainer leftBoard;
    private final BoardContainer rightBoard;
    private final GameChat gameChat;
    private final TopBar topBar;

    public void refreshGameMessages() {
        gameChat.refreshGameMessages();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
