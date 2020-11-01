package com.agazizov.interview.wiley.cache;

import com.agazizov.interview.wiley.cache.storage.CacheStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Function;

class AbstractLoadingCacheTest {
    private AbstractLoadingCache<Object, Object> cache;
    private CacheStorage<Object, Object> storage;

    @BeforeEach
    void setUp() {
        this.storage = Mockito.mock(CacheStorage.class);
        this.cache = new AbstractLoadingCache<>(Function.identity()) {
            @Override
            protected CacheStorage<Object, Object> storage() {
                return storage;
            }
        };
    }

    @Test
    void get() {
        Object key = new Object();
        cache.get(key);
        Mockito.verify(storage).computeIfAbsent(Mockito.eq(key), Mockito.any(Function.class));
    }

    @Test
    void getAll() {
        Mockito.when(storage.computeIfAbsent(Mockito.any(), Mockito.any())).thenReturn(new Object());
        List<String> keys = List.of("key1", "key2", "key3");
        cache.getAll(keys);
        keys.forEach(key -> Mockito.verify(storage).computeIfAbsent(Mockito.eq(key), Mockito.any(Function.class)));
    }

    @Test
    void refresh() {
        Object key = new Object();
        cache.refresh(key);
        Mockito.verify(storage).remove(key);
        Mockito.verify(storage).computeIfAbsent(Mockito.eq(key), Mockito.any(Function.class));
    }
}