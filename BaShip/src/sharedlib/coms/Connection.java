package sharedlib.coms;

import java.io.*;
import java.net.*;

public class Connection extends Thread {

    private final Socket socket;
    private final ObjectInputStream receive;
    private final ObjectOutputStream send;

    public EventHandler handler;

    public interface EventHandler {

        public ConnectionObject handle(ConnectionObject object);
    }

    public Connection(Socket socket) throws IOException {
        super("Connection thread for socket at " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
        this.socket = socket;

        // Note: this order is important. First create OutputStream, then create InputStream!
        send = new ObjectOutputStream(socket.getOutputStream());
        receive = new ObjectInputStream(socket.getInputStream());
    }

    private boolean waitingForResponse = false;

    private synchronized boolean getWaitingForResponse() {
        return waitingForResponse;
    }

    private synchronized void setWaitingForResponse(boolean v) {
        waitingForResponse = v;
    }

    private ConnectionObject lastReceivedObject = null;

    private synchronized ConnectionObject getLastReceivedObject() {
        return lastReceivedObject;
    }

    private synchronized void setLastReceivedObject(ConnectionObject v) {
        lastReceivedObject = v;
    }

    private void send(ConnectionObject object) throws IOException {
        send.writeObject(object);
    }

    private ConnectionObject receive() throws IOException, ClassNotFoundException {
        return (ConnectionObject) receive.readObject();
    }

    public void sendOnly(ConnectionObject object) throws IOException {
        send(object);
    }

    public ConnectionObject sendAndReceive(ConnectionObject object) throws IOException, InterruptedException {
        setWaitingForResponse(true);
        send(object);

        while (getLastReceivedObject() == null) {
            Thread.sleep(1);
        }

        setWaitingForResponse(false);
        return getLastReceivedObject();
    }

    public final String address() {
        return socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }
    
    @Override
    public synchronized void start() {

        while (true) {
            try {
                ConnectionObject obj = receive();

                if (getWaitingForResponse()) {
                    setLastReceivedObject(obj);
                }
                else {
                    ConnectionObject response = handler.handle(obj);
                    if (response != null) {
                        send(response);
                    }
                }
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}
