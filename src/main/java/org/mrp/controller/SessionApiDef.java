package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface SessionApiDef {
    void login(HttpExchange exchange) throws IOException;
}
