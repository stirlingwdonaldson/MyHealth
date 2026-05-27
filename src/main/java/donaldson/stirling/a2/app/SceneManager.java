package donaldson.stirling.a2.app;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
  private final Stage stage;

  private static final double DEFAULT_WIDTH = 1000;
  private static final double DEFAULT_HEIGHT = 700;

  public SceneManager(Stage stage) {
    this.stage = stage;
  }

  public void show(String title, Parent root) {
    Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    stage.setTitle(title);
    stage.setScene(scene);
    stage.show();
  }

  // can remove, for future-proofing.
  public void show(Parent root) {
    show("MyHealth", root);
  }
}

  // will manage visible scenes.
  // manage as a stack? if stack >1 (exc. login), add tab component?
  // if !isAuthenticated, login screen permanently top of stack (+ make stack
  // immutable)?

// add interface for scenes?
