package org.mrp.repository;

import org.mrp.modal.Media;

import java.util.List;
import java.util.UUID;

public interface Repository<K,V> {
    public boolean add(K key,V value);
    public V get(K key);
    public void update(K key, V Value);
    public void remove(K key);
}
