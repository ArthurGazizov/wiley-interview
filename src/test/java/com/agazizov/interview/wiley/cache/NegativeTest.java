package com.agazizov.interview.wiley.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NegativeTest {
    @Test
    void testNullBehavior() {
        LoadingCache<Integer, Object> cache = CacheBuilder.<Integer, Object>builder()
                .maxSize(3)
                .lru()
                .loader(i -> new Object())
                .build();
        Assertions.assertThrows(NullPointerException.class, () -> cache.get(null));
    }
}
