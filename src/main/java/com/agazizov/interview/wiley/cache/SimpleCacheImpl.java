package com.agazizov.interview.wiley.cache;

import com.agazizov.interview.wiley.cache.storage.CacheStorage;

import java.util.Objects;

class SimpleCacheImpl<K, V> extends AbstractCache<K, V> {
    private final CacheStorage<K, V> storage;

    SimpleCacheImpl(CacheStorage<K, V> storage) {
        super();
        this.storage = Objects.requireNonNull(storage);
    }

    @Override
    protected CacheStorage<K, V> storage() {
        return storage;
    }
}
