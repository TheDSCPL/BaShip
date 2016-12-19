package server.logic.game;

import java.util.*;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.toSet;
import static sharedlib.constants.BoardK.*;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.BoardUIInfo.SquareFill;
import sharedlib.structs.Ship;
import sharedlib.utils.Coord;
import sharedlib.utils.Matrix;

class Board {

    private final Set<Coord> invalidShipSquares = new HashSet<>();
    private final Matrix<Boolean> shipsLayer = new Matrix(BOARD_SIZE, BOARD_SIZE, false);

    private final List<Ship> ships = new ArrayList<>();
    private final Matrix<Boolean> shotsLayer = new Matrix(BOARD_SIZE, BOARD_SIZE, false);

    // PLACING SHIPS
    public void togglePlaceShipOnSquare(Coord c) {
        shipsLayer.set(c, !shipsLayer.get(c));
        refreshPlacedShipSquares();
    }

    private void refreshPlacedShipSquares() {
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
                int minXpos = currentConnectedSquares.stream().map((c) -> c.x).reduce(Integer.MAX_VALUE, (a, b) -> (a < b ? a : b));
                int minYpos = currentConnectedSquares.stream().map((c) -> c.y).reduce(Integer.MAX_VALUE, (a, b) -> (a < b ? a : b));
                ships.add(new Ship(minXpos, minYpos, squareCount, xAllEqual));
            }
            else {
                invalidShipSquares.addAll(currentConnectedSquares);
            }
        }
    }

    private void checkSquareSurroundings(Coord pos, Set<Coord> currentConnectedSquares, Set<Coord> allSquaresSeen) {
        allSquaresSeen.add(pos);

        if (shipsLayer.get(pos)) {
            currentConnectedSquares.add(pos);
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Coord c = new Coord(pos.x + dx, pos.y + dy);

                if (!allSquaresSeen.contains(c) && shipsLayer.getOr(c, false)) {
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

        return invalidShipSquares.isEmpty();
    }

    public void setShips(List<Ship> ships) {
        this.ships.clear();
        this.ships.addAll(ships);

        shipsLayer.setAll(false);
        for (Coord c : allShipSquares()) {
            shipsLayer.set(c, true);
        }
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

    public void removeShotFromSquare(Coord c) {
        shotsLayer.set(c, false);
    }

    private Set<Coord> allShipSquares() {
        Set<Coord> allShipSquares = new HashSet<>();
        ships.stream().forEach(s -> allShipSquares.addAll(s.getShipSquares()));
        return allShipSquares;
    }

    private boolean shipIsCompletelyShot(Ship s) {
        return s.getShipSquares().stream().allMatch(c -> shotsLayer.get(c));
    }

    public boolean allShipsAreShot() {
        return allShipSquares().stream().allMatch(c -> shotsLayer.get(c));
    }

    // GET BOARD INFO
    public BoardUIInfo getBoardInfo(boolean playing, boolean showEverything) {
        if (playing) {
            return getBoardInfoPlaying(showEverything);
        }
        else {
            return getBoardInfoPlacingShips(showEverything);
        }
    }

    public BoardUIInfo getBoardInfoPlacingShips(boolean showEverything) {
        BoardUIInfo bi = new BoardUIInfo();

        if (showEverything) {
            // Populate top board
            shipsLayer.forEach((c, b) -> {
                bi.board.set(c, b ? BoardUIInfo.SquareFill.GraySquare : BoardUIInfo.SquareFill.Empty);
            });

            // Overwrite fill values where ship placement is invalid
            invalidShipSquares.forEach((c) -> {
                bi.board.set(c, BoardUIInfo.SquareFill.RedSquare);
            });

            // Populate bottom info rows
            List<Integer> shipCount = new ArrayList<>(nCopies(SHIP_COUNT_FOR_SIZE.keySet().size(), 0));

            for (Ship s : ships) {
                int count = shipCount.get(s.size - 1);

                for (int i = 0; i < s.size; i++) {
                    bi.setBottomInfo(s.size, count, i, SquareFill.GraySquare);
                }

                shipCount.set(s.size - 1, count + 1);
            }
        }

        return bi;
    }

    public BoardUIInfo getBoardInfoPlaying(boolean showEverything) {
        BoardUIInfo bi = new BoardUIInfo();

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
        }

        // Populate bottom info rows
        List<Integer> shipCount = new ArrayList<>(nCopies(SHIP_COUNT_FOR_SIZE.keySet().size(), 0));

        for (Ship s : ships) {
            if (showEverything || (!showEverything && shipIsCompletelyShot(s))) {
                int count = shipCount.get(s.size - 1);

                for (int i = 0; i < s.size; i++) {
                    bi.setBottomInfo(
                            s.size, count, i,
                            shotsLayer.get(s.coordForShipPos(i)) ? SquareFill.RedCross : SquareFill.Empty
                    );
                }

                shipCount.set(s.size - 1, count + 1);
            }
        }

        return bi;
    }

}
