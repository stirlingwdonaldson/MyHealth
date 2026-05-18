package donaldson.stirling.a2.app;

import java.sql.Connection;
import java.sql.SQLException;

import donaldson.stirling.a2.database.DatabaseConnection;
import donaldson.stirling.a2.database.DatabaseInitialiser;
import donaldson.stirling.a2.model.User;

public class AppContext implements AutoCloseable {

  private final Connection connection;
  private User currentUser;

  public AppContext() throws Exception {
    this.connection = DatabaseConnection.openConnection();
    DatabaseInitialiser.initialise(connection);
  }

  public Connection getConnection() {
    return this.connection;
  }

  public User getCurrentUser() {
    return this.currentUser;
  }

  // could move elsewhere, possibly insecure
  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  @Override
  public void close() throws SQLException {
    if (!connection.isClosed()) {
      connection.close();
    }
  }
}
