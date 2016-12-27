/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.other;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author luisp
 */
public class ImageResizer implements ComponentListener {

    public static ImageIcon resizeIcon(ImageIcon srcImg, int width, int height) {
        if(srcImg == null || srcImg.getImage() == null)
            return null;
        Image tempImage = srcImg.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(tempImage);
    }

    private int getPretendedWidth(Component c)
    {
        if(c == null || c.getWidth() <= 0)
        {
            System.err.println("Error getting width. Defaulting to 15.");
            return 15;
        }
        return (int)(c.getWidth() * 0.8);
    }
    
    private int getPretendedHeight(Component c)
    {
        if(c == null || c.getHeight() <= 0)
        {
            System.err.println("Error getting width. Defaulting to 15.");
            return 15;
        }
        return (int)(c.getHeight() * 0.8);
    }
    
    private ImageIcon getSetIcon(Component c)
    {
        if (c instanceof JButton) {
            JButton button = (JButton) c;
            return (ImageIcon) button.getIcon();
        } else if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            return (ImageIcon) label.getIcon();
        } else {
            throw new ClassCastException("Resizer used on a Component that is neither a Jlabel nor a JButton");
        }
    }
    
    private void setIcon(Component c, ImageIcon icon)
    {
        if (c instanceof JButton) {
            JButton button = (JButton) c;
            button.setIcon(icon);
        } else if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            label.setIcon(icon);
        } else {
            throw new ClassCastException("Resizer used on a Component that is neither a Jlabel nor a JButton");
        }
    }
    
    private void resizeIfNecessary(Component c)
    {
        ImageIcon setIcon = getSetIcon(c);
        if(setIcon == null || (setIcon.getIconWidth() == getPretendedWidth(c) && setIcon.getIconHeight() == getPretendedHeight(c)) )
            return;
        resizeIcon(c);
    }
    
    private void resizeIcon(Component c) {
        int width = getPretendedWidth(c);
        int height = getPretendedHeight(c);
        ImageIcon prevIcon = getSetIcon(c);
        if(prevIcon == null)
            return;
        
        setIcon(c, resizeIcon(prevIcon, width, height));
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        resizeIfNecessary(e.getComponent());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        resizeIfNecessary(e.getComponent());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        resizeIfNecessary(e.getComponent());
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        resizeIfNecessary(e.getComponent());
    }
    
}
