package com.lims.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for secure BCrypt hashing and password validation.
 */
public class PasswordUtils {

    private static final int LOG_ROUNDS = 10;

    /**
     * Hashes a plain-text password using BCrypt with salt.
     *
     * @param plainPassword Raw password string
     * @return 60-character BCrypt hashed string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    /**
     * Verifies if a plain password matches the hashed password.
     *
     * @param plainPassword Raw password string
     * @param hashedPassword Stored BCrypt hash
     * @return true if matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

