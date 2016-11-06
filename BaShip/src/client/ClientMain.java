package client;

import client.config.Configuration;
import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;

public class ClientMain {

    public static final Configuration config = new Configuration();
    public static ClientConnection connection;

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        System.out.println("Client started");

        // Create socket and connect
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));
        
        System.out.println("Socket created");
        
        connection = new ClientConnection(socket);

        System.out.println("Connection start");

        // Test
        System.out.println("Username available? " + connection.usernameAvailable("alex"));
    }

}
