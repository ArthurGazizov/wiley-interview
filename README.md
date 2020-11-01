# wiley-interview

Implementation of Caches
## Usage
```
public class ExampleUsage {
    public static void example1() {
        LoadingCache<Integer, Integer> cache = CacheBuilder.<Integer, Integer>builder()
                .maxSize(3)
                .lru()
                .loader(i -> i * i)
                .build();

        System.out.println(cache.get(1)); // prints 1
        System.out.println(cache.get(2)); // prints 4
        System.out.println(cache.get(3)); //prints 9

        System.out.println(cache.size()); // prints 3

        cache.get(1);
        cache.get(3); // use 1, 3
        System.out.println(cache.get(4)); //prints 16

        System.out.println(cache.size()); // prints 3
        System.out.println(cache.containsKey(2)); // prints false
    }

    public static void example2() {
        Cache<Integer, Integer> cache = CacheBuilder.<Integer, Integer>builder()
                .maxSize(3)
                .lru()
                .build();
        cache.put(1, 10);
        cache.getIfPresent(1).ifPresent(System.out::println); // prints 10
    }

    public static void example3() {
        Cache<Integer, String> cache = CacheBuilder.<Integer, String>builder()
                .maxSize(3)
                .lfu()
                .build();
        cache.get(1, String::valueOf);
        cache.getIfPresent(1).ifPresent(System.out::println); // prints string '1'
    }
}

```

### Implementation details
*   There are 2 interfaces for works with caches: `Cache<K, V>` and `LoadingCache<K,V>`. 
*   With `Cache<K, V>` you can persist key value pair in cache
*   `LoadingCache<K,V>` gives a more convenient way to  load or compute a value associated with a key
*   Interface `Storage<K, V>` represents an abstraction of storage
*   Only in-memory repository is currently implemented with 2 different eviction policies: LRU and LFU
*   LRU implementation based on `LinkedHashMap` with `accessOrder = true` flag. I chose it to solve the problem easier and avoid mistakes
*   LFU implementation based on http://dhruvbird.com/lfu.pdf
*   Cache doesn't work with nulls in keys or in values to make it clearer and avoid unnecessary mistakes
*   Not thread safe