package server.conn;

import java.sql.SQLException;
import java.util.logging.*;
import server.*;
import server.logic.user.*;
import sharedlib.conn.*;
import sharedlib.conn.packet.*;
import sharedlib.exceptions.*;

public class Client implements Connection.Delegate {

    private final Connection conn;

    @SuppressWarnings("LeakingThisInConstructor")
    public Client(Connection conn) {
        this.conn = conn;
        this.conn.delegate = this;
    }

    @Override
    public Packet handle(Packet request) {
        Packet response = null;

        switch (request.query) {
            case UsernameAvailable: {
                StringPacket ip = (StringPacket) request;
                boolean b = false;
                try {
                    b = User.isUsernameAvailable(ip.str);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                response = new BoolPacket(b);
                break;
            }
            case Login: {
                MapPacket ip = (MapPacket) request;
                MapPacket op = new MapPacket();
                try {
                    User u = User.login(this, ip.map.get("username"), ip.map.get("password"));
                    op.map.put("id", String.valueOf(u.id));
                    op.map.put("username", u.username);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    op.map.put("error", "Could not run SQL query: " + ex.getMessage());
                }
                catch (UserMessageException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.INFO, null, ex);
                    op.map.put("error", ex.getMessage());
                }
                response = op;
                break;
            }
            case Register: {
                MapPacket ip = (MapPacket) request;
                MapPacket op = new MapPacket();
                try {
                    User u = User.register(this, ip.map.get("username"), ip.map.get("password"));
                    op.map.put("id", String.valueOf(u.id));
                    op.map.put("username", u.username);
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    op.map.put("error", "Could not run SQL query: " + ex.getMessage());
                }
                response = op;
                break;
            }
            case Logout: {
                User.logout(this);
                response = new Packet(); // Empty packet just for confirmation
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
        User.logout(this);
        synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }

    public Delegate delegate;

    public interface Delegate {

    }
}
