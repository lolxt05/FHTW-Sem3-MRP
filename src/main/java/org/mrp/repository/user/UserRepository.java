package org.mrp.repository.user;

import org.mrp.modal.User;
import org.mrp.repository.Repository;

import java.util.List;
import java.util.UUID;

public abstract interface UserRepository extends Repository<UUID, User> {
    public boolean add(UUID key,User value);
    public User get(UUID key);
    public User get(String name);
    public void update(UUID key, User Value);
    public void remove(UUID key);
    public UUID login(String username, String passwordHash);
    public List<String> getNameCompletions(String name);
}