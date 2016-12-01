package client.logic;

import client.ClientMain;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.Message;

public class GlobalChatC {

    /**
     * Called automatically by the <code>Server</code> class whenever an
     * <code>SReceiveGlobalMessage</code> packet is received from the server.
     * Updates the global chat UI accordingly.
     *
     * @param message The message object to be displayed on the UI
     */
    static public void receiveGlobalMessage(Message message) {
        //messagesTextArea.setText(messagesTextArea.getText() + "[" + m.timestamp + "] " + m.username + "\n" + m.text + "\n\n");
        //messagesTextArea.setCaretPosition(messagesTextArea.getDocument().getLength()); // Scroll to bottom
    }

    public static void sendGlobalMessage(String text) throws UserMessageException {
        ClientMain.server.sendGlobalMessage(text);
    }

}
