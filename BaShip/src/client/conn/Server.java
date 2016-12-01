package client.conn;

import pt.up.fe.lpro1613.sharedlib.tuples.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.conn.Packet;
import pt.up.fe.lpro1613.sharedlib.conn.Query;
import pt.up.fe.lpro1613.sharedlib.tuples.ErrorMessage;
import pt.up.fe.lpro1613.sharedlib.tuples.UserInfo;
import pt.up.fe.lpro1613.sharedlib.tuples.UserSearch;
import pt.up.fe.lpro1613.sharedlib.utils.Crypto;
import pt.up.fe.lpro1613.sharedlib.tuples.GameInfo;
import pt.up.fe.lpro1613.sharedlib.tuples.Message;
import pt.up.fe.lpro1613.sharedlib.tuples.GameSearch;
import pt.up.fe.lpro1613.sharedlib.conn.Connection;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.tuples.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;
import pt.up.fe.lpro1613.sharedlib.exceptions.ConnectionException;
import client.*;
import client.logic.*;
import java.io.*;
import java.util.*;

/**
 * Wrapper around a Connection object that implements the Server-Client protocol
 * for the client side. Methods called on this class send the corresponding <code>Packet</code> objects
 * to the server, and wait for a response <code>Packet</code> if necessary.
 * @author Alex
 */
public class Server implements Connection.Delegate {

    private final Connection connection;

