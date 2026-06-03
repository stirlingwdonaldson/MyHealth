package donaldson.stirling.a2.model;

import java.time.LocalDate;

public class Record {
  public static final int MAX_NOTE_WORDS = 50;

  private final int id;
  private final int userId;
  private Double weight;
  private Double temperature;
  private String bloodPressure;
  private String note;
  private LocalDate date;

  public Record(
      int id,
      int userId,
      Double weight,
      Double temperature,
      String bloodPressure,
      String note,
      LocalDate date) {
    this.id = id;
    this.userId = userId;
    this.weight = weight;
    this.temperature = temperature;
    this.bloodPressure = cleanText(bloodPressure);
    this.note = cleanText(note);
    this.date = date == null ? LocalDate.now() : date;
  }

  public Record(int userId, Double weight, Double temperature, String bloodPressure, String note) {
    this(0, userId, weight, temperature, bloodPressure, note, LocalDate.now());
  }

  public boolean isValid() {
    return getValidationMessage() == null;
  }

  public String getValidationMessage() {
    if (weight == null && temperature == null && isBlank(bloodPressure) && isBlank(note)) {
      return "At least one health record field must be completed.";
    }

    if (countWords(note) > MAX_NOTE_WORDS) {
      return "Note must not exceed 50 words.";
    }

    return null;
  }

  public void validate() {
    String validationMessage = getValidationMessage();
    if (validationMessage != null) {
      throw new IllegalArgumentException(validationMessage);
    }
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public Double getWeight() {
    return weight;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public String getBloodPressure() {
    return bloodPressure;
  }

  public void setBloodPressure(String bloodPressure) {
    this.bloodPressure = cleanText(bloodPressure);
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = cleanText(note);
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date == null ? LocalDate.now() : date;
  }

  public static int countWords(String text) {
    if (isBlank(text)) {
      return 0;
    }

    return text.trim().split("\\s+").length;
  }

  private static boolean isBlank(String text) {
    return text == null || text.trim().isEmpty();
  }

  private static String cleanText(String text) {
    if (isBlank(text)) {
      return null;
    }
    return text.trim();
  }
}
