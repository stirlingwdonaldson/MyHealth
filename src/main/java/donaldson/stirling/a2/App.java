package donaldson.stirling.a2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class App extends Application{
  
  @Override
  public void start(Stage primaryStage) {
    Button confirm = new Button("CLIGGER");
    Scene scene = new Scene(confirm, 200, 250);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

public static void main(String[] args)  {
    System.out.println("Running App...");
    Application.launch(args);
    
  }

}
