
package donaldson.stirling.a2.controller;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.Record;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.RecordRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RecordFormController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML private Label fullNameLabel;

  @FXML private Label usernameLabel;

  @FXML private TextField weightField;

  @FXML private TextField temperatureField;

  @FXML private TextField bloodPressureField;

  @FXML private TextArea noteField;

  @FXML private Label recordMessageLabel;

  public RecordFormController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show() {
    if (appContext.getCurrentUser() == null) {
      new LoginController(appContext, sceneManager).show();
      return;
    }
    sceneManager.show("MyHealth | Add Record", loadView());
  }

  @FXML
  private void initialize() {
    User user = appContext.getCurrentUser();
    fullNameLabel.setText(user.getFullName());
    usernameLabel.setText(user.getUsername());
  }

  @FXML
  private void handleSaveRecord() {
    showMessage("", false);

    Double weight = parseOptionalDouble(weightField, "Weight");
    if (hasError()) {
      return;
    }

    Double temperature = parseOptionalDouble(temperatureField, "Temperature");
    if (hasError()) {
      return;
    }

    String bloodPressure = cleanText(bloodPressureField.getText());
    String note = cleanText(noteField.getText());

    Record record =
        new Record(appContext.getCurrentUser().getId(), weight, temperature, bloodPressure, note);
    String validationMessage = record.getValidationMessage();
    if (validationMessage != null) {
      showMessage(validationMessage, true);
      return;
    }

    RecordRepository recordRepository = new RecordRepository(appContext.getConnection());

    try {
      recordRepository.createRecord(record);
      new RecordsController(appContext, sceneManager).show();
    } catch (SQLException exception) {
      showMessage("Unable to save health record. Please try again.", true);
    }
  }

  @FXML
  private void handleClearForm() {
    weightField.clear();
    temperatureField.clear();
    bloodPressureField.clear();
    noteField.clear();
    showMessage("", false);
  }

  @FXML
  private void handleShowRecords() {
    new RecordsController(appContext, sceneManager).show();
  }

  @FXML
  private void handleShowDashboard() {
    new DashboardController(appContext, sceneManager).show();
  }

  @FXML
  private void handleShowProfile() {
    new ProfileController(appContext, sceneManager).show();
  }

  @FXML
  private void handleLogout() {
    appContext.setCurrentUser(null);
    new LoginController(appContext, sceneManager).show();
  }

  private Double parseOptionalDouble(TextField field, String label) {
    String value = cleanText(field.getText());
    if (value == null) {
      return null;
    }

    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException exception) {
      showMessage(label + " must be a number.", true);
      return null;
    }
  }

  private boolean hasError() {
    return recordMessageLabel.getText() != null && !recordMessageLabel.getText().isBlank();
  }

  private String cleanText(String value) {
    if (value == null || value.trim().isEmpty()) {
      return null;
    }
    return value.trim();
  }

  private void showMessage(String message, boolean isError) {
    recordMessageLabel.setText(message);
    recordMessageLabel.setStyle(
        isError
            ? "-fx-text-fill: #c81e1e; -fx-font-size: 12px;"
            : "-fx-text-fill: #486581; -fx-font-size: 12px;");
  }

  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/record-form-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to load record-form-view.fxml", exception);
    }
  }
}
