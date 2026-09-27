package org.mrp.repository.media;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.mrp.modal.Media;

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
    public void remove(UUID key) {
        Media media = uuidCache.getIfPresent(key);
        uuidCache.invalidate(key);
        if(media == null){
            return;
        }
        uuidMediaCache.remove(media.getMediaTitle(), key);
    }

    @Override
    public void update(UUID key, Media value) {
        uuidCache.put(key, value);
        uuidMediaCache.put(value.getMediaTitle(), value.getMediaId());
    }

    @Override
    public List<String> getNameCompletion(String name) {
        uuidMediaCache.get(name);
        return List.of(name);
    }
}