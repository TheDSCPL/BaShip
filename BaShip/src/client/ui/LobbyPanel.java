/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.ClientMain;
import client.logic.UserC;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;

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

        if (!UserC.isLoggedIn()) {
            ClientMain.showWarning("Lobby panel shown without an user logged in");
            ClientMain.mainFrame.changeToPanel(new LoginPanel());
            return;
        }

        loggedInAsLabel.setText(loggedInAsLabel.getText() + UserC.getLoggedInUsername());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lobby1 = new javax.swing.JLayeredPane();
        jLabel1 = new javax.swing.JLabel();
        topBar = new javax.swing.JPanel();
        logoutButton = new javax.swing.JButton();
        loggedInAsLabel = new javax.swing.JLabel();
        lobbyTabbedPanel1 = new client.ui.LobbyTabbedPanel();

        jLabel1.setText("jLabel1");

        lobby1.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lobby1Layout = new javax.swing.GroupLayout(lobby1);
        lobby1.setLayout(lobby1Layout);
        lobby1Layout.setHorizontalGroup(
            lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobby1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(jLabel1)
                .addContainerGap(117, Short.MAX_VALUE))
        );
        lobby1Layout.setVerticalGroup(
            lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobby1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel1)
                .addContainerGap(86, Short.MAX_VALUE))
        );

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
                .addContainerGap(299, Short.MAX_VALUE)
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(topBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lobbyTabbedPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(topBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lobbyTabbedPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        try {
            UserC.logout();
        }
        catch (UserMessageException ex) {
            ClientMain.showWarning(ex.getMessage());
        }
        ClientMain.mainFrame.changeToPanel(new LoginPanel());
    }//GEN-LAST:event_logoutButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane lobby1;
    private client.ui.LobbyTabbedPanel lobbyTabbedPanel1;
    private javax.swing.JLabel loggedInAsLabel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel topBar;
    // End of variables declaration//GEN-END:variables
}
