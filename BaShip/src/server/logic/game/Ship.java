package server.logic.game;

import java.util.HashSet;
import java.util.Set;
import sharedlib.utils.Coord;

public class Ship {

    public final int posx, posy;
    public final int size;
    public final boolean vertical;

    public Ship(int posx, int posy, int size, boolean vertical) {
        this.posx = posx;
        this.posy = posy;
        this.size = size;
        this.vertical = vertical;
    }

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
