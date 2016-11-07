package server.conn;

import java.util.logging.*;
import server.*;
import sharedlib.conn.*;
import sharedlib.conn.packet.*;

public class ClientHandler implements Connection.Handler {

    @Override
    public Packet handle(Connection connection, Packet packet) {
        Packet response = null;

        if (packet instanceof QueryPacket) {
            QueryPacket qpckt = (QueryPacket) packet;

            switch (qpckt.query) {
                case "UsernameAvailable":
                    response = new BoolPacket(isUsernameAvailable((String) qpckt.m.get("username")));
                    break;
                default:
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Unknown query ''{0}'' -> ignoring", qpckt.query);
            }

            if (response != null) {
                response.request = packet;
            }
        }

        return response;
    }

    private boolean isUsernameAvailable(String username) {
        return true;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Client connected from " + connection.address());
        synchronized (ServerMain.clients) {
            ServerMain.clients.add((ClientConnection) connection);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Client disconnected from " + connection.address());
        synchronized (ServerMain.clients) {
            ServerMain.clients.remove((ClientConnection) connection);
        }
    }

}
