package org.mrp.repository.user;

import org.mrp.modal.User;

import java.util.List;
import java.util.UUID;

public class UserManager implements UserRepository {
    public static final UserRepositoryCache cache = new UserRepositoryCache();
    public static final UserRepositoryDB DB = new UserRepositoryDB();

    @Override
    public boolean add(UUID key, User value) {
        return cache.add(key, value) && DB.add(key, value);
    }

    public boolean add(User value) {
        return cache.add(value.getUserId(), value) && DB.add(value.getUserId(), value);
    }

    @Override
    public User get(UUID key) {
        User user = cache.get(key);
        if(user != null) {
            return user;
        }
        return DB.get(key);
    }

    @Override
    public User get(String name) {
        User user = cache.get(name);

        if(user != null) {
            return user;
        }

        user = DB.get(name);

        return user;
    }

    @Override
    public void update(UUID key, User Value) {
        cache.update(key, Value);
        DB.update(key, Value);
    }

    @Override
    public void remove(UUID key) {
        cache.remove(key);
        DB.remove(key);
    }

    @Override
    public UUID login(String username, String passwordHash) {
        if(username.isEmpty() || passwordHash.isEmpty()) {
            return null;
        }

        UUID uuid = cache.login(username, passwordHash);

        if(uuid != null) {
            return uuid;
        }

        uuid = DB.login(username, passwordHash);

        return uuid;
    }

    @Override
    public List<String> getNameCompletions(String name) {
        return List.of(name);
    }
}
