package server.threads;

import java.net.*;
import server.*;
import sharedlib.coms.*;

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
        catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
