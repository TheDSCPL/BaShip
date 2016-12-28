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

// TODO: ???: Sound

// TODO: Alex: JUnit - For all classes in use cases "place ship" & "fire shot"
// TODO: Alex: Delete and remake SQL databse on FEUP

// TODO: Luis: Test SQL method UserDB.setUserBanned
// TODO: Luis: Javadoc
// TODO: Luis: Interface color (option in settings)
// TODO: Luis: Console
// TODO: Luis: Other things?

public class ServerMain {

    public static final Preferences prefs = new Preferences(ServerMain.class);
    public static final Console console = new Console();
    public static final Set<Client> clients = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void main(String args[]) throws SQLException, IOException {
        Database.initialize(); // Connect to the database
        console.start();
        Server.startServer(); // Automatically start server
    }
}
