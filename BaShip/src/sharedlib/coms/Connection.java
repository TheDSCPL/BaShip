package sharedlib.coms;

import java.io.*;
import java.net.*;

public class Connection {

    private final Socket socket;
    private final ObjectInputStream receive;
    private final ObjectOutputStream send;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.receive = new ObjectInputStream(socket.getInputStream());
        this.send = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("New connection with socket: " + address());
    }
    
    public final void send(ConnectionObject object) throws IOException {
        send.writeObject(object);
    }

    public final ConnectionObject receive() throws IOException, ClassNotFoundException {
        return (ConnectionObject) receive.readObject();
    }

    public final String address() {
        return socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }

}
