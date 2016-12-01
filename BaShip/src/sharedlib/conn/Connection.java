package sharedlib.conn;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;
import sharedlib.exceptions.ConnectionException;
import sharedlib.exceptions.PacketException;

/**
 * Wrapper around a socket which knows how to send and receive Packet objects.
 * Even though Connection is a Thread, do not call {@code Thread::start()}
 * directly. Instead, use the {@code Connection::connect()} method.
 */
final public class Connection extends Thread {

    private final Socket socket;
    private final BufferedReader receive;
    private final PrintWriter send;

    public Delegate delegate;

    /**
     * Events generated by the Connection are sent to an object that implements
     * this interface.
     */
    public interface Delegate {

        /**
         * Called whenever a packet is received that isn't part of a
         * request-response routine.
         *
         * @param packet The packet that has been received
         * @return A response packet to be sent back, or null if no response is
         * to be sent
         */
        public Packet handle(Packet packet);

        /**
         * Called when the socket connection has just been started.
         *
         * @param connection The connection object representing this connection
         */
        public void connected(Connection connection);

        /**
         * Called when the socket connection ceases to exist.
         *
         * @param connection The connection object representing this connection
         */
        public void disconnected(Connection connection);
    }

    /**
     * Create a new Connection around a socket.
     *
     * @param socket The socket to use
     * @throws ConnectionException If any problem occurred while getting the
     * input/output streams from the socket
     */
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

    /**
     * Start receiving and parsing Packet objects.
     */
    public void connect() {
        start();
    }

    /**
     * Disconnect and close the socket.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        socket.close();
    }

    /**
     * Verify if the given server parameters are valid. This will attempt to
     * connect to the server. If a connection is successful, it will immediately
     * disconnect and return True. If anything else happens, it returns False.
     *
     * @param ip The server ip
     * @param port The server port
     * @return True if it is possible to connect to the server, False otherwise
     */
    public static boolean test(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            socket.close();
            return true;
        }
        catch (IOException ex) {
            return false;
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

    /**
     * Send a Packet but do not wait for a response.
     *
     * @param p The packet to send.
     * @throws ConnectionException If any error occurred while sending the
     * packet.
     */
    public void sendOnly(Packet p) throws ConnectionException {
        try {
            send(p);
        }
        catch (IOException | PacketException ex) {
            throw new ConnectionException(ex);
        }
    }

    /**
     * Send a Packet and wait for a response Packet. This function blocks
     * execution while waiting for a response.
     *
     * @param p the Packet to send.
     * @return The response Packet.
     * @throws ConnectionException If any error occurred while sending the
     * packet.
     */
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

    /**
     * @return Address string in the format "ip:port".
     */
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

        System.gc();
    }
}
