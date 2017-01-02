package sharedlib.structs;

/**
 * Class that contains the user interface info of the game
 */
public class GameUIInfo implements UIInfo{

    /**
     * The name of the player who appears n the left side. 
     */
    public final String titleLeft;

    /**
     * The name of the player who appears on the right side.
     */
    public final String titleRight;

    /**
     * If it is to show the right board or not. 
     */
    public final Boolean showRightBoard;
    public final String waitMessageLine1;
    public final String waitMessageLine2;

    /**
     * Indicates if the ships are correctly placed so the ready button can
     * appear.
     */
    public final Boolean showReadyButton;

    /**
     * Player1 turn. If true, highlights player1 username
     */
    public final Boolean player1Turn;

    /**
     * Player2 turn. If true, highlights player2 username
     */
    public final Boolean player2Turn;
    public final UIType uiType;

    /**
     * If there is a previous move to show. (i.e, if there is not the first move
     * of the game)
     */
    public final Boolean canShowPreviousMove;

    /**
     * If there is a next move to show (i.e., if there is not the last move of
     * the game)
     */
    public final Boolean canShowNextMove;
    public final String moveCounterText;

    public GameUIInfo(String titleLeft, String titleRight, Boolean showRightBoard, String waitMessageLine1, String waitMessageLine2, Boolean showReadyButton, Boolean player1Turn, Boolean player2Turn, UIType uiType, Boolean canShowPreviousMove, Boolean canShowNextMove, String moveCounterText) {
        this.titleLeft = titleLeft;
        this.titleRight = titleRight;
        this.showRightBoard = showRightBoard;
        this.waitMessageLine1 = waitMessageLine1;
        this.waitMessageLine2 = waitMessageLine2;
        this.showReadyButton = showReadyButton;
        this.player1Turn = player1Turn;
        this.player2Turn = player2Turn;
        this.uiType = uiType;
        this.canShowPreviousMove = canShowPreviousMove;
        this.canShowNextMove = canShowNextMove;
        this.moveCounterText = moveCounterText;
    }
    
    public enum UIType {
        Play, Spectate, Replay;
    }

    @Override
    public String toString() {
        return "GameUIInfo{" + "titleLeft=" + titleLeft + ", titleRight=" + titleRight + ", showRightBoard=" + showRightBoard + ", waitMessageLine1=" + waitMessageLine1 + ", waitMessageLine2=" + waitMessageLine2 + ", showReadyButton=" + showReadyButton + ", player1Turn=" + player1Turn + ", player2Turn=" + player2Turn + ", uiType=" + uiType + ", canShowPreviousMove=" + canShowPreviousMove + ", canShowNextMove=" + canShowNextMove + ", moveCounterText=" + moveCounterText + '}';
    }
    
}
