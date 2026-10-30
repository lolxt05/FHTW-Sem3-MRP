package org.mrp.repository.media;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.mrp.model.Media;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MediaRepositoryCache implements MediaRepository{

    private static final TreeMap<String, UUID> uuidMediaCache = new TreeMap<>();

    private static final Cache<UUID, Media> uuidCache = Caffeine.newBuilder().expireAfterWrite(100, TimeUnit.MINUTES).maximumSize(10000).build();

    @Override
    public boolean add(UUID key, Media media) {

        if(uuidCache.getIfPresent(key) != null) {
            return false;
        }

        uuidCache.put(key,  media);
        uuidMediaCache.put(media.getMediaTitle(), media.getMediaId());
        return true;
    }

    @Override
    public Media get(UUID uuid) {
        return uuidCache.getIfPresent(uuid);
    }

    @Override
    public Media get(String name) {
        UUID uuid = uuidMediaCache.get(name);
        if(uuid == null) {
            return null;
        }
        return uuidCache.getIfPresent(uuid);
    }

    @Override
    public List<Media> getAll() {
        return List.copyOf(uuidCache.asMap().values());
    }

    @Override
    public boolean remove(UUID key) {
        uuidMediaCache.values().remove(key);
        uuidCache.invalidate(key);
        return true;
    }

    public void removeByCreator(UUID creatorId) {
        for (Media media : List.copyOf(uuidCache.asMap().values())) {
            if (media.getCreatorId().equals(creatorId)) {
                remove(media.getMediaId());
            }
        }
    }

    @Override
    public boolean update(UUID key, Media value) {
        uuidMediaCache.values().remove(key);
        uuidCache.put(key, value);
        uuidMediaCache.put(value.getMediaTitle(), value.getMediaId());
        return true;
    }

    @Override
    public List<String> getNameCompletion(String name) {
        uuidMediaCache.get(name);
        return List.of(name);
    }
}