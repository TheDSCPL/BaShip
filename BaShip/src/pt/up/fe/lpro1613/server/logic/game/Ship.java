package pt.up.fe.lpro1613.server.logic.game;

import java.util.HashSet;
import java.util.Set;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;

/**
 * Represents a single ship on a battleship game. This is an immutable class.
 * @author Alex
 */
public class Ship {

    /**
     * The position of the ship, meaning, the position of the bottom-left square.
     */
    public final int posx, posy;
    
    /**
     * The size (length) of the ship.
     */
    public final int size;
    
    /**
     * Orientation of the ship.
     */
    public final boolean vertical;
    
    /**
     * 
     * @param posx The left-most coordinate of the ship.
     * @param posy The bottom-most coordinate of the ship.
     * @param size The length of the ship.
     * @param vertical Orientation of the ship.
     */
    public Ship(int posx, int posy, int size, boolean vertical) {
        this.posx = posx;
        this.posy = posy;
        this.size = size;
        this.vertical = vertical;
    }

    /**
     * @return The set of squares (coordinates) this ship contains, based on its position and size.
     */
    public Set<Coord> getShipSquares() {
        Set<Coord> set = new HashSet<>();

        for (int i = 0; i < size; i++) {
            int x = posx + (vertical ? 0 : i);
            int y = posy + (vertical ? i : 0);
            set.add(new Coord(x, y));
        }

        return set;
    }

}
