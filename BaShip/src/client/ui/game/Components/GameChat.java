/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import client.ClientMain;
import client.logic.*;
import client.other.ImageResizer;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.GameUIInfo;

/**
 *
 * @author luisp
 */
public class GameChat extends javax.swing.JPanel {

    /**
     * Creates new form GameChat
     */
    public GameChat() {
        initComponents();
        chatPane.setContentType("text/html");
    }

    public void setPlaying() {
        if (lastType == GameUIInfo.UIType.Play) {
            return;
        }
        lastType = GameUIInfo.UIType.Play;

        removeAll();

        gameChatSendMessageField = new javax.swing.JTextField();
        gameChatSendButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatPane = new javax.swing.JTextPane();

        gameChatSendMessageField.setToolTipText("Press enter to send message");
        gameChatSendMessageField.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        gameChatSendMessageField.addActionListener(evt -> {
            gameChatSendMessageFieldActionPerformed(evt);
        }
        );

        gameChatSendButton.setText("Send");
        gameChatSendButton.addActionListener(evt -> {
            gameChatSendButtonActionPerformed(evt);
        }
        );

        jScrollPane1.setViewportView(chatPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 264, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(gameChatSendMessageField, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(gameChatSendButton)))
                                .addContainerGap()))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 432, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(gameChatSendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(gameChatSendMessageField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap()))
        );
    }

    public void setSpectating() {
        if (lastType == GameUIInfo.UIType.Spectate) {
            return;
        }
        lastType = GameUIInfo.UIType.Spectate;

        removeAll();

        spactatingLabel = new javax.swing.JLabel();

        spactatingLabel.setText("Spectator");

        javax.swing.GroupLayout spectatingChatPanelLayout = new javax.swing.GroupLayout(this);
        this.setLayout(spectatingChatPanelLayout);
        spectatingChatPanelLayout.setHorizontalGroup(
                spectatingChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(spectatingChatPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, spectatingChatPanelLayout.createSequentialGroup()
                          .addContainerGap(109, Short.MAX_VALUE)
                          .addComponent(spactatingLabel)
                          .addGap(108, 108, 108))
        );
        spectatingChatPanelLayout.setVerticalGroup(
                spectatingChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(spectatingChatPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spactatingLabel)
                        .addGap(15, 15, 15))
        );
    }

    public void setReplay() {
        if (lastType == GameUIInfo.UIType.Replay) {
            return;
        }
        lastType = GameUIInfo.UIType.Replay;

        removeAll();

        previousTurnButton = new javax.swing.JButton();
        turnsLabel = new javax.swing.JLabel();
        nextTurnButton = new javax.swing.JButton();

        jScrollPane1.setViewportView(chatPane);

        previousTurnButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/back.png"))); // NOI18N
        previousTurnButton.addComponentListener(new ImageResizer());

        turnsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        turnsLabel.setText("Turn XX of YY");

        nextTurnButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/next.png"))); // NOI18N
        nextTurnButton.addComponentListener(new ImageResizer());

        previousTurnButton.addActionListener((evt) -> {
            previousTurnButtonActionPerformed(evt);
        });

        nextTurnButton.addActionListener((evt) -> {
            nextTurnButtonActionPerformed(evt);
        });

        javax.swing.GroupLayout replayPanelLayout = new javax.swing.GroupLayout(this);
        this.setLayout(replayPanelLayout);
        replayPanelLayout.setHorizontalGroup(
                replayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(replayPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                .addGroup(replayPanelLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(previousTurnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(turnsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextTurnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(49, Short.MAX_VALUE))
        );
        replayPanelLayout.setVerticalGroup(
                replayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(replayPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(replayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(replayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(nextTurnButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(previousTurnButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(turnsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
        );
    }

    private GameUIInfo.UIType lastType = GameUIInfo.UIType.Play;
    
    public void updateState(GameUIInfo info)
    {
        if(info == null)
            return;
        switch(info.uiType)
        {
            case Play:
                ClientMain.runOnUI(this::setPlaying);
                break;
            case Replay:
                boolean canShowPrevious = false, canShowNext = false;
                String labelText = "";
                if(info.canShowPreviousMove != null)
                    canShowPrevious = info.canShowPreviousMove;
                if(info.canShowNextMove != null)
                    canShowNext = info.canShowNextMove;
                if(info.moveCounterText != null)
                    labelText = info.moveCounterText;
                
                setReplay();
                
                previousTurnButton.setEnabled(canShowPrevious);
                nextTurnButton.setEnabled(canShowNext);
                turnsLabel.setText(labelText);
                break;
            case Spectate:
                ClientMain.runOnUI(this::setSpectating);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gameChatSendMessageField = new javax.swing.JTextField();
        gameChatSendButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatPane = new javax.swing.JTextPane();

        gameChatSendMessageField.setToolTipText("Press enter to send message");
        gameChatSendMessageField.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        gameChatSendMessageField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameChatSendMessageFieldActionPerformed(evt);
            }
        });

        gameChatSendButton.setText("Send");
        gameChatSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameChatSendButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(chatPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(gameChatSendMessageField, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(gameChatSendButton)))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 432, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(gameChatSendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(gameChatSendMessageField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void gameChatSendMessageFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameChatSendMessageFieldActionPerformed
        gameChatSendButtonActionPerformed(evt);
    }//GEN-LAST:event_gameChatSendMessageFieldActionPerformed

    private void gameChatSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gameChatSendButtonActionPerformed
        if (gameChatSendMessageField.getText().isEmpty()) {
            return;
        }

        GameChatC.sendGameMessage(gameChatSendMessageField.getText());
        gameChatSendMessageField.setText("");
    }//GEN-LAST:event_gameChatSendButtonActionPerformed

    private void previousTurnButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            ClientMain.server.showPreviousMove();
        }
        catch (UserMessageException ex) {
            ClientMain.showWarning(ex.getMessage());
        }
    }

    private void nextTurnButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            ClientMain.server.showNextMove();
        }
        catch (UserMessageException ex) {
            ClientMain.showWarning(ex.getMessage());
        }
    }

    public void refreshGameMessages() {
        chatPane.setText(GameChatC.messagesHTML());
        chatPane.setCaretPosition(chatPane.getDocument().getLength()); // Scroll to bottom
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane chatPane;
    private javax.swing.JButton gameChatSendButton;
    private javax.swing.JTextField gameChatSendMessageField;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel spactatingLabel;
    private javax.swing.JLabel turnsLabel;
    private javax.swing.JButton previousTurnButton;
    private javax.swing.JButton nextTurnButton;
}
