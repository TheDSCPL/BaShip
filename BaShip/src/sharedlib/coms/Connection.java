package sharedlib.coms;

import java.io.*;
import java.net.*;

public class Connection extends Thread {

    private final Socket socket;
    private final ObjectInputStream receive;
    private final ObjectOutputStream send;

    public Handler handler;

    public interface Handler {

        public Package handle(Package object);
    }

    protected Connection(Socket socket) throws IOException {
        super("Connection thread for socket at " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
        this.socket = socket;

        // Note: this order is important. First create OutputStream, then create InputStream!
        send = new ObjectOutputStream(socket.getOutputStream());
        receive = new ObjectInputStream(socket.getInputStream());
    }

    private Package packageWaitingForResponse = null;

    private synchronized Package getPackageWaitingForResponse() {
        return packageWaitingForResponse;
    }

    private synchronized void setPackageWaitingForResponse(Package v) {
        packageWaitingForResponse = v;
    }

    private Package receivedResponse = null;

    private synchronized Package getReceivedResponse() {
        return receivedResponse;
    }

    private synchronized void setReceivedResponse(Package v) {
        receivedResponse = v;
    }

    private void send(Package object) throws IOException {
        send.writeObject(object);
    }

    private Package receive() throws IOException, ClassNotFoundException {
        return (Package) receive.readObject();
    }

    protected void sendOnly(Package p) throws IOException {
        send(p);
    }

    protected Package sendAndReceive(Package p) throws IOException, InterruptedException {
        setPackageWaitingForResponse(p);
        send(p);

        Package response = null;
        while (response == null) {
            response = getReceivedResponse();
            Thread.sleep(1);
        }

        setPackageWaitingForResponse(null);
        return response;
    }

    public final String address() {
        return socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Package received = receive();

                if (received != null) {
                    boolean handle = true;

                    Package question = getPackageWaitingForResponse();
                    if (question != null) {
                        if (received.question != null) {
                            if (received.question.equals(question)) {
                                setReceivedResponse(received);
                                handle = false;
                            }
                        }
                    }
                    
                    if (handle && handler != null) {
                        Package response = handler.handle(received);
                        if (response != null) {
                            send(response);
                        }
                    }
                }
                else {
                    System.out.println("Received == null!");
                    return;
                }
            }
            catch (Throwable ex) {
                ex.printStackTrace();
                return;
            }
        }
    }
}
