package server.threads;

import java.net.*;
import sharedlib.coms.*;

/**
 *
 * @author Alex
 */
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
                ServerConnection sc = new ServerConnection(serverSocket.accept());
                System.out.println("Client connected from " + sc.address());
                sc.start();
            }
        }
        catch (Throwable ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
