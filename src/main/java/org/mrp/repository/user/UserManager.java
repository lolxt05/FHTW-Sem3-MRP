package org.mrp.repository.user;

import org.mrp.modal.User;

import java.util.UUID;

public class UserManager implements UserRepository {
    public static final UserRepositoryCache cache = new UserRepositoryCache();
    public static final UserRepositoryDB DB = new UserRepositoryDB();

    @Override
    public boolean addUser(User user) {
        return cache.addUser(user) && DB.addUser(user);
    }

    @Override
    public User getUser(UUID uuid) {
        User user = cache.getUser(uuid);
        if(user != null) {
            return user;
        }
        return DB.getUser(uuid);
    }

    @Override
    public User getUser(String username) {
        User user = cache.getUser(username);

        if(user != null) {
            return user;
        }

        user = DB.getUser(username);

        return user;
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

        if(uuid != null) {
            cache.addUser(DB.getUser(uuid));
        }

        return uuid;
    }

    @Override
    public void updateUser(User user) {
        cache.updateUser(user);
        DB.updateUser(user);
    }

    @Override
    public void removeUser(UUID uuid) {
        cache.removeUser(uuid);
        DB.removeUser(uuid);
    }
}
