package donaldson.stirling.a2.repository;

import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    String sql =
        "INSERT INTO users (username, hashed_password, first_name, last_name) VALUES (?, ?, ?, ?)";

    String hashedPassword = PasswordUtil.hash(rawPassword);

    try (PreparedStatement statement =
        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, username);
      statement.setString(2, hashedPassword);
      statement.setString(3, firstName);
      statement.setString(4, lastName);
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          int id = keys.getInt(1);
          return new User(id, username, hashedPassword, firstName, lastName);
        }
      }
    }

    throw new SQLException("Failed to create user.");
  }

  public User findByUsername(String username) throws SQLException {
    String sql =
        "SELECT id, username, hashed_password, first_name, last_name FROM users WHERE username = ?";

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
    User user = findByUsername(username);

    if (user == null) {
      return null;
    }

    String hashedInput = PasswordUtil.hash(rawPassword);

    if (!hashedInput.equals(user.getHashedPassword())) {
      return null;
    }

    return user;
  }

  public boolean updateProfile(int userId, String firstName, String lastName) throws SQLException {
    String sql = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setInt(3, userId);
      return statement.executeUpdate() == 1;
    }
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
