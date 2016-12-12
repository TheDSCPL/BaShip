/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.GeneralComponents;

import javax.swing.ImageIcon;
import static client.other.ImageResizer.resizeIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class LabelButton extends javax.swing.JPanel {

    public LabelButton() {
        initComponents();
    }
    
    public LabelButton(LabelButton.ClickListener cl, ImageIcon i) {
        super();
        setClickListener(cl);
        setIcon(i);
    }

    public void setClickListener(LabelButton.ClickListener cl)
    {
        Arrays.asList(getMouseListeners()).stream().forEach(o -> {
            removeMouseListener(o);
        });
        Arrays.asList(iconLabel.getMouseListeners()).stream().forEach(o -> {
            iconLabel.removeMouseListener(o);
        });
        final MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cl.onClick();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        };
        addMouseListener(ml);
        iconLabel.addMouseListener(ml);
    }
    
    public void setIcon(ImageIcon i)
    {
        if(i == null)
            return;
        //iconLabel.setIcon(i);
        //System.err.println("icon width: " + iconLabel.getWidth() + " | icon height: " + iconLabel.getHeight());
        //iconLabel.setIcon(resizeIcon(i, getWidth(), getHeight()));
        iconLabel.setIcon(resizeIcon(i, 18, 18));
        iconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    }
    
    public static interface ClickListener {
        void onClick();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iconLabel = new javax.swing.JLabel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iconLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iconLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel iconLabel;
    // End of variables declaration//GEN-END:variables
}
