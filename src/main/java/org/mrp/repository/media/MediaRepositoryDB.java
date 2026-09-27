package org.mrp.repository.media;

import org.mrp.modal.Media;

import java.util.List;
import java.util.UUID;

public class MediaRepositoryDB implements MediaRepository{

    @Override
    public boolean add(UUID key, Media value) {
        return false;
    }

    @Override
    public Media get(UUID key) {
        return null;
    }

    @Override
    public Media get(String name) {
        return null;
    }

    @Override
    public void update(UUID key, Media Value) {

    }

    @Override
    public void remove(UUID key) {

    }

    @Override
    public List<String> getNameCompletion(String name) {
        return List.of();
    }
}
