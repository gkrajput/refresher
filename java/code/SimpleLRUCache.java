package code;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleLRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public SimpleLRUCache(int capacity) {
        // The 'true' argument enables access-order, which is crucial for LRU behavior.
        // Entries are ordered from least-recently accessed to most-recently.
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    /**
     * This method is called by put() and putAll(). It returns true if the
     * eldest entry should be removed, which is when the size exceeds capacity.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    // --- Example Usage ---
    public static void main(String[] args) {
        System.out.println("--- Simple LRU Cache (LinkedHashMap) Demo ---");
        SimpleLRUCache<Integer, String> cache = new SimpleLRUCache<>(3);

        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        System.out.println("Initial cache: " + cache.keySet()); // Expected: [1, 2, 3]

        // Accessing 1 makes it the most recently used
        cache.get(1);
        System.out.println("Accessing key 1...");

        // Adding a new item (4) should evict the least recently used item (2)
        cache.put(4, "Four");
        System.out.println("Adding key 4, evicts key 2. Cache: " + cache.keySet()); // Expected: [3, 1, 4]

        // Accessing 3
        cache.get(3);
        System.out.println("Accessing key 3...");

        // Adding a new item (5) should evict the least recently used item (1)
        cache.put(5, "Five");
        System.out.println("Adding key 5, evicts key 1. Cache: " + cache.keySet()); // Expected: [4, 3, 5]
    }
}

