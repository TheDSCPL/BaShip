package client;

import client.ui.*;
import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;
import sharedlib.coms.packet.*;
import sharedlib.config.*;

public class ClientMain implements Connection.Handler {

    private ClientMain() {}
    
    public static final ClientMain instance = new ClientMain(); // Singleton
    private static final MainFrame mainFrame = new MainFrame();
    public static final Configuration config = new Configuration("src/client/config.properties");
    public static ServerConnection connection;

    public static MainFrame getMainFrame() {
        return mainFrame;
    }
    
    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        // Create socket and connect
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));
        connection = new ServerConnection(socket);
        connection.handler = instance;
        connection.start();
        
        // Run interface
        java.awt.EventQueue.invokeLater(() -> {
            mainFrame.setVisible(true);
        });
        
        // Test
        System.out.println("Username available? " + connection.isUsernameAvailable("alex"));
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
