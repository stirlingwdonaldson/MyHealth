package donaldson.stirling.a2.app;

import java.sql.Connection;
import java.sql.SQLException;

import donaldson.stirling.a2.database.DatabaseConnection;
import donaldson.stirling.a2.database.DatabaseInitialiser;

public class AppContext implements AutoCloseable {

  private final Connection connection;

  public AppContext() throws Exception {
    System.out.println("app context...");

    this.connection = DatabaseConnection.openConnection();
    DatabaseInitialiser.initialise(connection);
  }

  public Connection getConnection(){
    return this.connection;
  }

  @Override
  public void close() throws SQLException {
    if(!connection.isClosed()){
      connection.close();
    }
  }
}
