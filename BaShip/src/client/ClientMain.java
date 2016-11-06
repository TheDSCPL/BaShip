package client;

import client.ui.*;
import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;
import sharedlib.config.*;

public class ClientMain {

    private ClientMain() {
        
    }

    public static final ClientMain instance = new ClientMain(); // Singleton
    private static final MainFrame mainFrame = new MainFrame();

    public static final Configuration config = new Configuration("src/client/config.properties");
    public static ServerConnection connection;

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {

        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainFrame.setVisible(true);
            }
        });*/
 /*java.awt.EventQueue.invokeLater(new Runnable() {
              public void run() {
                  loginPanel.setVisible(true);
              }
        });*/
        // Create socket and connect
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));
        connection = new ServerConnection(socket);
        connection.start();

        // Test
        System.out.println("Username available? " + connection.isUsernameAvailable("alex"));
    }
}
