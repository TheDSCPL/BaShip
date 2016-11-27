package sharedlib.utils;

import java.util.ArrayList;
import java.util.List;

public class Matrix<T> {

    public final int sx;
    public final int sy;
    private final List<List<T>> list;

    public Matrix(int sx, int sy, T value) {
        list = new ArrayList<>();

        for (int x = 0; x < sx; x++) {
            List<T> l = new ArrayList<>();

            for (int y = 0; y < sy; y++) {
                l.add(value);
            }

            list.add(l);
        }

        this.sx = sx;
        this.sy = sy;
    }

    public void forEach(MatrixValueConsumer<T> f) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                Coord c = new Coord(x, y);
                f.accept(c, get(c));
            }
        }
    }

    public void forEach(MatrixCoordConsumer<T> f) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                f.accept(new Coord(x, y));
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

    public T get(Coord c) {
        return list.get(c.x).get(c.y);
    }

    public T getOr(Coord c, T value) {
        if (c.x >= 0 && c.y >= 0 && c.x < sx && c.y < sy) {
            return list.get(c.x).get(c.y);
        }
        else {
            return value;
        }
    }

    public T set(Coord c, T value) {
        return list.get(c.x).set(c.y, value);
    }
}
