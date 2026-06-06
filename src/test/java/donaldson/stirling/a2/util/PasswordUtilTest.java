package donaldson.stirling.a2.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordUtilTest {

  @Test
  void validPasswordMeetsAssignmentRules() {
    assertTrue(PasswordUtil.isValidPassword("Health1!"));
  }

  @Test
  void invalidPasswordMissingRequiredCharactersFails() {
    assertFalse(PasswordUtil.isValidPassword("healthpassword"));
    assertFalse(PasswordUtil.isValidPassword("Healthpassword"));
    assertFalse(PasswordUtil.isValidPassword("Health123"));
  }

  @Test
  void hashIsDeterministicAndNotPlainText() {
    String password = "Health1!";

    assertEquals(PasswordUtil.hash(password), PasswordUtil.hash(password));
    assertNotEquals(password, PasswordUtil.hash(password));
  }
}
