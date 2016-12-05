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

    /**
     * Updates the global chat UI accordingly. Called automatically by the
     * <code>Server</code> class whenever an <code>SReceiveGlobalMessage</code>
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
     * Send a global message to the server, that will then distribute the
     * message for all currently logged-in players.
     *
     * @param text The text of the message
     * @throws UserMessageException
     */
    public static void sendGameMessage(String text) throws UserMessageException {
        ClientMain.server.sendGameMessage(text);
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
