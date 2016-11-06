package client;

import client.config.Configuration;
import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;
import client.ui.*;
import javax.swing.*;

public class ClientMain {

    public static final Configuration config = new Configuration();
    public static ClientConnection connection;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
        
        /*java.awt.EventQueue.invokeLater(new Runnable() {
              public void run() {
                  loginPanel.setVisible(true);
              }
        });*/
        
        // Create socket and connect
        /*Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));        
        connection = new ClientConnection(socket);

        // Test
        System.out.println("Username available? " + connection.usernameAvailable("alex"));*/
        while(true);
    }

}
