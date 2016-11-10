package client.ui;

import client.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.logging.*;
import javax.swing.event.*;
import sharedlib.exceptions.*;

public class LoginPanel extends javax.swing.JPanel {

    /**
     * Creates new form LoginPanel
     */
    public LoginPanel() {
        initComponents();
        myInitComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        retypePasswordDialog = new javax.swing.JDialog();
        retypePasswordPanel = new javax.swing.JPanel();
        retypePasswordLabel = new javax.swing.JLabel();
        retypePasswordConfirmButton = new javax.swing.JButton();
        retypePasswordField = new javax.swing.JPasswordField();
        retypePasswordCancelButton = new javax.swing.JButton();
        retypePasswordNoMatchLabel = new javax.swing.JLabel();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialog3 = new javax.swing.JDialog();
        jDialog4 = new javax.swing.JDialog();
        loginPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        topBar = new javax.swing.JPanel();

        retypePasswordDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        retypePasswordDialog.setMinimumSize(new java.awt.Dimension(250, 170));
        retypePasswordDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        retypePasswordDialog.setName("Retype your password, please"); // NOI18N

        retypePasswordLabel.setText("Please, retype your password");

        retypePasswordConfirmButton.setText("Confirm");
        retypePasswordConfirmButton.setEnabled(false);
        retypePasswordConfirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retypePasswordConfirmButtonActionPerformed(evt);
            }
        });

        retypePasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retypePasswordFieldActionPerformed(evt);
            }
        });

        retypePasswordCancelButton.setText("Cancel");
        retypePasswordCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retypePasswordCancelButtonActionPerformed(evt);
            }
        });

        retypePasswordNoMatchLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        retypePasswordNoMatchLabel.setForeground(new java.awt.Color(255, 51, 51));
        retypePasswordNoMatchLabel.setText("Passwords don't match!");
        retypePasswordNoMatchLabel.setVisible(false);

        javax.swing.GroupLayout retypePasswordPanelLayout = new javax.swing.GroupLayout(retypePasswordPanel);
        retypePasswordPanel.setLayout(retypePasswordPanelLayout);
        retypePasswordPanelLayout.setHorizontalGroup(
            retypePasswordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retypePasswordPanelLayout.createSequentialGroup()
                .addGroup(retypePasswordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retypePasswordPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(retypePasswordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(retypePasswordLabel)
                            .addComponent(retypePasswordNoMatchLabel)))
                    .addGroup(retypePasswordPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(retypePasswordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(retypePasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(retypePasswordPanelLayout.createSequentialGroup()
                                .addComponent(retypePasswordConfirmButton)
                                .addGap(28, 28, 28)
                                .addComponent(retypePasswordCancelButton)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        retypePasswordPanelLayout.setVerticalGroup(
            retypePasswordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retypePasswordPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(retypePasswordLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retypePasswordNoMatchLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retypePasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retypePasswordPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(retypePasswordConfirmButton)
                    .addComponent(retypePasswordCancelButton))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout retypePasswordDialogLayout = new javax.swing.GroupLayout(retypePasswordDialog.getContentPane());
        retypePasswordDialog.getContentPane().setLayout(retypePasswordDialogLayout);
        retypePasswordDialogLayout.setHorizontalGroup(
            retypePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retypePasswordDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(retypePasswordPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        retypePasswordDialogLayout.setVerticalGroup(
            retypePasswordDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retypePasswordDialogLayout.createSequentialGroup()
                .addComponent(retypePasswordPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setMaximumSize(new java.awt.Dimension(386, 251));
        setMinimumSize(new java.awt.Dimension(386, 251));

        usernameField.setToolTipText("Insert Username");
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        usernameLabel.setText("Username:");

        passwordField.setToolTipText("Insert your password");
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        passwordLabel.setText("Password:");

        loginButton.setText("Login");
        loginButton.setToolTipText("Click to login with the inserted credentials");
        loginButton.setEnabled(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        registerButton.setText("Register");
        registerButton.setToolTipText("Click to retype your password");
        registerButton.setEnabled(false);
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameLabel)
                            .addComponent(passwordLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordField)
                            .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(loginPanelLayout.createSequentialGroup()
                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(registerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel))
                .addGap(26, 26, 26)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginButton)
                    .addComponent(registerButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        topBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout topBarLayout = new javax.swing.GroupLayout(topBar);
        topBar.setLayout(topBarLayout);
        topBarLayout.setHorizontalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(topBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(topBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void myInitComponents() {
        //CHECK USERNAME/PASSWORD FIELDS TO SEE IF BUTTONS SHOULD BE ENABLED
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkFilledUserPassFields();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkFilledUserPassFields();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkFilledUserPassFields();
            }
        };

        usernameField.getDocument().addDocumentListener(dl);
        passwordField.getDocument().addDocumentListener(dl);

        //LISTENER TO KNOW WHEN THE RETYPE PASSWORD DIALOG IS CLOSED
        retypePasswordDialog.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                resetRetypeDialog();
                setPanelEnabled(loginPanel, false, false);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                setPanelEnabled(loginPanel, true, false);
                checkFilledUserPassFields();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                setPanelEnabled(loginPanel, true, false);
                checkFilledUserPassFields();
                resetRetypeDialog();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {
                setPanelEnabled(loginPanel, false, false);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                setPanelEnabled(loginPanel, false, false);
            }
        });

        //CHECK IF RETYPE PASSWORD FIELD IS FILLED TO ENABLE THE CONFIRM BUTTON
        retypePasswordField.getDocument().addDocumentListener(new DocumentListener() {
            private void check() {
                retypePasswordConfirmButton.setEnabled(retypePasswordField.getPassword().length > 0);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                this.check();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                this.check();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                this.check();
            }
        });
    }

    private final Callable blinker = new Callable() {
        @Override
        public Object call() throws Exception {
            try {
                for (int i = 0; i < 4; i++) {
                    ClientMain.runOnUI(() -> {
                        retypePasswordNoMatchLabel.setForeground(new java.awt.Color(0, 0, 0));
                    }); //black
                    Thread.sleep(50);
                    ClientMain.runOnUI(() -> {
                        retypePasswordNoMatchLabel.setForeground(new java.awt.Color(255, 51, 51));
                    });  //red
                    Thread.sleep(50);
                }
            }
            catch (InterruptedException leaveThread) {
                ClientMain.runOnUI(() -> {
                    retypePasswordNoMatchLabel.setForeground(new java.awt.Color(255, 51, 51));
                });  //red
            }
            return null;
        }
    ;

    };
    private Future lastActiveBlinker = null;

    private void blinkPasswordsMismatch() {
        retypePasswordNoMatchLabel.setVisible(true);
        try {
            if (lastActiveBlinker != null) {
                lastActiveBlinker.cancel(true);
            }
            lastActiveBlinker = ClientMain.runBackground(blinker);
        }
        catch (Exception ignored) {

        }
    }

    private void resetRetypeDialog() {
        retypePasswordField.setText("");
        retypePasswordNoMatchLabel.setVisible(false);
        try {
            Point p = null;
            synchronized (this.getTreeLock()) {
                p = this.getLocationOnScreen();
            }
            retypePasswordDialog.setLocation(p);
        }
        catch (Throwable ignored) {

        }
        retypePasswordDialog.setLocation(getPanelLocation());
        retypePasswordNoMatchLabel.setForeground(new java.awt.Color(255, 51, 51));  //red
        retypePasswordNoMatchLabel.setFont(new java.awt.Font("Tahoma", 1, 11));     //bold
    }

    /**
     * Sets all components in a Container as enabled or not.
     *
     * @param pane pane to operate on
     * @param enabled if all components should be set to enabled or not
     * @param recursive if all nested Containers' components should also be
     * considered
     */
    private void setPanelEnabled(Container pane, boolean enabled, boolean recursive) {
        synchronized (pane.getTreeLock()) //necessary to call getComponents
        {
            for (Component c : pane.getComponents()) {
                if (recursive && (c instanceof Container)) {
                    System.out.println("Recursive setPanelEnabled " + enabled + " " + c.getClass().getSimpleName());
                    setPanelEnabled((Container) c, enabled, true);
                }
                else {
                    System.out.println("Non-recursive setPanelEnabled " + enabled + " " + c.getClass().getSimpleName());
                }
                c.setEnabled(enabled);
            }
            pane.setEnabled(enabled);
        }
    }

    private void setLoadingVisible(boolean visible) {
        System.out.println("setLoadingVisible " + visible);
        Component[] components;
        setPanelEnabled(loginPanel, !visible, false);
        loginButton.setVisible(!visible);
        registerButton.setVisible(!visible);

        if (!visible) //if deactivating the loading pane, recheck all the enabled and disabled components in the main pane
        {
            setPanelEnabled(loginPanel, true, false);
            //checkFilledUserPassFields();
        }
    }

    /**
     * Sets the buttons to enabled or disabled depending on if the
     * username/password fields are empty or not
     */
    private void checkFilledUserPassFields() {
        if (usernameField.getText().length() > 0 && passwordField.getPassword().length > 0) {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
        }
        else {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
        }
    }

    public Point getPanelLocation() {
        Point p = null;
        try {
            synchronized (this.getTreeLock()) {
                p = this.getLocationOnScreen();
            }
        }
        catch (Throwable ignored) {

        }
        return p;
    }

    private void retypePasswordCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retypePasswordCancelButtonActionPerformed
        retypePasswordDialog.dispose();
    }//GEN-LAST:event_retypePasswordCancelButtonActionPerformed

    /**
     * Compares two char[]
     *
     * @return true of they have the same chars in the same order and false
     * otherwise
     */
    private boolean compareCharArray(char[] ca1, char[] ca2) {
        if (ca1.length != ca2.length) {
            return false;
        }
        for (int i = 0; i < ca1.length; i++) {
            if (ca1[i] != ca2[i]) {
                return false;
            }
        }
        return true;
    }

    private void retypePasswordConfirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retypePasswordConfirmButtonActionPerformed
        if (!compareCharArray(passwordField.getPassword(), retypePasswordField.getPassword())) {
            retypePasswordNoMatchLabel.setVisible(true);
            blinkPasswordsMismatch();
            return;
        }

        try {
            ClientMain.loggedInUser = ClientMain.server.doRegister(usernameField.getText(), passwordField.getPassword());
        }
        catch (UserMessageException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
            ClientMain.showAlert(ex.getMessage());
        }

        retypePasswordDialog.dispose();
        setLoadingVisible(true);
    }//GEN-LAST:event_retypePasswordConfirmButtonActionPerformed

    private void retypePasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retypePasswordFieldActionPerformed
        if (!retypePasswordConfirmButton.isEnabled() || !retypePasswordConfirmButton.isVisible()) {
            return;
        }
        retypePasswordConfirmButtonActionPerformed(evt);
    }//GEN-LAST:event_retypePasswordFieldActionPerformed

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        resetRetypeDialog();
        retypePasswordDialog.setVisible(true);
    }//GEN-LAST:event_registerButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if (!loginButton.isEnabled() || !loginButton.isVisible()) {
            return;
        }
        
        try {
            ClientMain.loggedInUser = ClientMain.server.doLogin(usernameField.getText(), passwordField.getPassword());
        }
        catch (UserMessageException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
            ClientMain.showAlert(ex.getMessage());
        }

        setLoadingVisible(true);
    }//GEN-LAST:event_loginButtonActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        loginButtonActionPerformed(evt);
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        loginButtonActionPerformed(evt);
    }//GEN-LAST:event_usernameFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginPanel;
    public final javax.swing.JPasswordField passwordField = new javax.swing.JPasswordField();
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton registerButton;
    private javax.swing.JButton retypePasswordCancelButton;
    private javax.swing.JButton retypePasswordConfirmButton;
    private javax.swing.JDialog retypePasswordDialog;
    private javax.swing.JPasswordField retypePasswordField;
    private javax.swing.JLabel retypePasswordLabel;
    private javax.swing.JLabel retypePasswordNoMatchLabel;
    private javax.swing.JPanel retypePasswordPanel;
    private javax.swing.JPanel topBar;
    public final javax.swing.JTextField usernameField = new javax.swing.JTextField();
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
