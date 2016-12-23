/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.ui.game.Components;

public abstract class ShipsPreview extends javax.swing.JPanel {
    public abstract PreviewBlock getPreviewBlock(int shipSize, int shipNumber, int blockNumber);
    
    protected Ship[] ships;
    
    protected class Ship {

        Ship(PreviewBlock... pbs) {
            a = java.util.Arrays.asList(pbs);
        }

        private final java.util.List<PreviewBlock> a;

        public int size() {
            return a.size();
        }

        public PreviewBlock get(int i) {
            PreviewBlock ret;
            try {
                ret = a.get(i);
            } catch (IndexOutOfBoundsException e) {
                ret = null;
            }
            return ret;
        }
    }
}
