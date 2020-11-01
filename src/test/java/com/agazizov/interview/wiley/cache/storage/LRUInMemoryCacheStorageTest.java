package com.agazizov.interview.wiley.cache.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

class LRUInMemoryCacheStorageTest {
    @Test
    void testMaxSize() {
        final int maxSize = 10;
        LRUInMemoryCacheStorage<Integer, Integer> storage = new LRUInMemoryCacheStorage<>(maxSize);
        ThreadLocalRandom.current().ints().limit(1000).forEach(i -> storage.put(i, i));
        Assertions.assertTrue(storage.size() <= maxSize);
    }

    @Test
    void testEvict() {
        final int maxSize = 3;
        LRUInMemoryCacheStorage<Integer, Integer> storage = new LRUInMemoryCacheStorage<>(maxSize);
        IntStream.range(0, 3).forEach(i -> storage.put(i, i));

        Assertions.assertEquals(3, storage.size());
        storage.get(1);

        storage.put(5, 5);
        storage.put(6, 6);
        Assertions.assertAll(
                () -> Assertions.assertEquals(3, storage.size()),
                () -> Assertions.assertTrue(storage.containsKey(1)),
                () -> Assertions.assertTrue(storage.containsKey(5)),
                () -> Assertions.assertTrue(storage.containsKey(6)),
                () -> Assertions.assertFalse(storage.containsKey(0)),
                () -> Assertions.assertFalse(storage.containsKey(2))
        );
    }
}