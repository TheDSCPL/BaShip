package pt.up.fe.lpro1613.sharedlib.constants;

import client.ui.game.Components.Block;
import java.awt.Color;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class BoardK {

    /**
     * Width and height of one battleship board
     */
    public static final int BOARD_SIZE = 10;

    /**
     * Keys: size of ship Values: number of ships for that size (for one player
     * only) in a game of Battleship
     */
    public static final Map<Integer, Integer> SHIP_COUNT_FOR_SIZE
            = Collections.unmodifiableMap(
                    new HashMap<Integer, Integer>() {
                {
                    put(1, 4);
                    put(2, 3);
                    put(3, 2);
                    put(4, 1);
                }
            });

    public static final Color HOVER_OVER_BLOCK_COLOR = new Color(40, 150, 180);
    //public static final Color GREY_BLOCK_COLOR = new Color(143,143,143);
    public static final Color GREY_BLOCK_COLOR = new Color(202,202,202);
    public static final Color RED_BLOCK_COLOR = new Color(255,0,0);
    
    public static final Icon RED_CROSS_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/redCross.png"));
    public static final Icon BLUE_DIAMOND_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/blueDiamond.png"));
    public static final Icon GREY_CIRCLE_ICON = new ImageIcon(Block.class.getResource("/client/ui/Images/greyCircle.png"));
    public static final Icon[] ICONS = new Icon[] { RED_CROSS_ICON, BLUE_DIAMOND_ICON, GREY_CIRCLE_ICON };
    
    /**
     * Total number of ships (for one player only) in a game of Battleship
     */
    public static final int SHIP_COUNT = SHIP_COUNT_FOR_SIZE.values().stream().reduce(0, Integer::sum);

    /**
     * Number of squares below a board of battleship on the UI (they contain
     * information about the state of the ships)
     */
    public static final int BOTTOM_INFO_SQUARES_COUNT = SHIP_COUNT_FOR_SIZE.entrySet().stream().map((e) -> e.getKey() * e.getValue()).reduce(0, Integer::sum);

}
