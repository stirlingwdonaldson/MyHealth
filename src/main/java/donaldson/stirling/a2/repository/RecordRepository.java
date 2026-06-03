import java.sql.Connection;
import java.sql.SQLException;

import donaldson.stirling.a2.model.Record;

public class RecordRepository {
  private final Connection connection;

  public RecordRepository(Connection connection) {
    this.connection = connection;
  }

  public Record createRecord(Record record) throws SQLException {
    record.validate();
  }

}
