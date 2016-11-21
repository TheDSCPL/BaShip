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
                "SELECT gid, username, rank, ngames, nwins, nshots "
                + "FROM users JOIN user_ranks USING(uid) JOIN user_stats USING(uid) "
                + "WHERE username LIKE ? "
                + "ORDER BY ? LIMIT ?"
        ); // TODO: statement
        //stmt.setString(1, "%" + s.usernameFilter + "%");
        //stmt.setInt(2, s.orderByColumn);
        //stmt.setInt(3, s.rowLimit);

        ResultSet results = stmt.executeQuery();

        List<GameInfo> games = new ArrayList<>();
        while (results.next()) {
            long id = results.getLong(1);
            // TODO: status? currrent move?
            games.add(new GameInfo(id, results.getLong(2), results.getLong(3), results.getString(4), results.getString(4), results.getDate(5), results.getDate(6)));
        }
        
        return games;
    }

}
