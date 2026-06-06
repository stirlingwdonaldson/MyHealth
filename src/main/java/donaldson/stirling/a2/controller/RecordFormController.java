package donaldson.stirling.a2.controller;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.Record;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.RecordRepository;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RecordFormController {
  private final AppContext appContext;
  private final SceneManager sceneManager;
  private final Record editingRecord;

  @FXML private Label fullNameLabel;

  @FXML private Label usernameLabel;

  @FXML private Label formTitleLabel;

  @FXML private TextField weightField;

  @FXML private TextField temperatureField;

  @FXML private TextField bloodPressureField;

  @FXML private TextArea noteField;

  @FXML private Button saveRecordButton;

  @FXML private Label recordMessageLabel;

  public RecordFormController(AppContext appContext, SceneManager sceneManager) {
    this(appContext, sceneManager, null);
  }

  public RecordFormController(
      AppContext appContext, SceneManager sceneManager, Record editingRecord) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
    this.editingRecord = editingRecord;
  }

  public void show() {
    if (appContext.getCurrentUser() == null) {
      new LoginController(appContext, sceneManager).show();
      return;
    }
    sceneManager.show(isEditing() ? "MyHealth | Edit Record" : "MyHealth | Add Record", loadView());
  }

  @FXML
  private void initialize() {
    User user = appContext.getCurrentUser();
    fullNameLabel.setText(user.getFullName());
    usernameLabel.setText(user.getUsername());

    if (isEditing()) {
      formTitleLabel.setText("Edit Health Record");
      saveRecordButton.setText("Save Changes");
      populateForm(editingRecord);
    }
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

    Record record = buildRecord(weight, temperature, bloodPressure, note);
    String validationMessage = record.getValidationMessage();
    if (validationMessage != null) {
      showMessage(validationMessage, true);
      return;
    }

    RecordRepository recordRepository = new RecordRepository(appContext.getConnection());

    try {
      if (isEditing()) {
        if (!recordRepository.updateRecord(record)) {
          showMessage("Unable to update health record. Please try again.", true);
          return;
        }
      } else {
        recordRepository.createRecord(record);
      }
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

  @FXML
  private void handleExit() {
    Platform.exit();
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

  private Record buildRecord(Double weight, Double temperature, String bloodPressure, String note) {
    if (!isEditing()) {
      return new Record(
          appContext.getCurrentUser().getId(), weight, temperature, bloodPressure, note);
    }

    return new Record(
        editingRecord.getId(),
        editingRecord.getUserId(),
        weight,
        temperature,
        bloodPressure,
        note,
        editingRecord.getDate());
  }

  private void populateForm(Record record) {
    weightField.setText(record.getWeight() == null ? "" : record.getWeight().toString());
    temperatureField.setText(
        record.getTemperature() == null ? "" : record.getTemperature().toString());
    bloodPressureField.setText(record.getBloodPressure() == null ? "" : record.getBloodPressure());
    noteField.setText(record.getNote() == null ? "" : record.getNote());
  }

  private boolean isEditing() {
    return editingRecord != null;
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
