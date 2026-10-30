package org.mrp.model.requests;

import org.json.JSONObject;
import org.mrp.model.exceptions.ValidationException;

import java.util.Set;

final class RequestFields {

    private RequestFields() {
    }

    static void checkAllowed(JSONObject json, Set<String> allowedFields) {
        for (String key : json.keySet()) {
            if (!allowedFields.contains(key)) {
                throw new ValidationException("Unknown field: " + key);
            }
        }
    }

    static String requireString(JSONObject json, String key) {
        if (!json.has(key) || json.isNull(key))
            throw new ValidationException(key + " is required");
        Object value = json.get(key);
        if (!(value instanceof String s))
            throw new ValidationException(key + " must be a string");
        return s;
    }

    static int requireInt(JSONObject json, String key) {
        if (!json.has(key) || json.isNull(key))
            throw new ValidationException(key + " is required");
        Object value = json.get(key);
        if (!(value instanceof Integer i))
            throw new ValidationException(key + " must be an integer");
        return i;
    }
}
