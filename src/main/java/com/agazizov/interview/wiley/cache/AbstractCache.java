package com.agazizov.interview.wiley.cache;

import com.agazizov.interview.wiley.cache.storage.CacheStorage;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

abstract class AbstractCache<K, V> implements Cache<K, V> {
    protected abstract CacheStorage<K, V> storage();

    @Override
    public Optional<V> getIfPresent(K key) {
        Objects.requireNonNull(key);
        return Optional.ofNullable(storage().get(key));
    }

    @Override
    public V get(K key, Function<? super K, ? extends V> loader) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(loader);
        return storage().computeIfAbsent(key, loader.andThen(Objects::requireNonNull));
    }

    @Override
    public Map<K, V> getAllPresent(Iterable<? extends K> keys) {
        Objects.requireNonNull(keys);
        return StreamSupport.stream(keys.spliterator(), false)
                .map(k -> Map.entry(k, getIfPresent(k)))
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, value -> value.getValue().get()));
    }

    @Override
    public void put(K key, V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        storage().put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);
        m.forEach((k, v) -> {
            Objects.requireNonNull(k);
            Objects.requireNonNull(v);
        });
        storage().putAll(m);
    }

    @Override
    public void invalidate(K key) {
        Objects.requireNonNull(key);
        storage().remove(key);
    }

    @Override
    public void invalidateAll(Iterable<? extends K> keys) {
        Objects.requireNonNull(keys);
        StreamSupport.stream(keys.spliterator(), false).forEach(this::invalidate);
    }

    @Override
    public void invalidateAll() {
        storage().clear();
    }

    @Override
    public boolean containsKey(K key) {
        return storage().containsKey(key);
    }

    @Override
    public int size() {
        return storage().size();
    }

    @Override
    public boolean isEmpty() {
        return storage().isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return storage().keySet();
    }

    @Override
    public Stream<V> values() {
        return storage().values().stream();
    }
}
