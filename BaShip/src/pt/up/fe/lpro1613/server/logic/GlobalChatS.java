package pt.up.fe.lpro1613.server.logic;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.up.fe.lpro1613.server.conn.Client;
import pt.up.fe.lpro1613.server.database.GlobalChatDB;
import static pt.up.fe.lpro1613.server.logic.UserS.isClientLoggedIn;
import pt.up.fe.lpro1613.sharedlib.structs.Message;

/**
 * Class responsible for managing the global chat between players.
 *
 * @author Alex
 */
public class GlobalChatS {

    /**
     * Send a message from the given client to everyone that is currently
     * logged-in. This method saves the message on the database and sends it to
     * every user logged-in on the server so that it appears on their UI.
     *
     * @param client The client that is sending the message. This client must be
     * logged-in.
     * @param message The message string to send. Cannot be null.
     * @throws SQLException
     */
    public static void sendGlobalMessage(Client client, String message) throws SQLException {
        if (isClientLoggedIn(client)) {
            Message msg = GlobalChatDB.saveGlobalMessage(UserS.idFromClient(client), message);
            UserS.distributeGlobalMessage(msg);
        }
        else {
            Logger.getLogger(UserS.class.getName()).log(Level.SEVERE, "Client tried to send global message without being logged in");
        }
    }
}
