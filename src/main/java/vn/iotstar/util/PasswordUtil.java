package vn.iotstar.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** PBKDF2 password and short-lived OTP hashing helpers. */
public final class PasswordUtil {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 210_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hash(String value) {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] hash = derive(value, salt, ITERATIONS);
        return "pbkdf2-sha256$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verify(String value, String encoded) {
        if (value == null || encoded == null) return false;
        String[] pieces = encoded.split("\\$", -1);
        if (pieces.length != 4 || !"pbkdf2-sha256".equals(pieces[0])) {
            return false;
        }
        try {
            int iterations = Integer.parseInt(pieces[1]);
            if (iterations < 100_000 || iterations > 2_000_000) return false;
            byte[] salt = Base64.getDecoder().decode(pieces[2]);
            byte[] expected = Base64.getDecoder().decode(pieces[3]);
            byte[] actual = derive(value, salt, iterations);
            return MessageDigest.isEqual(expected, actual);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /** Temporary compatibility for accounts created before password hashing. */
    public static boolean matchesLegacyPlaintext(String candidate, String stored) {
        if (candidate == null || stored == null || stored.startsWith("pbkdf2-sha256$")) {
            return false;
        }
        return MessageDigest.isEqual(candidate.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                stored.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    private static byte[] derive(String value, byte[] salt, int iterations) {
        char[] chars = value.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, KEY_LENGTH);
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("Không thể tạo password hash.", e);
        } finally {
            spec.clearPassword();
            Arrays.fill(chars, '\0');
        }
    }
}
