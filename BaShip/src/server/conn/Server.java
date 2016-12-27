package server.conn;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import server.ServerMain;
import static server.ServerMain.prefs;
import server.other.PrefsKey;
import sharedlib.conn.Connection;
import sharedlib.exceptions.ConnectionException;

/**
 * Thread responsible for accepting new clients and creating a new
 * {@code Client} instance for each.
 *
 * @author Alex
 */
public class Server extends Thread {

    private static Server instance;

    public synchronized static void startServer() throws IOException {
        stopServer();
        instance = new Server(prefs.getI(PrefsKey.ServerPort));
        instance.start();
    }

    public synchronized static void stopServer() throws IOException {
        // Close server
        if (instance != null) {
            instance.close();
        }
        
        // Close client connections
        for (Client client : ServerMain.clients) {
            client.disconnect();
        }
    }

    private final ServerSocket serverSocket;
    
    private Server(int port) throws IOException {
        super("Server thread");
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        ServerMain.console.println("Server running on " + serverSocket.getInetAddress().getHostName() + ":" + serverSocket.getLocalPort());
        
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                Connection conn = new Connection(socket);
                Client client = new Client(conn);
                conn.start();
            }
            catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.WARNING, null, ex);
            }
            catch (ConnectionException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.WARNING, "Accepted a client socket but could not connect -> ignoring connection", ex);
            }
        }
    }

    private void close() throws IOException {
        serverSocket.close();
    }

}
