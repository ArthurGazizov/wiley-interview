# wiley-interview

Implementation of Cachese
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
