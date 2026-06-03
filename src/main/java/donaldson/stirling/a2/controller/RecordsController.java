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
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

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

