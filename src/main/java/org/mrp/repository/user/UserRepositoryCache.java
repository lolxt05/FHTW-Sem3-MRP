package org.mrp.repository.user;

import org.mrp.cache.UUIDCache;
import org.mrp.modal.User;

import java.util.TreeMap;
import java.util.UUID;

public class UserRepositoryCache implements UserRepository{

    private static final TreeMap<String, UUID> uuidNameCache = new TreeMap<>();
    private static final UUIDCache<User> uuidCache = new UUIDCache<User>();

    public UserRepositoryCache(){}

    @Override
    public boolean addUser(User user) {
        UUID uuid = user.getUserId();

        if(uuidCache.contains(uuid)) {
            return false;
        }

        uuidCache.insert(uuid,  user);
        uuidNameCache.put(user.getUserName(), user.getUserId());

        return true;
    }

    @Override
    public User getUser(UUID uuid) {
        return uuidCache.get(uuid);
    }

    @Override
    public User getUser(String username) {
        UUID uuid = uuidNameCache.get(username);

        if(uuid == null) {
            return null;
        }

        return uuidCache.get(uuid);
    }

    @Override
    public UUID login(String username, String passwordHash) {
        UUID uuid = uuidNameCache.get(username);

        if(uuid == null) {
            return null;
        }

        User user = uuidCache.get(uuid);

        if(user.getUserPw().equals(passwordHash)) {
            return uuid;
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        uuidCache.insert(user.getUserId(), user);
        uuidNameCache.put(user.getUserName(), user.getUserId());
    }

    @Override
    public void removeUser(UUID uuid) {
        User user = uuidCache.get(uuid);
        uuidNameCache.remove(user.getUserName());
        uuidCache.remove(uuid);
    }
}
