package org.mrp.repository.media;

import org.mrp.modal.Media;
import org.mrp.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface MediaRepository extends Repository<UUID, Media> {
    public boolean add(UUID key,Media value);
    public Media get(UUID key);
    public Media get(String name);
    public void update(UUID key, Media Value);
    public void remove(UUID key);
    public List<String> getNameCompletion(String name);
}
