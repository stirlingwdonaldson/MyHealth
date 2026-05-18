package donaldson.stirling.a2.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import donaldson.stirling.a2.model.User;

public class UserRepository {
  private final Connection connection;

  public UserRepository(Connection connection) {
    this.connection = connection;
  }

  public boolean usernameExists(String username) throws SQLException {
    String sql = "SELECT 1 FROM users WHERE username = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);

      try(ResultSet resultSet = statement.executeQuery()){
        return resultSet.next();
      }
    }
  }

  public User createUser(String username, String hashedPassword, String firstName, String lastName)
      throws SQLException {
  }

  public User findByUsername(String username) throws SQLException {
  }

  public User authenticate(String username, String rawPassword) throws SQLException {
  }

}
