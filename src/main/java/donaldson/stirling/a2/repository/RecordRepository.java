
package donaldson.stirling.a2.repository;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

import donaldson.stirling.a2.model.Record;

public class RecordRepository {
  private final Connection connection;

  public RecordRepository(Connection connection) {
    this.connection = connection;
  }

  public Record createRecord(Record record) throws SQLException {
    record.validate();

    String sql = """
        INSERT INTO records (user_id, weight, temperature, blood_pressure, note, date)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
      setRecordFields(statement, record);
      statement.executeUpdate();

      try (ResultSet keys = statement.getGeneratedKeys()) {
        if (keys.next()) {
          return new Record(
              keys.getInt(1),
              record.getUserId(),
              record.getWeight(),
              record.getTemperature(),
              record.getBloodPressure(),
              record.getNote(),
              record.getDate());
        }
      }
    }

    throw new SQLException("Failed to create health record.");
  }

  public List<Record> findAllByUserId(int userId) throws SQLException {
    String sql = """
        SELECT id, user_id, weight, temperature, blood_pressure, note, date
        FROM records
        WHERE user_id = ?
        ORDER BY date DESC, id DESC
        """;

    List<Record> records = new ArrayList<>();

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, userId);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          records.add(mapRecord(resultSet));
        }
      }
    }

    return records;
  }

  public Record findByIdForUser(int recordId, int userId) throws SQLException {
    String sql = """
        SELECT id, user_id, weight, temperature, blood_pressure, note, date
        FROM records
        WHERE id = ? AND user_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, recordId);
      statement.setInt(2, userId);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (!resultSet.next()) {
          return null;
        }
        return mapRecord(resultSet);
      }
    }
  }



  public boolean updateRecord(Record record) throws SQLException {
    record.validate();

    String sql =
        """
        UPDATE records
        SET weight = ?, temperature = ?, blood_pressure = ?, note = ?, date = ?
        WHERE id = ? AND user_id = ?
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      setNullableDouble(statement, 1, record.getWeight());
      setNullableDouble(statement, 2, record.getTemperature());
      statement.setString(3, record.getBloodPressure());
      statement.setString(4, record.getNote());
      statement.setString(5, record.getDate().toString());
      statement.setInt(6, record.getId());
      statement.setInt(7, record.getUserId());
      return statement.executeUpdate() == 1;
    }
  }

  private void setRecordFields(PreparedStatement statement, Record record) throws SQLException {
    statement.setInt(1, record.getUserId());
    setNullableDouble(statement, 2, record.getWeight());
    setNullableDouble(statement, 3, record.getTemperature());
    statement.setString(4, record.getBloodPressure());
    statement.setString(5, record.getNote());
    statement.setString(6, record.getDate().toString());
  }

  private void setNullableDouble(PreparedStatement statement, int index, Double value)
      throws SQLException {
    if (value == null) {
      statement.setNull(index, Types.REAL);
      return;
    }

    statement.setDouble(index, value);
  }

  private Record mapRecord(ResultSet resultSet) throws SQLException {
    return new Record(
        resultSet.getInt("id"),
        resultSet.getInt("user_id"),
        getNullableDouble(resultSet, "weight"),
        getNullableDouble(resultSet, "temperature"),
        resultSet.getString("blood_pressure"),
        resultSet.getString("note"),
        LocalDate.parse(resultSet.getString("date")));
  }

  private Double getNullableDouble(ResultSet resultSet, String columnName) throws SQLException {
    double value = resultSet.getDouble(columnName);
    if (resultSet.wasNull()) {
      return null;
    }
    return value;
  }

}
