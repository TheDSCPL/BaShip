package server.logic.game;

import java.util.*;
import static java.util.stream.Collectors.toSet;
import sharedlib.tuples.*;
import sharedlib.utils.*;

public class Board {

    public static final int BOARD_SIZE = 10;

    public static final Map<Integer, Integer> SHIP_COUNT_FOR_SIZE
            = Collections.unmodifiableMap(
                    new HashMap<Integer, Integer>() {
                {
                    put(1, 4);
                    put(2, 3);
                    put(3, 2);
                    put(4, 1);
                }
            });

    public static final int SHIP_COUNT = SHIP_COUNT_FOR_SIZE.values().stream().reduce(0, Integer::sum);
    public static final int BOTTOM_INFO_SQUARES_COUNT = SHIP_COUNT_FOR_SIZE.entrySet().stream().map((e) -> e.getKey() * e.getValue()).reduce(0, Integer::sum);

    private final Matrix<Boolean> shipsLayer = new Matrix(BOARD_SIZE, BOARD_SIZE, false);
    private final Matrix<Boolean> shotsLayer = new Matrix(BOARD_SIZE, BOARD_SIZE, false);

    private final List<Ship> ships = new ArrayList<>();
    private final Set<Coord> invalidShipSquares = new HashSet<>();

    // PLACING SHIPS
    public void togglePlaceShipOnSquare(Coord c) {
        shipsLayer.set(c, !shipsLayer.get(c));
    }

    public boolean refreshPlacedShipSquares() {
        ships.clear();

        // Get sets of connected squares (independent of the shape they may be)
        Set<Set<Coord>> setsOfConnectedSquares = new HashSet<>();

        Set<Coord> allSquaresSeen = new HashSet<>();
        shipsLayer.forEach((c) -> {
            if (!allSquaresSeen.contains(c)) {
                Set<Coord> currentConnectedSquares = new HashSet<>();
                checkSquareSurroundings(c, currentConnectedSquares, allSquaresSeen);
                setsOfConnectedSquares.add(currentConnectedSquares);
            }
        });

        // Parse sets of connected squares to see if they can be a ship
        invalidShipSquares.clear();
        for (Set<Coord> currentConnectedSquares : setsOfConnectedSquares) {
            int squareCount = currentConnectedSquares.size();

            // Check size
            if (!SHIP_COUNT_FOR_SIZE.keySet().contains(squareCount)) {
                invalidShipSquares.addAll(currentConnectedSquares);
                continue;
            }

            // Check if we still don't have all the ships for that size
            int maxShipCount = SHIP_COUNT_FOR_SIZE.get(squareCount);
            int currentShipCount = (int) ships.stream().filter((s) -> s.size == squareCount).count();
            if (currentShipCount >= maxShipCount) {
                invalidShipSquares.addAll(currentConnectedSquares);
                continue;
            }

            // Check if all squares are on the same line
            // If yes, it's a valid ship; if not, it's an invalid square set
            boolean xAllEqual = currentConnectedSquares.stream().map((c) -> c.x).collect(toSet()).size() == 1;
            boolean yAllEqual = currentConnectedSquares.stream().map((c) -> c.y).collect(toSet()).size() == 1;

            if (xAllEqual || yAllEqual) {
                int minXpos = currentConnectedSquares.stream().map((c) -> c.x).reduce(Integer.MAX_VALUE, Integer::min);
                int minYpos = currentConnectedSquares.stream().map((c) -> c.y).reduce(Integer.MAX_VALUE, Integer::min);
                ships.add(new Ship(minXpos, minYpos, currentConnectedSquares.size(), xAllEqual));
            }
            else {
                invalidShipSquares.addAll(currentConnectedSquares);
            }
        }

        return invalidShipSquares.isEmpty();
    }

    private void checkSquareSurroundings(Coord pos, Set<Coord> currentConnectedSquares, Set<Coord> allSquaresSeen) {
        currentConnectedSquares.add(pos);
        allSquaresSeen.add(pos);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }

                Coord c = new Coord(pos.x + dx, pos.y + dy);

                if (shipsLayer.getOr(c, false)) {
                    checkSquareSurroundings(c, currentConnectedSquares, allSquaresSeen);
                }
            }
        }
    }

    public boolean placedShipsAreValid() {
        for (Map.Entry<Integer, Integer> entry : SHIP_COUNT_FOR_SIZE.entrySet()) {
            int count = (int) ships.stream().filter(s -> s.size == entry.getKey()).count();
            if (entry.getValue() != count) {
                return false;
            }
        }

        return true;
    }

    // PLAYING
    public boolean canShootOnSquare(Coord c) {
        return !shotsLayer.get(c);
    }

    public void shootOnSquare(Coord c) {
        shotsLayer.set(c, true);
    }

    public boolean allShipsAreShot() {
        Set<Coord> allShipSquares = new HashSet<>();
        ships.stream().forEach(s -> allShipSquares.addAll(s.getShipSquares()));
        return allShipSquares.stream().allMatch(c -> shotsLayer.get(c));
    }

    // GET BOARD INFO
    public BoardInfo getBoardInfoNotPlaying() {
        BoardInfo bi = new BoardInfo();

        // Populate top board
        shipsLayer.forEach((c, b) -> {
            bi.board.set(c, b ? BoardInfo.SquareFill.GraySquare : BoardInfo.SquareFill.Empty);
        });

        // Overwirte fill values where ship placement is invalid
        invalidShipSquares.forEach((c) -> {
            bi.board.set(c, BoardInfo.SquareFill.RedSquare);
        });

        // Populate bottom info rows
        final int[] offset = {16, 10, 4, 0}; // static final
        int[] count = {0, 0, 0, 0};
        for (Ship s : ships) {
            int index = s.size - 1;

            for (int i = offset[index] + count[index]; i < s.size; i++) {
                bi.bottomInfo.set(i, BoardInfo.SquareFill.GraySquare);
            }

            count[index]++;
        }

        return bi;
    }

    public BoardInfo getBoardInfoPlaying(boolean showAll) {
        BoardInfo bi = new BoardInfo();

        if (showAll) {
            // TODO: finish
        }
        else {
            // TODO: finish
        }

        return bi;
    }

}
