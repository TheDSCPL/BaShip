/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;

/**
 *
 * @author Alex
 */
public class Client extends Thread {

    private final Socket clientSocket;

    public Client(Socket socket) {
        super(String.format("Client socket %s:%d", socket.getInetAddress().getHostName(), socket.getPort()));
        this.clientSocket = socket;
        
        System.out.println("Client connected from " + clientAddr());
    }
    
    private String clientAddr() {
        return clientSocket.getInetAddress().getHostName() + ":" + clientSocket.getPort();
    }

    @Override
    public void run() {

        try (
                BufferedReader receiveStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter sendWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            )
        {
            
            String fromClient, toClient;
            while ((fromClient = receiveStream.readLine()) != null) {
                System.out.println("Received from " + clientAddr() + " -> " + fromClient);
                
                toClient = fromClient.toUpperCase(); // <<<---- processing here
                
                sendWriter.println(toClient);
            }
            
            clientSocket.close();
        }
        catch (Exception ex) {
            Main.exit(ex);
        }
    }

}
