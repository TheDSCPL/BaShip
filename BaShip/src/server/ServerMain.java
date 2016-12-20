package server;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import server.conn.Client;
import server.conn.Server;
import server.database.Database;
import server.other.Console;
import server.other.PrefsKey;
import sharedlib.utils.Preferences;

// TODO: Alex: NEXT: Review all error logging and error messages
// TODO: Alex: rank calculation

// TODO: Sound
// TODO: Interface color (option in settings)
// TODO: JUnit
// TODO: Javadoc
// TODO: Luis: Server console

public class ServerMain {

    public static final Preferences prefs = new Preferences(ServerMain.class);
    public static final Server server = new Server(prefs.getI(PrefsKey.ServerPort));
    public static final Console console = new Console();
    public static final Set<Client> clients = new HashSet<>();

    public static void main(String args[]) throws SQLException {
        Database.initialize();
        console.start();
        server.start();
    }
}
