package server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.logic.UserS;
import sharedlib.constants.DBK;
import sharedlib.structs.UserInfo;
import sharedlib.structs.UserSearch;

/**
 * Collection of static methods that access, set and return information present
 * on the table "users" of the database.
 *
 * @author Alex
 */
public class UserDB {

    /**
     * Connect to the database and verify if there is already a user with that
     * username.
     *
     * @param username The username to verify.
     * @return True if the username isn't present on the database and is
     * therefore available, false otherwise.
     * @throws SQLException
     */
    public static boolean isUsernameAvailable(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();

            stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }

    /**
     * Register a new user on the database. This inserts a new row on the table
     * "users" with the specified username and password hash.
     *
     * @param username
     * @param passwordHash
     * @return A UserInfo class with the #id and #username fields non-null
     * @throws SQLException
     */
    public static UserInfo register(String username, String passwordHash) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            
            stmt = conn.prepareStatement("INSERT INTO users VALUES (DEFAULT, ?, ?, DEFAULT) RETURNING (uid)");
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

    /**
     * Queries the database for a user that has that username and password
     * combination.
     *
     * @param username
     * @param passwordHash
     * @return User id for the user with that username and password, or null if
     * none was found
     * @throws SQLException
     */
    public static Long verifyLogin(String username, String passwordHash) throws SQLException {
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

    /**
     * Queries the database for the id of the user with the given username.
     *
     * @param id
     * @return The username of the user with the given id, or null if none was found
     * @throws SQLException
     */
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
        return getUserList(s, DBK.pageSize);
    }
    
    /**
     * Get the list of all users according to the search parameters.
     *
     * @param s The search parameters
     * @param pageSize the number of users per page
     * @return The list of users
     * @throws SQLException
     */
    public static List<UserInfo> getUserList(UserSearch s, int pageSize) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement(
                    "SELECT uid, username, rank, ngames, nwins, nshots "
                    + "FROM users JOIN user_ranks USING(uid) JOIN user_stats USING(uid) "
                    + "WHERE username LIKE ? "
                    + "ORDER BY username LIMIT ? OFFSET ?"
                    //+ "ORDER BY ? LIMIT ?"
            );
            stmt.setString(1, "%" + s.usernameFilter + "%");
            stmt.setInt(2, pageSize);
            stmt.setInt(3, pageSize * s.pageIndex);

            rs = stmt.executeQuery();

            List<UserInfo> users = new ArrayList<>();

            while (rs.next()) {
                long id = rs.getLong(1);
                users.add(
                        new UserInfo(
                                id, rs.getString(2), null,
                                rs.getInt(3), rs.getInt(4), rs.getInt(5),
                                rs.getInt(6), UserS.statusOfUser(id)
                        )
                );
            }

            return users;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }
    
    /**
     * Get the banned info of a player
     * @param username Username of the player
     * @return True if he is banned
     * @throws SQLException
     */
    public static boolean isUserBanned(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConn();

            stmt = conn.prepareStatement("SELECT banned FROM users WHERE username = ?");
            stmt.setString(1, username);

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }

            return false;
        }
        finally {
            Database.close(conn, stmt, rs);
        }
    }
    
    /**
     * Ban/Unban a player from the game
     * @param username Username of the player to ban
     * @param banned Ban status to set. True to ban, false otherwise
     * @throws SQLException
     */
    public static void setUserBanned(String username, boolean banned) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConn();
            stmt = conn.prepareStatement("UPDATE users SET banned = ? WHERE username = ?");
            stmt.setBoolean(1, banned);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
        finally {
            Database.close(conn, stmt);
        }
    }
}
