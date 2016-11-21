package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.ServerMain;
import sharedlib.tuples.GameInfo;
import sharedlib.tuples.GameSearch;

public class GameDB {

    public static List<GameInfo> getGameList(GameSearch s) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "SELECT gmid, p1.uid, p2.uid, p1.username, p2.username, startdate, enddate "
                + "FROM games JOIN users AS p1 ON player1 = p1.uid JOIN users AS p2 ON player2 = p2.uid "
                + "WHERE p1.username LIKE ? OR p1.username LIKE ?"
        ); // TODO: finish SQL statement
        stmt.setString(1, "%" + s.usernameFilter + "%");
        stmt.setString(2, "%" + s.usernameFilter + "%");

        ResultSet results = stmt.executeQuery();

        List<GameInfo> games = new ArrayList<>();
        while (results.next()) {
            long id = results.getLong(1);
            // TODO: status? currrent move number?
            games.add(new GameInfo(id, results.getLong(2), results.getLong(3), results.getString(4), results.getString(4), results.getDate(5), results.getDate(6)));
        }

        return games;
    }

}
