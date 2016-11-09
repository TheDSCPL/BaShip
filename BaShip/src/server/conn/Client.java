package server.conn;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.*;
import server.logic.user.User;
import sharedlib.conn.*;
import sharedlib.conn.packet.*;

public class Client implements Connection.Delegate {

    private final Connection conn;

    public Client(Connection conn) {
        this.conn = conn;
        this.conn.delegate = this;
    }

    @Override
    public Packet handle(Packet request) {
        Packet response = null;

        switch (request.query) {
            case Login: {
                MapPacket ip = (MapPacket) request;
                MapPacket op = new MapPacket();
                try {
                    User u = User.login(ip.map.get("username"), ip.map.get("password"));
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
            case Register: {
                MapPacket ip = (MapPacket) request;
                MapPacket op = new MapPacket();
                try {
                    User u = User.register(ip.map.get("username"), ip.map.get("password"));
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
        synchronized (ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }

    public Delegate delegate;

    public interface Delegate {

        public void receiveGameMessage();

        public void receiveGlobalMessage();
        // ...
    }

}
