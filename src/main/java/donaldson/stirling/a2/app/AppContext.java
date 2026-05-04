package donaldson.stirling.a2.app;

import java.sql.Connection;

import donaldson.stirling.a2.database.DatabaseConnection;
import donaldson.stirling.a2.database.DatabaseInitialiser;

public class AppContext {


  private final Connection connection;

  public AppContext() throws Exception {
    System.out.println("app context...");

    this.connection = DatabaseConnection.getConnection();

    DatabaseInitialiser.initialise(connection);

  }
}
