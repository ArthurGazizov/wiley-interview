package com.agazizov.interview.wiley.cache;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A semi-persistent mapping from keys to values. Cache entries are manually added using
 * {@link #get(K, Function loader)} or {@link #put(K, V)}, and are stored in the cache until
 * either evicted or manually invalidated.
 *
 * <p>Implementations can be NOT THREAD SAFE
 * <p>
 * This cache can't works with null as key, also as value. All methods can produce NPE, if argument will be null
 */
public interface Cache<K, V> {
    /**
     * Returns the optional value associated with {@code key} in this cache, or empty optional if there is no
     * cached value for {@code key}.
     */
    Optional<V> getIfPresent(K key);

    /**
     * Returns the value associated with {@code key} in this cache, obtaining that value from {@code
     * loader} if necessary. The method improves upon the conventional "if cached, return; otherwise
     * create, cache and return" pattern.
     */
    V get(K key, Function<? super K, ? extends V> loader);

    /**
     * Returns a map of the values associated with {@code keys} in this cache. The returned map will
     * only contain entries which are already present in the cache.
     */
    Map<K, V> getAllPresent(Iterable<? extends K> keys);

    /**
     * Associates {@code value} with {@code key} in this cache. If the cache previously contained a
     * value associated with {@code key}, the old value is replaced by {@code value}.
     */
    void put(K key, V value);

    /**
     * Copies all of the mappings from the specified map to the cache. The effect of this call is
     * equivalent to that of calling {@code put(k, v)} on this map once for each mapping from key
     * {@code k} to value {@code v} in the specified map. The behavior of this operation is undefined
     * if the specified map is modified while the operation is in progress.
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * Discards any cached value for key {@code key}.
     */
    void invalidate(K key);

    /**
     * Discards any cached values for keys {@code keys}.
     */
    void invalidateAll(Iterable<? extends K> keys);

    /**
     * Discards all entries in the cache.
     */
    void invalidateAll();

    /**
     * Returns true if key presents in cache, else false
     */
    boolean containsKey(K key);

    /**
     * Returns the number of entries in this cache.
     */
    int size();

    /**
     * Returns true is cache is empty, else false
     */
    boolean isEmpty();


    /**
     * Returns set of keys in cache
     */
    Set<K> keySet();

    /**
     * Returns stream of values in cache
     */
    Stream<V> values();
}


