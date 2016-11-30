package sharedlib.tuples;

import java.util.ArrayList;
import java.util.List;
import static sharedlib.constants.BoardK.*;
import sharedlib.utils.Matrix;

public class BoardInfo {
    
    public boolean leftBoard;
    public final Matrix<SquareFill> board = new Matrix(BOARD_SIZE, BOARD_SIZE, SquareFill.Empty);
    public final List<SquareFill> bottomInfo = new ArrayList(BOTTOM_INFO_SQUARES_COUNT);
    
    public enum SquareFill {
        Empty,
        GraySquare, RedSquare, 
        RedCross,
        BlueDiamond,
        GrayCircle,
        GraySquareRedCross
    }
}
