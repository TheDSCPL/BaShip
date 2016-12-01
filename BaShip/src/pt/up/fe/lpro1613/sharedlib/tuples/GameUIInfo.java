package pt.up.fe.lpro1613.sharedlib.tuples;

public class GameUIInfo {
    public final String titleLeft;
    public final String titleRight;
    public final Boolean showRightBoard;
    public final String waitMessageLine1;
    public final String waitMessageLine2;
    public final Boolean showReadyButton;
    public final Boolean player1Turn;
    public final Boolean player2Turn;

    public GameUIInfo(String titleLeft, String titleRight, Boolean showRightBoard, String waitMessageLine1, String waitMessageLine2, Boolean showReadyButton, Boolean player1Turn, Boolean player2Turn) {
        this.titleLeft = titleLeft;
        this.titleRight = titleRight;
        this.showRightBoard = showRightBoard;
        this.waitMessageLine1 = waitMessageLine1;
        this.waitMessageLine2 = waitMessageLine2;
        this.showReadyButton = showReadyButton;
        this.player1Turn = player1Turn;
        this.player2Turn = player2Turn;
    }
    
}
