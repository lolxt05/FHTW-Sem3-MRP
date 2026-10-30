package org.mrp.model.requests;

import lombok.Data;
import org.json.JSONObject;

import java.util.Set;

@Data
public final class LoginRequest {

    String username;
    String password;
    private static final Set<String> ALLOWED_FIELDS = Set.of("username", "password");


    public LoginRequest(JSONObject json) {
        RequestFields.checkAllowed(json, ALLOWED_FIELDS);
        this.username = RequestFields.requireString(json,"username");
        this.password = RequestFields.requireString(json,"password");
    }
}
