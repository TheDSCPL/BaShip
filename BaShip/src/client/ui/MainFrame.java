package client.ui;

import client.other.ImageResizer;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;

// TODO: Luis: bottom info no board
// TODO: Luis: filter interface for players and games (note: columns cannot be ordered now)
// TODO: Luis: replay & spectator interfaces

public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        imageResizer = new ImageResizer();
    }

    public final ComponentListener imageResizer;

    private JComponent currentPanel;
    public JComponent getCurrentPanel() {
        return currentPanel;
    }

    public void changeToPanel(JComponent panel) {        
        getContentPane().removeAll();
        getContentPane().add(advertisementPanel, java.awt.BorderLayout.PAGE_END);
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);
        pack();

        currentPanel = panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        advertisementPanel = new client.ui.AdvertisementBanner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(advertisementPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private client.ui.AdvertisementBanner advertisementPanel;
    // End of variables declaration//GEN-END:variables
}
