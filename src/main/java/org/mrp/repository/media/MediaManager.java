package org.mrp.repository.media;

import org.mrp.model.Media;

import java.util.List;
import java.util.UUID;

public class MediaManager implements MediaRepository {
    private final MediaRepositoryCache cache;
    private final MediaRepositoryDB DB;

    private static MediaManager instance = null;

    private MediaManager() {
        this.cache = new MediaRepositoryCache();
        this.DB  = new MediaRepositoryDB();
    }

    public static synchronized MediaManager getInstance() {
        if (instance == null)
            instance = new MediaManager();
        return instance;
    }

    @Override
    public boolean add(UUID key, Media value) {
        return DB.add(key, value) && cache.add(key, value);
    }

    public boolean add(Media value) {
        return DB.add(value.getMediaId(), value) && cache.add(value.getMediaId(), value);
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
    public List<Media> getAll() {
        return DB.getAll();
    }

    @Override
    public boolean update(UUID key, Media Value) {
        return DB.update(key, Value) && cache.update(key, Value);
    }

    @Override
    public boolean remove(UUID key) {
        return DB.remove(key) && cache.remove(key);
    }

    public void removeCachedByCreator(UUID creatorId) {
        cache.removeByCreator(creatorId);
    }

    @Override
    public List<String> getNameCompletion(String name) {
        return List.of(name);
    }
}
