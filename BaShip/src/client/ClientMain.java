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
import sharedlib.exceptions.ConnectionException;

public class ClientMain {

    private ClientMain() {

    }

    public static final ClientMain instance = new ClientMain(); // Singleton
    public static final MainFrame mainFrame = new MainFrame();
    public static final Configuration config = new Configuration(ClientMain.class.getResource("config.properties"));
    public static Server server;
    public static User loggedInUser;
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();

    public static void main(String args[]) {
        runOnUI(() -> {
            mainFrame.changeToPanel(new LoginPanel());
            mainFrame.setVisible(true);
        });
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
     * @param disconnectPreviousConnection If false, only connects if a connection is not already present. If true, always creates a new connection, even if a disconnect is first necessary
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
            catch (IOException ignored) {}
        }
        
        try {
            Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));
            Connection conn = new Connection(socket);
            server = new Server(conn);
            conn.start();
            return true;
        }
        catch (ConnectionException | IOException ex) {
            showAlert("Could not connect to server: " + ex.getMessage());
            return false;
        }
    }

}
