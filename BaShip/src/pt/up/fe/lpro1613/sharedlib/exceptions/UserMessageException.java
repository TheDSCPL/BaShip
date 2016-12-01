package pt.up.fe.lpro1613.sharedlib.exceptions;

/**
 * This class always contains a message (via the {@code Throwable::getMessage()}
 * method) that should be shown to the user.
 *
 * @author Alex
 */
public class UserMessageException extends Exception {

    /**
     * Constructs an instance of <code>UserMessageException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UserMessageException(String msg) {
        super(msg);
    }
}
