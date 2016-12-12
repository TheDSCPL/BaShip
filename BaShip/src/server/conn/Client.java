package server.conn;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServerMain;
import server.database.GameDB;
import server.database.UserDB;
import server.logic.GlobalChatS;
import server.logic.UserS;
import server.logic.game.GameS;
import sharedlib.conn.Connection;
import sharedlib.conn.Packet;
import sharedlib.conn.Query;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.UserMessageException;
import sharedlib.structs.BoardUIInfo;
import sharedlib.structs.ErrorMessage;
import sharedlib.structs.GameSearch;
import sharedlib.structs.GameUIInfo;
import sharedlib.structs.Message;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;
import sharedlib.utils.Coord;

/**
 * Wrapper around a Connection object that implements the Server-Client protocol
 * for the server side. Methods called on this class send the corresponding
 * <code>Packet</code> objects to the client, and wait for a response
 * <code>Packet</code> if necessary.
 *
 * @author Alex
 */
public class Client implements Connection.Delegate {

    private final Connection connection;

    @SuppressWarnings("LeakingThisInConstructor")
    public Client(Connection conn) {
        this.connection = conn;
        this.connection.delegate = this;
    }

    @Override
    public Packet handle(Packet request) {
        Packet response = null;

        switch (request.query) {
            case C_UsernameAvailable: {
                boolean b = false;
                try {
                    b = UserDB.isUsernameAvailable((String) request.info);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                response = new Packet(Query.SR_UsernameAvailable, b);
                break;
            }
            case B_Login: {
                UserInfo um = (UserInfo) request.info;
                try {
                    response = new Packet(Query.B_Login, UserS.login(this, um.username, um.passwordHash));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                catch (UserMessageException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.INFO, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage(ex.getMessage()));
                }
                break;
            }
            case B_Register: {
                UserInfo um = (UserInfo) request.info;
                try {
                    response = new Packet(Query.B_Register, UserS.register(this, um.username, um.passwordHash));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                break;
            }
            case C_Logout: {
                UserS.logout(this);
                response = new Packet(); // Empty packet just for confirmation
                break;
            }
            case C_GetUserList: {
                UserSearch s = (UserSearch) request.info;

                try {
                    response = new Packet(Query.SR_GetUserList, UserDB.getUserList(s));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }

                break;
            }
            case C_GetGameList: {
                GameSearch s = (GameSearch) request.info;

                try {
                    response = new Packet(Query.SR_GetGameList, GameDB.getGameList(s));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }

                break;
            }
            case C_SendGlobalMessage: {
                try {
                    GlobalChatS.sendGlobalMessage(this, (String) request.info);
                    response = new Packet();
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                break;
            }
            case C_SendGameMessage: {
                try {
                    GameS.Actions.sendGameMessage(this, (String) request.info);
                    response = new Packet();
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage("Connection problem: " + ex.getMessage()));
                }
                break;
            }
            case C_StartRandomGame: {
                try {
                    GameS.Actions.startRandomGame(this);
                    response = new Packet();
                }
                catch (UserMessageException ex) {
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage(ex.getMessage()));
                }
                break;
            }
            case C_ClickLeftBoard: {
                GameS.Actions.clientClickedLeftBoard(this, (Coord) request.info);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_ClickRightBoard: {
                GameS.Actions.clientClickedRightBoard(this, (Coord) request.info);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_ClickReadyButton: {
                GameS.Actions.clickReadyButton(this);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_CloseGame: {
                GameS.Actions.clientClosedGame(this);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_DoubleClickGame: {
                GameS.Actions.clientDoubleClickedGame(this, (Long) request.info);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_ShowNextMove: {
                GameS.Actions.showNextMove(this);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_ShowPreviousMove: {
                GameS.Actions.showPreviousMove(this);
                response = new Packet(); // Empty response just for confirmation
                break;
            }
            case C_DoubleClickUser: {
                try {
                    GameS.Actions.clientDoubleClickedUser(this, (Long) request.info);
                    response = new Packet();
                }
                catch (UserMessageException ex) {
                    response = new Packet(Query.SR_ErrorMessage, new ErrorMessage(ex.getMessage()));
                }
                break;
            }
            case C_AnswerGameInvitation: {
                try {
                    GameS.Actions.answerGameInvitation(this, (Boolean) request.info);
                }
                catch (UserMessageException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

        return response;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to client on " + connection.address());
        synchronized (ServerMain.clients) {
            ServerMain.clients.add(this);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnected from client on " + connection.address());

        // It's important that GameS be called first
        GameS.Actions.clientDisconnected(this);
        UserS.clientDisconnected(this);

        synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }

    /**
     * Inform the client about a global message that has been sent by a user.
     * The client should then update the UI accordingly.
     *
     * @param msg The Message object.
     * @throws ConnectionException
     */
    public void informAboutGlobalMessage(Message msg) throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_ReceiveGlobalMessage, msg));
    }

    public void informAboutGameMessage(Message msg) throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_ReceiveGameMessage, msg));
    }

    public void clearGameMessages() throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_ClearGameMessages));
    }

    /**
     * Tell the client to update the game UI using the given information.
     *
     * @param info The tuple that contains all the information needed to update
     * the UI.
     * @throws ConnectionException
     */
    public void updateGameScreen(GameUIInfo info) throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_UpdateGameScreen, info));
    }

    /*
     * Inform the client that he has received an invitation from another player.
     * Note: API not yet finished.
     *
     * @param usernameOfUserInvitingPlayer
     * @throws ConnectionException
     */
    public void sendGameInvitation(String usernameOfUserInvitingPlayer) throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_ReceiveGameInvitation, usernameOfUserInvitingPlayer));
    }

    /**
     * Tell the client to update the UI of one of the boards of the game using
     * the given information.
     *
     * @param info The tuple that contains all the information needed to update
     * the UI.
     * @throws ConnectionException
     */
    public void updateGameBoard(BoardUIInfo info) throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_UpdateGameBoard, info));
    }
    
    public void showMessageAndCloseGame(String message) throws ConnectionException {
        connection.sendOnly(new Packet(Query.S_ShowMessageAndCloseGame, message));
    }

}
