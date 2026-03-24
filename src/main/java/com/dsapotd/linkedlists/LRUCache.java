package com.dsapotd.linkedlists;

/**
 * <h1>146. LRU Cache</h1>
 *
 * <p>Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.</p>
 *
 * <p>Implement the <code>LRUCache</code> class:</p>
 * <ul>
 *   <li><code>LRUCache(int capacity)</code> Initialize the LRU cache with positive size <code>capacity</code>.</li>
 *   <li><code>int get(int key)</code> Return the value of the <code>key</code> if the key exists, otherwise return <code>-1</code>.</li>
 *   <li><code>void put(int key, int value)</code> Update the value of the <code>key</code> if the <code>key</code> exists. Otherwise, add the <code>key-value</code> pair to the cache. If the number of keys exceeds the <code>capacity</code> from this operation, evict the least recently used key.</li>
 * </ul>
 *
 * <p>The functions <code>get</code> and <code>put</code> must each run in O(1) average time complexity.</p>
 *
 * <h2>Example:</h2>
 * <pre>
 * Input:
 * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
 * Output:
 * [null, null, null, 1, null, -1, null, -1, 3, 4]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= capacity <= 3000</li>
 *   <li>0 <= key <= 10^4</li>
 *   <li>0 <= value <= 10^5</li>
 *   <li>At most 2 * 10^5 calls will be made to get and put.</li>
 * </ul>
 *
 * @tags Linked List, Hash Table, Design, Doubly Linked List
 */
public class LRUCache {

    // Tier 2: Senior (Manual Doubly Linked List + HashMap)
    // Time Complexity: O(1) for both get and put
    // Space Complexity: O(Capacity)
    private static class Node {
        int key, value;
        Node prev, next;
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final java.util.Map<Integer, Node> cache;
    private final Node head, tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new java.util.HashMap<>();
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!cache.containsKey(key)) return -1;
        Node node = cache.get(key);
        moveToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            moveToHead(node);
        } else {
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addNode(newNode);
            if (cache.size() > capacity) {
                Node last = popTail();
                cache.remove(last.key);
            }
        }
    }

    private void addNode(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addNode(node);
    }

    private Node popTail() {
        Node res = tail.prev;
        removeNode(res);
        return res;
    }

    /**
     * Tier 1: Mid-Level (Using LinkedHashMap)
     * Time Complexity: O(1)
     * Space Complexity: O(Capacity)
     */
    static class Intermediate extends java.util.LinkedHashMap<Integer, Integer> {
        private final int capacity;

        public Intermediate(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        public int getVal(int key) {
            return super.getOrDefault(key, -1);
        }

        public void putVal(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<Integer, Integer> eldest) {
            return size() > capacity;
        }
    }

    /**
     * Tier 3: Staff (Thread Safety & Scalability)
     * Discussion:
     * 1. Thread Safety: In production, use ConcurrentHashMap + ConcurrentLinkedQueue (though Queue lacks O(1) random removal).
     * 2. Lock Striping: Partition the cache into segments to reduce lock contention.
     * 3. Memory Locality: Using Node objects adds overhead. High-performance caches like Caffeine use complex 
     *    Window TinyLFU algorithms and handle object layout carefully to minimize GC.
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Manual DLL):");
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));    // returns 1
        cache.put(3, 3);                    // evicts key 2
        System.out.println(cache.get(2));    // returns -1 (not found)
        cache.put(4, 4);                    // evicts key 1
        System.out.println(cache.get(1));    // returns -1 (not found)
        System.out.println(cache.get(3));    // returns 3
        System.out.println(cache.get(4));    // returns 4
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (LinkedHashMap):");
        Intermediate cache = new Intermediate(2);
        cache.putVal(1, 1);
        cache.putVal(2, 2);
        System.out.println(cache.getVal(1));
        cache.putVal(3, 3);
        System.out.println(cache.getVal(2));
    }
}
