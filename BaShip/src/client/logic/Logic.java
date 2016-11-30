package client.logic;

import sharedlib.tuples.BoardInfo;
import sharedlib.tuples.GameScreenInfo;
import sharedlib.tuples.Message;

public class Logic {

    static public void receiveGameMessage(Message message) {
        
    }

    static public void receiveGlobalMessage(Message message) {
        //messagesTextArea.setText(messagesTextArea.getText() + "[" + m.timestamp + "] " + m.username + "\n" + m.text + "\n\n");
        //messagesTextArea.setCaretPosition(messagesTextArea.getDocument().getLength()); // Scroll to bottom
    }

    static public void showGameInvitation(String message) {
        
    }

    static public void updateGameScreen(GameScreenInfo info) {
        
    }

    static public void updateBoardInfo(BoardInfo info) {
        
    }

    public static void gameFinished(String message) {
        // TODO: show message in a popup, close game panel and go back to lobby
    }

}
