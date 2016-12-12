/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import sharedlib.utils.Coord;
import client.ClientMain;
import client.logic.GameC;
import client.other.ImageResizer;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static sharedlib.constants.UIK.*;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo.SquareFill;

/**
 *
 * @author luisp
 */
public final class Block extends javax.swing.JPanel {

    /**
     * Creates new form Block
     *
     * @param coordinates where this block is in the board
     * @param left true if this block if on a left board
     */
    public Block(Coord coordinates, boolean left) {
        this(coordinates, left, null);
    }

    /**
     * Creates new form Block
     *
     * @param coordinates where this block is in the board
     * @param text Text to be set. If text is not null, no icon can be set.
     * @param left true if this block if on a left board
     */
    public Block(Coord coordinates, boolean left, String text) {
        initComponents();
        this.coordinates = coordinates;
        this.left = left;
        sanityCheck();

        this.text = text;
        if (text != null) // Label
        {
            jLabel1.setText(text);
            jLabel1.setForeground(BOARD_BORDER_COLOR_NORMAL);
        }
        else // Button
        {
            addMouseListener(clickListener);
            jLabel1.addMouseListener(clickListener);
            setBorder(BorderFactory.createLineBorder(BOARD_BORDER_COLOR_NORMAL));
        }
        
        setOpaque(false);
    }
    
    private final boolean left;

    /**
     * not null if this Block is a line/row label
     */
    public final String text;

    /**
     * @return true if this is a block that is clickable. false if it is a Board
     * label
     */
    public final boolean isClickableBlock() {
        return text == null;
    }

    private void sanityCheck() {
        for (Icon i : ICONS) {
            if (i == null) {
                ClientMain.showError("Couldn't open an Icon resource");
                throw new Error("Couldn't open an Icon resource");
            }
        }

        boolean containsLabel = false;
        for (Component _c : getComponents()) {
            if (_c == jLabel1) {
                containsLabel = true;
                break;
            }
        }
        if (!containsLabel) {
            add(jLabel1);
        }
        jLabel1.setVisible(true);
        jLabel1.setOpaque(true);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setVerticalAlignment(SwingConstants.CENTER);
    }

    private final MouseListener clickListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                GameC.clickBoardCoordinate(left, Block.this.coordinates);
            }
            catch (UserMessageException ex) {
                Logger.getLogger(Block.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Block.this.setBorder(BorderFactory.createLineBorder(BOARD_BORDER_COLOR_PRESSED));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Block.this.setBorder(BorderFactory.createLineBorder(BOARD_BORDER_COLOR_NORMAL));
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            Block.this.setBorder(BorderFactory.createLineBorder(BOARD_BORDER_COLOR_HOVER));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Block.this.setBorder(BorderFactory.createLineBorder(BOARD_BORDER_COLOR_NORMAL));
        }
    };

    private boolean iconIsResizable = false;

    private void setImageResizer() {
        if (iconIsResizable) {
            return;
        }
        iconIsResizable = true;

        ComponentListener cl = ClientMain.mainFrame.imageResizer;

        jLabel1.addComponentListener(cl);
    }

    private static ImageIcon RESIZED_RED_CROSS_ICON = RED_CROSS_ICON;
    private static ImageIcon RESIZED_BLUE_DIAMOND_ICON = BLUE_DIAMOND_ICON;
    private static ImageIcon RESIZED_GREY_CIRCLE_ICON = GREY_CIRCLE_ICON;
    
    public void setSquareFill(SquareFill sf) {
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
                RESIZED_RED_CROSS_ICON = setIcon(RESIZED_RED_CROSS_ICON);
                setColor(null);
                break;
            case BlueDiamond:
                RESIZED_BLUE_DIAMOND_ICON = setIcon(RESIZED_BLUE_DIAMOND_ICON);
                setColor(null);
                break;
            case GrayCircle:
                RESIZED_GREY_CIRCLE_ICON = setIcon(RESIZED_GREY_CIRCLE_ICON);
                setColor(null);
                break;
            case GraySquareRedCross:
                RESIZED_RED_CROSS_ICON = setIcon(RESIZED_RED_CROSS_ICON);
                setColor(GREY_BLOCK_COLOR);
                break;
        }
    }

    private ImageIcon setIcon(ImageIcon icon) {
        if (text != null) {
            return null;
        }

        setImageResizer();

        jLabel1.removeAll();

        jLabel1.setIcon(icon);
        
        ImageIcon ret = ImageResizer.resizeIcon(icon, jLabel1.getWidth(), jLabel1.getHeight());
        
        jLabel1.setIcon(ret);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setVerticalAlignment(SwingConstants.CENTER);

        //To trigger the image resizing
        jLabel1.setVisible(false);
        jLabel1.setVisible(true);

        jLabel1.revalidate();
        
        return ret;
    }

    /**
     * Sets the background color of this Block
     *
     * @param _c new color for this Block. If null, the default Color will be
     * used, aka resets the Block's Color
     */
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

    public final Coord coordinates;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
