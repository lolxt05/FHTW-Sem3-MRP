package org.mrp.repository.user;

import org.mrp.modal.User;

import java.util.UUID;

public class UserRepositoryDB implements UserRepository{

    UserRepositoryDB(){}

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public User getUser(UUID uuid) {
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public UUID login(String username, String passwordHash) {
        return null;
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void removeUser(UUID uuid) {

    }
}
