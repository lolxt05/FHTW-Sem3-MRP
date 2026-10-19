package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import org.mrp.model.HttpResponse;
import org.mrp.model.exceptions.SessionTokenInvalid;
import org.mrp.model.exceptions.ValidationException;
import org.mrp.model.requests.UserAddRequest;
import org.mrp.model.requests.UserDeleteRequest;
import org.mrp.services.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UserApi implements UserApiDef, HttpHandler {
    UserService userService = new UserService();
    HttpResponse notFound = new HttpResponse(404, "Not Found");
    HttpResponse error = new HttpResponse(500, "Internal Server Error");
    HttpResponse invalid = new HttpResponse(400, "Bad Request");
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getPath().split("/");
        try {
            if (parts.length == 3) {
                switch (parts[2]) {
                    case "add" -> addUser(exchange);
                    case "delete" -> deleteUser(exchange);
                    case "update" -> updateUser(exchange);
                    case "get" -> getUser(exchange);
                    case "login" -> login(exchange);
                    case "completion" -> getNameCompletions(exchange);
                }
            } else {
                send(exchange, notFound);
            }
        } catch (ValidationException e) {
            send(exchange, new HttpResponse(400, e.getMessage()));
        } catch (org.json.JSONException e) {
            send(exchange, new HttpResponse(400, "Invalid JSON"));
        } catch (Exception e) {
            send(exchange, error);
        }
    }
    @Override
    public void addUser(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, invalid);
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        UserAddRequest request = new UserAddRequest(json);
        send(exchange, userService.add(request));
    }

    @Override
    public void deleteUser(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("DELETE")) {
            send(exchange, invalid);
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        try {
            UserDeleteRequest request = new UserDeleteRequest(json);
            send(exchange, userService.remove(request));
        }catch (SessionTokenInvalid s) {
            send(exchange, new  HttpResponse(400, s.getMessage()));
        }
    }

    @Override
    public void updateUser(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("POST")) {
            send(exchange, invalid);
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        send(exchange, userService.update(json));
    }

    @Override
    public void getUser(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            send(exchange, invalid);
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        send(exchange,  userService.get(json));
    }

    @Override
    public void login(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            send(exchange, invalid);
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        send(exchange,  userService.login(json));
    }

    @Override
    public void getNameCompletions(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            send(exchange, invalid);
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(body);
        send(exchange,  userService.getNameCompletions(json));
    }

    private void send(HttpExchange exchange, HttpResponse response) throws IOException {
        byte[] bytes = response.getBody().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(response.getStatusCode(), bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
