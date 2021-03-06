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
import com.lpcsd.CompanionLibrary.Strings.HTMLLib;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    private void myInitComponents() {
        //Add tabs
        jTabbedPane.removeAll();
        addNewTab(usersTab, "Players");
        addNewTab(gamesTab, "Games");
        addNewTab(globalChatTab, "Global Chat");

        if (!UserC.isLoggedIn()) {
            return;
        }

        usersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2 && row != -1) {
                    doubleClickUsersTable(row);
                }
            }
        });

        gamesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2 && row != -1) {
                    doubleClickGamesTable(row);
                }
            }
        });

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
        nextUsersPageButton = new javax.swing.JButton();
        previousUsersPageButton = new javax.swing.JButton();
        usersPageLabel = new javax.swing.JLabel();
        gamesTab = new javax.swing.JLayeredPane();
        scrollableGamesTable = new javax.swing.JScrollPane();
        gamesTable = new javax.swing.JTable();
        filterGamesField = new javax.swing.JTextField();
        applyGamesFilterButton = new javax.swing.JButton();
        clearGamesFilterButton = new javax.swing.JButton();
        currentPlayingCheckbox = new javax.swing.JCheckBox();
        previousGamesPageButton = new javax.swing.JButton();
        gamesPageLabel = new javax.swing.JLabel();
        nextGamesPageButton = new javax.swing.JButton();
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

        nextUsersPageButton.setText(">");
        nextUsersPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextUsersPageButtonActionPerformed(evt);
            }
        });

        previousUsersPageButton.setText("<");
        previousUsersPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousUsersPageButtonActionPerformed(evt);
            }
        });

        usersPageLabel.setText("Page 1");

        usersTab.setLayer(scrollableUsersTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(filterUserField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(applyUserFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(clearUserFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(nextUsersPageButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(previousUsersPageButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        usersTab.setLayer(usersPageLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout usersTabLayout = new javax.swing.GroupLayout(usersTab);
        usersTab.setLayout(usersTabLayout);
        usersTabLayout.setHorizontalGroup(
            usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollableUsersTable, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addGroup(usersTabLayout.createSequentialGroup()
                        .addComponent(filterUserField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(applyUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(previousUsersPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usersPageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextUsersPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        usersTabLayout.setVerticalGroup(
            usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(usersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollableUsersTable, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(applyUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(clearUserFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(filterUserField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                    .addGroup(usersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nextUsersPageButton)
                        .addComponent(previousUsersPageButton)
                        .addComponent(usersPageLabel)))
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

        currentPlayingCheckbox.setText("Playing");
        currentPlayingCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentPlayingCheckboxActionPerformed(evt);
            }
        });

        previousGamesPageButton.setText("<");
        previousGamesPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousGamesPageButtonActionPerformed(evt);
            }
        });

        gamesPageLabel.setText("Page 1");

        nextGamesPageButton.setText(">");
        nextGamesPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextGamesPageButtonActionPerformed(evt);
            }
        });

        gamesTab.setLayer(scrollableGamesTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(filterGamesField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(applyGamesFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(clearGamesFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(currentPlayingCheckbox, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(previousGamesPageButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(gamesPageLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        gamesTab.setLayer(nextGamesPageButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout gamesTabLayout = new javax.swing.GroupLayout(gamesTab);
        gamesTab.setLayout(gamesTabLayout);
        gamesTabLayout.setHorizontalGroup(
            gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollableGamesTable, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addGroup(gamesTabLayout.createSequentialGroup()
                        .addComponent(filterGamesField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(applyGamesFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(clearGamesFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentPlayingCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(previousGamesPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gamesPageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextGamesPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(gamesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nextGamesPageButton)
                            .addComponent(previousGamesPageButton)
                            .addComponent(gamesPageLabel))
                        .addComponent(currentPlayingCheckbox)))
                .addContainerGap())
        );

        globalChatSendMessageField.setToolTipText("Press enter to send message");
        globalChatSendMessageField.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        globalChatSendMessageField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalChatSendMessageFieldActionPerformed(evt);
            }
        });

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
        updateUsersTableData();
        updateGamesTableData();
    }

    private void filterUserFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterUserFieldActionPerformed
        applyUserFilterButtonActionPerformed(evt);
    }//GEN-LAST:event_filterUserFieldActionPerformed

    private List<UserInfo> usersList;
    private int usersPageIndex = 0;
    private int gamesPageIndex = 0;
    
    private void updateUsersTableData() {
        usersPageIndex = Math.max(usersPageIndex, 0);   //will always be positive
        List<UserInfo> list = UserC.getUserList(new UserSearch(filterUserField.getText(), usersPageIndex));
        
        if( (list == null || list.isEmpty()) && (usersPageIndex > 0) )
        {
            usersPageIndex--;
            updateUsersTableData();
            return;
        }

        usersPageLabel.setText("Page " + (usersPageIndex+1));
        
        if (list != null) {
            while (usersTableModel.getRowCount() > 0) {
                usersTableModel.removeRow(usersTableModel.getRowCount() - 1);
            }
            for (int i = 0; i < list.size(); i++) {
                UserInfo userInfo = list.get(i);
                String statusColor;
                switch(userInfo.status)
                {
                    case Offline:
                        statusColor = "e60000";    //red
                        break;
                    case Online:
                        statusColor = "009933";    //green
                        break;
                    case Playing:
                        statusColor = "ff9900";    //orange
                        break;
                    case Waiting:
                        statusColor = "6699ff";    //blue
                        break;
                    default:
                        statusColor = "black";     //black
                }
                /*StringBuilder status = new StringBuilder("<html><body><font color=\"");
                switch(userInfo.status)
                {
                    case Offline:
                        status.append("e60000");    //red
                        break;
                    case Online:
                        status.append("009933");    //green
                        break;
                    case Playing:
                        status.append("ff9900");    //orange
                        break;
                    case Waiting:
                        status.append("6699ff");    //blue
                        break;
                    default:
                        status.append("black");     //black
                }
                status  .append("\">")
                        .append(userInfo.status.name())
                        .append(UserC.getLoggedInUserID().equals(userInfo.id) ? " (me)" : "")
                        .append("</font></body></html>");*/
                String status = HTMLLib.HTMLFormat.build(
                                                            new HTMLLib.HTMLFormat(userInfo.status.name() + (UserC.getLoggedInUserID().equals(userInfo.id) ? " (me)" : ""))
                                                                    .setColor(statusColor)
                                                        );
                usersTableModel.addRow(new Object[]{userInfo.username, userInfo.rank, userInfo.nGames, userInfo.nWins, userInfo.nShots, status});
            }
        }
        usersList = list;
    }

    List<GameInfo> gamesList;

    private void updateGamesTableData() {
        gamesPageIndex = Math.max(gamesPageIndex, 0);   //will always be positive
        List<GameInfo> list = GameC.getGameList(new GameSearch(currentPlayingCheckbox.isSelected(), filterGamesField.getText(), gamesPageIndex));

        if( (list == null || list.isEmpty()) && (gamesPageIndex > 0) )
        {
            gamesPageIndex--;
            updateGamesTableData();
            return;
        }
        
        gamesPageLabel.setText("Page " + (gamesPageIndex+1));
        
        if (list != null) {
            while (gamesTableModel.getRowCount() > 0) {
                gamesTableModel.removeRow(gamesTableModel.getRowCount() - 1);
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
            for (int i = 0; i < list.size(); i++) {
                GameInfo gameInfo = list.get(i);

                String s1 = gameInfo.player1Username + " VS " + gameInfo.player2Username;

                String s2 = null;
                switch (gameInfo.state) {
                    case Finished:
                        s2 = "Finished on " + df.format(gameInfo.endDate);
                        break;
                    case Created:
                        s2 = "Placing ships";
                        break;
                    case Playing:
                        s2 = "Playing, started on " + df.format(gameInfo.startDate);
                        break;
                }

                gamesTableModel.addRow(new Object[]{s1, s2});
            }
        }
        gamesList = list;
    }

    private void doubleClickUsersTable(int row) {
        UserC.doubleClickUser(usersList.get(row));
    }

    private void doubleClickGamesTable(int row) {
        GameC.doubleClickGame(gamesList.get(row));
    }

    private void applyUserFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyUserFilterButtonActionPerformed
        updateUsersTableData();
    }//GEN-LAST:event_applyUserFilterButtonActionPerformed

    private void clearUserFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearUserFilterButtonActionPerformed
        filterUserField.setText("");
        updateUsersTableData();
    }//GEN-LAST:event_clearUserFilterButtonActionPerformed

    private void filterGamesFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterGamesFieldActionPerformed
        applyGamesFilterButtonActionPerformed(evt);
    }//GEN-LAST:event_filterGamesFieldActionPerformed

    private void applyGamesFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyGamesFilterButtonActionPerformed
        updateGamesTableData();
    }//GEN-LAST:event_applyGamesFilterButtonActionPerformed

    private void clearGamesFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearGamesFilterButtonActionPerformed
        filterGamesField.setText("");
        updateGamesTableData();
    }//GEN-LAST:event_clearGamesFilterButtonActionPerformed

    private void globalChatSendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalChatSendButtonActionPerformed
        if (globalChatSendMessageField.getText().isEmpty()) {
            return;
        }

        GlobalChatC.sendGlobalMessage(globalChatSendMessageField.getText());
        globalChatSendMessageField.setText("");
    }//GEN-LAST:event_globalChatSendButtonActionPerformed

    private void globalChatSendMessageFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalChatSendMessageFieldActionPerformed
        globalChatSendButtonActionPerformed(evt);
    }//GEN-LAST:event_globalChatSendMessageFieldActionPerformed

    private void currentPlayingCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentPlayingCheckboxActionPerformed
        updateGamesTableData();
    }//GEN-LAST:event_currentPlayingCheckboxActionPerformed

    private void previousUsersPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousUsersPageButtonActionPerformed
        usersPageIndex--;
        updateUsersTableData();
    }//GEN-LAST:event_previousUsersPageButtonActionPerformed

    private void nextUsersPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextUsersPageButtonActionPerformed
        usersPageIndex++;
        updateUsersTableData();
    }//GEN-LAST:event_nextUsersPageButtonActionPerformed

    private void previousGamesPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousGamesPageButtonActionPerformed
        gamesPageIndex--;
        updateGamesTableData();
    }//GEN-LAST:event_previousGamesPageButtonActionPerformed

    private void nextGamesPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextGamesPageButtonActionPerformed
        gamesPageIndex++;
        updateGamesTableData();
    }//GEN-LAST:event_nextGamesPageButtonActionPerformed

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
    private javax.swing.JLabel gamesPageLabel;
    private javax.swing.JLayeredPane gamesTab;
    private javax.swing.JTable gamesTable;
    private javax.swing.JButton globalChatSendButton;
    private javax.swing.JTextField globalChatSendMessageField;
    private javax.swing.JLayeredPane globalChatTab;
    private javax.swing.JTextPane globalChatTextPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JButton nextGamesPageButton;
    private javax.swing.JButton nextUsersPageButton;
    private javax.swing.JButton previousGamesPageButton;
    private javax.swing.JButton previousUsersPageButton;
    private javax.swing.JScrollPane scrollableGamesTable;
    private javax.swing.JScrollPane scrollableUsersTable;
    private javax.swing.JLabel usersPageLabel;
    private javax.swing.JLayeredPane usersTab;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables

}
