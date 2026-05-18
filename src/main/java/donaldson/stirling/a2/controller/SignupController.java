package donaldson.stirling.a2.controller;

import java.io.IOException;
import java.net.URL;
import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML
  private TextField firstNameField;

  @FXML
  private TextField lastNameField;

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private Label errorLabel;

  public SignupController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show(){
    sceneManager.show("MyHealth | Sign Up", loadView());
  }




  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/donaldson/stirling/a2/view/signup-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch ( IOException exception){
      throw new IllegalStateException("Failed to load signup-view.fxml", exception);
    }
  }

}
