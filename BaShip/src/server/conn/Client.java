package server.conn;

import java.util.stream.Collectors;
import server.ServerMain;
import sharedlib.conn.*;
import sharedlib.conn.packet.ListMapPacket;
import sharedlib.conn.packet.ListPacket;
import sharedlib.conn.packet.MapPacket;
import sharedlib.conn.packet.Packet;
import sharedlib.conn.packet.QueryPacket;
import sharedlib.conn.packet.StringPacket;

public class Client implements Connection.Delegate {

    private final Connection conn;

    public Client(Connection conn) {
        this.conn = conn;
        this.conn.delegate = this;
    }

    @Override
    public Packet handle(Packet packet) {
        Packet response = null;

        if (packet instanceof StringPacket) {
            StringPacket pckt = (StringPacket) packet;
            if (pckt.s.equals("REQUEST")) {
                response = new StringPacket("RESPONSE");
            }
        }

        if (packet instanceof ListPacket) {
            ListPacket pckt = (ListPacket) packet;
            response = new ListPacket(pckt.l.stream().map(String::toUpperCase).collect(Collectors.toList()));
        }

        if (packet instanceof ListMapPacket) {
            ListMapPacket pckt = (ListMapPacket) packet;
            System.out.println(pckt.lm);
        }

        if (packet instanceof QueryPacket) {
            QueryPacket pckt = (QueryPacket) packet;
            System.out.println(pckt.query + " -> " + pckt.info);
        }

        if (packet instanceof MapPacket) {
            MapPacket pckt = (MapPacket) packet;
            System.out.println(pckt.m);
        }

        /*if (packet instanceof QueryPacket) {
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
        }*/
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
