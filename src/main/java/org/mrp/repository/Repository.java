package org.mrp.repository;

public interface Repository<K,V> {
    public boolean add(K key,V value);
    public V get(K key);
    public void update(K key, V Value);
    public void remove(K key);
}
