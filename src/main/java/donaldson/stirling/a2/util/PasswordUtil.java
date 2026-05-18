package donaldson.stirling.a2.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

  private PasswordUtil() {

  }

  public static String hash(String password) {

    try{
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

      StringBuilder builder = new StringBuilder();
      for(byte hashByte : hashBytes){
        builder.append(String.format("%02x", hashByte));
      }
      return builder.toString();
    } catch (NoSuchAlgorithmException exception){
      throw new IllegalStateException("SHA-256 not available", exception);
    }
  }

}
