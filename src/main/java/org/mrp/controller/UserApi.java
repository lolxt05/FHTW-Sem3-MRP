package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;
import org.mrp.model.Session;
import org.mrp.model.requests.UserAddRequest;
import org.mrp.services.UserService;

import java.io.IOException;

public class UserApi extends BaseApi implements UserApiDef {
    UserService userService = new UserService();

    public UserApi() {
        super("users");
    }

    @Override
    protected void route(HttpExchange exchange, String[] parts) throws IOException {
        String method = exchange.getRequestMethod();
        if (parts.length == 2) {
            switch (method) {
                case "POST" -> addUser(exchange);
                case "GET" -> getNameCompletions(exchange);
                default -> send(exchange, notAllowed);
            }
        } else if (parts.length == 3) {
            switch (method) {
                case "GET" -> getUser(exchange, parts[2]);
                case "PUT" -> updateUser(exchange, parts[2]);
                case "DELETE" -> deleteUser(exchange, parts[2]);
                default -> send(exchange, notAllowed);
            }
        } else {
            send(exchange, notFound);
        }
    }

    @Override
    public void addUser(HttpExchange exchange) throws IOException {
        UserAddRequest request = new UserAddRequest(readJson(exchange));
        send(exchange, userService.add(request));
    }

    @Override
    public void deleteUser(HttpExchange exchange, String id) throws IOException {
        Session session = authenticate(exchange);
        send(exchange, userService.remove(parseId(id), session));
    }

    @Override
    public void updateUser(HttpExchange exchange, String id) throws IOException {
        Session session = authenticate(exchange);
        UserAddRequest request = new UserAddRequest(readJson(exchange));
        send(exchange, userService.update(parseId(id), session, request));
    }

    @Override
    public void getUser(HttpExchange exchange, String id) throws IOException {
        authenticate(exchange);
        send(exchange, userService.get(parseId(id)));
    }

    @Override
    public void getNameCompletions(HttpExchange exchange) throws IOException {
        authenticate(exchange);
        send(exchange, userService.getNameCompletions(getQueryParam(exchange, "prefix")));
    }
}
