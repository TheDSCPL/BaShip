/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

import client.ClientMain;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo.SquareFill;
import pt.up.fe.lpro1613.sharedlib.utils.*;

/**
 *
 * @author luisp
 */
public final class Block extends javax.swing.JPanel {

    /**
     * Creates new form Block
     * @param coordinates where this block is in the board 
     */
    public Block(Coord coordinates) {
        this(coordinates,null);
    }
    
    /**
     * Creates new form Block
     * @param coordinates where this block is in the board
     * @param text Text to be set. If text is not null, no icon can be set.
     */
    public Block(Coord coordinates, String text) {
        initComponents();
        this.coordinates = coordinates;
        sanityCheck();
        
        this.text = text;
        if(text != null)    //label
        {
            jLabel1.setText(text);
        }
        else    //button
        {
            addMouseListener(clickListener);
            jLabel1.addMouseListener(clickListener);
            setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
            //jLabel1.setText("" + coordinates.x + "" + coordinates.y); //debug block coordinates
        }
    }
    
    /**
     * not null if this Block is a line/row label
     */
    public final String text;
    
    /**
     * @return true if this is a block that is clickable. false if it is a Board label
     */
    public final boolean isClickableBlock()
    {
        return text == null;
    }
    
    private void sanityCheck()
    {
        boolean containsLabel = false;
        for(Component _c : getComponents())
        {
            if(_c == jLabel1)
            {
                containsLabel = true;
                break;
            }
        }
        if(!containsLabel)
            add(jLabel1);
        jLabel1.setVisible(true);
        jLabel1.setOpaque(true);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setVerticalAlignment(SwingConstants.CENTER);
    }
    
    private static final Color HOVER_COLOR = new Color(40, 150, 180);
    
    private final MouseListener clickListener = new MouseListener() {
        private Color previousColor;
        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            setIcon(RED_CROSS_ICON);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //System.out.println("released");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //System.out.println("entered");
            previousColor = jLabel1.getBackground();
            setColor(HOVER_COLOR);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //System.out.println("exited");
            setColor(previousColor);
        }
    };
    
    private static final Icon RED_CROSS_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/redCross.png"));
    private static final Icon BLUE_DIAMOND_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/blueDiamond.png"));
    private static final Icon GREY_CIRCLE_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/greyCircle.png"));
    
    private boolean iconIsResizable = false;
    
    private void setImageResizer()
    {
        if(iconIsResizable)
            return;
        iconIsResizable = true;

        ComponentListener cl = ClientMain.mainFrame.imageResizer;
        
        jLabel1.addComponentListener(cl);
    }
    
    public void setSquareFill(SquareFill sf) {
        // TODO: LUIS
        switch (sf) {
            case Empty:
                setIcon(BLUE_DIAMOND_ICON); break;
            case BlueDiamond:
                setIcon(BLUE_DIAMOND_ICON); break;
            case GrayCircle:
                setIcon(GREY_CIRCLE_ICON); break;
            case RedCross:
                setIcon(RED_CROSS_ICON); break;
        }
    }
    
    private void setIcon(Icon icon)
    {
        if(text != null)
            return;
        
        setImageResizer();
        
        jLabel1.removeAll();
        
        jLabel1.setIcon(icon);
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setVerticalAlignment(SwingConstants.CENTER);
        
        //To trigger the image resizing
        jLabel1.setVisible(false);
        jLabel1.setVisible(true);
    }
    
    /**
     * Sets the background color of this Block
     * @param _c new color for this Block. If null, the default Color will be used, aka resets the Block's Color
     */
    private void setColor(java.awt.Color _c)
    {
        Color color;
        if(_c == null)
        {
            color = new Color(240,240,240);
        }
        else
        {
            color = _c;
            //color = new Color(143,143,143);
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
