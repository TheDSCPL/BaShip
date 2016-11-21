package server.conn;

import sharedlib.conn.Packet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.*;
import static java.util.stream.Collectors.toList;
import server.*;
import server.logic.user.*;
import sharedlib.UserM;
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
                    b = UserS.isUsernameAvailable(ip.str);
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
                    UserM u = UserS.login(this, ip.map.get("username"), ip.map.get("password"));
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
                    UserM u = UserS.register(this, ip.map.get("username"), ip.map.get("password"));
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
                UserS.logout(this);
                response = new Packet(); // Empty packet just for confirmation
                break;
            }
            case GetUserList: {
                MapPacket ip = (MapPacket) request;
                ListMapPacket op = new ListMapPacket();
                try {
                    List<UserM> ul = UserS.getUserList(Boolean.parseBoolean(ip.map.get("onlineOnly")),
                                                         ip.map.get("usernameFilter"),
                                                         Integer.parseInt(ip.map.get("orderByColumn")),
                                                         Integer.parseInt(ip.map.get("rowLimit")));
                    
                    op.listmap = ul.stream().map(u -> u.getMap()).collect(toList());
                }
                catch (SQLException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    //op.map.put("error", "Could not run SQL query: " + ex.getMessage());
                    // TODO: errors on ListMapPacket??
                }
                response = op;
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
        UserS.logout(this);
        synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }

    public Delegate delegate;

    public interface Delegate {

    }
}
