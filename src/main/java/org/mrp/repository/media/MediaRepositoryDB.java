package org.mrp.repository.media;

import org.mrp.Main;
import org.mrp.modal.Media;

import java.util.List;
import java.util.UUID;

import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MediaRepositoryDB implements MediaRepository{

    private final Logger LOGGER = Logger.getLogger(MediaRepositoryDB.class.getName());
    private Connection conn = null;

    public MediaRepositoryDB(){
        final String url = "jdbc:postgresql://<node ip address>:5432,<node ip address>:5432/postgres?targetServerType=primary";
        final Properties props = new Properties();
        props.setProperty("user", "icpostgresql");
        props.setProperty("password", "<password>");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            LOGGER.info("Connected to database.");
        } catch(SQLException e) {
            LOGGER.severe("Error connecting to database " + Arrays.toString(e.getStackTrace()));
        }
    };

    @Override
    public boolean add(UUID key, Media value) {
        try{
            Statement st = conn.createStatement();
            st.execute("INSERT INTO cities VALUES ('canberra', '(35.3, 149.1)', 395790)");
        }catch(SQLException e){
            LOGGER.severe("Error Inserting Media " + Arrays.toString(e.getStackTrace()));
        }

        return false;
    }

    @Override
    public Media get(UUID key) {
        return null;
    }

    @Override
    public Media get(String name) {
        return null;
    }

    @Override
    public void update(UUID key, Media Value) {

    }

    @Override
    public void remove(UUID key) {

    }

    @Override
    public List<String> getNameCompletion(String name) {
        return List.of();
    }
}
