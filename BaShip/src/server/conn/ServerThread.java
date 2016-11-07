package server.conn;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread {

    public final int port;

    public ServerThread(int port) {
        super("Server thread");
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on " + serverSocket.getInetAddress().getHostName() + ":" + serverSocket.getLocalPort());

            while (true) {
                ClientConnection clientConn = new ClientConnection(serverSocket.accept());
                clientConn.handler = new ClientHandler();
                clientConn.start();
            }
        }
        catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not start server -> exiting", ex);
            System.exit(-1);
        }
        
    }
}
