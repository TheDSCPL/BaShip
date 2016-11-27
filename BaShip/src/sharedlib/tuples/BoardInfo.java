package sharedlib.tuples;

import java.util.ArrayList;
import java.util.List;
import server.logic.Board;
import sharedlib.utils.Matrix;

public class BoardInfo {
    
    public boolean leftBoard;
    public final Matrix<SquareFill> board = new Matrix(Board.BOARD_SIZE, Board.BOARD_SIZE, SquareFill.Empty);
    public final List<SquareFill> bottomInfo = new ArrayList(Board.TOTAL_BOTTOM_SQUARES_COUNT);
    
    public enum SquareFill {
        Empty,
        GraySquare, RedSquare, 
        RedCross,
        BlueDiamond,
        GrayCircle,
        GraySquareRedCross
    }
}
