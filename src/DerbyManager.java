import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages Derby database.
 * Creates the database, loads initial data, and reads a setting value for use in the game.
 */
public class DerbyManager
{
    private static final String DB_URL = "jdbc:derby:koherenceDB;create=true";

    //  Initializes the database by creating a table and inserting a default row.
    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            if (!settingsTableExists(conn)) {
                stmt.executeUpdate(
                        "CREATE TABLE GAME_SETTINGS (" +
                                "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                                "PLAYER_NAME VARCHAR(50), " +
                                "LOOP_COUNT INT)"
                );
                stmt.executeUpdate(
                        "INSERT INTO GAME_SETTINGS (PLAYER_NAME, LOOP_COUNT) " +
                                "VALUES ('Alex', 0)"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates the stored loop count for the first settings row.
    public void updateLoopCount(int newLoopCount) {
        String sql = "UPDATE GAME_SETTINGS SET LOOP_COUNT = ? WHERE ID = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newLoopCount);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Reads the current loop count from the database.
    public int readLoopCount() {
        String sql = "SELECT LOOP_COUNT FROM GAME_SETTINGS WHERE ID = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("LOOP_COUNT");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Checks whether the GAME_SETTINGS table exists.
    private boolean settingsTableExists(Connection conn) throws SQLException {
        try (ResultSet rs = conn.getMetaData()
                .getTables(null, null, "GAME_SETTINGS", null)) {
            return rs.next();
        }
    }
}