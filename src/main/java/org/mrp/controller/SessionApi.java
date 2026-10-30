package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;
import org.mrp.model.requests.LoginRequest;
import org.mrp.services.UserService;

import java.io.IOException;

public class SessionApi extends BaseApi implements SessionApiDef {
    UserService userService = new UserService();

    public SessionApi() {
        super("sessions");
    }

    @Override
    protected void route(HttpExchange exchange, String[] parts) throws IOException {
        if (parts.length != 2) {
            send(exchange, notFound);
        } else if (exchange.getRequestMethod().equals("POST")) {
            login(exchange);
        } else {
            send(exchange, notAllowed);
        }
    }

    @Override
    public void login(HttpExchange exchange) throws IOException {
        LoginRequest request = new LoginRequest(readJson(exchange));
        send(exchange, userService.login(request));
    }
}
