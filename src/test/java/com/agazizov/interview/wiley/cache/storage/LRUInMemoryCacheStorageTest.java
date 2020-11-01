package com.agazizov.interview.wiley.cache.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
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
        storage.get(1); // call 1 to up it

        storage.put(5, 5); // after this, key '0' should be removed

        Assertions.assertAll(
                () -> Assertions.assertEquals(maxSize, storage.size()),
                () -> Assertions.assertEquals(Set.of(1, 2, 5), storage.keySet())
        );

        storage.put(6, 6); // after this, key '2' should be removed
        Assertions.assertAll(
                () -> Assertions.assertEquals(maxSize, storage.size()),
                () -> Assertions.assertEquals(Set.of(1, 5, 6), storage.keySet())
        );
    }
}