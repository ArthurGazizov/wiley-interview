package com.agazizov.interview.wiley.cache;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract class AbstractLoadingCache<K, V> extends AbstractCache<K, V> implements LoadingCache<K, V> {
    private final Function<? super K, ? extends V> loader;

    AbstractLoadingCache(Function<? super K, ? extends V> loader) {
        this.loader = Objects.requireNonNull(loader);
    }

    @Override
    public V get(K key) {
        Objects.requireNonNull(key);
        return get(key, loader);
    }

    @Override
    public Map<K, V> getAll(Iterable<? extends K> keys) {
        Objects.requireNonNull(keys);
        return StreamSupport.stream(keys.spliterator(), false)
                .collect(Collectors.toMap(Function.identity(), this::get));
    }

    @Override
    public void refresh(K key) {
        storage().remove(key);
        get(key);
    }
}
