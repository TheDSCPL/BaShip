package client;

import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;
import sharedlib.config.*;

public class ClientMain {

    public static final Configuration config = new Configuration("src/client/config.properties");
    public static ClientConnection connection;

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {
        
        /*LoginPanel loginPanel = new LoginPanel();
        JFrame frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(loginPanel);
        frame.pack();
        frame.setVisible(true);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
              public void run() {
                  loginPanel.setVisible(true);
              }
        });*/
        
        // Create socket and connect
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));        
        connection = new ClientConnection(socket);
        connection.start();

        // Test
        System.out.println("Username available? " + connection.usernameAvailable("alex"));
    }

}
