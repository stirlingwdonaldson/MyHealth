package donaldson.stirling.a2.controller;

import donaldson.stirling.a2.app.AppContext;
import donaldson.stirling.a2.app.SceneManager;
import donaldson.stirling.a2.model.Record;
import donaldson.stirling.a2.model.User;
import donaldson.stirling.a2.repository.RecordRepository;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

public class RecordsController {
  private final AppContext appContext;
  private final SceneManager sceneManager;

  @FXML private Label fullNameLabel;

  @FXML private Label usernameLabel;

  @FXML private Label recordsMessageLabel;

  @FXML private TableView<Record> recordsTable;

  @FXML private TableColumn<Record, String> dateColumn;

  @FXML private TableColumn<Record, String> weightColumn;

  @FXML private TableColumn<Record, String> temperatureColumn;

  @FXML private TableColumn<Record, String> bloodPressureColumn;

  @FXML private TableColumn<Record, String> noteColumn;

  public RecordsController(AppContext appContext, SceneManager sceneManager) {
    this.appContext = appContext;
    this.sceneManager = sceneManager;
  }

  public void show() {
    if (appContext.getCurrentUser() == null) {
      new LoginController(appContext, sceneManager).show();
      return;
    }
    sceneManager.show("MyHealth | Health Records", loadView());
  }

  @FXML
  private void initialize() {
    User user = appContext.getCurrentUser();
    fullNameLabel.setText(user.getFullName());
    usernameLabel.setText(user.getUsername());

    configureTable();
    loadRecords();
  }

  @FXML
  private void handleRefreshRecords() {
    loadRecords();
  }

  @FXML
  private void handleShowRecordForm() {
    new RecordFormController(appContext, sceneManager).show();
  }

  @FXML
  private void handleEditRecord() {
    Record selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
    if (selectedRecord == null) {
      showMessage("Select a health record to edit.", true);
      return;
    }

    new RecordFormController(appContext, sceneManager, selectedRecord).show();
  }

  @FXML
  private void handleDeleteRecord() {
    Record selectedRecord = recordsTable.getSelectionModel().getSelectedItem();
    if (selectedRecord == null) {
      showMessage("Select a health record to delete.", true);
      return;
    }

    Alert confirmation =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Delete the selected health record?",
            ButtonType.CANCEL,
            ButtonType.OK);
    confirmation.setTitle("Delete Health Record");
    confirmation.setHeaderText("This action cannot be undone.");

    if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
      return;
    }

    RecordRepository recordRepository = new RecordRepository(appContext.getConnection());

    try {
      if (!recordRepository.deleteRecord(
          selectedRecord.getId(), appContext.getCurrentUser().getId())) {
        showMessage("Unable to delete the selected health record.", true);
        return;
      }

      loadRecords();
      showMessage("Health record deleted.", false);
    } catch (SQLException exception) {
      showMessage("Unable to delete health record. Please try again.", true);
    }
  }

  @FXML
  private void handleExportRecords() {
    RecordRepository recordRepository = new RecordRepository(appContext.getConnection());

    try {
      List<Record> records = recordRepository.findAllByUserId(appContext.getCurrentUser().getId());
      if (records.isEmpty()) {
        showMessage("There are no health records to export.", true);
        return;
      }

      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Export Health Records");
      fileChooser.setInitialFileName("myhealth-records.txt");
      fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

      File selectedFile = fileChooser.showSaveDialog(recordsTable.getScene().getWindow());
      if (selectedFile == null) {
        return;
      }

      Files.writeString(selectedFile.toPath(), buildExportText(records), StandardCharsets.UTF_8);
      showMessage("Health records exported to " + selectedFile.getName() + ".", false);
    } catch (SQLException | IOException exception) {
      showMessage("Unable to export health records. Please try again.", true);
    }
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

  private void configureTable() {
    dateColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDate().toString()));
    weightColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(formatDouble(cellData.getValue().getWeight())));
    temperatureColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(formatDouble(cellData.getValue().getTemperature())));
    bloodPressureColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(formatText(cellData.getValue().getBloodPressure())));
    noteColumn.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(formatText(cellData.getValue().getNote())));
  }

  private void loadRecords() {
    RecordRepository recordRepository = new RecordRepository(appContext.getConnection());

    try {
      List<Record> records = recordRepository.findAllByUserId(appContext.getCurrentUser().getId());
      recordsTable.setItems(FXCollections.observableArrayList(records));

      if (records.isEmpty()) {
        showMessage(
            "No health records yet. Use Add Record next to create your first entry.", false);
      } else {
        showMessage(records.size() + " health record(s) loaded.", false);
      }
    } catch (SQLException exception) {
      recordsTable.setItems(FXCollections.observableArrayList());
      showMessage("Unable to load health records. Please try again.", true);
    }
  }

  private String formatDouble(Double value) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }

  private String formatText(String value) {
    if (value == null) {
      return "";
    }
    return value;
  }

  private String buildExportText(List<Record> records) {
    User user = appContext.getCurrentUser();
    StringBuilder builder = new StringBuilder();
    builder.append("MyHealth Records").append(System.lineSeparator());
    builder
        .append("User: ")
        .append(user.getFullName())
        .append(" (")
        .append(user.getUsername())
        .append(")");
    builder.append(System.lineSeparator()).append(System.lineSeparator());

    for (Record record : records) {
      builder.append("Date: ").append(record.getDate()).append(System.lineSeparator());
      builder.append("Weight: ").append(formatText(formatDouble(record.getWeight())));
      builder.append(System.lineSeparator());
      builder.append("Temperature: ").append(formatText(formatDouble(record.getTemperature())));
      builder.append(System.lineSeparator());
      builder.append("Blood Pressure: ").append(formatText(record.getBloodPressure()));
      builder.append(System.lineSeparator());
      builder.append("Note: ").append(formatText(record.getNote()));
      builder.append(System.lineSeparator()).append(System.lineSeparator());
    }

    return builder.toString();
  }

  private void showMessage(String message, boolean isError) {
    recordsMessageLabel.setText(message);
    recordsMessageLabel.setStyle(
        isError
            ? "-fx-text-fill: #c81e1e; -fx-font-size: 12px;"
            : "-fx-text-fill: #486581; -fx-font-size: 12px;");
  }

  private Parent loadView() {
    try {
      URL resource = getClass().getResource("/records-view.fxml");
      FXMLLoader loader = new FXMLLoader(resource);
      loader.setController(this);
      return loader.load();
    } catch (IOException exception) {
      throw new IllegalStateException("Failed to load records-view.fxml", exception);
    }
  }
}
