package sharedlib.exceptions;

/**
 * An exception that signals an error occurred when dealing with a
 * {@code Connection} object.
 *
 * @author Alex
 */
public class ConnectionException extends Exception {

    /**
     * Creates a new instance of <code>ConnectionException</code> without detail
     * message.
     */
    public ConnectionException() {

    }

    /**
     * Constructs an instance of <code>ConnectionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ConnectionException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified cause
     *
     * @param cause the cause
     */
    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
