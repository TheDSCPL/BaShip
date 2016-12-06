package client;

import client.conn.*;
import client.other.PrefsKey;
import client.ui.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.*;
import sharedlib.conn.Connection;
import sharedlib.exceptions.ConnectionException;
import sharedlib.utils.Preferences;

public class ClientMain {

    private ClientMain() {

    }
    
    public static Server server;
    public static final MainFrame mainFrame = new MainFrame();
    public static final Preferences prefs = new Preferences(ClientMain.class);
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();

    public static void main(String args[]) {
        runOnUI(() -> {
            mainFrame.changeToPanel(new LoginPanel());
            mainFrame.setVisible(true);
        });
    }

    /**
     * Create a <code>Socket</code> and connect to server using a
     * <code>Connection</code> object.
     *
     * @param disconnectPreviousConnection If false, only connects if a
     * connection is not already present. If true, always creates a new
     * connection, even if a disconnect is first necessary.
     * @return True if connected to server successfully.
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

    /**
     * Called by the <code>server</code> object automatically whenever the
     * server is connected
     *
     * @param address The address of the newly connected server
     */
    public static void connected(String address) {
        System.out.println("Connected to server on " + address);
    }

    /**
     * Called by the <code>Server</code> object automatically whenever the
     * server is disconnected
     *
     * @param address The address of the recently disconnected server
     */
    public static void disconnected(String address) {
        System.out.println("Disconnected from server on " + address);
        server = null; // Remove server
    }

    /**
     * Show a pop-up with an info icon and the specified message.
     *
     * @param message
     */
    public static void showInfo(String message) {
        runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Info", INFORMATION_MESSAGE);
        });
    }

    /**
     * Show a pop-up with a warning icon and the specified message.
     *
     * @param message
     */
    public static void showWarning(String message) {
        runOnUI(() -> {
            JOptionPane.showMessageDialog(ClientMain.mainFrame, message, "Warning", WARNING_MESSAGE);
        });
    }

    /**
     * Show a pop-up with an error icon and the specified message.
     *
     * @param message
     */
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
     * Run a Callable on a background thread asynchronously
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
