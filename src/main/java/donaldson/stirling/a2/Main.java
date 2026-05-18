package donaldson.stirling.a2;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  private AppContext appContext;

  // BEFORE GUI LOADS
  @Override
  public void init() throws Exception {
    // initialise context for the app
    this.appContext = new AppContext();
  }

  // ON GUI LOAD
  @Override
  public void start(Stage primaryStage) {
    SceneManager sceneManager = new SceneManager(primaryStage);

    new LoginController(appContext, sceneManager).show();
  }

  // ON GUI CLOSE
  @Override
  public void stop() throws Exception {
    if (this.appContext != null) {
      appContext.close();
    }
  }

  public static void main(String[] args) {
    Application.launch(args);
  }

}
