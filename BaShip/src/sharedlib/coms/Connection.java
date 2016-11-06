package sharedlib.coms;

import java.io.*;
import java.net.*;

public class Connection {

    private final Socket socket;
    private final ObjectInputStream receive;
    private final ObjectOutputStream send;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        
        // Note: this order is important. First create OutputStream, then create InputStream!
        send = new ObjectOutputStream(socket.getOutputStream());
        receive = new ObjectInputStream(socket.getInputStream());
        
        System.out.println("New connection with socket: " + address());
    }
    
    protected final void send(ConnectionObject object) throws IOException {
        send.writeObject(object);
    }

    protected final ConnectionObject receive() throws IOException, ClassNotFoundException {
        return (ConnectionObject) receive.readObject();
    }
    
    public final String address() {
        return socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }
    
}
