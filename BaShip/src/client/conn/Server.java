package client.conn;

import client.ClientMain;
import client.logic.*;
import sharedlib.Crypto;
import sharedlib.conn.*;
import sharedlib.conn.packet.*;
import sharedlib.exceptions.*;

public class Server implements Connection.Delegate {

    private final Connection conn;

    @SuppressWarnings("LeakingThisInConstructor")
    public Server(Connection conn) {
        this.conn = conn;
        this.conn.delegate = this;
    }

    @Override
    public Packet handle(Packet packet) {
        return null;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to server on " + connection.address());
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnected from server on " + connection.address());

    }

    public Delegate delegate;

    public interface Delegate {

        public void receiveGameMessage();

        public void receiveGlobalMessage();
    }

    public boolean getUsernameAvailable(String username) throws UserMessageException {
        StringPacket request = new StringPacket(username);
        request.query = Query.UsernameAvailable;

        BoolPacket response;
        try {
            response = (BoolPacket) conn.sendAndReceive(request);
        }
        catch (ConnectionException ex) {
            throw new UserMessageException("Could not connect to server: " + ex.getMessage());
        }

        return response.b;
    }

    public User doLogin(String username, char[] password) throws UserMessageException {
        MapPacket request = new MapPacket();
        request.query = Query.Login;
        request.map.put("username", username);
        request.map.put("password", Crypto.SHA1(password));

        MapPacket response;
        try {
            response = (MapPacket) conn.sendAndReceive(request);
        }
        catch (ConnectionException ex) {
            throw new UserMessageException("Could not connect to server: " + ex.getMessage());
        }

        if (response.map.containsKey("error")) {
            throw new UserMessageException("Could not login: " + response.map.get("error"));
        }
        else {
            return new User(
                    Long.parseLong(response.map.get("id")),
                    response.map.get("username")
            );
        }
    }

    public User doRegister(String username, char[] password) throws UserMessageException {
        MapPacket request = new MapPacket();
        request.query = Query.Register;
        request.map.put("username", username);
        request.map.put("password", Crypto.SHA1(password));

        MapPacket response;
        try {
            response = (MapPacket) conn.sendAndReceive(request);
        }
        catch (ConnectionException ex) {
            throw new UserMessageException("Could not connect to server: " + ex.getMessage());
        }

        if (response.map.containsKey("error")) {
            throw new UserMessageException("Could not register: " + response.map.get("error"));
        }
        else {
            return new User(
                    Long.parseLong(response.map.get("id")),
                    response.map.get("username")
            );
        }
    }
    
    public void doLogout() throws UserMessageException {
        Packet request = new Packet();
        request.query = Query.Logout;

        try {
            conn.sendAndReceive(request); // Response is an empty packet, just for confirmation
        }
        catch (ConnectionException ex) {
            throw new UserMessageException("Could not connect to server: " + ex.getMessage());
        }
    }
}
