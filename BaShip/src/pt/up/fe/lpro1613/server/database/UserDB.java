package pt.up.fe.lpro1613.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pt.up.fe.lpro1613.server.logic.UserS;
import pt.up.fe.lpro1613.sharedlib.tuples.UserInfo;
import pt.up.fe.lpro1613.sharedlib.tuples.UserSearch;

public class UserDB {

    public static boolean isUsernameAvailable(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();

            stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            stmt.setString(1, username);

            rs = stmt.executeQuery(username);
            rs.next();
            return rs.getInt(1) == 0;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    public static UserInfo register(String username, String passwordHash) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();

            stmt = conn.prepareStatement("INSERT INTO users VALUES (DEFAULT, ?, ?) RETURNING (uid)");
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            rs = stmt.executeQuery();
            rs.next();
            Long id = rs.getLong(1);

            return new UserInfo(id, username);
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    public static Long verifyLoginAndReturnUserID(String username, String passwordHash) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();

            stmt = conn.prepareStatement("SELECT uid FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }

            return null;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    public static String getUsernameFromID(long id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();

            stmt = conn.prepareStatement("SELECT username FROM users WHERE uid = ?");
            stmt.setLong(1, id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            
            return null;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    public static List<UserInfo> getUserList(UserSearch s) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement(
                    "SELECT uid, username, rank, ngames, nwins, nshots "
                    + "FROM users JOIN user_ranks USING(uid) JOIN user_stats USING(uid) "
                    + "WHERE username LIKE ? "
                    + "ORDER BY ? LIMIT ?"
            );
            stmt.setString(1, "%" + s.usernameFilter + "%");
            stmt.setInt(2, s.orderByColumn);
            stmt.setInt(3, s.rowLimit);

            rs = stmt.executeQuery();

            List<UserInfo> users = new ArrayList<>();

            while (rs.next()) {
                long id = rs.getLong(1);
                users.add(
                        new UserInfo(
                                id, rs.getString(2), null,
                                rs.getInt(3), rs.getInt(4), rs.getInt(5),
                                rs.getInt(6), UserS.getUserStatus(id) // TODO: filter by online only
                        )
                );
            }

            return users;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

}
