package org.mrp.model.requests;

import lombok.Data;
import org.json.JSONObject;
import org.mrp.model.exceptions.ValidationException;
import org.mrp.services.PasswordValidator;

import java.util.Set;
@Data
public final class UserAddRequest {

    String username;
    String password;
    private static final Set<String> ALLOWED_FIELDS = Set.of("username", "password");


    public UserAddRequest(JSONObject json) {
        for (String key : json.keySet()) {
            if (!ALLOWED_FIELDS.contains(key)) {
                throw new ValidationException("Unknown field: " + key);
            }
        }
        this.username = requireString(json,"username");
        if(!PasswordValidator.isValidPassword(requireString(json,"password"))){
            throw new ValidationException("Invalid password must have 8-72 chars, one digit, one lowercase, one uppercase, no whitespace, and one special characters.");

        }
        this.password = PasswordValidator.hashPassword(requireString(json,"password"));

        if (username.isBlank() || username.length() > 50)
            throw new ValidationException("name must be 1-50 characters");
        if(password.isBlank())
            throw new ValidationException("password must be set");
    }

    private static String requireString(JSONObject json, String key) {
        if (!json.has(key) || json.isNull(key))
            throw new ValidationException(key + " is required");
        Object value = json.get(key);
        if (!(value instanceof String s))
            throw new ValidationException(key + " must be a string");
        return s;
    }

    private static int requireInt(JSONObject json, String key) {
        if (!json.has(key) || json.isNull(key))
            throw new ValidationException(key + " is required");
        Object value = json.get(key);
        if (!(value instanceof Integer i))
            throw new ValidationException(key + " must be an integer");
        return i;
    }
}