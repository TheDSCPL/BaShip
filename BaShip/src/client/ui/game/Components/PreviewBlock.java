/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import client.other.ImageResizer;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import static sharedlib.constants.UIK.*;
import sharedlib.structs.BoardUIInfo.SquareFill;

/**
 *
 * @author luisp
 */
public final class PreviewBlock extends javax.swing.JPanel {

    private static int counter = 0;

    /**
     * Creates new form previewBlock
     */
    public PreviewBlock() {
        id = counter++;
        initComponents();
        setBorder(BorderFactory.createLineBorder(BOARD_BORDER_COLOR_NORMAL));
    }

    private final int id;
    private final static ImageIcon RESIZED_RED_CROSS_ICON = ImageResizer.resizeIcon(RED_CROSS_ICON, 13, 13);
    private final static ImageIcon RESIZED_BLUE_DIAMOND_ICON = ImageResizer.resizeIcon(BLUE_DIAMOND_ICON, 13, 13);
    private final static ImageIcon RESIZED_GREY_CIRCLE_ICON = ImageResizer.resizeIcon(GREY_CIRCLE_ICON, 13, 13);

    private SquareFill lastSF = SquareFill.Empty;

    public void setSquareFill(SquareFill sf) {
        if (sf != lastSF) {
            System.err.println("Preview block " + id + " | Square fill: " + sf.name());
            lastSF = sf;
        }
        switch (sf) {
            case Empty:
                setIcon(null);
                setColor(null);
                break;
            case GraySquare:
                setIcon(null);
                setColor(GREY_BLOCK_COLOR);
                break;
            case RedSquare:
                setIcon(null);
                setColor(RED_BLOCK_COLOR);
                break;
            case RedCross:
                setIcon(RESIZED_RED_CROSS_ICON);
                setColor(null);
                break;
            case BlueDiamond:
                setIcon(RESIZED_BLUE_DIAMOND_ICON);
                setColor(null);
                break;
            case GrayCircle:
                setIcon(RESIZED_GREY_CIRCLE_ICON);
                setColor(null);
                break;
            case GraySquareRedCross:
                setIcon(RESIZED_RED_CROSS_ICON);
                setColor(GREY_BLOCK_COLOR);
                break;
        }
    }

    private void setIcon(ImageIcon icon) {
        jLabel1.removeAll();
        jLabel1.setIcon(icon);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setVerticalAlignment(SwingConstants.CENTER);
//        jLabel1.setVisible(true);
//        jLabel1.setOpaque(true);

//        jLabel1.revalidate();
    }

    private void setColor(java.awt.Color _c) {
        Color color;
        if (_c == null) {
            color = new Color(240, 240, 240);
        }
        else {
            color = _c;
        }
        setBackground(color);
        jLabel1.setBackground(color);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
