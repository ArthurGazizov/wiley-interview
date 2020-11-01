package com.agazizov.interview.wiley.cache.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

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

    @Test
    void testMaxSize() {
        final int maxSize = 10;
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(maxSize);
        ThreadLocalRandom.current().ints().limit(1000).forEach(i -> storage.put(i, i));
        Assertions.assertTrue(storage.size() <= maxSize);
    }

    @Test
    void testEvict() {
        final int maxSize = 5;
        LFUInMemoryCacheStorage<Integer, Integer> storage = new LFUInMemoryCacheStorage<>(maxSize);
        IntStream.range(0, maxSize).forEach(i -> storage.put(i, i));

        Assertions.assertEquals(maxSize, storage.size());

        // Call get with frequency
        storage.get(1);
        storage.get(1);
        storage.get(1);
        storage.get(1);
        storage.get(3);
        storage.get(3);
        storage.get(3);
        storage.get(0);
        storage.get(0);
        storage.get(4);
        storage.get(2);

        storage.put(5, 5); // after this key '4' should be removed
        Assertions.assertAll(
                () -> Assertions.assertEquals(maxSize, storage.size()),
                () -> Assertions.assertEquals(Set.of(0, 1, 2, 3, 5), storage.keySet())
        );
        storage.get(5);
        storage.get(5);  // call '5' to increase his frequency

        storage.put(6, 6); // after this key '2' should be removed
        Assertions.assertAll(
                () -> Assertions.assertEquals(maxSize, storage.size()),
                () -> Assertions.assertEquals(Set.of(0, 1, 3, 5, 6), storage.keySet())
        );
    }
}