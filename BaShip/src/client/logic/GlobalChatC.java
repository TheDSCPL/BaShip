package client.logic;

import client.ClientMain;
import client.ui.lobby.LobbyPanel;
import javax.swing.JComponent;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.Message;

/**
 * TODO: JAVADOC
 * @author Alex
 */
public class GlobalChatC {

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * <code>SReceiveGlobalMessage</code> packet is received from the server.
     * Updates the global chat UI accordingly.
     *
     * @param message The message object to be displayed on the UI
     */
    public static void receiveGlobalMessage(Message message) {
        JComponent panel = ClientMain.mainFrame.getCurrentPanel();
        if (panel instanceof LobbyPanel) {
            ((LobbyPanel) panel).receiveGlobalMessage(message);
        }
    }

    /**
     * TODO: JAVADOC
     * @param text
     * @throws UserMessageException 
     */
    public static void sendGlobalMessage(String text) throws UserMessageException {
        ClientMain.server.sendGlobalMessage(text);
    }

}
