package server.logic.game;

import server.logic.player.Player;

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
