package client.logic;

import client.ClientMain;
import client.ui.game.GamePanel;
import client.ui.lobby.LobbyPanel;
import java.util.List;
import javax.swing.JComponent;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameInfo;
import sharedlib.structs.GameSearch;
import sharedlib.structs.GameUIInfo;
import sharedlib.utils.Coord;

/**
 * Class responsible for managing the state of the game of this user and getting
 * other game-related info.
 *
 * @author Alex
 */
public class GameC {

    /**
     * Get all games played on the server. This list may include all games
     * finished and/or currently being played.
     *
     * @param gs The parameters of this search for games
     * @return A list of <code>GameInfo</code> objects with all its fields
     * non-null.
     * @throws UserMessageException
     */
    public static List<GameInfo> getGameList(GameSearch gs) throws UserMessageException {
        return ClientMain.server.getGameList(gs);
    }

    /**
     * Ask the server to start a game with another random player.
     *
     * @throws UserMessageException
     */
    public static void startRandomGame() throws UserMessageException {
        ClientMain.server.startRandomGame();
    }

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
                GameChatC.clearGameMessages();
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
     * Inform the server that this user clicked on the board on a specific
     * position.
     *
     * @param leftBoard True if player clicked on the left board, false if
     * clicked on the right.
     * @param c Coordinates of the square where the player clicked
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
     * Inform the server that this user has finished placing ships and is ready
     * to start the game.
     *
     * @throws UserMessageException
     */
    static public void clickReadyButton() throws UserMessageException {
        ClientMain.server.clickReadyButton();
    }

    /**
     * Close the game. Switches the UI to the lobby and informs the server.
     *
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
