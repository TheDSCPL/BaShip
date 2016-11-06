package server;

import server.threads.ServerThread;
import server.threads.ConsoleThread;
import server.database.Database;
import java.sql.SQLException;

/**
 *
 * @author Alex
 */
public class ServerMain {

    public static final Database db = new Database("jdbc:postgresql://localhost:5432/");
    public static final ServerThread server = new ServerThread(4444);
    public static final ConsoleThread console = new ConsoleThread();

    public static void main(String args[]) throws SQLException {

        // Connect database
        //db.connect("Alex", "");

        // Start server
        server.start();

        // Start console
        console.start();
    }

    public static void exit(Exception ex) {
        ex.printStackTrace(System.err);
        System.exit(-1);
    }
}
