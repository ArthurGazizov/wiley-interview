package com.agazizov.interview.wiley.cache;

import com.agazizov.interview.wiley.cache.storage.CacheStorage;
import com.agazizov.interview.wiley.cache.storage.LFUInMemoryCacheStorage;
import com.agazizov.interview.wiley.cache.storage.LRUInMemoryCacheStorage;

import java.util.function.Function;

public class CacheBuilder {
    public static <K, V> MaxSizeStep<K, V> builder() {
        return new Builder<>();
    }

    public interface MaxSizeStep<K, V> {
        EvictStrategyStep<K, V> maxSize(int maxSize);
    }

    public interface EvictStrategyStep<K, V> {
        LoadingStep<K, V> lru();

        LoadingStep<K, V> lfu();
    }

    public interface LoadingStep<K, V> extends SimpleCacheBuildStep<K, V> {
        LoadingCacheBuildStep<K, V> loader(Function<? super K, ? extends V> loader);
    }

    public interface SimpleCacheBuildStep<K, V> {
        Cache<K, V> build();
    }

    public interface LoadingCacheBuildStep<K, V> {
        LoadingCache<K, V> build();
    }

    private static class Builder<K, V> implements MaxSizeStep<K, V>, EvictStrategyStep<K, V>, LoadingStep<K, V>, SimpleCacheBuildStep<K, V> {
        private int maxSize;
        private CacheStorage<K, V> storage;

        @Override
        public EvictStrategyStep<K, V> maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        @Override
        public LoadingStep<K, V> lru() {
            storage = new LRUInMemoryCacheStorage<>(maxSize);
            return this;
        }

        @Override
        public LoadingStep<K, V> lfu() {
            storage = new LFUInMemoryCacheStorage<>(maxSize);
            return this;
        }

        @Override
        public LoadingCacheBuildStep<K, V> loader(Function<? super K, ? extends V> loader) {
            return new LoadingCacheBuilder<>(storage, loader);
        }

        @Override
        public Cache<K, V> build() {
            return new SimpleCacheImpl<>(storage);
        }
    }

    private static class LoadingCacheBuilder<K, V> extends Builder<K, V> implements LoadingCacheBuildStep<K, V> {
        private final CacheStorage<K, V> storage;
        private final Function<? super K, ? extends V> loader;

        public LoadingCacheBuilder(CacheStorage<K, V> storage, Function<? super K, ? extends V> loader) {
            this.storage = storage;
            this.loader = loader;
        }

        @Override
        public LoadingCache<K, V> build() {
            return new SimpleLoadingCacheImpl<>(storage, loader);
        }
    }
}
