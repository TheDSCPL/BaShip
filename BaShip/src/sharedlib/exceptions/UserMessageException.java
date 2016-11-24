package sharedlib.exceptions;

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
