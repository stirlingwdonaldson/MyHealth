package donaldson.stirling.a2.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitialiser {

  private DatabaseInitialiser() {
    // class should be used statically, ∴ constructor is private.
  }

  // @TODO review if this is following good programming practises
  public static void initialise(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      for (String sql : Schema.CREATE_TABLES) {
        statement.execute(sql);
      }
    }
  }
}
