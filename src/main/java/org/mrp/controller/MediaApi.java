package org.mrp.controller;

import com.sun.net.httpserver.HttpExchange;
import org.mrp.model.Session;
import org.mrp.model.requests.MediaRequest;
import org.mrp.services.MediaService;

import java.io.IOException;

public class MediaApi extends BaseApi implements MediaApiDef {
    MediaService mediaService = new MediaService();

    public MediaApi() {
        super("media");
    }

    @Override
    protected void route(HttpExchange exchange, String[] parts) throws IOException {
        String method = exchange.getRequestMethod();
        if (parts.length == 2) {
            switch (method) {
                case "POST" -> addMedia(exchange);
                case "GET" -> getAllMedia(exchange);
                default -> send(exchange, notAllowed);
            }
        } else if (parts.length == 3) {
            switch (method) {
                case "GET" -> getMedia(exchange, parts[2]);
                case "PUT" -> updateMedia(exchange, parts[2]);
                case "DELETE" -> deleteMedia(exchange, parts[2]);
                default -> send(exchange, notAllowed);
            }
        } else {
            send(exchange, notFound);
        }
    }

    @Override
    public void addMedia(HttpExchange exchange) throws IOException {
        Session session = authenticate(exchange);
        MediaRequest request = new MediaRequest(readJson(exchange));
        send(exchange, mediaService.add(session, request));
    }

    @Override
    public void getAllMedia(HttpExchange exchange) throws IOException {
        authenticate(exchange);
        send(exchange, mediaService.getAll());
    }

    @Override
    public void getMedia(HttpExchange exchange, String id) throws IOException {
        authenticate(exchange);
        send(exchange, mediaService.get(parseId(id)));
    }

    @Override
    public void updateMedia(HttpExchange exchange, String id) throws IOException {
        Session session = authenticate(exchange);
        MediaRequest request = new MediaRequest(readJson(exchange));
        send(exchange, mediaService.update(parseId(id), session, request));
    }

    @Override
    public void deleteMedia(HttpExchange exchange, String id) throws IOException {
        Session session = authenticate(exchange);
        send(exchange, mediaService.remove(parseId(id), session));
    }
}
