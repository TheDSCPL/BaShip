package sharedlib.tuples;

public class GameScreenInfo {
    public final String titleLeft;
    public final String titleRight;
    public final boolean showRightBoard;
    public final String waitMessageLine1;
    public final String waitMessageLine2;
    public final boolean showReadyButton;

    public GameScreenInfo(String titleLeft, String titleRight, boolean showRightBoard, String waitMessageLine1, String waitMessageLine2, boolean showReadyButton) {
        this.titleLeft = titleLeft;
        this.titleRight = titleRight;
        this.showRightBoard = showRightBoard;
        this.waitMessageLine1 = waitMessageLine1;
        this.waitMessageLine2 = waitMessageLine2;
        this.showReadyButton = showReadyButton;
    }
    
}
