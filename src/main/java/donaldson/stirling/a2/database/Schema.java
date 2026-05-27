package donaldson.stirling.a2.database;

public final class Schema {

  private Schema() {
    // shouldn't be able instantiate schema objects
  }

  // @TODO add role (enum, user || admin).
  private static final String CREATE_USERS_TABLE =
      """
      CREATE TABLE IF NOT EXISTS users (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              username TEXT NOT NULL UNIQUE,
              hashed_password TEXT NOT NULL,
              first_name TEXT NOT NULL,
              last_name TEXT NOT NULL
          );
      """;

  private static final String CREATE_RECORDS_TABLE =
      """
          CREATE TABLE IF NOT EXISTS records (
              id INTEGER PRIMARY KEY AUTOINCREMENT,
              user_id INTEGER NOT NULL,
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

  public static final String[] CREATE_TABLES = {CREATE_USERS_TABLE, CREATE_RECORDS_TABLE};
}
