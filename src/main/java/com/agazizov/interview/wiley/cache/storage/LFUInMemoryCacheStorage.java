package com.agazizov.interview.wiley.cache.storage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class LFUInMemoryCacheStorage<K, V> implements CacheStorage<K, V> {
    private final Map<K, CacheItem<K, V>> cache;
    private final LinkedHashSet<CacheItem<K, V>>[] frequencyList;
    private final int maxFrequency;
    private final int maxSize;
    private int lowestFrequency;

    @SuppressWarnings("unchecked")
    public LFUInMemoryCacheStorage(int maxSize) {
        this.cache = new HashMap<>(maxSize);
        this.frequencyList = new LinkedHashSet[maxSize];
        this.lowestFrequency = 0;
        this.maxFrequency = maxSize - 1;
        this.maxSize = maxSize;
        IntStream.rangeClosed(0, maxFrequency).forEach(i -> frequencyList[i] = new LinkedHashSet<>());
    }

    public V put(K k, V v) {
        V oldValue = null;
        CacheItem<K, V> currentNode = cache.get(k);
        if (currentNode == null) {
            if (cache.size() == maxSize) {
                eviction();
            }
            LinkedHashSet<CacheItem<K, V>> nodes = frequencyList[0];
            currentNode = new CacheItem<>(k, v, 0);
            nodes.add(currentNode);
            cache.put(k, currentNode);
            lowestFrequency = 0;
        } else {
            oldValue = currentNode.getValue();
            currentNode.setValue(v);
        }
        return oldValue;
    }


    public void putAll(Map<? extends K, ? extends V> map) {
        map.forEach(this::put);
    }

    @SuppressWarnings("unchecked")
    public V get(Object k) {
        CacheItem<K, V> currentNode = cache.get(k);
        if (currentNode != null) {
            int currentFrequency = currentNode.getFrequency();
            if (currentFrequency < maxFrequency) {
                int nextFrequency = currentFrequency + 1;
                LinkedHashSet<CacheItem<K, V>> currentNodes = frequencyList[currentFrequency];
                LinkedHashSet<CacheItem<K, V>> newNodes = frequencyList[nextFrequency];
                moveToNextFrequency(currentNode, nextFrequency, currentNodes, newNodes);
                cache.put((K) k, currentNode);
                if (lowestFrequency == currentFrequency && currentNodes.isEmpty()) {
                    lowestFrequency = nextFrequency;
                }
            } else {
                LinkedHashSet<CacheItem<K, V>> nodes = frequencyList[currentFrequency];
                nodes.remove(currentNode);
                nodes.add(currentNode);
            }
            return currentNode.getValue();
        } else {
            return null;
        }
    }

    public V remove(Object k) {
        CacheItem<K, V> currentNode = cache.remove(k);
        if (currentNode != null) {
            LinkedHashSet<CacheItem<K, V>> nodes = frequencyList[currentNode.getFrequency()];
            nodes.remove(currentNode);
            if (lowestFrequency == currentNode.getFrequency()) {
                findNextLowestFrequency();
            }
            return currentNode.getValue();
        } else {
            return null;
        }
    }

    public void clear() {
        Arrays.stream(frequencyList).forEach(HashSet::clear);
        cache.clear();
        lowestFrequency = 0;
    }

    public Set<K> keySet() {
        return cache.keySet();
    }

    public Collection<V> values() {
        return cache.values()
                .stream()
                .map(CacheItem::getValue)
                .collect(Collectors.toList());
    }

    public Set<Entry<K, V>> entrySet() {
        return cache.entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), e.getValue().getValue()))
                .collect(Collectors.toSet());
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public boolean containsKey(Object o) {
        return cache.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return cache.values().stream().map(CacheItem::getValue).anyMatch(e -> e.equals(o));
    }

    private void eviction() {
        LinkedHashSet<CacheItem<K, V>> nodes = frequencyList[lowestFrequency];
        if (nodes.isEmpty()) {
            throw new ConcurrentModificationException();
        } else {
            Iterator<CacheItem<K, V>> it = nodes.iterator();
            CacheItem<K, V> node = it.next();
            it.remove();
            cache.remove(node.key);
            if (!it.hasNext()) {
                findNextLowestFrequency();
            }
        }
    }

    private void moveToNextFrequency(CacheItem<K, V> currentNode, int nextFrequency, LinkedHashSet<CacheItem<K, V>> currentNodes, LinkedHashSet<CacheItem<K, V>> newNodes) {
        currentNodes.remove(currentNode);
        newNodes.add(currentNode);
        currentNode.setFrequency(nextFrequency);
    }

    private void findNextLowestFrequency() {
        while (lowestFrequency <= maxFrequency && frequencyList[lowestFrequency].isEmpty()) {
            lowestFrequency++;
        }
        if (lowestFrequency > maxFrequency) {
            lowestFrequency = 0;
        }
    }

    private static class CacheItem<K, V> {
        public final K key;
        private V value;
        private int frequency;

        public CacheItem(K key, V value, int frequency) {
            this.key = key;
            this.setValue(value);
            this.setFrequency(frequency);
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }
}