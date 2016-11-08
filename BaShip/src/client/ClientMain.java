package client;

import client.conn.Server;
import client.ui.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;
import sharedlib.config.*;
import sharedlib.conn.Connection;
import sharedlib.conn.ConnectionException;

public class ClientMain {

    private ClientMain() {

    }

    public static final ClientMain instance = new ClientMain(); // Singleton
    private static final MainFrame mainFrame = new MainFrame();
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();
    public static final Configuration config = new Configuration("src/client/config.properties");
    public static Server server;

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException, ConnectionException {
        // Run interface
        runOnUI(() -> {
            mainFrame.setVisible(true);
        });
        
        // Test
        connectToServer();
        System.out.println("Test: " + server.stringTest());
        System.out.println("Test: " + server.listTest());
        server.listMapTest();
        server.mapTest();
        server.queryTest();
    }

    /**
     * Run a Runnable on a background thread asynchronously
     *
     * @param r The runnable to run
     */
    public static void runBackground(Runnable r) {
        backgroundExecutor.execute(r);
    }

    /**
     * Run a Runnable on a background thread asynchronously
     *
     * @param r The runnable to run
     * @return
     */
    public static Future runBackground(Callable r) {
        return backgroundExecutor.submit(r);
    }

    /**
     * Run a Runnable object on the interface thread
     *
     * @param r The runnable to run
     */
    public static void runOnUI(Runnable r) {
        java.awt.EventQueue.invokeLater(r);
    }

    /**
     * Create socket and connect to server
     *
     * @throws ConnectionException if cannot connect to server
     */
    public static void connectToServer() throws ConnectionException {
        Socket socket;
        try {
            socket = new Socket(config.getS("server.ip"), config.getI("server.port"));
        }
        catch (IOException ex) {
            throw new ConnectionException(ex);
        }
        
        Connection conn = new Connection(socket);        
        server = new Server(conn);
        conn.start();
    }
}
