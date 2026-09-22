package org.mrp.repository.user;

import org.mrp.modal.User;

import java.util.UUID;

public interface UserRepository {
    public boolean addUser(User user);
    public User getUser(UUID uuid);
    public User getUser(String username);
    public UUID login(String username, String passwordHash);
    public void updateUser(User user);
    public void removeUser(UUID uuid);
}