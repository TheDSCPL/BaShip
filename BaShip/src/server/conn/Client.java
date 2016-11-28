package server.conn;

import java.sql.SQLException;
import java.util.logging.*;
import server.*;
import server.database.*;
import server.logic.*;
import sharedlib.conn.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.*;
import sharedlib.utils.*;

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
            case CLogin: {
                UserInfo um = (UserInfo) request.info;
                try {
                    response = new Packet(Query.CLogin, UserS.login(this, um.username, um.passwordHash));
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
            case CRegister: {
                UserInfo um = (UserInfo) request.info;
                try {
                    response = new Packet(Query.CRegister, UserS.register(this, um.username, um.passwordHash));
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
                    UserS.sendGlobalMessage(this, (String) request.info);
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
        }

        return response;
    }

    public void informAboutGlobalMessage(Message msg) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SReceiveGlobalMessage, msg));
    }

    public void updateGameScreen(GameScreenInfo info) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SUpdateGameScreen, info));
    }

    public void sendGameInvitation() throws ConnectionException {
        connection.sendOnly(new Packet(Query.SReceiveGameInvitation, "")); // TODO: info?
    }

    public void updateGameBoard(BoardInfo info) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SUpdateGameBoard, info));
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
