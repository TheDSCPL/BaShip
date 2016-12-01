package pt.up.fe.lpro1613.server;

import java.sql.SQLException;
import java.util.*;
import pt.up.fe.lpro1613.server.conn.Client;
import pt.up.fe.lpro1613.server.conn.ServerThread;
import pt.up.fe.lpro1613.server.other.ConsoleThread;
import pt.up.fe.lpro1613.server.other.PrefsKey;
import pt.up.fe.lpro1613.sharedlib.utils.Preferences;

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
