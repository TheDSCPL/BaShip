package server;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import server.conn.Client;
import server.conn.Server;
import server.database.Database;
import server.other.Console;
import server.other.PrefsKey;
import sharedlib.utils.Preferences;

// TODO: ???: Sound
// TODO: ???: Interface color (option in settings)
// TODO: ???: JUnit
// TODO: ???: Javadoc

public class ServerMain {

    public static final Preferences prefs = new Preferences(ServerMain.class);
    public static final Server server = new Server(prefs.getI(PrefsKey.ServerPort));
    public static final Console console = new Console();
    public static final Set<Client> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void main(String args[]) throws SQLException {
        Database.initialize();
        console.start();
        server.start();
    }
}
