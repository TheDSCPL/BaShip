package server.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import server.ServerMain;
import server.logic.GameS;
import sharedlib.tuples.GameInfo;
import sharedlib.tuples.GameSearch;

public class GameDB {

    public static List<GameInfo> getGameList(GameSearch s) throws SQLException {
        String query
                = "SELECT gmid, p1.uid, p2.uid, p1.username, p2.username, startdate, enddate "
                  + "FROM games JOIN users AS p1 ON player1 = p1.uid JOIN users AS p2 ON player2 = p2.uid "
                  + "WHERE (p1.username LIKE ? OR p1.username LIKE ?)";

        if (s.currentlyPlayingOnly) {
            query += " AND enddate IS NOT NULL";
        }

        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(query);
        stmt.setString(1, "%" + s.usernameFilter + "%");
        stmt.setString(2, "%" + s.usernameFilter + "%");

        ResultSet results = stmt.executeQuery();

        List<GameInfo> games = new ArrayList<>();
        while (results.next()) {
            long id = results.getLong(1);
            
            Date start = results.getDate(6);
            Date end = results.getDate(7);

            GameInfo.State state;
            if (end != null) {
                state = GameInfo.State.Finished;
            }
            else if (start != null) {
                state = GameInfo.State.Playing;
            }
            else {
                state = GameInfo.State.Created;
            }
            
            games.add(
                    new GameInfo(
                            id,
                            results.getLong(2),
                            results.getLong(3),
                            results.getString(4),
                            results.getString(5),
                            state,
                            start,
                            end,
                            state == GameInfo.State.Playing ? GameS.getGameCurrentMoveNumber(id) : null
                    )
            );
        }
        
        return games;
    }

    public static Long createGame(Long player1ID, Long player2ID) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "INSERT INTO games VALUES (DEFAULT, NULL, NULL, ?, ?, NULL) RETURNING (uid)"
        );
        stmt.setLong(1, player1ID);
        stmt.setLong(2, player2ID);

        ResultSet r = stmt.executeQuery();
        r.next();
        return r.getLong(1);
    }

    public static void setStartTimeToNow(Long gameID) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "UPDATE games SET startdate = NOW() WHERE gmid = ?"
        );
        stmt.setLong(1, gameID);
        stmt.executeUpdate();
    }
    
    public static void setEndTimeToNow(Long gameID) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "UPDATE games SET enddate = NOW() WHERE gmid = ?"
        );
        stmt.setLong(1, gameID);
        stmt.executeUpdate();
    }
    
    public static void setWinner(Long gameID, Long playerID) throws SQLException {
        PreparedStatement stmt = ServerMain.db.getConn().prepareStatement(
                "UPDATE games SET winner = ? WHERE gmid = ?"
        );
        stmt.setLong(1, playerID);
        stmt.setLong(2, gameID);
        stmt.executeUpdate();
    }

}
