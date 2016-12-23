package sharedlib.constants;

import client.ui.game.Components.Block;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Class holder of various UI-related constants, such as colors and images.
 *
 * @author Alex
 */
public class UIK {

    public static final Color BOARD_BORDER_COLOR_NORMAL = new Color(170, 170, 170);
    public static final Color BOARD_BORDER_COLOR_HOVER = new Color(120, 120, 120);
    public static final Color BOARD_BORDER_COLOR_PRESSED = new Color(80, 80, 80);
    public static final Color GREY_BLOCK_COLOR = new Color(202, 202, 202);
    public static final Color RED_BLOCK_COLOR = new Color(255, 0, 0);

    public static final ImageIcon RED_CROSS_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/redCross.png"));
    public static final ImageIcon BLUE_DIAMOND_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/blueDiamond.png"));
    public static final ImageIcon GREY_CIRCLE_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/greyCircle.png"));
    public static final ImageIcon SETTINGS_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/settings.png"));
    public static final ImageIcon[] ICONS = new ImageIcon[]{RED_CROSS_ICON, BLUE_DIAMOND_ICON, GREY_CIRCLE_ICON};

    public static boolean allIconsOK()
    {
        for(ImageIcon ii : ICONS)
            if(ii == null)
                return false;
        return true;
    }
    
}
