package client.conn;

import client.ClientMain;
import java.io.IOException;
import java.util.List;
import sharedlib.conn.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.ErrorMessage;
import sharedlib.tuples.GameInfo;
import sharedlib.tuples.GameSearch;
import sharedlib.tuples.Message;
import sharedlib.tuples.UserInfo;
import sharedlib.tuples.UserSearch;
import sharedlib.utils.Crypto;

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
            case ReceiveGameMessage: {
                if (delegate != null) {
                    delegate.receiveGameMessage((Message) request.info);
                }
                break;
            }
            case ReceiveGlobalMessage: {
                if (delegate != null) {
                    delegate.receiveGlobalMessage((Message) request.info);
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
    }

    public boolean getUsernameAvailable(String username) throws UserMessageException {
        Packet request = new Packet(Query.UsernameAvailable, username);
        Packet response = sendAndReceiveWrapper(request);
        return (Boolean) response.info;
    }

    public UserInfo doLogin(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.Login, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.info instanceof ErrorMessage) {
            throw new UserMessageException("Could not login: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    public UserInfo doRegister(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.Register, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.info instanceof ErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    public void doLogout() throws UserMessageException {
        Packet request = new Packet(Query.Logout);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    public List<UserInfo> getUserList(boolean onlineOnly, String usernameFilter, int orderByColumn, int rowLimit) throws UserMessageException {
        Packet request = new Packet(Query.GetUserList, new UserSearch(onlineOnly, usernameFilter, orderByColumn, rowLimit));
        Packet response = sendAndReceiveWrapper(request);

        if (response.info instanceof ErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<UserInfo>) response.info;
        }
    }

    public List<GameInfo> getGameList(boolean currentlyPlayingOnly, String usernameFilter, int orderByColumn, int rowLimit) throws UserMessageException {
        Packet request = new Packet(Query.GetUserList, new GameSearch(currentlyPlayingOnly, usernameFilter, orderByColumn, rowLimit));
        Packet response = sendAndReceiveWrapper(request);

        if (response.info instanceof ErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<GameInfo>) response.info;
        }
    }

    public void sendGlobalMessage(String message) throws UserMessageException {
        Packet request = new Packet(Query.SendGlobalMessage, message);
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
