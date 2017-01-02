package sharedlib.structs;

import java.util.ArrayList;
import static java.util.Collections.nCopies;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static sharedlib.constants.BoardK.*;
import sharedlib.utils.Matrix;

/**
 * A class that represents the information to be displayed on the UI for a board
 * of battleship. A board consists of a square matrix of positions where ships
 * and shots are placed, and a bottom row of small squares indicating the status
 * of each ship of a player.
 *
 * @author Alex
 */
public class BoardUIInfo implements UIInfo {

    /**
     * Indicates whether this the left board on the UI, or the right.
     */
    public boolean leftBoard;

    /**
     * Matrix of squares where ships and shots are shown.
     */
    public final Matrix<SquareFill> board = new Matrix(BOARD_SIZE, BOARD_SIZE, SquareFill.Empty);

    /**
     * Row of squares where the status of each ship (for one player) is shown.
     * List starts with the biggest ships first, and the last positions are for
     * the smaller ships: [0|1|2|3] [4|5|6] [7|8|9] [10|11] [12|13] [14|15] [16]
     * [17] [18] [19] (numbers are indices on the list)
     */
    public final List<SquareFill> bottomInfo = new ArrayList(nCopies(BOTTOM_INFO_SQUARES_COUNT, SquareFill.Empty));

    public void setBottomInfo(int shipSize, int shipIndex, int shipSquareIndex, SquareFill value) {
        bottomInfo.set(bottomInfoOffsets.get(shipSize) + shipIndex * shipSize + shipSquareIndex, value);
    }
    
    public SquareFill getBottomInfo(int shipSize, int shipIndex, int shipSquareIndex) {
        return bottomInfo.get(bottomInfoOffsets.get(shipSize) + shipIndex * shipSize + shipSquareIndex);
    }
    
    private static final Map<Integer, Integer> bottomInfoOffsets = new HashMap<Integer, Integer>() {
        {
            SHIP_COUNT_FOR_SIZE.keySet().stream().forEach((size) -> {
                int offset = SHIP_COUNT_FOR_SIZE
                        .entrySet()
                        .stream()
                        .filter((e) -> e.getKey() > size)
                        .map((e) -> e.getKey() * e.getValue())
                        .reduce(0, Integer::sum);

                put(size, offset);
            });
        }
    };

    /**
     * The various ways a square can be drawn on the UI.
     */
    public enum SquareFill {

        /**
         * Empty square 
         */
        Empty,

        /**
         * Indicates a placed ship
         */
        GraySquare,

        /**
         * Red Square
         */
        RedSquare,

        /**
         * Indicates a downed part of ship
         */
        RedCross,

        /**
         * Inicates a shot missed
         */
        BlueDiamond,

        /**
         * Indicates an impossible position to place ships
         */
        GrayCircle,

        /**
         *  Indicates a downed ship. Only when the whole ship is downed the
         *  consecutive RedCrosses become consecutive GraySquareRedCrosses.
         */
        GraySquareRedCross
    }

    @Override
    public String toString() {
        return "BoardUIInfo{" + "leftBoard=" + leftBoard + ", board=" + board + ", bottomInfo=" + bottomInfo + '}';
    }

}
