package sharedlib.conn;

import sharedlib.exceptions.PacketException;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;
import sharedlib.conn.packet.*;
import sharedlib.exceptions.ConnectionException;

final public class Connection extends Thread {

    private final Socket socket;
    private final BufferedReader receive;
    private final PrintWriter send;

    public Delegate delegate;

    public interface Delegate {

        /**
         * Called whenever a packet is received that isn't part of a
         * request-response routine
         *
         * @param connection The connection that received the packet
         * @param packet The packet that has been received
         * @return A response packet to be sent back, or null
         */
        public Packet handle(Packet packet);

        /**
         * Called when the socket connection has just been started
         *
         * @param connection The connection object representing this connection
         */
        public void connected(Connection connection);

        /**
         * Called when the socket connection ceases to exist
         *
         * @param connection The connection object representing this connection
         */
        public void disconnected(Connection connection);
    }

    public Connection(Socket socket) throws ConnectionException {
        super("Connection thread for socket at " + socket.getInetAddress().getHostName() + ":" + socket.getPort());
        this.socket = socket;

        try {
            send = new PrintWriter(socket.getOutputStream(), true);
            receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException ex) {
            throw new ConnectionException(ex);
        }
    }

    private Map<String, Optional<Packet>> packagesWaiting = new ConcurrentHashMap<>();

    private void send(Packet object) throws IOException, PacketException {
        send.println(object.getString());
    }

    private Packet receive() throws IOException, PacketException {
        String line = receive.readLine();
        if (line != null) {
            return Packet.fromString(line);
        }
        return null;
    }

    public void sendOnly(Packet p) throws ConnectionException {
        try {
            send(p);
        }
        catch (IOException | PacketException ex) {
            throw new ConnectionException(ex);
        }
    }

    public final Packet sendAndReceive(Packet p) throws ConnectionException {
        packagesWaiting.put(p.id, Optional.empty());

        try {
            send(p);
        }
        catch (IOException | PacketException ex) {
            throw new ConnectionException(ex);
        }

        while (!packagesWaiting.get(p.id).isPresent()) {
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException ex) {
                // Ignore interrupted exception
            }
        }

        return packagesWaiting.remove(p.id).get();
    }

    public String address() {
        return socket.getInetAddress().getHostName() + ":" + socket.getPort();
    }

    @Override
    public void run() {
        if (delegate != null) {
            delegate.connected(this);
        }

        while (true) {
            try {
                Packet received = receive();

                if (received == null) { // Disconnected
                    break;
                }
                else if (packagesWaiting.containsKey(received.pid)) {
                    packagesWaiting.put(received.pid, Optional.of(received));
                }
                else if (delegate != null) {
                    Packet response = delegate.handle(received);
                    if (response != null) {
                        response.pid = received.id;
                        send(response);
                    }
                }
            }
            catch (IOException | PacketException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Error when processing packet -> disconnecting", ex);
                break;
            }
        }

        if (delegate != null) {
            delegate.disconnected(this);
        }
    }
}
