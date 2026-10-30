package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface MediaApiDef {
    void addMedia(HttpExchange exchange) throws IOException;
    void getAllMedia(HttpExchange exchange) throws IOException;
    void getMedia(HttpExchange exchange, String id) throws IOException;
    void updateMedia(HttpExchange exchange, String id) throws IOException;
    void deleteMedia(HttpExchange exchange, String id) throws IOException;
}
