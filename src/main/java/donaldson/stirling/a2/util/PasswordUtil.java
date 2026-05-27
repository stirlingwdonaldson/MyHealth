package donaldson.stirling.a2.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

  private PasswordUtil() {}

  public static boolean isValidPassword(String password) {
    if (password == null || password.length() < 8) {
      return false;
    }

    boolean hasLetter = false;
    boolean hasNumber = false;
    boolean hasUppercase = false;
    boolean hasSpecial = false;

    // for each letter, check if it satisfies conditions.
    for (int i = 0; i < password.length(); i++) {
      char character = password.charAt(i);

      if (Character.isLetter(character)) {
        hasLetter = true;
      }

      if (Character.isDigit(character)) {
        hasNumber = true;
      }

      if (Character.isUpperCase(character)) {
        hasUppercase = true;
      }

      if (!Character.isLetterOrDigit(character) && !Character.isWhitespace(character)) {
        hasSpecial = true;
      }
    }

    // propositional logic  (AND) on return statement
    // if one is false, the whole return statement is
    return hasLetter && hasNumber && hasUppercase && hasSpecial;
  }

  public static String hash(String password) {

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

      StringBuilder builder = new StringBuilder();
      for (byte hashByte : hashBytes) {
        builder.append(String.format("%02x", hashByte));
      }
      return builder.toString();
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 not available", exception);
    }
  }
}
