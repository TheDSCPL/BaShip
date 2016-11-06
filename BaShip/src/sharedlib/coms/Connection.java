package sharedlib.coms;

import java.io.*;
import java.net.*;
import sharedlib.coms.packet.Packet;

abstract public class Connection extends Thread {

    private final Socket socket;
    private final ObjectInputStream receive;
    private final ObjectOutputStream send;

    public Handler handler;

    public interface Handler {

        public Packet handle(Packet packet);

        public void connected();

        public void disconnected();
    }

    protected Connection(Socket socket) throws IOException {
        super("Connection thread for socket at " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
        this.socket = socket;

        // Note: this order is important. First create OutputStream, then create InputStream!
        send = new ObjectOutputStream(socket.getOutputStream());
        receive = new ObjectInputStream(socket.getInputStream());
    }

    private Packet packageWaitingForResponse = null;

    private synchronized Packet getPackageWaitingForResponse() {
        return packageWaitingForResponse;
    }

    private synchronized void setPackageWaitingForResponse(Packet v) {
        packageWaitingForResponse = v;
    }

    private Packet receivedResponse = null;

    private synchronized Packet getReceivedResponse() {
        return receivedResponse;
    }

    private synchronized void setReceivedResponse(Packet v) {
        receivedResponse = v;
    }

    private void send(Packet object) throws IOException {
        send.writeObject(object);
    }

    private Packet receive() throws IOException, ClassNotFoundException {
        return (Packet) receive.readObject();
    }

    protected final void sendOnly(Packet p) throws IOException {
        send(p);
    }

    protected final Packet sendAndReceive(Packet p) throws IOException, InterruptedException {
        setPackageWaitingForResponse(p);
        send(p);

        Packet response = null;
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
        if (handler != null) {
            handler.connected();
        }

        while (true) {
            try {
                Packet received = receive();

                if (received != null) {
                    boolean handle = true;

                    Packet question = getPackageWaitingForResponse();
                    if (question != null) {
                        if (received.request != null) {
                            if (received.request.equals(question)) {
                                setReceivedResponse(received);
                                handle = false;
                            }
                        }
                    }

                    if (handle && handler != null) {
                        Packet response = handler.handle(received);
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
                break;
            }
        }
        
        if (handler != null) {
            handler.disconnected();
        }
    }
}
