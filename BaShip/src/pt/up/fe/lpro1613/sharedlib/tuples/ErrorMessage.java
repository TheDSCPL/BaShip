package pt.up.fe.lpro1613.sharedlib.tuples;

/**
 * Immutable wrapper around a String. Used for when one of the sides of the
 * client-server connection needs to report to the other that there was an
 * error.
 *
 * @author Alex
 */
public class ErrorMessage {

    public final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

}
