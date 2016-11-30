package client.conn;

import client.*;
import client.logic.*;
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
                GameC.receiveGameMessage((Message) request.info);
                break;
            }
            case SReceiveGlobalMessage: {
                GlobalChatC.receiveGlobalMessage((Message) request.info);
                break;
            }
            case SUpdateGameScreen: {
                GameC.updateGameScreen((GameScreenInfo) request.info);
                break;
            }
            case SReceiveGameInvitation: {
                GameC.showGameInvitation((String) request.info);
                break;
            }
            case SUpdateGameBoard: {
                GameC.updateBoardInfo((BoardInfo) request.info);
                break;
            }
            case SGameFinished: {
                GameC.gameFinished((String) request.info);
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

    public boolean getUsernameAvailable(String username) throws UserMessageException {
        Packet request = new Packet(Query.CUsernameAvailable, username);
        Packet response = sendAndReceiveWrapper(request);
        return (Boolean) response.info;
    }

    public UserInfo doLogin(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.CLogin, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not login: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    public UserInfo doRegister(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.CRegister, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SRErrorMessage) {
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

        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<UserInfo>) response.info;
        }
    }

    public List<GameInfo> getGameList(boolean currentlyPlayingOnly, String usernameFilter, int orderByColumn, int rowLimit) throws UserMessageException {
        Packet request = new Packet(Query.CGetGameList, new GameSearch(currentlyPlayingOnly, usernameFilter, orderByColumn, rowLimit));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<GameInfo>) response.info;
        }
    }

    public void sendGlobalMessage(String message) throws UserMessageException {
        Packet request = new Packet(Query.CSendGlobalMessage, message);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not send global message: " + ((ErrorMessage) response.info).message);
        }
    }

    public void startRandomGame() throws UserMessageException {
        Packet request = new Packet(Query.CStartRandomGame);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not start random game: " + ((ErrorMessage) response.info).message);
        }
    }
    
    public void startGameWithPlayer(Long id) throws UserMessageException {
        Packet request = new Packet(Query.CStartGameWithPlayer, id);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not start game with player: " + ((ErrorMessage) response.info).message);
        }
    }
    
    public void anwserGameInvitation(boolean accepted) throws UserMessageException {
        Packet request = new Packet(Query.CAnswerGameInvitation, accepted);
        sendOnlyWrapper(request);
    }
    
    public void togglePlaceShipOnSquare(Coord pos) throws UserMessageException {
        Packet request = new Packet(Query.CTogglePlaceOnShipSquare, pos);
        sendOnlyWrapper(request);
    }
    
    public void clickReadyButton() throws UserMessageException {
        Packet request = new Packet(Query.CClickReadyButton);
        sendOnlyWrapper(request);
    }

    public void fireShot(Coord pos) throws UserMessageException {
        Packet request = new Packet(Query.CFireShot, pos);
        sendOnlyWrapper(request);
    }
    
    public void closeGame() throws UserMessageException {
        Packet request = new Packet(Query.CCloseGame);
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
