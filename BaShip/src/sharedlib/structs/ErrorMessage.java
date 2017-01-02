package sharedlib.structs;

/**
 * Immutable wrapper around a String. Used for when one of the sides of the
 * client-server connection needs to report to the other that there was an
 * error.
 *
 * @author Alex
 */
public class ErrorMessage {

    /**
     * Error message to sent
     */
    public final String message;

    /**
     * Initialize the error message with a message passed by argument
     * @param message The error message
     */
    public ErrorMessage(String message) {
        this.message = message;
    }

}
