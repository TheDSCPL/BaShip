package client.logic;

import client.ClientMain;
import client.ui.game.GamePanel;
import client.ui.lobby.LobbyPanel;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
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
    public static List<GameInfo> getGameList(GameSearch gs) {
        try {
            return ClientMain.server.getGameList(gs);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
            return null;
        }
    }

    /**
     * Ask the server to start a game with another random player.
     *
     * @throws UserMessageException
     */
    public static void startRandomGame() {
        try {
            ClientMain.server.startRandomGame();
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
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
    static public void clickBoardCoordinate(boolean leftBoard, Coord c) {
        try {
            if (leftBoard) {
                ClientMain.server.clickLeftBoard(c);
            }
            else {
                ClientMain.server.clickRightBoard(c);
            }
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

    /**
     * Inform the server that this user has finished placing ships and is ready
     * to start the game.
     *
     * @throws UserMessageException
     */
    static public void clickReadyButton() {
        try {
            ClientMain.server.clickReadyButton();
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

    static public void showNextMove() throws UserMessageException {
        ClientMain.server.showNextMove();
    }

    static public void showPreviousMove() throws UserMessageException {
        ClientMain.server.showPreviousMove();
    }

    /**
     * Close the game. Switches the UI to the lobby and informs the server.
     *
     * @throws UserMessageException
     */
    static public void closeGame() {
        try {
            ClientMain.server.closeGame();
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }

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
    public static void showMessageAndCloseGame(String message) {
        ClientMain.runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Info", INFORMATION_MESSAGE);
            ClientMain.mainFrame.changeToPanel(new LobbyPanel());
        });
    }

    public static void doubleClickGame(GameInfo gameInfo) {
        try {
            ClientMain.server.doubleClickGame(gameInfo.id);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

    public static void showGameInvitation(String username) {
        ClientMain.runOnUI(() -> {
            int dialogResult = JOptionPane.showConfirmDialog(ClientMain.mainFrame, "Player '" + username + "' invited you. Would you like to play?", "Invite", JOptionPane.YES_NO_OPTION);
            
            try {
                ClientMain.server.anwserGameInvitation(dialogResult == JOptionPane.YES_OPTION);
            }
            catch (UserMessageException ex) {
                ClientMain.showError(ex.getMessage());
            }
        });
    }

    public static void closeGameInvitation() {
        ClientMain.runOnUI(() -> {
            // TODO: Luis: fechar o confirm dialog criado na linha 165 deste ficheiro
        });
    }

}
