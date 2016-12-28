package server.logic.game;

import java.util.HashMap;
import java.util.Map;
import server.conn.Client;
import sharedlib.exceptions.ConnectionException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.Message;

/**
 *
 * @author Alex
 */
public class MockClient extends Client {

    public MockClient() {
        super(null);
    }

    Map<String, Object> calledMethods = new HashMap<>();

    public boolean wasCalled(String method) {
        return calledMethods.containsKey(method);
    }

    public Object callArgument(String method) {
        return calledMethods.get(method);
    }

    public void clearCalled() {
        calledMethods.clear();
    }

    private void registerCalled(String method, Object info) {
        calledMethods.put(method, info); // TODO: info == null?
    }
    
    private String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public void informAboutGlobalMessage(Message msg) throws ConnectionException {
        registerCalled(getMethodName(), msg);
    }

    public void informAboutGameMessage(Message msg) throws ConnectionException {
        registerCalled(getMethodName(), msg);
    }

    public void clearGameMessages() throws ConnectionException {
        registerCalled(getMethodName(), null);
    }

    public void updateGameScreen(GameUIInfo info) throws ConnectionException {
        registerCalled(getMethodName(), info);
    }

    public void sendGameInvitation(String usernameOfUserInvitingPlayer) throws ConnectionException {
        registerCalled(getMethodName(), usernameOfUserInvitingPlayer);
    }

    public void closeGameInvitation() throws ConnectionException {
        registerCalled(getMethodName(), null);
    }

    public void updateGameBoard(BoardUIInfo info) throws ConnectionException {
        registerCalled(getMethodName(), info);
    }

    public void showMessageAndCloseGame(String message) throws ConnectionException {
        registerCalled(getMethodName(), message);
    }

}
