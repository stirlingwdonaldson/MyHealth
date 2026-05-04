package donaldson.stirling.a2.view;

import donaldson.stirling.a2.component.AppButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView {

  private final StackPane root;

  public LoginView() {

    // Root container
    root = new StackPane();
    root.setStyle("-fx-background-color: #f4f6f8;");

    // Main login card
    VBox card = new VBox(18);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(40));
    card.setMaxWidth(390);
    card.setMaxHeight(460);

    card.setStyle("""
            -fx-background-color: white;
            -fx-border-radius: 18;
            -fx-background-radius: 18;
        """);

    Text subtitle = new Text("Log in to continue");
    subtitle.setFont(Font.font("System", 14));
    subtitle.setStyle("-fx-fill: #6b7280;");

    VBox headingBox = new VBox(6, subtitle);
    headingBox.setAlignment(Pos.CENTER);

    // Username input
    TextField usernameField = new TextField();
    usernameField.setPromptText("Username");
    usernameField.setPrefHeight(44);
    usernameField.setMaxWidth(Double.MAX_VALUE);
    usernameField.setStyle(inputStyle());

    // Password input
    PasswordField passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    passwordField.setPrefHeight(44);
    passwordField.setMaxWidth(Double.MAX_VALUE);
    passwordField.setStyle(inputStyle());

    VBox inputBox = new VBox(12, usernameField, passwordField);
    inputBox.setMaxWidth(Double.MAX_VALUE);

    // Login button
    AppButton loginButton = new AppButton("Login", AppButton.Variant.PRIMARY);

    // Sign up button
    AppButton signUpButton = new AppButton("Sign Up", AppButton.Variant.SECONDARY);

    VBox buttonBox = new VBox(10, loginButton, signUpButton);
    buttonBox.setMaxWidth(Double.MAX_VALUE);

    card.getChildren().addAll(
        headingBox,
        inputBox,
        buttonBox);

    root.getChildren().add(card);

  }

  public Parent getRoot() {
    return root;
  }

  private String inputStyle() {
    return """
            -fx-background-color: #f9fafb;
            -fx-border-color: #d1d5db;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 0 14 0 14;
            -fx-font-size: 14px;
            -fx-text-fill: #111827;
            -fx-prompt-text-fill: #9ca3af;
        """;
  }
}
