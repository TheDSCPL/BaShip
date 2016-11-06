package sharedlib.coms;

import java.io.IOException;
import java.net.*;

public class ClientConnection extends Connection {

    public ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    public boolean usernameAvailable(String username) throws IOException, ClassNotFoundException {
        System.out.println("usernameAvailable entry");
        ConnectionObject query = new ConnectionObject("usernameavailable");
        query.contents.put("username", username);
        send(query);
        System.out.println("usernameAvailable sent");

        ConnectionObject response = receive();
        System.out.println("Got response: " + response);
        return (Boolean) response.contents.get("isavaliable");
    }

}
