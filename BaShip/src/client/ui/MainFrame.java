package client.ui;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        imageResizer = new ComponentListener() {
            private ImageIcon _resizeIcon(ImageIcon srcImg, int width, int height) {
                Image tempImage = srcImg.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(tempImage);
            }

            private void resizeIcon(ComponentEvent e) {
                Component c = e.getComponent();
                int width = (int) (c.getWidth() * 0.8);
                int height = (int) (c.getHeight() * 0.8);
                if (c instanceof JButton) {
                    JButton button = (JButton) c;
                    ImageIcon prevIcon = (ImageIcon) button.getIcon();
                    if (prevIcon == null) {
                        return;
                    }
                    button.setIcon(_resizeIcon(prevIcon, width, height));
                }
                else if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    ImageIcon prevIcon = (ImageIcon) label.getIcon();
                    if (prevIcon == null) {
                        return;
                    }
                    label.setIcon(_resizeIcon(prevIcon, width, height));
                }
                else {
                    throw new ClassCastException("Resizer used on a Component that is neither a Jlabel nor a JButton");
                }
            }

            @Override
            public void componentResized(ComponentEvent e) {
                resizeIcon(e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                resizeIcon(e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                resizeIcon(e);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                resizeIcon(e);
            }
        };
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
