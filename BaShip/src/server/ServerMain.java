package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import server.conn.Client;
import server.conn.Server;
import server.database.Database;
import server.other.Console;
import sharedlib.utils.Preferences;

// TODO: Nuno: Sound

// TODO: Luis: Javadoc
// TODO: Luis: Interface color (option in settings)
// TODO: Luis: HTML help window

/**
 * Main Class of server side code. Start connections, preferences, console
 * and database. 
 */

public class ServerMain {

    /**
     * Preferences of the server
     */
    public static final Preferences prefs = new Preferences(ServerMain.class);

    /**
     * Access of the character-based input of the administrator system
     */
    public static final Console console = new Console();

    /**
     * Clients connected to the server
     */
    public static final Set<Client> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Main method where the database and console are initialized so the server
     * can start.
     * 
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String args[]) throws SQLException, IOException {
        Database.initialize(); // Connect to the database
        console.start();
        Server.startServer(); // Automatically start server
    }
}
