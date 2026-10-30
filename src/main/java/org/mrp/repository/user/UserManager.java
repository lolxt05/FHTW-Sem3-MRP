package org.mrp.repository.user;

import org.mrp.model.User;

import java.util.List;
import java.util.UUID;

public class UserManager implements UserRepository {
    private final UserRepositoryCache cache;
    private final UserRepositoryDB DB;

    private static UserManager instance = null;

    private UserManager() {
        this.cache = new UserRepositoryCache();
        this.DB = new UserRepositoryDB();
    }

    public static synchronized UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    @Override
    public boolean add(UUID key, User value) {
        return DB.add(key, value) && cache.add(key, value);
    }

    public boolean add(User value) {
        return DB.add(value.getUserId(), value) && cache.add(value.getUserId(), value);
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
    public boolean update(UUID key, User Value) {
        return DB.update(key, Value) && cache.update(key, Value);
    }

    @Override
    public boolean remove(UUID key) {
        return DB.remove(key) && cache.remove(key);
    }

    @Override
    public List<String> getNameCompletions(String name) {
        return DB.getNameCompletions(name);
    }
}
