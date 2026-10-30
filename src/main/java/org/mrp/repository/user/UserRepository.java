package org.mrp.repository.user;

import org.mrp.model.User;
import org.mrp.repository.Repository;

import java.util.List;
import java.util.UUID;

public abstract interface UserRepository extends Repository<UUID, User> {
    public boolean add(UUID key,User value);
    public User get(UUID key);
    public User get(String name);
    public boolean update(UUID key, User Value);
    public boolean remove(UUID key);
    public List<String> getNameCompletions(String name);
}