package pt.up.fe.lpro1613.sharedlib.constants;

import client.ui.game.Components.Block;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * TODO: JAVADOC
 *
 * @author Alex
 */
public class UIK {

    public static final Color BOARD_BORDER_COLOR_NORMAL = new Color(170, 170, 170);
    public static final Color BOARD_BORDER_COLOR_HOVER = new Color(120, 120, 120);
    public static final Color BOARD_BORDER_COLOR_PRESSED = new Color(80, 80, 80);
    public static final Color GREY_BLOCK_COLOR = new Color(202, 202, 202);
    public static final Color RED_BLOCK_COLOR = new Color(255, 0, 0);

    public static final Icon RED_CROSS_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/redCross.png"));
    public static final Icon BLUE_DIAMOND_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/blueDiamond.png"));
    public static final Icon GREY_CIRCLE_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/greyCircle.png"));
    public static final Icon[] ICONS = new Icon[]{RED_CROSS_ICON, BLUE_DIAMOND_ICON, GREY_CIRCLE_ICON};

}
