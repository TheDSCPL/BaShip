package client.conn;

import client.ClientMain;
import client.logic.*;
import java.io.*;
import java.util.*;
import sharedlib.conn.Connection;
import sharedlib.conn.Packet;
import sharedlib.conn.Query;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.ErrorMessage;
import sharedlib.structs.GameInfo;
import sharedlib.structs.GameSearch;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.Message;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;
import sharedlib.utils.Coord;
import sharedlib.utils.Crypto;

/**
 * Wrapper around a Connection object that implements the Server-Client protocol
 * for the client side. Methods called on this class send the corresponding
 * <code>Packet</code> objects to the server, and wait for a response
 * <code>Packet</code> if necessary.
 *
 * @author Alex
 */
public class Server implements Connection.Delegate {

    private final Connection connection;

    /**
     * Create a new Server based on the given connection object.
     *
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
            case S_ReceiveGameMessage: {
                GameChatC.receiveGameMessage((Message) request.info);
                break;
            }
            case S_ReceiveGlobalMessage: {
                GlobalChatC.receiveGlobalMessage((Message) request.info);
                break;
            }
            case S_UpdateGameScreen: {
                GameC.updateGameScreen((GameUIInfo) request.info);
                break;
            }
            case S_ClearGameMessages: {
                GameChatC.clearGameMessages();
                break;
            }
            case S_ReceiveGameInvitation: {
                GameC.showGameInvitation((String) request.info);
                break;
            }
            case S_UpdateGameBoard: {
                GameC.updateBoardInfo((BoardUIInfo) request.info);
                break;
            }
            case S_ShowMessageAndCloseGame: {
                GameC.showMessageAndCloseGame((String) request.info);
                break;
            }
            case S_CloseGameInvitation: {
                GameC.closeGameInvitation();
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
     *
     * @throws IOException if disconnect was unsuccessful
     */
    public void disconnect() throws IOException {
        connection.disconnect();
    }

    /**
     * Login using the specified username and password
     *
     * @param username
     * @param password
     * @return A <code>UserInfo</code> object with the fields <code>id</code>
     * and <code>username</code> non-null
     * @throws UserMessageException
     */
    public UserInfo login(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.B_Login, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    /**
     * Register a new user on the server.
     *
     * @param username The username of the new user. Cannot be null.
     * @param password The password of the new user.
     * @return A <code>UserInfo</code> object with the fields <code>id</code>
     * and <code>username</code> non-null
     * @throws UserMessageException
     */
    public UserInfo register(String username, char[] password) throws UserMessageException {
        Packet request = new Packet(Query.B_Register, new UserInfo(username, Crypto.SHA1(password)));
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
        else {
            return (UserInfo) response.info;
        }
    }

    /**
     * Logout the current logged-in user for this client.
     *
     * @throws UserMessageException
     */
    public void logout() throws UserMessageException {
        Packet request = new Packet(Query.C_Logout);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    /**
     * Get a filtered list of all users registered on the server. This list may
     * include all players, online and/or offline.
     *
     * @param us The filter parameters.
     * @return A list of <code>UserInfo</code> objects with all its fields
     * non-null.
     * @throws UserMessageException
     */
    public List<UserInfo> getUserList(UserSearch us) throws UserMessageException {
        Packet request = new Packet(Query.C_GetUserList, us);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
        else {
            return (List<UserInfo>) response.info;
        }
    }

    /**
     * Get a filtered list of all games played on the server. This list may
     * include all games finished and/or currently being played.
     *
     * @param gs The filter parameters.
     * @return A list of <code>GameInfo</code> objects with all its fields
     * non-null.
     * @throws UserMessageException
     */
    public List<GameInfo> getGameList(GameSearch gs) throws UserMessageException {
        Packet request = new Packet(Query.C_GetGameList, gs);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
        else {
            return (List<GameInfo>) response.info;
        }
    }

    /**
     * Send a text message to all logged-in users on the server. Requires that
     * this client be logged in on the server.
     *
     * @param message The text message
     * @throws UserMessageException
     */
    public void sendGlobalMessage(String message) throws UserMessageException {
        Packet request = new Packet(Query.C_SendGlobalMessage, message);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
    }

    public void sendGameMessage(String message) throws UserMessageException {
        Packet request = new Packet(Query.C_SendGameMessage, message);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
    }

    public void doubleClickGame(Long gameID) throws UserMessageException {
        Packet request = new Packet(Query.C_DoubleClickGame, gameID);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
    }

    public void doubleClickUser(Long playerID) throws UserMessageException {
        Packet request = new Packet(Query.C_DoubleClickUser, playerID);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
    }

    /**
     * Ask the server to start a game with another random player. If the server
     * decides to start a game, it will later (not as a response to this packet)
     * send a S_UpdateGameScreen packet to this client that will be handled by
     * this class separately.
     *
     * @throws UserMessageException
     */
    public void startRandomGame() throws UserMessageException {
        Packet request = new Packet(Query.C_StartRandomGame);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
    }

    /**
     * Inform the server if the player accepted the invitation or not. This
     * should be called after receiving an invitation from another player.
     *
     * @param accepted True if the player accepted the invitation and a game is
     * to be started.
     * @throws UserMessageException
     */
    public void anwserGameInvitation(boolean accepted) throws UserMessageException {
        Packet request = new Packet(Query.C_AnswerGameInvitation, accepted);

        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
    }

    /**
     * Inform the server that this user clicked on the left board on a specific
     * position.
     *
     * @param pos The coordinates of the square on the grid.
     * @throws UserMessageException
     */
    public void clickLeftBoard(Coord pos) throws UserMessageException {
        Packet request = new Packet(Query.C_ClickLeftBoard, pos);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    /**
     * Inform the server that this user clicked on the right board on a specific
     * position.
     *
     * @param pos The coordinates of the square on the grid.
     * @throws UserMessageException
     */
    public void clickRightBoard(Coord pos) throws UserMessageException {
        Packet request = new Packet(Query.C_ClickRightBoard, pos);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    /**
     * When placing ships, inform the server that the user has finished placing
     * ships and is ready to start the game.
     *
     * @throws UserMessageException
     */
    public void clickReadyButton() throws UserMessageException {
        Packet request = new Packet(Query.C_ClickReadyButton);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    /**
     * Inform the server that the user closed the game, meaning, clicked the
     * close button on the UI.
     *
     * @throws UserMessageException
     */
    public void closeGame() throws UserMessageException {
        Packet request = new Packet(Query.C_CloseGame);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    public void showPreviousMove() throws UserMessageException {
        Packet request = new Packet(Query.C_ShowPreviousMove);
        sendAndReceiveWrapper(request); // Response is an empty packet, just for confirmation
    }

    public void showNextMove() throws UserMessageException {
        Packet request = new Packet(Query.C_ShowNextMove);
        Packet response = sendAndReceiveWrapper(request);

        if (response.query == Query.SR_ErrorMessage) {
            throw new UserMessageException(((ErrorMessage) response.info).message);
        }
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
