package org.mrp.repository.media;

import org.mrp.modal.Media;
import org.mrp.repository.user.UserManager;
import org.mrp.repository.user.UserRepository;
import org.mrp.repository.user.UserRepositoryCache;
import org.mrp.repository.user.UserRepositoryDB;

import java.util.List;
import java.util.UUID;

public class MediaManager implements MediaRepository {
    private final MediaRepositoryCache cache;
    private final MediaRepositoryDB DB;

    private static MediaManager instance = null;

    private MediaManager() {
        this.cache = new MediaRepositoryCache();
        this.DB = new MediaRepositoryDB();
    }

    public static synchronized MediaManager getInstance() {
        if (instance == null)
            instance = new MediaManager();
        return instance;
    }

    @Override
    public boolean add(UUID key, Media value) {
        return cache.add(key, value) && DB.add(key, value);
    }

    public boolean add(Media value) {
        return cache.add(value.getMediaId(), value) && DB.add(value.getMediaId(), value);
    }

    @Override
    public Media get(UUID key) {
        Media media = cache.get(key);
        if(media != null) {
            return media;
        }
        return DB.get(key);
    }

    @Override
    public Media get(String name) {
        Media media = cache.get(name);

        if(media != null) {
            return media;
        }

        media = DB.get(name);

        return media;
    }

    @Override
    public void update(UUID key, Media Value) {
        cache.update(key, Value);
        DB.update(key, Value);
    }

    @Override
    public void remove(UUID key) {
        cache.remove(key);
        DB.remove(key);
    }

    @Override
    public List<String> getNameCompletion(String name) {
        return List.of(name);
    }
}
