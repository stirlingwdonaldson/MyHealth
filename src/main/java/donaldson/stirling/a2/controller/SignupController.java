package donaldson.stirling.a2.controller;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.UserRepository;
import donaldson.stirling.a2.util.PasswordUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML private TextField firstNameField;

  @FXML private TextField lastNameField;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML private Label errorLabel;

  public SignupController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show() {
    sceneManager.show("MyHealth | Sign Up", loadView());
  }

  @FXML
  private void handleSignUp() {
    hideError();

    String firstName = firstNameField.getText() == null ? "" : firstNameField.getText().trim();
    String lastName = lastNameField.getText() == null ? "" : lastNameField.getText().trim();
    String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
    String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

    if (firstName.isEmpty()) {
      showError("Please enter your first name.");
      return;
    }

    if (lastName.isEmpty()) {
      showError("Please enter your last name.");
      return;
    }
    if (username.isEmpty()) {
      showError("Please enter a username.");
      return;
    }
    if (password.isEmpty()) {
      showError("Please enter a password.");
      return;
    }

    if (!PasswordUtil.isValidPassword(password)) {
      showError(
          "Password must be at least 8 characters and include letters, numbers, one uppercase letter, and one special character.");
      return;
    }

    UserRepository userRepository = new UserRepository(appContext.getConnection());

    try {

      if (userRepository.usernameExists(username)) {
        showError("That username is already taken.");
        return;
      }

      User user = userRepository.createUser(username, password, firstName, lastName);
      appContext.setCurrentUser(user);
      new DashboardController(appContext, sceneManager).show();

    } catch (SQLException exception) {
      showError("Unable to create account. Please try again.");
    }
    // @TODO add additional handling of inputs

  }

  @FXML
  private void handleBackToLogin() {
    new LoginController(appContext, sceneManager).show();
  }

  @FXML
  private void handleExit() {
    Platform.exit();
  }

  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/signup-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to load signup-view.fxml", exception);
    }
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  private void hideError() {
    errorLabel.setText("");
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }
}
