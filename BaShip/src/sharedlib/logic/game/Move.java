package sharedlib.logic.game;

import sharedlib.logic.player.*;

/**
 *
 * @author Alex
 */
public class Move {
    
    public final Player player;
    public final Coordinate coord;
    
    Move(Player player, Coordinate coord) {
        this.player = player;
        this.coord = coord;
    }
}
