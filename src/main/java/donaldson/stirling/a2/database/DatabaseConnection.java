package donaldson.stirling.a2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
  private static final String DB_URL = "jdbc:sqlite:MyHealth.db";

  private DatabaseConnection() {
    // shouldn't be instantiated
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL);
  }
}
