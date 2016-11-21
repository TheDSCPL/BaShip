package server.conn;

import java.sql.SQLException;
import java.util.logging.*;
import server.*;
import server.database.GameDB;
import server.database.UserDB;
import server.logic.user.*;
import sharedlib.conn.*;
import sharedlib.exceptions.*;
import sharedlib.tuples.ErrorMessage;
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
            case UsernameAvailable: {
                boolean b = false;
                try {
                    b = UserS.isUsernameAvailable((String) request.info);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                response = new Packet(b);
                break;
            }
            case Login: {
                UserInfo um = (UserInfo) request.info;
                Object info;
                try {
                    info = UserS.login(this, um.username, um.passwordHash);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    info = new ErrorMessage("Could not run SQL query: " + ex.getMessage());
                }
                catch (UserMessageException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.INFO, null, ex);
                    info = new ErrorMessage(ex.getMessage());
                }
                response = new Packet(info);
                break;
            }
            case Register: {
                UserInfo um = (UserInfo) request.info;
                Object info;
                try {
                    info = UserS.register(this, um.username, um.passwordHash);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    info = new ErrorMessage("Could not run SQL query: " + ex.getMessage());
                }
                response = new Packet(info);
                break;
            }
            case Logout: {
                UserS.logout(this);
                response = new Packet(); // Empty packet just for confirmation
                break;
            }
            case GetUserList: {
                UserSearch s = (UserSearch) request.info;
                Object info;

                try {
                    info = UserDB.getUserList(s);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    info = new ErrorMessage("Could not run SQL query: " + ex.getMessage());
                }
                response = new Packet(info);
                break;
            }
            case GetGameList: {
                GameSearch s = (GameSearch) request.info;
                Object info;

                try {
                    info = GameDB.getGameList(s);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    info = new ErrorMessage("Could not run SQL query: " + ex.getMessage());
                }
                response = new Packet(info);
                break;
            }
            case SendGlobalMessage: {                
                UserS.sendGlobalMessage(this, (String)request.info);
                break;
            }
        }

        return response;
    }
    
    public void informAboutGlobalMessage(Message msg) throws ConnectionException {
        connection.sendOnly(new Packet(Query.ReceiveGlobalMessage, msg));
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
