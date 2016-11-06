package sharedlib.coms;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author Alex
 */
public class ServerConnection extends Connection {

    public ServerConnection(Socket socket) throws IOException {
        super(socket);
    }

    public void process() throws IOException, ClassNotFoundException {
        System.out.println("ServerConnection process receving...");
        ConnectionObject object = receive();

        System.out.println("Got object: " + object);
        System.out.println("  \\-with command " + object.command);

        if ("usernameavailable".equals(object.command)) {
            ConnectionObject response = new ConnectionObject("");
            response.contents.put("isavaliable", true);
            send(response);
            System.out.println("Sent response" + response);
        }
    }
}