    /**
     * Create a new Server based on the given connection object.
     * @param conn The connection object. Cannot be null
     */
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
                GameC.updateGameScreen((GameUIInfo) request.info);
                break;
            }
            case SReceiveGameInvitation: {
                GameC.showGameInvitation((String) request.info);
                break;
            }
            case SUpdateGameBoard: {
                GameC.updateBoardInfo((BoardUIInfo) request.info);
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

    /**
     * Connect to the server through the underlying connection object.
     */
    public void connect() {
        connection.connect();
    }

    /**
     * Disconnect from the server.
     * @throws IOException if disconnect was unsuccessful
     */
    public void disconnect() throws IOException {
        connection.disconnect();
    }

    /**
     * Ask the server if the specified username is available
     * @param username The username to test. Cannot be null
     * @return True if the username can be used to create a new account
     * @throws UserMessageException 
     */
    public boolean getUsernameAvailable(String username) throws UserMessageException {
        Packet request = new Packet(Query.CUsernameAvailable, username);
        Packet response = sendAndReceiveWrapper(request);
        return (Boolean) response.info;
    }

    /**
     * Login using the specified username and password
     * @param username
     * @param password
     * @return A <code>UserInfo</code> object with the fields <code>id</code> and <code>username</code> non-null
     * @throws UserMessageException 
     */
    public UserInfo login(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.CLogin, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not login: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    /**
     * Register a new user on the server.
     * @param username The username of the new user. Cannot be null.
     * @param password The password of the new user. 
     * @return A <code>UserInfo</code> object with the fields <code>id</code> and <code>username</code> non-null
     * @throws UserMessageException 
     */
    public UserInfo register(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.CRegister, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    /**
     * Logout the current logged-in user for this client.
     * @throws UserMessageException 
     */
    public void logout() throws UserMessageException {
        Packet request = new Packet(Query.CLogout);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    /**
     * Get all users registered on the server.
     * @param onlineOnly Filter by users that are online only.
     * @param usernameFilter Filter by users that contain this string in their
     * username. To include all users pass in an empty String.
     * @param orderByColumn Which user information to order the results by.
     * 1=id, 2=username, 3=rank, 4=ngames, 5=nwins, 6=nshots.
     * @param rowLimit The maximum number of users to retrieve.
     * @return A list of <code>UserInfo</code> objects with all its fields non-null.
     * @throws UserMessageException 
     */
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

    /**
     * Get all games played on the server. This list may include all games
     * finished and/or currently being played.
     * @param currentlyPlayingOnly Return only games currently being played on the sever.
     * @param usernameFilter Filter by games whose players have usernames that
     * contain this string. To ignore this filter pass in an empty String.
     * @param rowLimit The maximum number of games to retrieve.
     * @return A list of <code>GameInfo</code> objects with all its fields non-null.
     * @throws UserMessageException 
     */
    public List<GameInfo> getGameList(boolean currentlyPlayingOnly, String usernameFilter, int rowLimit) throws UserMessageException {
        Packet request = new Packet(Query.CGetGameList, new GameSearch(currentlyPlayingOnly, usernameFilter, rowLimit));
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not register: " + ((ErrorMessage) response.info).message);
        }
        else {
            return (List<GameInfo>) response.info;
        }
    }

    /**
     * Send a text message to all logged-in users on the server. Requires that
     * this client be logged in on the server.
     * @param message The text message
     * @throws UserMessageException 
     */
    public void sendGlobalMessage(String message) throws UserMessageException {
        Packet request = new Packet(Query.CSendGlobalMessage, message);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not send global message: " + ((ErrorMessage) response.info).message);
        }
    }

    /**
     * Ask the server to start a game with another random player.
     * If the server decides to start a game, it will later (not as a response
     * to this packet) send a SUpdateGameScreen packet to this client that will
     * be handled by this class separately.
     * @throws UserMessageException 
     */
    public void startRandomGame() throws UserMessageException {
        Packet request = new Packet(Query.CStartRandomGame);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not start random game: " + ((ErrorMessage) response.info).message);
        }
    }
    
    /**
     * Ask the server to start a game with another (specific) player.
     * If the server decides to start a game, it will later (not as a response
     * to this packet) send a SUpdateGameScreen packet to this client that will
     * be handled by this class separately.
     * @param id The id of the other player, to which the server will send an invite
     * @throws UserMessageException 
     */
    public void startGameWithPlayer(Long id) throws UserMessageException {
        Packet request = new Packet(Query.CStartGameWithPlayer, id);
        Packet response = sendAndReceiveWrapper(request);
        
        if (response.query == Query.SRErrorMessage) {
            throw new UserMessageException("Could not start game with player: " + ((ErrorMessage) response.info).message);
        }
    }
    
    /**
     * Inform the server if the player accepted the invitation or not.
     * This should be called after receiving an invitation from another player.
     * @param accepted True if the player accepted the invitation and a game is
     * to be started.
     * @throws UserMessageException 
     */
    public void anwserGameInvitation(boolean accepted) throws UserMessageException {
        Packet request = new Packet(Query.CAnswerGameInvitation, accepted);
        sendOnlyWrapper(request);
    }
    
    /**
     * When placing ships, inform the server that the player has clicked on a
     * square on the grid.
     * @param pos The coordinates of the square on the grid.
     * @throws UserMessageException 
     */
    public void togglePlaceShipOnSquare(Coord pos) throws UserMessageException {
        Packet request = new Packet(Query.CTogglePlaceOnShipSquare, pos);
        sendOnlyWrapper(request);
    }
    
    /**
     * When placing ships, inform the server that the user has finished placing ships
     * and is ready to start the game.
     * @throws UserMessageException 
     */
    public void clickReadyButton() throws UserMessageException {
        Packet request = new Packet(Query.CClickReadyButton);
        sendOnlyWrapper(request);
    }

    /**
     * When playing (after placing ships), inform the server that the player intends
     * to fire a missile on the given coordinates. Requires that it's this
     * client's turn to play
     * @param pos The coordinates of the square on the grid of the opponent
     * where to fire the missile.
     * @throws UserMessageException 
     */
    public void fireShot(Coord pos) throws UserMessageException {
        Packet request = new Packet(Query.CFireShot, pos);
        sendOnlyWrapper(request);
    }
    
    /**
     * Inform the server that the user closed the game, meaning, clicked the
     * close button on the UI.
     * @throws UserMessageException 
     */
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
