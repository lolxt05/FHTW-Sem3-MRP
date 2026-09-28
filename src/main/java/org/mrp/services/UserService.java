package org.mrp.services;

import lombok.Data;
import org.mrp.modal.User;
import org.mrp.repository.user.UserManager;
import org.mrp.repository.user.UserRepository;

import java.util.*;

public class UserService{
    UserManager userManager =  UserManager.getInstance();
    public boolean add(String username, String password) {
        return false;
    }

    public User get(UUID key) {
        return null;
    }

    public User get(String name) {
        return null;
    }

    public void update(UUID key, User Value) {

    }

    public void remove(UUID key) {

    }

    public UUID login(String username, String passwordHash) {
        return null;
    }

    public List<String> getNameCompletions(String name) {
        return List.of();
    }
}

