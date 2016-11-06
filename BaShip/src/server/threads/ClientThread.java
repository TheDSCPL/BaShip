package server.threads;

import java.io.IOException;
import java.net.*;
import server.ServerMain;
import sharedlib.coms.*;

/**
 *
 * @author Alex
 */
public class ClientThread extends Thread {

    public final ServerConnection connection;

    public ClientThread(Socket socket) throws IOException {
        super(String.format("Client socket %s:%d", socket.getInetAddress().getHostName(), socket.getPort()));
        connection = new ServerConnection(socket);

        System.out.println("Client connected from " + connection.address());
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting " + connection.address());

            while (true) {
                System.out.println("ClientThread while true process");
                connection.process();
            }
        }
        catch (Exception ex) {
            ServerMain.exit(ex);
        }
    }

}
