package server;

import server.other.PrefsKey;
import server.other.ConsoleThread;
import java.sql.SQLException;
import java.util.*;
import server.conn.*;
import sharedlib.utils.Preferences;

public class ServerMain {
    
    public static final Preferences prefs = new Preferences(ServerMain.class);
    public static final ServerThread server = new ServerThread(prefs.getI(PrefsKey.ServerPort));
    public static final ConsoleThread console = new ConsoleThread();
    public static final Set<Client> clients = new HashSet<>();
    
    public static void main(String args[]) throws SQLException {
        server.start();
        console.start();
    }
}
