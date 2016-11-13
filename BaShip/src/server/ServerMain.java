package server;

import java.sql.SQLException;
import java.util.*;
import server.conn.*;
import server.database.*;
import sharedlib.config.*;

public class ServerMain {

    public static final Configuration config = new Configuration(ServerMain.class.getResourceAsStream("config.properties"));
    public static final Database db = new Database(config.getS("database.url"));
    public static final ServerThread server = new ServerThread(config.getI("server.port"));
    public static final ConsoleThread console = new ConsoleThread();
    public static final Set<Client> clients = new HashSet<>();
    
    public static void main(String args[]) throws SQLException {
        db.connect(config.getS("database.username"), config.getS("database.password"));
        server.start();
        console.start();
    }
}
