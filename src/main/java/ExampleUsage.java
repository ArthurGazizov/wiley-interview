import com.agazizov.interview.wiley.cache.CacheBuilder;
import com.agazizov.interview.wiley.cache.LoadingCache;

/**
 * Example usage of cache
 */
public class ExampleUsage {
    public static void main(String[] args) {
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
}
