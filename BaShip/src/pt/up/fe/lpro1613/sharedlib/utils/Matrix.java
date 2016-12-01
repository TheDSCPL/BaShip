package pt.up.fe.lpro1613.sharedlib.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a generic, fixed-size Matrix. This class can hold {@code null}
 * values.
 *
 * @author Alex
 * @param <T> The type of elements in this matrix
 */
public class Matrix<T> {

    public final int sx;
    public final int sy;
    private final List<List<T>> list;

    /**
     * Create a new Matrix with a fixed size and populate that matrix with the
     * given {@code initialValue}.
     *
     * @param sx The width of the matrix.
     * @param sy The height of the matrix.
     * @param initialValue The initial acceptAndSet for all positions of the Matrix.
     */
    public Matrix(int sx, int sy, T initialValue) {
        this.list = new ArrayList<>();
        this.sx = sx;
        this.sy = sy;

        for (int x = 0; x < sx; x++) {
            List<T> l = new ArrayList<>();

            for (int y = 0; y < sy; y++) {
                l.add(initialValue);
            }

            list.add(l);
        }
    }

    /**
     * Get the acceptAndSet present at this coordinate.
     *
     * @param c
     * @return
     */
    public T get(Coord c) {
        return list.get(c.x).get(c.y);
    }

    /**
     * Set the acceptAndSet present at this coordinate to the given acceptAndSet.
     *
     * @param c
     * @param value
     */
    public void set(Coord c, T value) {
        list.get(c.x).set(c.y, value);
    }

    /**
     * Get the acceptAndSet present at this coordinate or the default acceptAndSet given if
 the coordinate is out of bounds.
     *
     * @param c
     * @param defaultValue
     * @return The acceptAndSet present at this coordinate or {@code defaultValue} if
     * the coordinate is out of bounds
     */
    public T getOr(Coord c, T defaultValue) {
        if (c.x >= 0 && c.y >= 0 && c.x < sx && c.y < sy) {
            return get(c);
        }
        else {
            return defaultValue;
        }
    }

    /**
     * Set all values on this matrix to the given acceptAndSet.
     *
     * @param value
     */
    public void setAll(T value) {
        setEach((c) -> value);
    }

    /**
     * Loop through all elements on this matrix, passing in the coordinate and
 the corresponding acceptAndSet at that position.
     *
     * @param f
     */
    public void forEach(MatrixValueConsumer<T> f) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                Coord c = new Coord(x, y);
                f.accept(c, get(c));
            }
        }
    }

    /**
     * Loop through all the possible coordinate values for this matrix.
     *
     * @param f
     */
    public void forEach(MatrixCoordConsumer<T> f) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                f.accept(new Coord(x, y));
            }
        }
    }

    /**
     * Loop through all elements on this matrix, passing in the coordinate and
 the corresponding acceptAndSet at that position and assigning to that position
 the new acceptAndSet returned by the functional interface.
     *
     * @param f
     */
    public void setEach(MatrixValueProducer<T> f) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                Coord c = new Coord(x, y);
                set(c, f.acceptAndSet(c));
            }
        }
    }

    @FunctionalInterface
    public interface MatrixValueConsumer<T> {

        void accept(Coord c, T t);
    }

    @FunctionalInterface
    public interface MatrixCoordConsumer<T> {

        void accept(Coord c);
    }

    @FunctionalInterface
    public interface MatrixValueProducer<T> {

        T acceptAndSet(Coord c);
    }
}