package client.conn;

import client.*;
import java.io.*;
import java.util.*;
import sharedlib.conn.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.*;
import sharedlib.utils.*;

public class Server implements Connection.Delegate {

    private final Connection connection;

    @SuppressWarnings("LeakingThisInConstructor")
    public Server(Connection conn) {
        this.connection = conn;
        this.connection.delegate = this;
    }

    @Override
    public Packet handle(Packet request) {
        Packet response = null;

        switch (request.query) {
            case SReceiveGameMessage: {
                if (delegate != null) {
                    delegate.receiveGameMessage((Message) request.info);
                }
                break;
            }
            case SReceiveGlobalMessage: {
                if (delegate != null) {
                    delegate.receiveGlobalMessage((Message) request.info);
                }
                break;
            }
            case SShowGameScreen: {
                if (delegate != null) {
                    delegate.showGameScreen((GameScreenInfo) request.info);
                }
                break;
            }
            case SReceiveGameInvitation: {
                if (delegate != null) {
                    delegate.showGameInvitation((String) request.info);
                }
                break;
            }
        }

        return response;
    }
    
    @Override
    public void connected(Connection connection) {
        ClientMain.connected(connection.address());
    }

    @Override
    public void disconnected(Connection connection) {
        ClientMain.disconnected(connection.address());
    }

    public void connect() {
        connection.connect();
    }

    public void disconnect() throws IOException {
        connection.disconnect();
    }

    public Delegate delegate;

    public interface Delegate {

        public void receiveGameMessage(Message message);

        public void receiveGlobalMessage(Message message);
        
        public void showGameScreen(GameScreenInfo info);
        
        public void showGameInvitation(String message);
    }

    public boolean getUsernameAvailable(String username) throws UserMessageException {
        Packet request = new Packet(Query.CUsernameAvailable, username);
        Packet response = sendAndReceiveWrapper(request);
        return (Boolean) response.info;
    }

    public UserInfo doLogin(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.CLogin, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SErrorMessageResponse) {
            throw new UserMessageException("Could not login: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    public UserInfo doRegister(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.CRegister, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SErrorMessageResponse) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    public void doLogout() throws UserMessageException {
        Packet request = new Packet(Query.CLogout);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    public List<UserInfo> getUserList(boolean onlineOnly, String usernameFilter, int orderByColumn, int rowLimit) throws UserMessageException {
        Packet request = new Packet(Query.CGetUserList, new UserSearch(onlineOnly, usernameFilter, orderByColumn, rowLimit));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SErrorMessageResponse) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<UserInfo>) response.info;
        }
    }

    public List<GameInfo> getGameList(boolean currentlyPlayingOnly, String usernameFilter, int orderByColumn, int rowLimit) throws UserMessageException {
        Packet request = new Packet(Query.CGetGameList, new GameSearch(currentlyPlayingOnly, usernameFilter, orderByColumn, rowLimit));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SErrorMessageResponse) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<GameInfo>) response.info;
        }
    }

    public void sendGlobalMessage(String message) throws UserMessageException {
        Packet request = new Packet(Query.CSendGlobalMessage, message);
        sendOnlyWrapper(request);
    }

    public void startRandomGame() throws UserMessageException {
        Packet request = new Packet(Query.CStartRandomGame);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SErrorMessageResponse) {
            throw new UserMessageException("Could not start random game: " + ((ErrorMessage) response.info).message);
        }
    }
    
    public void startGameWithPlayer(Long id) throws UserMessageException {
        Packet request = new Packet(Query.CStartGameWithPlayer, id);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SErrorMessageResponse) {
            throw new UserMessageException("Could not start game with player: " + ((ErrorMessage) response.info).message);
        }
    }
    
    public void anwserGameInvitation(boolean accepted) throws UserMessageException {
        Packet request = new Packet(Query.CAnswerGameInvitation, accepted);
        sendOnlyWrapper(request);
    }

    private Packet sendAndReceiveWrapper(Packet request) throws UserMessageException {
        Packet response;
        try {
            response = connection.sendAndReceive(request);
        }
        catch (ConnectionException ex) {
            throw new UserMessageException("Could not connect to server: " + ex.getMessage());
        }
        return response;
    }

    private void sendOnlyWrapper(Packet request) throws UserMessageException {
        try {
            connection.sendOnly(request);
        }
        catch (ConnectionException ex) {
            throw new UserMessageException("Could not connect to server: " + ex.getMessage());
        }
    }
}
