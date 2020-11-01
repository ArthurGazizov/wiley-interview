package com.agazizov.interview.wiley.cache;

import com.agazizov.interview.wiley.cache.storage.CacheStorage;

import java.util.Objects;
import java.util.function.Function;

class SimpleLoadingCacheImpl<K, V> extends AbstractLoadingCache<K, V> {
    private final CacheStorage<K, V> storage;

    public SimpleLoadingCacheImpl(CacheStorage<K, V> storage, Function<? super K, ? extends V> loader) {
        super(loader);
        this.storage = Objects.requireNonNull(storage);
    }

    @Override
    protected CacheStorage<K, V> storage() {
        return storage;
    }
}
