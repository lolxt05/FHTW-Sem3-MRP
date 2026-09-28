package org.mrp.repository.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import org.mrp.cache.UUIDCache;
import org.mrp.modal.User;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserRepositoryCache implements UserRepository {

    private static final TreeMap<String, UUID> uuidNameCache = new TreeMap<>();
    private static final Cache<UUID, User> uuidCache = Caffeine.newBuilder().expireAfterWrite(100, TimeUnit.MINUTES).maximumSize(10000).build();


    public UserRepositoryCache(){
    }

    @Override
    public boolean add(UUID key, User user) {
        UUID uuid = user.getUserId();

        if(uuidCache.getIfPresent(uuid) != null) {
            return false;
        }

        uuidCache.put(uuid,  user);
        uuidNameCache.put(user.getUserName(), user.getUserId());

        return true;
    }
    @Override
    public User get(UUID uuid) {
        return uuidCache.getIfPresent(uuid);
    }
    @Override
    public User get(String username) {
        UUID uuid = uuidNameCache.get(username);

        if(uuid == null) {
            return null;
        }

        return uuidCache.getIfPresent(uuid);
    }

    @Override
    public void update(UUID key, User value) {
        uuidCache.put(value.getUserId(), value);
        uuidNameCache.put(value.getUserName(), value.getUserId());
    }

    @Override
    public UUID login(String username, String passwordHash) {
        UUID uuid = uuidNameCache.get(username);

        if(uuid == null) {
            return null;
        }

        User user = uuidCache.getIfPresent(uuid);
        if(user == null) {
            return null;
        }
        if(user.getUserPw().equals(passwordHash)) {
            return uuid;
        }
        return null;
    }

    @Override
    public void remove(UUID uuid) {
        User user = uuidCache.getIfPresent(uuid);
        if(user != null) {
            uuidNameCache.remove(user.getUserName());
            uuidCache.invalidate(uuid);
        }
    }
    @Override
    public List<String> getNameCompletions(String name) {
        return List.of();
    }
}
