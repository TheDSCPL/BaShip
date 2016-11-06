/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;

/**
 *
 * @author Alex
 */
public class Server extends Thread {

    public final int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on " + serverSocket.getInetAddress().getHostName() + ":" + serverSocket.getLocalPort());

            while (true) {
                new Client(serverSocket.accept()).start();
            }
        }
        catch (Exception ex) {
            Main.exit(ex);
        }
    }
}
