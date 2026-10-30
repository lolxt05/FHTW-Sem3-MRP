package org.mrp.config;


import com.sun.net.httpserver.HttpServer;
import org.mrp.controller.MediaApi;
import org.mrp.controller.SessionApi;
import org.mrp.controller.UserApi;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server  {
    public Server(){
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/users", new UserApi());
            server.createContext("/sessions", new SessionApi());
            server.createContext("/media", new MediaApi());

            server.setExecutor(null);
            server.start();

            System.out.println("Server is running on port 8000");
        } catch (IOException e) {
            System.out.println("Error starting the server: " + e.getMessage());
        }
    }
}