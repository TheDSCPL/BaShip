package sharedlib.tuples;

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

}
