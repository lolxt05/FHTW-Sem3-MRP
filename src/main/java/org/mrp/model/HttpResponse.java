package org.mrp.model;

import lombok.Data;

@Data
public class HttpResponse {
    public HttpResponse(int status, String body) {
        this.statusCode = status;
        this.body = body;
    }
    int statusCode;
    String body;
}
