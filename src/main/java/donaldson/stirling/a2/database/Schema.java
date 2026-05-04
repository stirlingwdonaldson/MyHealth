package donaldson.stirling.a2.database;

public final class Schema {

  private Schema() {
    // shouldn't be able to make schema objects
  }

  private static final String CREATE_USERS_TABLE = """
      CREATE TABLE IF NOT EXISTS users (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              username TEXT NOT NULL UNIQUE,
              first_name TEXT NOT NULL,
              last_name TEXT NOT NULL,
              email TEXT NOT NULL UNIQUE,
          );
      """;

  private static final String CREATE_RECORDS_TABLE = """
          CREATE TABLE IF NOT EXISTS records (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              weight REAL,
              temperature REAL,
              blood_pressure TEXT,
              note TEXT,
              date TEXT NOT NULL,

              FOREIGN KEY (user_id)
                  REFERENCES users(id)
                  ON DELETE CASCADE
          );
      """;

}
