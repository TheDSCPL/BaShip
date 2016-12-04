package pt.up.fe.lpro1613.sharedlib.constants;

import java.util.*;

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
                    //put(2, 3);
                    //put(3, 2);
                    put(4, 1);
                }
            });
    
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
