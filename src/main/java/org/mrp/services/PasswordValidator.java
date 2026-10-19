package org.mrp.services;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9])(?!.*\\s).{8,72}$"
    );

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verifyPassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }
}