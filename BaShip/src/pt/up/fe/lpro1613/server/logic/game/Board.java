package pt.up.fe.lpro1613.server.logic.game;

import pt.up.fe.lpro1613.sharedlib.tuples.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.utils.Matrix;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;
import java.util.*;
import static java.util.stream.Collectors.toSet;
import static pt.up.fe.lpro1613.sharedlib.constants.BoardK.*;

public class Board {

    private final Set<Coord> invalidShipSquares = new HashSet<>();
    private final Matrix<Boolean> shipsLayer = new Matrix(BOARD_SIZE, BOARD_SIZE, false);

    private final List<Ship> ships = new ArrayList<>();
    private final Matrix<Boolean> shotsLayer = new Matrix(BOARD_SIZE, BOARD_SIZE, false);

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
                int minXpos = currentConnectedSquares.stream().map((c) -> c.x).min(Integer::min).get();
                int minYpos = currentConnectedSquares.stream().map((c) -> c.y).min(Integer::min).get();
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

    public List<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    // PLAYING
    public boolean canShootOnSquare(Coord c) {
        return !shotsLayer.get(c);
    }

    public void shootOnSquare(Coord c) {
        shotsLayer.set(c, true);
    }

    public Set<Coord> allShipSquares() {
        Set<Coord> allShipSquares = new HashSet<>();
        ships.stream().forEach(s -> allShipSquares.addAll(s.getShipSquares()));
        return allShipSquares;
    }

    public boolean allShipsAreShot() {
        return allShipSquares().stream().allMatch(c -> shotsLayer.get(c));
    }

    // GET BOARD INFO
    public BoardUIInfo getBoardInfo(boolean leftBoard, boolean playing, boolean showEverything) {
        if (playing) {
            return getBoardInfoPlaying(leftBoard, showEverything);
        }
        else {
            return getBoardInfoNotPlaying(leftBoard);
        }
    }

    public BoardUIInfo getBoardInfoNotPlaying(boolean leftBoard) {
        BoardUIInfo bi = new BoardUIInfo();
        bi.leftBoard = leftBoard;

        // Populate top board
        shipsLayer.forEach((c, b) -> {
            bi.board.set(c, b ? BoardUIInfo.SquareFill.GraySquare : BoardUIInfo.SquareFill.Empty);
        });

        // Overwirte fill values where ship placement is invalid
        invalidShipSquares.forEach((c) -> {
            bi.board.set(c, BoardUIInfo.SquareFill.RedSquare);
        });

        // Populate bottom info rows
        final int[] offset = {16, 10, 4, 0}; // static final TODO: should change based on info in SHIP_COUNT_FOR_SIZE
        int[] count = {0, 0, 0, 0};
        for (Ship s : ships) {
            int index = s.size - 1;

            for (int i = offset[index] + count[index]; i < s.size; i++) {
                bi.bottomInfo.set(i, BoardUIInfo.SquareFill.GraySquare);
            }

            count[index]++;
        }

        return bi;
    }

    public BoardUIInfo getBoardInfoPlaying(boolean leftBoard, boolean showEverything) {
        BoardUIInfo bi = new BoardUIInfo();
        bi.leftBoard = leftBoard;

        if (showEverything) {
            bi.board.setEach((c) -> {
                if (shotsLayer.get(c) && shipsLayer.get(c)) {
                    return BoardUIInfo.SquareFill.GraySquareRedCross;
                }
                else if (shotsLayer.get(c) && !shipsLayer.get(c)) {
                    return BoardUIInfo.SquareFill.BlueDiamond;
                }
                else if (!shotsLayer.get(c) && shipsLayer.get(c)) {
                    return BoardUIInfo.SquareFill.GraySquare;
                }
                else {
                    return BoardUIInfo.SquareFill.Empty;
                }
            });
            
            // TODO: finish
        }
        else {
            shotsLayer.forEach((coord, shot) -> {
                BoardUIInfo.SquareFill fill;

                if (shot) {
                    if (shipsLayer.get(coord)) {
                        fill = BoardUIInfo.SquareFill.GraySquareRedCross;
                    }
                    else {
                        fill = BoardUIInfo.SquareFill.BlueDiamond;
                    }
                }
                else {
                    fill = BoardUIInfo.SquareFill.Empty;
                }

                bi.board.set(coord, fill);
            });

            // TODO: finish
        }

        return bi;
    }

}
