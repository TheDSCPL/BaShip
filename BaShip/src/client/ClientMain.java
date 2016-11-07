package client;

import client.conn.ServerConnection;
import client.ui.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import sharedlib.coms.*;
import sharedlib.coms.packet.*;
import sharedlib.config.*;

public class ClientMain implements Connection.Handler {

    private ClientMain() {
        
    }

    public static final ClientMain instance = new ClientMain(); // Singleton
    private static final MainFrame mainFrame = new MainFrame();
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();
    public static final Configuration config = new Configuration("src/client/config.properties");
    public static ServerConnection connection;

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        // Run interface
        runOnUI(() -> {
            mainFrame.setVisible(true);
        });
        
        // Test
        //System.out.println("Username available? " + connection.isUsernameAvailable("alex"));
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
     * @throws java.io.IOException if cannot connect to server
     */
    public void connectToServer() throws IOException {
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));
        connection = new ServerConnection(socket);
        connection.handler = instance;
        connection.start();
    }

    @Override
    public Packet handle(Connection connection, Packet packet) {
        System.out.println("Received packet (not an answer to a request): " + packet);
        return null;
    }

    @Override
    public void connected(Connection connection) {
        System.out.println("Connected to server");
    }

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Disconnected from server");
    }
}
