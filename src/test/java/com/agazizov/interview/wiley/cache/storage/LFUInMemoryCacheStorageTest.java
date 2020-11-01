package com.agazizov.interview.wiley.cache.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

class LFUInMemoryCacheStorageTest {

    @Test
    void put() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(1);
        storage.put(1, 1);
        Assertions.assertFalse(storage.isEmpty());
    }

    @Test
    void putAll() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Map<Integer, Integer> map = Map.of(1, 1, 2, 2);
        storage.putAll(map);
        Assertions.assertEquals(map.size(), storage.size());
    }

    @Test
    void get() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(1);
        storage.put(1, 1);
        Assertions.assertEquals(1, storage.get(1));
    }

    @Test
    void remove() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(1);
        storage.put(1, 1);
        storage.remove(1);
        Assertions.assertTrue(storage.isEmpty());
    }

    @Test
    void clear() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(1);
        storage.put(1, 1);
        storage.clear();
        Assertions.assertTrue(storage.isEmpty());
    }

    @Test
    void keySet() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Map<Integer, Integer> map = Map.of(1, 1, 2, 2);
        storage.putAll(map);
        Assertions.assertEquals(map.keySet(), storage.keySet());
    }

    @Test
    void values() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Map<Integer, Integer> map = Map.of(1, 1, 2, 2);
        storage.putAll(map);
        Assertions.assertEquals(Set.copyOf(map.values()), Set.copyOf(storage.values()));
    }

    @Test
    void entrySet() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Map<Integer, Integer> map = Map.of(1, 1, 2, 2);
        storage.putAll(map);
        Assertions.assertEquals(Set.copyOf(map.entrySet()), Set.copyOf(storage.entrySet()));
    }

    @Test
    void size() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Map<Integer, Integer> map = Map.of(1, 1, 2, 2);
        storage.putAll(map);
        Assertions.assertEquals(map.size(), storage.size());
    }

    @Test
    void isEmpty() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Assertions.assertTrue(storage.isEmpty());
        storage.put(1, 1);
        Assertions.assertFalse(storage.isEmpty());
    }

    @Test
    void containsKey() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Assertions.assertFalse(storage.containsKey(1));
        storage.put(1, 1);
        Assertions.assertTrue(storage.containsKey(1));
    }

    @Test
    void containsValue() {
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(10);
        Assertions.assertFalse(storage.containsValue(2));
        storage.put(1, 2);
        Assertions.assertTrue(storage.containsValue(2));
    }
}