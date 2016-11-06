/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.SQLException;

/**
 *
 * @author Alex
 */
public class Main {

    public static final Database db = new Database("jdbc:postgresql://localhost:5432/");
    public static final Server server = new Server(4444);
    public static final Console console = new Console();

    public static void main(String args[]) throws SQLException {

        // Connect database
        db.connect("Alex", "");

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
