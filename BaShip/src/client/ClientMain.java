package client;

import client.config.Configuration;
import java.io.IOException;
import java.net.*;
import sharedlib.coms.*;

public class ClientMain {

    public static final Configuration config = new Configuration();
    public static ClientConnection connection;

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        
        // Create socket and connect
        Socket socket = new Socket(config.getS("server.ip"), config.getI("server.port"));        
        connection = new ClientConnection(socket);

        // Test
        System.out.println("Username available? " + connection.usernameAvailable("alex"));
    }

}
