package donaldson.stirling.a2.controller;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.Record;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.RecordRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class DashboardController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML private Label welcomeLabel;

  @FXML private Label fullNameLabel;

  @FXML private Label usernameLabel;

  @FXML private Label recordCountLabel;

  @FXML private Label latestRecordLabel;

  @FXML private Label profileStatusLabel;

  @FXML private Label dashboardMessageLabel;

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
    User user = appContext.getCurrentUser();

    refreshUserDetails(user);
    refreshRecordSummary(user);
  }

  @FXML
  private void handleShowRecordForm() {
    new RecordFormController(appContext, sceneManager).show();
  }

  @FXML
  private void handleShowRecords() {
    new RecordsController(appContext, sceneManager).show();
  }

  @FXML
  private void handleShowProfile() {
    new ProfileController(appContext, sceneManager).show();
  }

  @FXML
  private void handleLogout() {
    appContext.setCurrentUser(null); // clear context of user
    new LoginController(appContext, sceneManager).show(); // force logout action
  }

  @FXML
  private void handleExit() {
    Platform.exit();
  }

  private void refreshUserDetails(User user) {
    welcomeLabel.setText("Welcome, " + user.getFullName());
    fullNameLabel.setText(user.getFullName());
    usernameLabel.setText(user.getUsername());
    profileStatusLabel.setText(user.getFullName());
  }

  private void refreshRecordSummary(User user) {
    RecordRepository recordRepository = new RecordRepository(appContext.getConnection());

    try {
      List<Record> records = recordRepository.findAllByUserId(user.getId());
      recordCountLabel.setText(String.valueOf(records.size()));

      if (records.isEmpty()) {
        latestRecordLabel.setText("No records yet");
        dashboardMessageLabel.setText(
            "Start by adding your first health record. You can enter any one or more fields.");
        return;
      }

      latestRecordLabel.setText(records.get(0).getDate().toString());
      dashboardMessageLabel.setText("Your records are saved and shown newest first.");
    } catch (SQLException exception) {
      recordCountLabel.setText("-");
      latestRecordLabel.setText("Unavailable");
      dashboardMessageLabel.setText("Unable to load dashboard summary. Please try again.");
    }
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
