package client.logic;

import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.Message;

public class GameC {

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

    }

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * <code>SGameFinished</code> packet is received from the server.
     * Shows a pop-up telling the user the game finished and why (player won,
     * other player disconnected, etc).
     *
     * @param message The message string to be displayed to the user
     */
    public static void gameFinished(String message) {
        // TODO: show message in a popup, close game panel and go back to lobby (or wait for close button press)
    }

}
