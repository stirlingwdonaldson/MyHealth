package donaldson.stirling.a2.controller;

import java.io.IOException;
import java.net.URL;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class DashboardController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML
  private Label welcomeLabel;

  @FXML
  private Label fullNameLabel;

  @FXML
  private Label usernameLabel;

  public DashboardController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show() {
    if (appContext.getCurrentUser() == null) {
      new LoginController(appContext, sceneManager).show();
      return;
    }
    sceneManager.show("MyHealth | Dashboard", loadView());
  }

  @FXML
  private void initialize() {
    User user  = appContext.getCurrentUser();
    
    welcomeLabel.setText("Welcome, " + user.getUsername() + "!!");
    fullNameLabel.setText(user.getFullName());
    usernameLabel.setText(user.getUsername());
  }

  @FXML
  private void handleLogout() {
    appContext.setCurrentUser(null);
    new LoginController(appContext, sceneManager).show();
  }

  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/dashboard-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to load dashboard-view.fxml", exception);
    }
  }

}
