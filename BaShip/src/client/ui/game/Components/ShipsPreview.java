/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

public abstract class ShipsPreview extends javax.swing.JPanel {
    public abstract PreviewBlock getPreviewBlock(int shipSize, int shipNumber, int blockNumber);
}
