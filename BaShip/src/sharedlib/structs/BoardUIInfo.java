package sharedlib.structs;

import java.util.ArrayList;
import java.util.List;
import static sharedlib.constants.BoardK.*;
import sharedlib.utils.Matrix;

/**
 * A class that represents the information to be displayed on the UI for a board
 * of battleship. A board consists of a quare matrix of positions where ships and
 * shots are placed, and a bottom row of small squares indicating the status of
 * each ship of a player.
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
     */
    public final List<SquareFill> bottomInfo = new ArrayList(BOTTOM_INFO_SQUARES_COUNT);

    /**
     * The various ways a square can be drawn on the UI.
     */
    public enum SquareFill {
        Empty,
        GraySquare,
        RedSquare,
        RedCross,
        BlueDiamond,
        GrayCircle,
        GraySquareRedCross
    }

    @Override
    public String toString() {
        return "BoardUIInfo{" + "leftBoard=" + leftBoard + ", board=" + board + ", bottomInfo=" + bottomInfo + '}';
    }
    
    
}
