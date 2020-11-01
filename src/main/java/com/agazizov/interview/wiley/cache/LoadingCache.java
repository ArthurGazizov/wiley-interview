package com.agazizov.interview.wiley.cache;

import java.util.Map;

/**
 * A semi-persistent mapping from keys to values. Values are automatically loaded by the cache, and are stored in the
 * cache until either evicted or manually invalidated.
 * <p>
 * * <p>Implementations can be NOT THREAD SAFE
 * *
 * * This cache can't works with null as key, also as value. All methods can produce NPE, if argument will be null
 */

public interface LoadingCache<K, V> extends Cache<K, V> {
    /**
     * Returns the value associated with key in this cache, first loading that value if necessary.
     */
    V get(K key);

    /**
     * Returns a map of the values associated with keys, creating or retrieving those values if necessary.
     */
    Map<K, V> getAll(Iterable<? extends K> keys);

    /**
     * Loads a new value for key key,
     */
    void refresh(K key);
}
