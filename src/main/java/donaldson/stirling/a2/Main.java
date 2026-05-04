package donaldson.stirling.a2;

import java.sql.Connection;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.database.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
 
    AppContext appContext = new AppContext();

    // sceneManager

    // login view

    Button confirm = new Button("CLIGGER");
    Scene scene = new Scene(confirm, 200, 250);
    primaryStage.setScene(scene);
    primaryStage.show();

    //sceneManager.show (loginview)
  }

  public static void main(String[] args) {
    Application.launch(args);
  }

}
