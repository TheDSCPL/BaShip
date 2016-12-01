package pt.up.fe.lpro1613.server.conn;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.up.fe.lpro1613.server.ServerMain;
import pt.up.fe.lpro1613.server.database.GameDB;
import pt.up.fe.lpro1613.server.database.UserDB;
import pt.up.fe.lpro1613.server.logic.GlobalChatS;
import pt.up.fe.lpro1613.server.logic.UserS;
import pt.up.fe.lpro1613.server.logic.game.GameS;
import pt.up.fe.lpro1613.sharedlib.conn.Connection;
import pt.up.fe.lpro1613.sharedlib.conn.Packet;
import pt.up.fe.lpro1613.sharedlib.conn.Query;
import pt.up.fe.lpro1613.sharedlib.exceptions.ConnectionException;
import pt.up.fe.lpro1613.sharedlib.exceptions.UserMessageException;
import pt.up.fe.lpro1613.sharedlib.structs.BoardUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.ErrorMessage;
import pt.up.fe.lpro1613.sharedlib.structs.GameSearch;
import pt.up.fe.lpro1613.sharedlib.structs.GameUIInfo;
import pt.up.fe.lpro1613.sharedlib.structs.Message;
import pt.up.fe.lpro1613.sharedlib.structs.UserInfo;
import pt.up.fe.lpro1613.sharedlib.structs.UserSearch;
import pt.up.fe.lpro1613.sharedlib.utils.Coord;

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
            case CUsernameAvailable: {
                boolean b = false;
                try {
                    b = UserS.isUsernameAvailable((String) request.info);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                response = new Packet(Query.SRUsernameAvailable, b);
                break;
            }
            case BLogin: {
                UserInfo um = (UserInfo) request.info;
                try {
                    response = new Packet(Query.BLogin, UserS.login(this, um.username, um.passwordHash));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                catch (UserMessageException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.INFO, null, ex);
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage(ex.getMessage()));
                }
                break;
            }
            case BRegister: {
                UserInfo um = (UserInfo) request.info;
                try {
                    response = new Packet(Query.BRegister, UserS.register(this, um.username, um.passwordHash));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                break;
            }
            case CLogout: {
                UserS.logout(this);
                response = new Packet(); // Empty packet just for confirmation
                break;
            }
            case CGetUserList: {
                UserSearch s = (UserSearch) request.info;

                try {
                    response = new Packet(Query.SRGetUserList, UserDB.getUserList(s));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }

                break;
            }
            case CGetGameList: {
                GameSearch s = (GameSearch) request.info;

                try {
                    response = new Packet(Query.SRGetGameList, GameDB.getGameList(s));
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }

                break;
            }
            case CSendGlobalMessage: {
                try {
                    GlobalChatS.sendGlobalMessage(this, (String) request.info);
                    response = new Packet();
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage("Could not run SQL query: " + ex.getMessage()));
                }
                break;
            }
            case CStartRandomGame: {
                try {
                    GameS.startRandomGame(this);
                    response = new Packet();
                }
                catch (UserMessageException ex) {
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage(ex.getMessage()));
                }
                break;
            }
            case CStartGameWithPlayer: {
                try {
                    GameS.startGameWithPlayer(this, (Long) request.info);
                    response = new Packet();
                }
                catch (UserMessageException ex) {
                    response = new Packet(Query.SRErrorMessage, new ErrorMessage(ex.getMessage()));
                }
                break;
            }

            // TODO: Empty responses just for confirmation?
            case CAnswerGameInvitation: {
                GameS.answerGameInvitation(this, (Boolean) request.info);
                break;
            }
            case CTogglePlaceOnShipSquare: {
                GameS.togglePlaceShipOnSquare(this, (Coord) request.info);
                break;
            }
            case CClickReadyButton: {
                GameS.clickReadyButton(this);
                break;
            }
            case CFireShot: {
                GameS.fireShot(this, (Coord) request.info);
                break;
            }
            case CCloseGame: {
                GameS.closeGame(this);
                break;
            }
        }

        return response;
    }

    /**
     * Inform the client about a global message that has been sent by a user.
     * The client should then update the UI accordingly.
     *
     * @param msg The Message object.
     * @throws ConnectionException
     */
    public void informAboutGlobalMessage(Message msg) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SReceiveGlobalMessage, msg));
    }

    /**
     * Tell the client to update the game UI using the given information.
     *
     * @param info The tuple that contains all the information needed to update
     * the UI.
     * @throws ConnectionException
     */
    public void updateGameScreen(GameUIInfo info) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SUpdateGameScreen, info));
    }

    /**
     * Inform the client that he has received an invitation from another player.
     * Note: API not yet finished.
     *
     * @throws ConnectionException
     */
    public void sendGameInvitation() throws ConnectionException {
        connection.sendOnly(new Packet(Query.SReceiveGameInvitation, "")); // TODO: info?
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
        connection.sendOnly(new Packet(Query.SUpdateGameBoard, info));
    }

    /**
     * Tell the client that the game he's playing has finished.
     *
     * @param message The text to display to the player.
     * @throws ConnectionException
     */
    public void gameFinished(String message) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SGameFinished, message));
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
        UserS.clientDisconnected(this);
        GameS.clientDisconnected(this);
        synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }
}
