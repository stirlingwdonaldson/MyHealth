package donaldson.stirling.a2.component;

public class AppButton extends javafx.scene.control.Button {
  public enum Variant {
    PRIMARY,
    SECONDARY
  }

  public AppButton(String text, Variant variant) {
    super(text);
    setPrefHeight(44);
    setMaxWidth(Double.MAX_VALUE);
    setStyle(variant == Variant.PRIMARY ? primaryStyle() : secondaryStyle());
  }

  private String primaryStyle() {
    return """
            -fx-background-color: #2563eb;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-cursor: hand;
        """;
  }

  private String secondaryStyle() {
    return """
            -fx-background-color: transparent;
            -fx-text-fill: #2563eb;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-border-color: #2563eb;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-cursor: hand;
        """;
  }
}
