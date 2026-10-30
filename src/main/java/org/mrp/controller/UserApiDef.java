package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface UserApiDef {
    void addUser(HttpExchange exchange) throws IOException;
    void deleteUser(HttpExchange exchange, String id) throws IOException;
    void updateUser(HttpExchange exchange, String id) throws IOException;
    void getUser(HttpExchange exchange, String id) throws IOException;
    void getNameCompletions(HttpExchange exchange) throws IOException;
}
