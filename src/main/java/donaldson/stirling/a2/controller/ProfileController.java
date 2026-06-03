package donaldson.stirling.a2.controller;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.UserRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfileController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML private Label fullNameLabel;

  @FXML private Label usernameLabel;

  @FXML private TextField profileFirstNameField;

  @FXML private TextField profileLastNameField;

  @FXML private Label profileMessageLabel;

  public ProfileController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show() {
    if (appContext.getCurrentUser() == null) {
      new LoginController(appContext, sceneManager).show();
      return;
    }
    sceneManager.show("MyHealth | Profile", loadView());
  }

  @FXML
  private void initialize() {
    User user = appContext.getCurrentUser();

    refreshUserDetails(user);
    profileFirstNameField.setText(user.getFirstName());
    profileLastNameField.setText(user.getLastName());
  }

  @FXML
  private void handleSaveProfile() {
    User user = appContext.getCurrentUser();
    String firstName =
        profileFirstNameField.getText() == null ? "" : profileFirstNameField.getText().trim();
    String lastName =
        profileLastNameField.getText() == null ? "" : profileLastNameField.getText().trim();

    if (firstName.isEmpty()) {
      showProfileMessage("Please enter your first name.", true);
      return;
    }

    if (lastName.isEmpty()) {
      showProfileMessage("Please enter your last name.", true);
      return;
    }

    UserRepository userRepository = new UserRepository(appContext.getConnection());

    try {
      if (!userRepository.updateProfile(user.getId(), firstName, lastName)) {
        showProfileMessage("Unable to update profile. Please try again.", true);
        return;
      }

      user.setFirstName(firstName);
      user.setLastName(lastName);
      refreshUserDetails(user);
      showProfileMessage("Profile updated.", false);
    } catch (SQLException exception) {
      showProfileMessage("Unable to update profile. Please try again.", true);
    }
  }

  @FXML
  private void handleShowRecordForm() {
    new RecordFormController(appContext, sceneManager).show();
  }

  @FXML
  private void handleShowRecords() {
    new RecordsController(appContext, sceneManager).show();
  }

  @FXML
  private void handleShowDashboard() {
    new DashboardController(appContext, sceneManager).show();
  }

  @FXML
  private void handleLogout() {
    appContext.setCurrentUser(null);
    new LoginController(appContext, sceneManager).show();
  }

  private void refreshUserDetails(User user) {
    fullNameLabel.setText(user.getFullName());
    usernameLabel.setText(user.getUsername());
  }

  private void showProfileMessage(String message, boolean isError) {
    profileMessageLabel.setText(message);
    profileMessageLabel.setStyle(
        isError
            ? "-fx-text-fill: #c81e1e; -fx-font-size: 12px;"
            : "-fx-text-fill: #0f766e; -fx-font-size: 12px;");
  }

  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/profile-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to load profile-view.fxml", exception);
    }
  }
}
