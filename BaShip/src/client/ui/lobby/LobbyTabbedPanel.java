/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.lobby;

import client.ClientMain;
import client.logic.GameC;
import client.logic.GlobalChatC;
import client.logic.UserC;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.GameInfo;
import sharedlib.structs.GameSearch;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;

/**
 *
 * @author luisp
 */
public class LobbyTabbedPanel extends JPanel {

    /**
     * Creates new form lobbyTabbedPanel
     */
    public LobbyTabbedPanel() {
        preInitComponents();
        initComponents();
        myInitComponents();
    }

    private ImageIcon filterIcon, clearIcon;

    private void addNewTab(JComponent tab, String name) {
        if (tab == null) {
            return;
        }
        jTabbedPane.add(name, tab);
        jTabbedPane.setTabComponentAt(jTabbedPane.getTabCount() - 1, null);
    }

    private DefaultTableModel usersTableModel;
    private DefaultTableModel gamesTableModel;

    private void preInitComponents() {
        usersTableModel = new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Username", "# Rank", "# Games", "# Wins", "# Shots", "Status"}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        gamesTableModel = new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Players", "Status"}) {
            @Override
            public Class getColumnClass(int columnIndex) {
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
    }

    private int sortingColumn = 1;
    private int maxEntriesPerTable = 20;

    private void myInitComponents() {
        //Add tabs
        jTabbedPane.removeAll();
        addNewTab(usersTab, "Players");
        addNewTab(gamesTab, "Games");
        addNewTab(globalChatTab, "Global Chat");

        if (!UserC.isLoggedIn()) {
            return;
        }

        /*filterIcon = new ImageIcon(getClass().getResource("/client/ui/Images/find.png"));
        clearIcon = new ImageIcon(getClass().getResource("/client/ui/Images/cancel.png"));
        applyFilterButton.setIcon(filterIcon);*/
        applyUserFilterButton.addComponentListener(ClientMain.mainFrame.imageResizer);
        clearUserFilterButton.addComponentListener(ClientMain.mainFrame.imageResizer);
        applyGamesFilterButton.addComponentListener(ClientMain.mainFrame.imageResizer);
        clearGamesFilterButton.addComponentListener(ClientMain.mainFrame.imageResizer);

        globalChatTextPane.setContentType("text/html");
        globalChatTextPane.setEditable(false);
        globalChatTextPane.setText("");

        refreshList();
        refreshGlobalMessages();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        usersTab = new javax.swing.JLayeredPane();
        scrollableUsersTable = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        filterUserField = new javax.swing.JTextField();
        applyUserFilterButton = new javax.swing.JButton();
        clearUserFilterButton = new javax.swing.JButton();
        gamesTab = new javax.swing.JLayeredPane();
        scrollableGamesTable = new javax.swing.JScrollPane();
        gamesTable = new javax.swing.JTable();
        filterGamesField = new javax.swing.JTextField();
        applyGamesFilterButton = new javax.swing.JButton();
        clearGamesFilterButton = new javax.swing.JButton();
        currentPlayingCheckbox = new javax.swing.JCheckBox();
        globalChatTab = new javax.swing.JLayeredPane();
        globalChatSendMessageField = new javax.swing.JTextField();
        globalChatSendButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        globalChatTextPane = new javax.swing.JTextPane();
        jTabbedPane = new javax.swing.JTabbedPane();

        usersTable.setModel(usersTableModel);
        usersTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollableUsersTable.setViewportView(usersTable);

        filterUserField.setToolTipText("Press enter to apply filter");
        filterUserField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterUserFieldActionPerformed(evt);
            }
        });

        applyUserFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/find.png"))); // NOI18N
        applyUserFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyUserFilterButtonActionPerformed(evt);
            }
        });

        clearUserFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/cancel.png"))); // NOI18N
        clearUserFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearUserFilterButtonActionPerformed(evt);
            }
        });

        usersTab.setLayer(scrollableUsersTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(filterUserField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(applyUserFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(clearUserFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout usersTabLayout = new javax.swing.GroupLayout(usersTab);
        usersTab.setLayout(usersTabLayout);
        usersTabLayout.setHorizontalGroup(
            usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollableUsersTable, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addGroup(usersTabLayout.createSequentialGroup()
                        .addComponent(filterUserField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(applyUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        usersTabLayout.setVerticalGroup(
            usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollableUsersTable, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(applyUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(clearUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(filterUserField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addContainerGap())
        );

        gamesTable.setModel(gamesTableModel);
        gamesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollableGamesTable.setViewportView(gamesTable);

        filterGamesField.setToolTipText("Press enter to apply filter");
        filterGamesField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterGamesFieldActionPerformed(evt);
            }
        });

        applyGamesFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/find.png"))); // NOI18N
        applyGamesFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyGamesFilterButtonActionPerformed(evt);
            }
        });

        clearGamesFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/cancel.png"))); // NOI18N
        clearGamesFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearGamesFilterButtonActionPerformed(evt);
            }
        });

        currentPlayingCheckbox.setText("Currently playing");

        gamesTab.setLayer(scrollableGamesTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(filterGamesField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(applyGamesFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(clearGamesFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(currentPlayingCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout gamesTabLayout = new javax.swing.GroupLayout(gamesTab);
        gamesTab.setLayout(gamesTabLayout);
        gamesTabLayout.setHorizontalGroup(
            gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gamesTabLayout.createSequentialGroup()
                        .addComponent(scrollableGamesTable, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(gamesTabLayout.createSequentialGroup()
                        .addComponent(filterGamesField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(applyGamesFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearGamesFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(currentPlayingCheckbox)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        gamesTabLayout.setVerticalGroup(
            gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollableGamesTable, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clearGamesFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(applyGamesFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(filterGamesField, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(currentPlayingCheckbox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        globalChatSendMessageField.setToolTipText("Press enter to send message");
        globalChatSendMessageField.setMaximumSize(new java.awt.Dimension(2147483647, 26));

        globalChatSendButton.setText("Send message");
        globalChatSendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalChatSendButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(globalChatTextPane);

        globalChatTab.setLayer(globalChatSendMessageField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        globalChatTab.setLayer(globalChatSendButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        globalChatTab.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout globalChatTabLayout = new javax.swing.GroupLayout(globalChatTab);
        globalChatTab.setLayout(globalChatTabLayout);
        globalChatTabLayout.setHorizontalGroup(
            globalChatTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(globalChatTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(globalChatTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(globalChatTabLayout.createSequentialGroup()
                        .addComponent(globalChatSendMessageField, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(globalChatSendButton)))
                .addContainerGap())
        );
        globalChatTabLayout.setVerticalGroup(
            globalChatTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(globalChatTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(globalChatTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(globalChatSendMessageField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(globalChatSendButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    void refreshList() {
        // TODO: finish
        updateUsersTableData(sortingColumn, maxEntriesPerTable);
        updateGamesTableData(1, maxEntriesPerTable);
    }

    private void filterUserFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterUserFieldActionPerformed
        applyUserFilterButtonActionPerformed(evt);
    }//GEN-LAST:event_filterUserFieldActionPerformed

    private void updateUsersTableData(int columnToSortWith, int maxUsers) {
        if (columnToSortWith < 0 || columnToSortWith >= usersTableModel.getColumnCount()) {
            throw new IndexOutOfBoundsException("Invalid column index to sort");
        }
        try {
            List<UserInfo> userList = UserC.getUserList(new UserSearch(false, filterUserField.getText(), columnToSortWith, maxUsers));
            while (usersTableModel.getRowCount() > 0) {
                usersTableModel.removeRow(usersTableModel.getRowCount() - 1);
            }
            for (int i = 0; i < userList.size(); i++) {
                UserInfo userInfo = userList.get(i);
                usersTableModel.addRow(new Object[]{userInfo.username, userInfo.rank, userInfo.nGames, userInfo.nWins, userInfo.nShots, userInfo.status});
            }
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

    private void updateGamesTableData(int columnToSortWith, int maxUsers) {
        if (columnToSortWith < 0 || columnToSortWith >= gamesTableModel.getColumnCount()) {
            throw new IndexOutOfBoundsException("Invalid column index to sort");
        }
        try {
            // TODO: checkbox hardcoded to false
            List<GameInfo> gamesList = GameC.getGameList(new GameSearch(false, filterGamesField.getText(), maxUsers));
            while (gamesTableModel.getRowCount() > 0) {
                gamesTableModel.removeRow(gamesTableModel.getRowCount() - 1);
            }
            SimpleDateFormat dF = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            for (int i = 0; i < gamesList.size(); i++) {
                try {
                    GameInfo gameInfo = gamesList.get(i);
                    gamesTableModel.addRow(new Object[]{gameInfo.player1Username + " VS " + gameInfo.player2Username,
                                                        gameInfo.state != GameInfo.State.Finished ? ("Playing: " + dF.format(gameInfo.startDate)) : "Played on: " + dF.format(gameInfo.endDate)});
                }
                catch (Exception ignored) {
                    // TODO: code in the try statement has bugs and crashes
                }
            }
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

    private String prevFilter = "";

    private void applyUserFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyUserFilterButtonActionPerformed
        if (filterUserField.getText().equals(prevFilter)) {
            return; //so it doesn't send unnecessary requests to the server
        }
        prevFilter = filterUserField.getText();
        updateUsersTableData(sortingColumn, maxEntriesPerTable);
    }//GEN-LAST:event_applyUserFilterButtonActionPerformed

    private void clearUserFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearUserFilterButtonActionPerformed
        if (filterUserField.getText() == null || filterUserField.getText().length() <= 0) {
            return; //so it doesn't send unnecessary requests to the server
        }
        filterUserField.setText("");
        applyUserFilterButtonActionPerformed(evt);
    }//GEN-LAST:event_clearUserFilterButtonActionPerformed

    private void filterGamesFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterGamesFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterGamesFieldActionPerformed

    private void applyGamesFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyGamesFilterButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_applyGamesFilterButtonActionPerformed

    private void clearGamesFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearGamesFilterButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clearGamesFilterButtonActionPerformed

    private void globalChatSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalChatSendButtonActionPerformed
        if (globalChatSendMessageField.getText().isEmpty()) {
            return;
        }

        try {
            GlobalChatC.sendGlobalMessage(globalChatSendMessageField.getText());
            globalChatSendMessageField.setText("");
        }
        catch (UserMessageException ex) {
            Logger.getLogger(LobbyTabbedPanel.class.getName()).log(Level.SEVERE, null, ex);
            ClientMain.showError(ex.getMessage());
        }
    }//GEN-LAST:event_globalChatSendButtonActionPerformed

    public void refreshGlobalMessages() {
        globalChatTextPane.setText(GlobalChatC.messagesHTML());
        globalChatTextPane.setCaretPosition(globalChatTextPane.getDocument().getLength()); // Scroll to bottom
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyGamesFilterButton;
    private javax.swing.JButton applyUserFilterButton;
    private javax.swing.JButton clearGamesFilterButton;
    private javax.swing.JButton clearUserFilterButton;
    private javax.swing.JCheckBox currentPlayingCheckbox;
    private javax.swing.JTextField filterGamesField;
    private javax.swing.JTextField filterUserField;
    private javax.swing.JLayeredPane gamesTab;
    private javax.swing.JTable gamesTable;
    private javax.swing.JButton globalChatSendButton;
    private javax.swing.JTextField globalChatSendMessageField;
    private javax.swing.JLayeredPane globalChatTab;
    private javax.swing.JTextPane globalChatTextPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JScrollPane scrollableGamesTable;
    private javax.swing.JScrollPane scrollableUsersTable;
    private javax.swing.JLayeredPane usersTab;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables

}
