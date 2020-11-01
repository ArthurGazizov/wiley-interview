package com.agazizov.interview.wiley.cache.storage;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUInMemoryCacheStorage<K, V> extends LinkedHashMap<K, V> implements CacheStorage<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final int maxSize;

    public LRUInMemoryCacheStorage(int maxSize) {
        super(maxSize, DEFAULT_LOAD_FACTOR, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}
