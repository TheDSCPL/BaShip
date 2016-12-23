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

    private static ImageIcon resizeIcon(Component c) {
        int width = (int) (c.getWidth() * 0.8);
        int height = (int) (c.getHeight() * 0.8);
        ImageIcon ret;
        if (c instanceof JButton) {
            JButton button = (JButton) c;
            ImageIcon prevIcon = (ImageIcon) button.getIcon();
            if (prevIcon == null) {
                return null;
            }
            ret = resizeIcon(prevIcon, width, height);
            button.setIcon(ret);
        } else if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            ImageIcon prevIcon = (ImageIcon) label.getIcon();
            if (prevIcon == null) {
                return null;
            }
            ret = resizeIcon(prevIcon, width, height);
            label.setIcon(ret);
        } else {
            throw new ClassCastException("Resizer used on a Component that is neither a Jlabel nor a JButton");
        }
        return ret;
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        ImageResizer.resizeIcon(e.getComponent());
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        ImageResizer.resizeIcon(e.getComponent());
    }

    @Override
    public void componentShown(ComponentEvent e) {
        ImageResizer.resizeIcon(e.getComponent());
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        ImageResizer.resizeIcon(e.getComponent());
    }
    
}
