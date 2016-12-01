package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import server.logic.GameS;
import sharedlib.tuples.GameInfo;
import sharedlib.tuples.GameSearch;

public class GameDB {

    public static List<GameInfo> getGameList(GameSearch s) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query
                    = "SELECT gmid, p1.uid, p2.uid, p1.username, p2.username, startdate, enddate "
                      + "FROM games JOIN users AS p1 ON player1 = p1.uid JOIN users AS p2 ON player2 = p2.uid "
                      + "WHERE (p1.username LIKE ? OR p1.username LIKE ?)";

            if (s.currentlyPlayingOnly) {
                query += " AND enddate IS NOT NULL";
            }

            conn = Database.getConn();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + s.usernameFilter + "%");
            stmt.setString(2, "%" + s.usernameFilter + "%");

            rs = stmt.executeQuery();

            List<GameInfo> games = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong(1);

                Date start = rs.getDate(6);
                Date end = rs.getDate(7);

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
                                rs.getLong(2),
                                rs.getLong(3),
                                rs.getString(4),
                                rs.getString(5),
                                state, // TODO: filter by cuurently playing only
                                start,
                                end,
                                state == GameInfo.State.Playing ? GameS.getGameCurrentMoveNumber(id) : null
                        )
                );
            }

            return games;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    public static Long createGame(Long player1ID, Long player2ID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement(
                    "INSERT INTO games VALUES (DEFAULT, NULL, NULL, ?, ?, NULL) RETURNING (uid)"
            );
            stmt.setLong(1, player1ID);
            stmt.setLong(2, player2ID);

            rs = stmt.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    public static void setStartTimeToNow(Long gameID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("UPDATE games SET startdate = NOW() WHERE gmid = ?");
            stmt.setLong(1, gameID);
            stmt.executeUpdate();
        }
        finally {
            Database.close(conn, stmt);
        }
    }

    public static void setEndTimeToNow(Long gameID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("UPDATE games SET enddate = NOW() WHERE gmid = ?");
            stmt.setLong(1, gameID);
            stmt.executeUpdate();
        }
        finally {
            Database.close(conn, stmt);
        }
    }

    public static void setWinner(Long gameID, Long playerID) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("UPDATE games SET winner = ? WHERE gmid = ?");
            stmt.setLong(1, playerID);
            stmt.setLong(2, gameID);
            stmt.executeUpdate();
        }
        finally {
            Database.close(conn, stmt);
        }
    }

}
