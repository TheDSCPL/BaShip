package sharedlib.structs;

import java.util.HashSet;
import java.util.Set;
import sharedlib.utils.Coord;

/**
 * Represents a single ship on a battleship game. This is an immutable class.
 *
 * @author Alex
 */
public class Ship {

    /**
     * The position of the ship, meaning, the position of the bottom-left
     * square.
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
     * @return The set of squares (coordinates) this ship contains, based on its
     * position and size.
     */
    public Set<Coord> getShipSquares() {
        Set<Coord> set = new HashSet<>();

        for (int i = 0; i < size; i++) {
            set.add(coordForShipPos(i));
        }

        return set;
    }
    
    public Coord coordForShipPos(int index) {
        int x = posx + (vertical ? 0 : index);
        int y = posy + (vertical ? index : 0);
        return new Coord(x, y);
    }

    @Override
    public String toString() {
        return "Ship{" + "posx=" + posx + ", posy=" + posy + ", size=" + size + ", vertical=" + vertical + '}';
    }

}
