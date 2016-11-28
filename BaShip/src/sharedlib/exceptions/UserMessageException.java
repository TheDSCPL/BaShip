package sharedlib.exceptions;

import java.sql.SQLException;

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

    public UserMessageException(String could_not_access_DB_and_create_game, SQLException ex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
