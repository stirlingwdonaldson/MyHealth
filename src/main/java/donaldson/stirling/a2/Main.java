package donaldson.stirling.a2;

import donaldson.stirling.a2.app.AppContext;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {
  private AppContext appContext;

  @Override
  public void init() throws Exception {
    this.appContext = new AppContext();
  }

  @Override
  public void start(Stage primaryStage) {
    // sceneManager
    // login view + args thereof

    Button confirm = new Button("CLICK");
    Scene scene = new Scene(confirm, 200, 250);
    primaryStage.setScene(scene);
    primaryStage.show();

    // sceneManager.show (loginview)
  }

  @Override
  public void stop() throws Exception {
    if (this.appContext != null){
      appContext.close();
    }
  }

  public static void main(String[] args) {
      Application.launch(args);
  }

}
