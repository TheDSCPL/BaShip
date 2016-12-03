package client.logic;

import client.ClientMain;
import client.ui.game.GamePanel;
import java.util.List;
import javax.swing.JComponent;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.GameInfo;
import pt.up.fe.lpro1613.sharedlib.structs.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.Message;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;

public class GameC {

    public static List<GameInfo> getGameList(boolean currentlyPlayingOnly, String usernameFilter, int rowLimit) throws UserMessageException {
        return ClientMain.server.getGameList(currentlyPlayingOnly, usernameFilter, rowLimit);
    }

    public static void startRandomGame() throws UserMessageException {
        ClientMain.server.startRandomGame();
    }

    /**
     * Called automatically by the <code>Server</code> class whenever a new
     * in-game message is received.
     *
     * @param message The Message object
     */
    static public void receiveGameMessage(Message message) {

    }

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * invitation is received. Shows a pop-up to the user with the given message
     * String, allowing the user to choose whether he wants to accept the
     * invitation and start a game or not.
     *
     * @param message The message string to be displayed to the user.
     */
    static public void showGameInvitation(String message) {

    }

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * <code>SUpdateGameScreen</code> packet is received from the server.
     * Updates the interface accordingly.
     *
     * @param info The object containing what is to be displayed on the UI
     */
    static public void updateGameScreen(GameUIInfo info) {
        if (!(ClientMain.mainFrame.getCurrentPanel() instanceof GamePanel)) {
            ClientMain.mainFrame.changeToPanel(new GamePanel());
        }

        ((GamePanel) ClientMain.mainFrame.getCurrentPanel()).updateGameScreen(info);
    }

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * <code>SUpdateGameBoard</code> packet is received from the server. Updates
     * the board on the UI accordingly.
     *
     * @param info The object containing what is to be displayed on one of the
     * boards
     */
    static public void updateBoardInfo(BoardUIInfo info) {
        JComponent panel = ClientMain.mainFrame.getCurrentPanel();
        if (panel instanceof GamePanel) {
            ((GamePanel) panel).updateBoardInfo(info);
        }
        else {
            System.err.println("PROBLEM!!!");
        }
    }
    
    static public void clickBoardCoordinate(boolean leftBoard, Coord c) {
        
    }

    /*static public void togglePlaceShipOnSquare(Coord c) throws UserMessageException {
        ClientMain.server.togglePlaceShipOnSquare(c);
    }

    static public void fireShot(Coord c) throws UserMessageException {
        ClientMain.server.fireShot(c);
    }*/

    static public void clickReadyButton() throws UserMessageException {
        ClientMain.server.clickReadyButton();
    }

    static public void closeGame() throws UserMessageException {
        ClientMain.server.closeGame();
    }

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * <code>SGameFinished</code> packet is received from the server. Shows a
     * pop-up telling the user the game finished and why (player won, other
     * player disconnected, etc).
     *
     * @param message The message string to be displayed to the user
     */
    public static void gameFinished(String message) {
        // TODO: show message in a popup, close game panel and go back to lobby (or wait for close button press)
    }

}
