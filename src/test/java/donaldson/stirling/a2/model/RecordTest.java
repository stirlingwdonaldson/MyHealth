package donaldson.stirling.a2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RecordTest {

  @Test
  void emptyRecordIsInvalid() {
    Record record = new Record(1, null, null, null, null);

    assertFalse(record.isValid());
    assertEquals("At least one health record field must be completed.", record.getValidationMessage());
    assertThrows(IllegalArgumentException.class, record::validate);
  }

  @Test
  void recordWithOneFieldIsValid() {
    Record record = new Record(1, 72.5, null, null, null);

    assertTrue(record.isValid());
    assertNull(record.getValidationMessage());
  }

  @Test
  void noteWithMoreThanFiftyWordsIsInvalid() {
    String note = "word ".repeat(51);
    Record record = new Record(1, null, null, null, note);

    assertFalse(record.isValid());
    assertEquals("Note must not exceed 50 words.", record.getValidationMessage());
  }

  @Test
  void countWordsIgnoresExtraWhitespace() {
    assertEquals(3, Record.countWords("  first\nsecond   third  "));
  }
}
