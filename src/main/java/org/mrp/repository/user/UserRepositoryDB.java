package org.mrp.repository.user;

import org.mrp.modal.User;

import java.util.List;
import java.util.UUID;

public class UserRepositoryDB implements UserRepository{


    @Override
    public boolean add(UUID key, User value) {
        return false;
    }

    @Override
    public User get(UUID key) {
        return null;
    }

    @Override
    public User get(String name) {
        return null;
    }

    @Override
    public void update(UUID key, User Value) {

    }

    @Override
    public void remove(UUID key) {

    }

    @Override
    public UUID login(String username, String passwordHash) {
        return null;
    }

    @Override
    public List<String> getNameCompletions(String name) {
        return List.of();
    }
}
