package server.conn;

import sharedlib.exceptions.ConnectionException;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import sharedlib.conn.*;

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
                Socket socket = serverSocket.accept();

                try {
                    Connection conn = new Connection(socket);
                    Client client = new Client(conn);
                    conn.start();                    
                }
                catch (ConnectionException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.WARNING, "Accepted a client socket but could not connect -> ignoring connection", ex);
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Could not start server -> exiting", ex);
            System.exit(-1);
        }

    }
}
