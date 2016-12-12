package server;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import server.conn.Client;
import server.conn.ServerThread;
import server.database.Database;
import server.other.ConsoleThread;
import server.other.PrefsKey;
import sharedlib.utils.Preferences;

/*
TODO:

 - Review all error logging and error messages
*/

public class ServerMain {
    
    public static final Preferences prefs = new Preferences(ServerMain.class);
    public static final ServerThread server = new ServerThread(prefs.getI(PrefsKey.ServerPort));
    public static final ConsoleThread console = new ConsoleThread();
    public static final Set<Client> clients = new HashSet<>();
    
    public static void main(String args[]) throws SQLException {
        Database.initialize();
        server.start();
        console.start();
    }
}
