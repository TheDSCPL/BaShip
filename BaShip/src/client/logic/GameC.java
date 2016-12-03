package client.logic;

import client.ClientMain;
import client.ui.game.GamePanel;
import client.ui.lobby.LobbyPanel;
import java.util.List;
import javax.swing.JComponent;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.GameInfo;
import pt.up.fe.lpro1613.sharedlib.structs.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;

/**
 * TODO: JAVADOC
 * @author Alex
 */
public class GameC {

    private static boolean playingGame = false;

    /**
     * TODO: JAVADOC
     * @param currentlyPlayingOnly
     * @param usernameFilter
     * @param rowLimit
     * @return
     * @throws UserMessageException
     */
    public static List<GameInfo> getGameList(boolean currentlyPlayingOnly, String usernameFilter, int rowLimit) throws UserMessageException {
        return ClientMain.server.getGameList(currentlyPlayingOnly, usernameFilter, rowLimit);
    }

    /**
     * TODO: JAVADOC
     * @throws UserMessageException 
     */
    public static void startRandomGame() throws UserMessageException {
        ClientMain.server.startRandomGame();
    }

    /**
     * Called automatically by the <code>Server</code> class whenever a new
     * in-game message is received.
     *
     * @param message The Message object
     */
    /*static public void receiveGameMessage(Message message) {

    }*/
    /**
     * Called automatically by the <code>Server</code> class whenever an
     * invitation is received. Shows a pop-up to the user with the given message
     * String, allowing the user to choose whether he wants to accept the
     * invitation and start a game or not.
     *
     * @param message The message string to be displayed to the user.
     */
    /*static public void showGameInvitation(String message) {

    }*/
    /**
     * Updates the game UI accordingly if a GamePanel is already being shown. If
     * not, shows a GamePanel first.
     *
     * @param info The object containing what is to be displayed on the UI
     */
    static public void updateGameScreen(GameUIInfo info) {
        ClientMain.runOnUI(() -> {
            if (!(ClientMain.mainFrame.getCurrentPanel() instanceof GamePanel)) {
                ClientMain.mainFrame.changeToPanel(new GamePanel());
            }

            ((GamePanel) ClientMain.mainFrame.getCurrentPanel()).updateGameScreen(info);
        });
    }

    /**
     * Updates the correct board on the game UI accordingly.
     *
     * @param info The object containing what is to be displayed on one of the
     * boards
     */
    static public void updateBoardInfo(BoardUIInfo info) {
        ClientMain.runOnUI(() -> {
            JComponent panel = ClientMain.mainFrame.getCurrentPanel();
            if (panel instanceof GamePanel) {
                ((GamePanel) panel).updateBoardInfo(info);
            }
        });
    }

    /**
     * TODO: JAVADOC
     * @param leftBoard
     * @param c
     * @throws UserMessageException 
     */
    static public void clickBoardCoordinate(boolean leftBoard, Coord c) throws UserMessageException {
        if (leftBoard) {
            ClientMain.server.clickLeftBoard(c);
        }
        else {
            ClientMain.server.clickRightBoard(c);
        }
    }

    /**
     * TODO: JAVADOC
     * @throws UserMessageException 
     */
    static public void clickReadyButton() throws UserMessageException {
        ClientMain.server.clickReadyButton();
    }

    /**
     * TODO: JAVADOC
     * @throws UserMessageException 
     */
    static public void closeGame() throws UserMessageException {
        ClientMain.server.closeGame();
        ClientMain.runOnUI(() -> {
            ClientMain.mainFrame.changeToPanel(new LobbyPanel());
        });
    }

    /**
     * Shows a pop-up telling the user the game finished and why (player won,
     * other player disconnected, etc) and changes the UI to the player lobby.
     *
     * @param message The message string to be displayed to the user
     */
    public static void gameFinished(String message) {
        ClientMain.runOnUI(() -> {
            ClientMain.showInfo(message);
            ClientMain.mainFrame.changeToPanel(new LobbyPanel());
        });
    }

}
