package client;

import client.conn.*;
import client.logic.*;
import client.ui.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.*;
import sharedlib.config.Preferences;
import sharedlib.conn.Connection;
import sharedlib.exceptions.ConnectionException;

public class ClientMain {

    private ClientMain() {

    }

    public static final ClientMain instance = new ClientMain(); // Singleton
    public static final MainFrame mainFrame = new MainFrame();
    public static final Preferences prefs = new Preferences(ClientMain.class);
    public static Server server;
    public static User loggedInUser;
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();
    
    public static void main(String args[]) {
        runOnUI(() -> {
            mainFrame.changeToPanel(new LoginPanel());
            mainFrame.setVisible(true);
        });
    }

    /**
     * Create socket and connect to server
     *
     * @param disconnectPreviousConnection If false, only connects if a
     * connection is not already present. If true, always creates a new
     * connection, even if a disconnect is first necessary
     * @return true if connected to server successfully
     */
    public static boolean connectToServer(boolean disconnectPreviousConnection) {
        if (server != null && !disconnectPreviousConnection) {
            return true;
        }

        if (server != null) {
            try {
                server.disconnect();
            }
            catch (IOException ignored) {
            }
        }

        try {
            server = new Server(new Connection(new Socket(prefs.getS(PrefsKey.ServerIP), prefs.getI(PrefsKey.ServerPort))));
            server.connect();
            return true;
        }
        catch (ConnectionException | IOException ex) {
            showWarning("Could not connect to server: " + ex.getMessage());
            return false;
        }
    }

    public static void connected(String address) {
        System.out.println("Connected to server on " + address);
    }
    
    public static void disconnected(String address) {
        System.out.println("Disconnected from server on " + address);
        server = null; // Remove server
    }

    public static void showInfo(String message) {
        runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Info", INFORMATION_MESSAGE);
        });
    }
    
    public static void showWarning(String message) {
        runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Warning", WARNING_MESSAGE);
        });
    }
    
    public static void showError(String message) {
        runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Error", ERROR_MESSAGE);
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
}
