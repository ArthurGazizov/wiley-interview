package com.agazizov.interview.wiley.cache;

import com.agazizov.interview.wiley.cache.storage.CacheStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


class AbstractCacheTest {
    private AbstractCache<Object, Object> cache;
    private CacheStorage<Object, Object> storage;

    @BeforeEach
    void setUp() {
        this.storage = Mockito.mock(CacheStorage.class);
        this.cache = new AbstractCache<>() {
            @Override
            protected CacheStorage<Object, Object> storage() {
                return storage;
            }
        };
    }

    @Test
    void storage() {
        Assertions.assertEquals(storage, cache.storage());
    }

    @Test
    void getIfPresent() {
        Mockito.when(storage.get(Mockito.any())).thenReturn(null);
        Assertions.assertTrue(cache.getIfPresent(new Object()).isEmpty());

        Mockito.when(storage.get(Mockito.any())).thenReturn(new Object());
        Assertions.assertTrue(cache.getIfPresent(new Object()).isPresent());
    }

    @Test
    void get() {
        Object key = new Object();
        cache.get(key, k -> k);
        Mockito.verify(storage).computeIfAbsent(Mockito.eq(key), Mockito.any(Function.class));
    }

    @Test
    void getAllPresent() {
        List<String> keys = List.of("key1", "key2", "key3");
        cache.getAllPresent(keys);
        keys.forEach(key -> Mockito.verify(storage).get(key));
    }

    @Test
    void put() {
        Object key = new Object();
        Object value = new Object();
        cache.put(key, value);
        Mockito.verify(storage).put(key, value);
    }

    @Test
    void putAll() {
        Map<Object, Object> map = Map.of(new Object(), new Object(), new Object(), new Object());
        cache.putAll(map);
        Mockito.verify(storage).putAll(map);
    }

    @Test
    void invalidate() {
        Object key = new Object();
        cache.invalidate(key);
        Mockito.verify(storage).remove(key);
    }

    @Test
    void invalidateAll() {
        cache.invalidateAll();
        Mockito.verify(storage).clear();
    }

    @Test
    void containsKey() {
        Object key = new Object();
        cache.containsKey(key);
        Mockito.verify(storage).containsKey(key);
    }

    @Test
    void size() {
        cache.size();
        Mockito.verify(storage).size();
    }
}