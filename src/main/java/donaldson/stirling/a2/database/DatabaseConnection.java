package donaldson.stirling.a2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private final String DB_URL = "jdbc:sqlite:MyHealth.db";
  private Connection connection;


  // @TODO should getConnection jjust return driverManager.getConnection(DB_URL), is constructor necessary?

  public DatabaseConnection() {
    try {
      this.connection = DriverManager.getConnection(DB_URL);
      System.out.println("connected to database: " + DB_URL); // @TODO update
    } catch (SQLException e){
      e.printStackTrace(System.err);
    }
  }

  public Connection getConnection() {
    return this.connection;
  }
}
