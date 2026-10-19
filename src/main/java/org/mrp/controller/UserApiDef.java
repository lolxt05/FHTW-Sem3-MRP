package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface UserApiDef {
    void addUser(HttpExchange exchange) throws IOException;
    void deleteUser(HttpExchange exchange) throws IOException;
    void updateUser(HttpExchange exchange) throws IOException;
    void getUser(HttpExchange exchange) throws IOException;
    void login(HttpExchange exchange) throws IOException;
    void getNameCompletions(HttpExchange exchange) throws IOException;
}