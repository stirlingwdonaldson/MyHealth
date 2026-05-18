package donaldson.stirling.a2.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label errorLabel;

  public LoginController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show() {
    sceneManager.show("MyHealth | Login", loadView());
  }

  @FXML
  private void handleLogin() {
    hideError();

    String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
    String password = passwordField.getText() == null ? "" : passwordField.getText().trim();

    if (username.isEmpty()) {
      showError("Please enter your username.");
      return;
    }

    if (password.isEmpty()) {
      showError("Please enter your password.");
      return;
    }

    UserRepository userRepository = new UserRepository(appContext.getConnection());

    try {
      User user = userRepository.authenticate(username, password);

      if(user == null){
        showError("Incorrect username or password");
        return;
      }

      appContext.setCurrentUser(user);
      new DashboardController(appContext, sceneManager).show();
    } catch (SQLException exception){
      showError("Unable to login. Please try again.");
    }
  }

  @FXML
  private void handleShowSignup() {
    new SignupController(appContext, sceneManager).show();
  }

  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/login-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to load login-view.fxml", exception);
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
