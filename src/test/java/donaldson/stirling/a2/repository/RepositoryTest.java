package donaldson.stirling.a2.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import donaldson.stirling.a2.database.DatabaseInitialiser;
import donaldson.stirling.a2.model.Record;
import donaldson.stirling.a2.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepositoryTest {
  private Connection connection;
  private UserRepository userRepository;
  private RecordRepository recordRepository;

  @BeforeEach
  void setUp() throws SQLException {
    connection = DriverManager.getConnection("jdbc:sqlite::memory:");
    DatabaseInitialiser.initialise(connection);
    userRepository = new UserRepository(connection);
    recordRepository = new RecordRepository(connection);
  }

  @AfterEach
  void tearDown() throws SQLException {
    connection.close();
  }

  @Test
  void createUserStoresHashedPasswordAndEnforcesUniqueUsername() throws SQLException {
    User user = userRepository.createUser("stirling", "Health1!", "Stirling", "Donaldson");

    assertTrue(userRepository.usernameExists("stirling"));
    assertNotEquals("Health1!", user.getHashedPassword());
    assertThrows(SQLException.class, () -> userRepository.createUser("stirling", "Health1!", "S", "D"));
  }

  @Test
  void authenticateReturnsUserOnlyForCorrectPassword() throws SQLException {
    userRepository.createUser("stirling", "Health1!", "Stirling", "Donaldson");

    assertNotNull(userRepository.authenticate("stirling", "Health1!"));
    assertNull(userRepository.authenticate("stirling", "Wrong1!"));
    assertNull(userRepository.authenticate("missing", "Health1!"));
  }

  @Test
  void updateProfilePersistsNameChanges() throws SQLException {
    User user = userRepository.createUser("stirling", "Health1!", "Stirling", "Donaldson");

    assertTrue(userRepository.updateProfile(user.getId(), "Alex", "Smith"));
    User updatedUser = userRepository.findByUsername("stirling");

    assertEquals("Alex", updatedUser.getFirstName());
    assertEquals("Smith", updatedUser.getLastName());
  }

  @Test
  void updatePasswordAcceptsNewPasswordAndRejectsOldPassword() throws SQLException {
    User user = userRepository.createUser("stirling", "Health1!", "Stirling", "Donaldson");

    assertTrue(userRepository.updatePassword(user.getId(), "Better2@"));

    assertNull(userRepository.authenticate("stirling", "Health1!"));
    assertNotNull(userRepository.authenticate("stirling", "Better2@"));
  }

  @Test
  void createRecordRejectsInvalidEmptyRecord() throws SQLException {
    User user = userRepository.createUser("stirling", "Health1!", "Stirling", "Donaldson");
    Record emptyRecord = new Record(user.getId(), null, null, null, null);

    assertThrows(IllegalArgumentException.class, () -> recordRepository.createRecord(emptyRecord));
  }

  @Test
  void recordsAreScopedToTheCurrentUser() throws SQLException {
    User firstUser = userRepository.createUser("first", "Health1!", "First", "User");
    User secondUser = userRepository.createUser("second", "Health1!", "Second", "User");

    recordRepository.createRecord(new Record(firstUser.getId(), 70.0, null, null, null));
    recordRepository.createRecord(new Record(secondUser.getId(), 80.0, null, null, null));

    List<Record> firstUserRecords = recordRepository.findAllByUserId(firstUser.getId());

    assertEquals(1, firstUserRecords.size());
    assertEquals(70.0, firstUserRecords.get(0).getWeight());
  }

  @Test
  void updateAndDeleteRecordRequireMatchingUser() throws SQLException {
    User owner = userRepository.createUser("owner", "Health1!", "Record", "Owner");
    User otherUser = userRepository.createUser("other", "Health1!", "Other", "User");
    Record savedRecord = recordRepository.createRecord(new Record(owner.getId(), 70.0, null, null, null));

    Record wrongUserRecord =
        new Record(savedRecord.getId(), otherUser.getId(), 71.0, null, null, null, LocalDate.now());

    assertFalse(recordRepository.updateRecord(wrongUserRecord));
    assertFalse(recordRepository.deleteRecord(savedRecord.getId(), otherUser.getId()));
    assertTrue(recordRepository.deleteRecord(savedRecord.getId(), owner.getId()));
  }
}
