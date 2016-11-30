package sharedlib.utils;

import java.util.ArrayList;
import java.util.List;

public class Matrix<T> {

    public final int sx;
    public final int sy;
    private final List<List<T>> list;

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

    public T get(Coord c) {
        return list.get(c.x).get(c.y);
    }

    public void set(Coord c, T value) {
        list.get(c.x).set(c.y, value);
    }

    public T getOr(Coord c, T value) {
        if (c.x >= 0 && c.y >= 0 && c.x < sx && c.y < sy) {
            return get(c);
        }
        else {
            return value;
        }
    }

    public void setAll(T value) {
        setEach((c) -> value);
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

    public void setEach(MatrixValueProducer<T> f) {
        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                Coord c = new Coord(x, y);
                set(c, f.value(c));
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

        T value(Coord c);
    }
}
