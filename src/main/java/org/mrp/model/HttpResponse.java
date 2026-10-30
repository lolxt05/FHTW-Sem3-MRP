package org.mrp.model;

import lombok.Data;
import org.json.JSONObject;

@Data
public class HttpResponse {
    public HttpResponse(int status, String body) {
        this.statusCode = status;
        this.body = body;
    }

    public static HttpResponse message(int status, String message) {
        return new HttpResponse(status, new JSONObject().put("message", message).toString());
    }

    int statusCode;
    String body;
}
