package client.logic;

import client.ClientMain;
import client.ui.game.GamePanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.Message;

/**
 *
 * @author Alex
 */
public class GameChatC {

    private static final List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    public static void clearGameMessages() {
        messages.clear();
    }

    /**
     * Updates the game chat UI accordingly. Called automatically by the
     * <code>Server</code> class whenever an <code>SReceiveGameMessage</code>
     * packet is received from the server.
     *
     * @param message The message object to be displayed on the UI
     */
    public static void receiveGameMessage(Message message) {
        messages.add(message);

        ClientMain.runOnUI(() -> {
            JComponent panel = ClientMain.mainFrame.getCurrentPanel();
            if (panel instanceof GamePanel) {
                ((GamePanel) panel).refreshGameMessages();
            }
        });
    }

    /**
     * Send a game message to the server, that will then distribute the message
     * for all players and spectators of the game.
     *
     * @param text The text of the message
     */
    public static void sendGameMessage(String text) {
        try {
            ClientMain.server.sendGameMessage(text);
        }
        catch (UserMessageException ex) {
            ClientMain.showError(ex.getMessage());
        }
    }

    public static String messagesHTML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>").append("<head>").append("</head>").append("<body>");

        for (Message m : messages) {
            sb.append("<p>")
                    .append("<font size=6>")
                    .append("<b>")
                    .append(m.username)
                    .append("</b>")
                    .append("</font>");
            sb.append("<br>")
                    .append("<font size=4; color='red'>")
                    .append(m.timestamp)
                    .append("</font>");
            sb.append("<br>")
                    .append(m.text);
            sb.append("</p>");
        }

        sb.append("</body>").append("</html>");

        return sb.toString();
    }
}
