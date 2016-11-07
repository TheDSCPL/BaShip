package server.conn;

import java.io.*;
import java.net.*;
import sharedlib.coms.Connection;

public class ClientConnection extends Connection {

    public ClientConnection(Socket socket) throws IOException {
        super(socket);
    }

    public void sendChatMessage() {

    }

    public void sendInvitation() {

    }

}
