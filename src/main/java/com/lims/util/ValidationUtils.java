package com.lims.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Validation and sanitization helpers for user input and forms.
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$"
    );

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrBlank(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isNullOrBlank(phone)) return false;
        String cleanPhone = phone.replaceAll("[\\s.-]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    public static int parsePositiveInt(String value, int defaultValue) {
        if (isNullOrBlank(value)) return defaultValue;
        try {
            int num = Integer.parseInt(value.trim());
            return num > 0 ? num : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static BigDecimal parseBigDecimal(String value, BigDecimal defaultValue) {
        if (isNullOrBlank(value)) return defaultValue;
        try {
            return new BigDecimal(value.trim().replaceAll(",", ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String sanitize(String input) {
        if (input == null) return null;
        return input.trim()
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}

