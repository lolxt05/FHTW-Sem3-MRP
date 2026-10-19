package org.mrp.model.requests;

import lombok.Data;
import org.json.JSONObject;
import org.mrp.model.Session;
import org.mrp.model.exceptions.ValidationException;
import org.mrp.repository.sessions.SessionManager;

import java.util.Set;
import java.util.UUID;

@Data
public final class UserDeleteRequest {
    private SessionManager sessionRepositoryCache = SessionManager.getInstance();
    private String token;
    private UUID userId;
    private static final Set<String> ALLOWED_FIELDS = Set.of("token");


    public UserDeleteRequest(JSONObject json) {
        for (String key : json.keySet()) {
            if (!ALLOWED_FIELDS.contains(key)) {
                throw new ValidationException("Unknown field: " + key);
            }
        }
        this.token = requireString(json,"token");

        if (token.isBlank())
            throw new ValidationException("token must be there");

        Session s =  sessionRepositoryCache.getSession(token);
        userId = s.getUserId();
    }

    private static String requireString(JSONObject json, String key) {
        if (!json.has(key) || json.isNull(key))
            throw new ValidationException(key + " is required");
        Object value = json.get(key);
        if (!(value instanceof String s))
            throw new ValidationException(key + " must be a string");
        return s;
    }

    private static UUID requireUUID(JSONObject json, String key) {
        if (!json.has(key) || json.isNull(key))
            throw new ValidationException(key + " is required");
        Object value = json.get(key);
        if (!(value instanceof UUID uuid))
            throw new ValidationException(key + " must be an uuid");
        return uuid;
    }
}