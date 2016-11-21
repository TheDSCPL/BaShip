package server;

import java.sql.SQLException;
import java.util.*;
import server.conn.*;
import server.database.*;
import sharedlib.config.*;

public class ServerMain {
    
    public static final Preferences prefs = new Preferences(ServerMain.class);
    public static final Database db = new Database(prefs.getS(PrefsKey.DatabaseURL));
    public static final ServerThread server = new ServerThread(prefs.getI(PrefsKey.ServerPort));
    public static final ConsoleThread console = new ConsoleThread();
    public static final Set<Client> clients = new HashSet<>();
    
    public static void main(String args[]) throws SQLException {
        db.connect(prefs.getS(PrefsKey.DatabaseUsername), prefs.getS(PrefsKey.DatabasePassword));
        server.start();
        console.start();
    }
}
