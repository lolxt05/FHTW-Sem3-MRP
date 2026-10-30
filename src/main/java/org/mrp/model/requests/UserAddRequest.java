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
        RequestFields.checkAllowed(json, ALLOWED_FIELDS);
        this.username = RequestFields.requireString(json,"username");
        if (username.isBlank() || username.length() > 50)
            throw new ValidationException("name must be 1-50 characters");

        String plainPassword = RequestFields.requireString(json,"password");
        if(!PasswordValidator.isValidPassword(plainPassword)){
            throw new ValidationException("Invalid password must have 8-72 chars, one digit, one lowercase, one uppercase, no whitespace, and one special characters.");
        }
        this.password = PasswordValidator.hashPassword(plainPassword);
    }
}
