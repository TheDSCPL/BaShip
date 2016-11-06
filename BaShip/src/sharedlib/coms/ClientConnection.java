package sharedlib.coms;

import java.io.*;
import java.net.*;
import server.*;

public class ClientConnection extends Connection {

    public ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    public void sendChatMessage() {

    }

    public void sendInvitation() {

    }

    @Override
    public void run() {
        System.out.println("Client connected from " + address());
        synchronized(ServerMain.clients) {
            ServerMain.clients.add(this);
        }
        
        super.run();
        
        System.out.println("Client disconnected from " + address());
        synchronized(ServerMain.clients) {
            ServerMain.clients.remove(this);
        }
    }

}
