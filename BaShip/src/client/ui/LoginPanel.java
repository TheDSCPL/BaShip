package client.ui;

import client.ClientMain;
import client.logic.UserC;
import client.ui.lobby.LobbyPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;
import java.util.logging.*;
import javax.swing.event.*;
import sharedlib.exceptions.UserMessageException;
import static sharedlib.constants.UIK.*;

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
        loginPanel = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        registerButton = new javax.swing.JButton();
        topBar = new javax.swing.JPanel();
        settingsLabelButton = new client.ui.GeneralComponents.LabelButton();

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

        setMaximumSize(new java.awt.Dimension(386, 251));
        setMinimumSize(new java.awt.Dimension(386, 251));

        usernameField.setToolTipText("");
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        usernameLabel.setText("Username:");

        passwordField.setToolTipText("");
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        passwordLabel.setText("Password:");

        loginButton.setText("Login");
        loginButton.setToolTipText("");
        loginButton.setEnabled(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        registerButton.setText("Register");
        registerButton.setToolTipText("");
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
            .addGroup(topBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsLabelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topBarLayout.setVerticalGroup(
            topBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsLabelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loginPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void myInitComponents() {
        //CHECK USERNAME/PASSWORD FIELDS TO SEE IF BUTTONS SHOULD BE ENABLED
        DocumentListener dL = new DocumentListener() {
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

        usernameField.getDocument().addDocumentListener(dL);
        passwordField.getDocument().addDocumentListener(dL);

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

        usernameField.setToolTipText(defaultUsernameFieldTooltip);
        passwordField.setToolTipText(defaultPasswordFieldTooltip);
        loginButton.setToolTipText(defaultLoginButtonTooltip);
        registerButton.setToolTipText(defaultRegisterButtonTooltip);
        
        settingsLabelButton.setClickListener(() -> {
            ClientMain.mainFrame.changeToPanel(new SettingsPanel());
        });
        settingsLabelButton.setIcon(SETTINGS_ICON);
    }

    private final String defaultUsernameFieldTooltip = "Insert username";
    private final String defaultPasswordFieldTooltip = "Insert password";

    private final String defaultLoginButtonTooltip = "Click to login with the inserted credentials";
    private final String defaultRegisterButtonTooltip = "Click to retype your password";

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
        retypePasswordDialog.setLocation(calculateRetypePasswordDialogPosition());
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
                    setPanelEnabled((Container) c, enabled, true);
                }
                c.setEnabled(enabled);
            }
            pane.setEnabled(enabled);
        }
    }

    /**
     * Sets the buttons to enabled or disabled depending on if the
     * username/password fields are valid or not
     */
    private void checkFilledUserPassFields() {
        String user = usernameField.getText();
        char[] pass = passwordField.getPassword();

        if (user.length() == 0 && pass.length == 0) {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);

            usernameField.setToolTipText(defaultUsernameFieldTooltip);
            passwordField.setToolTipText(defaultPasswordFieldTooltip);
            loginButton.setToolTipText(defaultLoginButtonTooltip);
            registerButton.setToolTipText(defaultRegisterButtonTooltip);
            return;
        }

        final boolean usernameIsValid = UserC.isUsernameValid(user);
        final boolean passwordIsValid = UserC.isPasswordValid(pass);
        final String userError = "Usernames must only contain letters, digits and underscores.";
        final String passError = "Passwords must only contain letters and digits. Password must also have at least 1 digit and its minimum length is 6.";

        if (!usernameIsValid && user.length() > 0) {
            usernameField.setToolTipText(userError);
            usernameField.setForeground(Color.red);
        }
        else {
            usernameField.setToolTipText(defaultUsernameFieldTooltip);
            usernameField.setForeground(Color.black);
        }

        if (!passwordIsValid && pass.length > 0) {
            passwordField.setToolTipText(passError);
            passwordField.setForeground(Color.red);
        }
        else {
            passwordField.setToolTipText(defaultPasswordFieldTooltip);
            passwordField.setForeground(Color.black);
        }

        if (usernameIsValid && passwordIsValid) {   //if there were no errors
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);

            usernameField.setToolTipText(defaultUsernameFieldTooltip);
            passwordField.setToolTipText(defaultPasswordFieldTooltip);
            loginButton.setToolTipText(defaultLoginButtonTooltip);
            registerButton.setToolTipText(defaultRegisterButtonTooltip);
        }
        else {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);

            String error;
            if (!usernameIsValid && user.length() > 0) {
                error = userError;
                if (!passwordIsValid && pass.length > 0) {
                    error += ". " + passError;
                }
            }
            else if (!passwordIsValid && pass.length > 0) {
                error = passError;
            }
            else if (user.length() <= 0 && pass.length <= 0) {
                error = "You must fill in the fields";
            }
            else if (user.length() <= 0) {
                error = "You must fill in the username";
            }
            else if (pass.length <= 0) {
                error = "You must fill in the password";
            }
            else {
                error = "Unknown error!!!";
            }
            loginButton.setToolTipText(error);
            registerButton.setToolTipText(error);

        }
    }

    public Point calculateRetypePasswordDialogPosition() {
        Point mainFrameLocation;
        try {
            synchronized (ClientMain.mainFrame.getTreeLock()) {
                mainFrameLocation = ClientMain.mainFrame.getLocationOnScreen();
            }
        }
        catch (Throwable ignored) {
            return new Point(0, 0);
        }
        return mainFrameLocation;
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
        if (ca1 == null || ca2 == null) {
            return false;
        }
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
            if (ClientMain.connectToServer(false)) {
                if (UserC.register(usernameField.getText(), passwordField.getPassword())) {
                    ClientMain.mainFrame.changeToPanel(new LobbyPanel());
                }
            }
        }
        catch (UserMessageException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
            ClientMain.showError(ex.getMessage());
        }

        retypePasswordDialog.dispose();
    }//GEN-LAST:event_retypePasswordConfirmButtonActionPerformed

    private void retypePasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retypePasswordFieldActionPerformed
        if (!retypePasswordConfirmButton.isEnabled() || !retypePasswordConfirmButton.isVisible()) {
            return;
        }
        retypePasswordConfirmButtonActionPerformed(evt);
    }//GEN-LAST:event_retypePasswordFieldActionPerformed

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerButtonActionPerformed
        resetRetypeDialog();
        //retypePasswordDialog = new 
        retypePasswordDialog.setVisible(true);
    }//GEN-LAST:event_registerButtonActionPerformed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if (!loginButton.isEnabled() || !loginButton.isVisible()) {
            return;
        }

        try {
            if (ClientMain.connectToServer(false)) {
                if (UserC.login(usernameField.getText(), passwordField.getPassword())) {
                    ClientMain.mainFrame.changeToPanel(new LobbyPanel());
                }
            }
        }
        catch (UserMessageException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
            ClientMain.showError(ex.getMessage());
        }
    }//GEN-LAST:event_loginButtonActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        loginButtonActionPerformed(evt);
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        loginButtonActionPerformed(evt);
    }//GEN-LAST:event_usernameFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private client.ui.GeneralComponents.LabelButton settingsLabelButton;
    private javax.swing.JPanel topBar;
    public final javax.swing.JTextField usernameField = new javax.swing.JTextField();
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
