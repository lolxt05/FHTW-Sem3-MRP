package org.mrp.cache;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class UUIDCache<T> {

    protected final TreeMap<UUID,T> map = new TreeMap<UUID,T>();

    public void insert(UUID uuid, T value){
        map.put(uuid, value);
    }

    public boolean contains(UUID uuid){
        return this.map.containsKey(uuid);
    }

    public boolean remove(UUID uuid){
        return this.map.remove(uuid) != null;
    }
    public T get(UUID uuid){
        return this.map.get(uuid);
    }
}