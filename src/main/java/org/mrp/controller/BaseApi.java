package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import org.mrp.model.HttpResponse;
import org.mrp.model.Session;
import org.mrp.model.exceptions.SessionTokenInvalid;
import org.mrp.model.exceptions.ValidationException;
import org.mrp.repository.sessions.SessionManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseApi implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(BaseApi.class.getName());
    private static final String BEARER = "Bearer ";

    SessionManager sessionManager = SessionManager.getInstance();
    HttpResponse notFound = HttpResponse.message(404, "Not Found");
    HttpResponse notAllowed = HttpResponse.message(405, "Method Not Allowed");
    HttpResponse error = HttpResponse.message(500, "Internal Server Error");
    private final String resource;

    protected BaseApi(String resource) {
        this.resource = resource;
    }

    protected abstract void route(HttpExchange exchange, String[] parts) throws IOException;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        try {
            if (parts.length >= 2 && parts[1].equals(resource)) {
                route(exchange, parts);
            } else {
                send(exchange, notFound);
            }
        } catch (ValidationException e) {
            send(exchange, HttpResponse.message(400, e.getMessage()));
        } catch (SessionTokenInvalid e) {
            send(exchange, HttpResponse.message(401, e.getMessage()));
        } catch (org.json.JSONException e) {
            send(exchange, HttpResponse.message(400, "Invalid JSON"));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling request", e);
            send(exchange, error);
        }
    }

    protected Session authenticate(HttpExchange exchange) {
        String header = exchange.getRequestHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith(BEARER)) {
            throw new SessionTokenInvalid("Authorization header is required");
        }
        return sessionManager.getSession(header.substring(BEARER.length()));
    }

    protected JSONObject readJson(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return new JSONObject(body);
    }

    protected UUID parseId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid id");
        }
    }

    protected String getQueryParam(HttpExchange exchange, String key) {
        String query = exchange.getRequestURI().getRawQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=", 2);
                if (pair.length == 2 && pair[0].equals(key)) {
                    return URLDecoder.decode(pair[1], StandardCharsets.UTF_8);
                }
            }
        }
        throw new ValidationException(key + " is required");
    }

    protected void send(HttpExchange exchange, HttpResponse response) throws IOException {
        byte[] bytes = response.getBody().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        if (bytes.length == 0) {
            exchange.sendResponseHeaders(response.getStatusCode(), -1);
            exchange.close();
            return;
        }
        exchange.sendResponseHeaders(response.getStatusCode(), bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
