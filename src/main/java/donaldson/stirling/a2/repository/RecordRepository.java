
package donaldson.stirling.a2.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

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

}
