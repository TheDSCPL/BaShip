package server.conn;

import server.ServerMain;
import server.conn.ClientConnection;
import sharedlib.coms.*;
import sharedlib.coms.packet.*;

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
                    System.out.println("server.ClientHandler.handle() unknown query: " + qpckt.query);
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
