package donaldson.stirling.a2.model;

public class User {
  private final int id;
  private String username;
  private String hashedPassword;
  private String firstName;
  private String lastName;

  public User(int id, String username, String hashedPassword, String firstName, String lastName) {
    this.id = id;
    this.username = username;
    this.hashedPassword = hashedPassword;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  // SETTERS

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.hashedPassword = password;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  // GETTERS

  public int getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public String getHashedPassword() {
    return this.hashedPassword;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }
}
