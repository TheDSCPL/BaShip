package server.conn;

import java.sql.SQLException;
import java.util.logging.*;
import server.*;
import server.database.GameDB;
import server.database.UserDB;
import server.logic.GameS;
import server.logic.UserS;
import sharedlib.conn.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.ErrorMessage;
import sharedlib.tuples.GameScreenInfo;
import sharedlib.tuples.GameSearch;
import sharedlib.tuples.Message;
import sharedlib.tuples.UserInfo;
import sharedlib.tuples.UserSearch;

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
                UserS.sendGlobalMessage(this, (String) request.info);
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
        }

        return response;
    }

    public void informAboutGlobalMessage(Message msg) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SReceiveGlobalMessage, msg));
    }

    public void showGameScreen(GameScreenInfo info) throws ConnectionException {
        connection.sendOnly(new Packet(Query.SShowGameScreen, info));
    }

    public void sendGameInvitation() throws ConnectionException {
        connection.sendOnly(new Packet(Query.SReceiveGameInvitation, "")); // TODO: info?
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
        UserS.logout(this);
        synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }

    public Delegate delegate;

    public interface Delegate {

    }
}
