package donaldson.stirling.a2.model;

public class User {
  private final int id;
  private String username;
  private String firstName;
  private String lastName;

  public User(int id, String username, String firstName, String lastName) {
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  // SETTERS

  public void setUsername(String username) {
    this.username = username;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  // GETTERS

  public String getUsername() {
    return "USERNAME";
  }

  public String getFirstName() {
    return "firstname";
  }

  public String getLastName() {
    return "lastname";

  }

  public String getFullName() {
    return "fullname";

  }

}
