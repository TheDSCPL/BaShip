package client.logic;

import client.ClientMain;
import client.ui.lobby.LobbyPanel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.Message;

/**
 * Class responsible for managing the global chat.
 *
 * @author Alex
 */
public class GlobalChatC {

    /**
     * List of messages received since the player logged-in.
     */
    public static List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    /**
     * Updates the global chat UI accordingly. Called automatically by the
     * <code>Server</code> class whenever an <code>SReceiveGlobalMessage</code>
     * packet is received from the server.
     *
     * @param message The message object to be displayed on the UI
     */
    public static void receiveGlobalMessage(Message message) {
        messages.add(message);

        ClientMain.runOnUI(() -> {
            JComponent panel = ClientMain.mainFrame.getCurrentPanel();
            if (panel instanceof LobbyPanel) {
                ((LobbyPanel) panel).refreshGlobalMessages();
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
    public static void sendGlobalMessage(String text) throws UserMessageException {
        ClientMain.server.sendGlobalMessage(text);
    }

}
