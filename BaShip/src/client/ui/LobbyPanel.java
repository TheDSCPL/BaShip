/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.ClientMain;
import sharedlib.exceptions.UserMessageException;

/**
 *
 * @author luisp
 */
public class LobbyPanel extends javax.swing.JPanel {

    /**
     * Creates new form LobbyPanel
     */
    public LobbyPanel() {
        initComponents();

        if (ClientMain.loggedInUser == null) {
            ClientMain.showWarning("Fatal Error!"); // TODO: more descriptive error message
            ClientMain.mainFrame.changeToPanel(new LoginPanel());
            return;
        }

        loggedInAsLabel.setText(loggedInAsLabel.getText() + ClientMain.loggedInUser.username);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topBar = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();
        loggedInAsLabel = new javax.swing.JLabel();
        lobbyMainPanel = new javax.swing.JPanel();
        placeholderLabel = new javax.swing.JLabel();

        topBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        loggedInAsLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loggedInAsLabel.setText("Logged in as: ");
        loggedInAsLabel.setToolTipText("");

        javax.swing.GroupLayout topBarLayout = new javax.swing.GroupLayout(topBar);
        topBar.setLayout(topBarLayout);
        topBarLayout.setHorizontalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loggedInAsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(logoutButton)
                .addContainerGap())
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(topBarLayout.createSequentialGroup()
                        .addComponent(logoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addComponent(loggedInAsLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );

        placeholderLabel.setText("Lobby placeholder");

        javax.swing.GroupLayout lobbyMainPanelLayout = new javax.swing.GroupLayout(lobbyMainPanel);
        lobbyMainPanel.setLayout(lobbyMainPanelLayout);
        lobbyMainPanelLayout.setHorizontalGroup(
            lobbyMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyMainPanelLayout.createSequentialGroup()
                .addContainerGap(106, Short.MAX_VALUE)
                .addComponent(placeholderLabel)
                .addGap(190, 190, 190))
        );
        lobbyMainPanelLayout.setVerticalGroup(
            lobbyMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobbyMainPanelLayout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(placeholderLabel)
                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lobbyMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(topBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lobbyMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        try {
            ClientMain.server.doLogout();
        }
        catch (UserMessageException ex) {
            ClientMain.showWarning(ex.getMessage());
        }
        ClientMain.loggedInUser = null;
        ClientMain.mainFrame.changeToPanel(new LoginPanel());
    }//GEN-LAST:event_logoutButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel lobbyMainPanel;
    private javax.swing.JLabel loggedInAsLabel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel placeholderLabel;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
