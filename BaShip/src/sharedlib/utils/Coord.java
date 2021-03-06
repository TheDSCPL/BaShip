package sharedlib.utils;

/**
 * An immutable coordinate class consisting of two integer values: x and y.
 * @author Alex
 */
public class Coord {

    /**
     * Value of the coordinate x axis 
     */
    public final int x;

    /**
     * Value of the coordinate y axis
     */
    public final int y;

    /**
     * Constructor of this class
     * @param x Value of x axis
     * @param y Value of y axis
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coord other = (Coord) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.x;
        hash = 47 * hash + this.y;
        return hash;
    }

    @Override
    public String toString() {
        return "Coord{" + "x=" + x + ", y=" + y + '}';
    }
    
    
}
