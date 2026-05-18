package donaldson.stirling.a2.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.util.PasswordUtil;

public class UserRepository {
  private final Connection connection;

  public UserRepository(Connection connection) {
    this.connection = connection;
  }

  public boolean usernameExists(String username) throws SQLException {
    String sql = "SELECT 1 FROM users WHERE username = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);

      try (ResultSet resultSet = statement.executeQuery()) {
        return resultSet.next();
      }
    }
  }

  public User createUser(String username, String rawPassword, String firstName, String lastName)
      throws SQLException {

    String sql = "INSERT INTO users (username, hashed_password, first_name, last_name) VALUES (?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, username);
      statement.setString(2, PasswordUtil.hash(rawPassword));
      statement.setString(3, firstName);
      statement.setString(4, lastName);
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          int id = keys.getInt(1);
          return new User(id, username, PasswordUtil.hash(rawPassword), firstName, lastName); // hashes twice
        }
      }
    }

    throw new SQLException("Failed to create user.");
  }

  public User findByUsername(String username) throws SQLException {
    String sql = "SELECT id, username, hashed_password, first_name, last_name FROM users WHERE username = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return null;
        }

        return mapUser(resultSet);
      }
    }
  }

  public User authenticate(String username, String rawPassword) throws SQLException {
  }

  private User mapUser(ResultSet resultSet) throws SQLException {
    return new User(
        resultSet.getInt("id"),
        resultSet.getString("username"),
        resultSet.getString("hashed_password"),
        resultSet.getString("first_name"),
        resultSet.getString("last_name"));
  }
}
