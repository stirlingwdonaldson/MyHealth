package donaldson.stirling.a2;

import donaldson.stirling.a2.database.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application{
  
  @Override
  public void start(Stage primaryStage) {
    Button confirm = new Button("CLIGGER");
    Scene scene = new Scene(confirm, 200, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

public static void main(String[] args)  {
    DatabaseConnection.DBConnection();

    System.out.println("Running App...");
    Application.launch(args);

    
  }

}
