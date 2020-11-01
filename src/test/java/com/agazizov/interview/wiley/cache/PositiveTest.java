package com.agazizov.interview.wiley.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PositiveTest {
    @Test
    void testLRU() {
        LoadingCache<Integer, Integer> cache = CacheBuilder.<Integer, Integer>builder()
                .maxSize(3)
                .lru()
                .loader(i -> i * i)
                .build();

        Assertions.assertTrue(cache.isEmpty());

        Assertions.assertEquals(1, cache.get(1));
        Assertions.assertEquals(4, cache.get(2));

        Assertions.assertFalse(cache.isEmpty());
        Assertions.assertEquals(2, cache.size());

        Assertions.assertEquals(9, cache.get(3));
        Assertions.assertEquals(3, cache.size());

        Assertions.assertEquals(16, cache.get(4));
        Assertions.assertEquals(3, cache.size());
        Assertions.assertFalse(cache.containsKey(1)); // key 1 was removed by evict policy

        cache.get(2); // fetch key 2

        Assertions.assertEquals(25, cache.get(5));
        Assertions.assertEquals(3, cache.size());
        Assertions.assertTrue(cache.containsKey(2)); // key 2 was used recently
        Assertions.assertFalse(cache.containsKey(3)); // key 3 was removed by evict policy
    }


    @Test
    void testLFU() {
        LoadingCache<Integer, Integer> cache = CacheBuilder.<Integer, Integer>builder()
                .maxSize(3)
                .lfu()
                .loader(i -> i * i)
                .build();
        Assertions.assertTrue(cache.isEmpty());


        cache.getAll(List.of(1, 2, 3));

        Assertions.assertFalse(cache.isEmpty());
        Assertions.assertEquals(3, cache.size());

        // lets use key 1 and key 3 frequently
        IntStream.range(0, 100).forEach(i -> {
            cache.get(1);
            cache.get(3);
        });

        cache.get(4);

        Assertions.assertFalse(cache.isEmpty());
        Assertions.assertEquals(3, cache.size());
        Assertions.assertFalse(cache.containsKey(2));

        Assertions.assertEquals(Set.of(1, 3, 4), Set.copyOf(cache.keySet()));
        Assertions.assertEquals(Set.of(1, 9, 16), cache.values().collect(Collectors.toSet()));
    }

}
