package client;

import client.conn.*;
import client.logic.*;
import client.ui.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.swing.*;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import sharedlib.config.*;
import sharedlib.conn.*;
import sharedlib.exceptions.*;

public class ClientMain {

    private ClientMain() {

    }

    public static final ClientMain instance = new ClientMain(); // Singleton
    public static final MainFrame mainFrame = new MainFrame();
    public static final Configuration config = new Configuration(ClientMain.class.getResourceAsStream("config.properties"));
    public static Server server;
    public static User loggedInUser;
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();

    public static void main(String args[]) {
        // Run interface
        runOnUI(() -> {
            mainFrame.changeToPanel(new LoginPanel());
            mainFrame.setVisible(true);
        });

        // Connect to server
        try {
            connectToServer();
        }
        catch (ConnectionException ex) {
            showAlert("Could not connect to server: " + ex.getMessage());
        }
    }

    public static void showAlert(String message) {
        runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Problem ocurred", ERROR_MESSAGE);
        });
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
    private static void connectToServer() throws ConnectionException {
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
