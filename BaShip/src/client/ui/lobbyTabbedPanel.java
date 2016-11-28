/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui;

import client.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import sharedlib.exceptions.*;
import sharedlib.tuples.*;

/**
 *
 * @author luisp
 */
public class lobbyTabbedPanel extends JPanel {

    /**
     * Creates new form lobbyTabbedPanel
     */
    public lobbyTabbedPanel() {
        preInitComponents();
        initComponents();
        myInitComponents();
    }

    private ImageIcon filterIcon, clearIcon;

    private void addNewTab(JComponent tab, String name) {
        if (tab == null) {
            return;
        }
        jTabbedPane1.add(name, tab);
        jTabbedPane1.setTabComponentAt(jTabbedPane1.getTabCount() - 1, null);
    }

    private DefaultTableModel tableModel;

    private void preInitComponents() {
        tableModel = new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{"Username", "# Rank", "# Games", "# Wins", "# Shots", "Status"}) {
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
    }

    private int sortingColumn = 1;
    private int maxUsersInTable = 20;

    private void myInitComponents() {
        
        //Add tabs
        jTabbedPane1.removeAll();
        addNewTab(lobby1, "Players");
        addNewTab(lobby2, "Games");
        //addNewTab(lobby3, "Global Chat");

        /*filterIcon = new ImageIcon(getClass().getResource("/client/ui/Images/find.png"));
        clearIcon = new ImageIcon(getClass().getResource("/client/ui/Images/cancel.png"));
        applyFilterButton.setIcon(filterIcon);*/
        applyFilterButton.addComponentListener(ClientMain.mainFrame.imageButtonResizer);
        clearFilterButton.addComponentListener(ClientMain.mainFrame.imageButtonResizer);

        updateUsersTableData(sortingColumn, maxUsersInTable);
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
        scrollableTable = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        filterField = new javax.swing.JTextField();
        applyFilterButton = new javax.swing.JButton();
        clearFilterButton = new javax.swing.JButton();
        lobby2 = new javax.swing.JLayeredPane();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        jTable1.setModel(tableModel);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollableTable.setViewportView(jTable1);

        filterField.setToolTipText("Press enter to apply filter");
        filterField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterFieldActionPerformed(evt);
            }
        });

        applyFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/find.png"))); // NOI18N
        applyFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyFilterButtonActionPerformed(evt);
            }
        });

        clearFilterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/client/ui/Images/cancel.png"))); // NOI18N
        clearFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFilterButtonActionPerformed(evt);
            }
        });

        lobby1.setLayer(scrollableTable, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lobby1.setLayer(filterField, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lobby1.setLayer(applyFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);
        lobby1.setLayer(clearFilterButton, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lobby1Layout = new javax.swing.GroupLayout(lobby1);
        lobby1.setLayout(lobby1Layout);
        lobby1Layout.setHorizontalGroup(
            lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobby1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lobby1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(filterField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(applyFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(lobby1Layout.createSequentialGroup()
                        .addComponent(scrollableTable, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        lobby1Layout.setVerticalGroup(
            lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobby1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollableTable, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lobby1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(applyFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(filterField))
                    .addComponent(clearFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jLabel2.setText("jLabel2");

        lobby2.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout lobby2Layout = new javax.swing.GroupLayout(lobby2);
        lobby2.setLayout(lobby2Layout);
        lobby2Layout.setHorizontalGroup(
            lobby2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lobby2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addContainerGap(493, Short.MAX_VALUE))
        );
        lobby2Layout.setVerticalGroup(
            lobby2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lobby2Layout.createSequentialGroup()
                .addContainerGap(485, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void filterFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterFieldActionPerformed
        applyFilterButtonActionPerformed(evt);
    }//GEN-LAST:event_filterFieldActionPerformed

    private void updateUsersTableData(int columnToSortWith, int maxUsers) {
        if (columnToSortWith < 0 || columnToSortWith >= tableModel.getColumnCount()) {
            throw new Error("Invalid column index to sort");
        }
        try {
            List<UserInfo> userList = ClientMain.server.getUserList(false, filterField.getText(), columnToSortWith, maxUsers);
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(tableModel.getRowCount() - 1);
            }
            for (int i = 0; i < userList.size(); i++) {
                UserInfo userInfo = userList.get(i);
                tableModel.addRow(new Object[]{userInfo.username, userInfo.rank, userInfo.nGames, userInfo.nWins, userInfo.nShots, userInfo.status});
            }
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
        catch (Exception ignored) {
            ignored.printStackTrace();
            try {
                System.err.println("Exception: " + ignored.getMessage());
                ClientMain.server.doLogout();
            }
            catch (UserMessageException ex) {
                Logger.getLogger(lobbyTabbedPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String prevFilter = "";

    private void applyFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyFilterButtonActionPerformed
        if (filterField.getText().equals(prevFilter)) {
            return; //so it doesn't send unnecessary requests to the server
        }
        prevFilter = filterField.getText();
        updateUsersTableData(sortingColumn, maxUsersInTable);
    }//GEN-LAST:event_applyFilterButtonActionPerformed

    private void clearFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFilterButtonActionPerformed
        if (filterField.getText() == null || filterField.getText().length() <= 0) {
            return; //so it doesn't send unnecessary requests to the server
        }
        filterField.setText("");
        filterFieldActionPerformed(evt);
    }//GEN-LAST:event_clearFilterButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyFilterButton;
    private javax.swing.JButton clearFilterButton;
    private javax.swing.JTextField filterField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLayeredPane lobby1;
    private javax.swing.JLayeredPane lobby2;
    private javax.swing.JScrollPane scrollableTable;
    // End of variables declaration//GEN-END:variables
}
