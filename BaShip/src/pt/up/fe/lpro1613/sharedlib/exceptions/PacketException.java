package pt.up.fe.lpro1613.sharedlib.exceptions;

/**
 * An exception that signals an error occurred when dealing with a
 * {@code Packet} object. This is mostly due to errors when transforming
 * {@code Packet} objects to {@code String} objects and vice-versa.
 *
 * @author Alex
 */
public class PacketException extends Exception {

    /**
     * Creates a new instance of <code>PacketException</code> without detail
     * message.
     */
    public PacketException() {
    }

    /**
     * Constructs an instance of <code>PacketException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public PacketException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by
     * the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method). (A <tt>null</tt> value is permitted, and
     * indicates that the cause is nonexistent or unknown.)
     */
    public PacketException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>). This
     * constructor is useful for exceptions that are little more than wrappers
     * for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     * {@link #getCause()} method). (A <tt>null</tt> value is permitted, and
     * indicates that the cause is nonexistent or unknown.)
     */
    public PacketException(Throwable cause) {
        super(cause);
    }
}
