package org.mrp.config;

import org.mrp.repository.media.MediaRepositoryDB;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    public static Connection getConnection(){
        String jdbcURL = "jdbc:postgresql://localhost:5432/testdb";
        String username = System.getenv("POSTGRES_USER");
        String password = System.getenv("POSTGRES_PASSWORD");

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(jdbcURL, username, password);
        }catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage());
            return null;
        }
    }
}