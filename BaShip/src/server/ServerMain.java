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
// TODO: ???: Interface color (option in settings)
// TODO: ???: Javadoc

// TODO: Alex: JUnit - For all classes in use cases "place ship" & "fire shot"
// TODO: Alex: Delete and remake SQL databse on FEUP

// TODO: Luis: Test SQL method UserDB.setUserBanned
// TODO: Luis: Client side: add check for null whenever ClientMain.server is accessed. This means the connection to the server has been shut down. Show message to user and go to login screen.

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
