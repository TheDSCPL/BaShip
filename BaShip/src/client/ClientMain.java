package client;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;
import sharedlib.config.*;

public class ClientMain {

    public static final Configuration config = new Configuration("src/client/config.properties");
    public static ClientConnection connection;

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
        
        // Create socket and connect
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));        
        connection = new ClientConnection(socket);
        connection.start();

        // Test
        System.out.println("Username available? " + connection.usernameAvailable("alex"));
    }

}
