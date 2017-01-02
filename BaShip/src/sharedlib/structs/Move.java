package sharedlib.structs;

import sharedlib.utils.Coord;

/**
 * A class representing a Move made by a player
 */
public class Move {
    
    /**
     * The coordinates of the move in the game panel
     */
    public final Coord coord;

    /**
     * The player who made the move
     */
    public final int playerN;

    /**
     * Constructor of this class
     * @param coord Coordinates of the move
     * @param playerN Player who made the mode
     */
    public Move(Coord coord, int playerN) {
        this.coord = coord;
        this.playerN = playerN;
    }
}
