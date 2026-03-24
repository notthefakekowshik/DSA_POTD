package com.dsapotd.linkedlists;

/**
 * <h1>460. LFU Cache</h1>
 *
 * <p>Design and implement a data structure for a Least Frequently Used (LFU) cache.</p>
 *
 * <p>Implement the <code>LFUCache</code> class:</p>
 * <ul>
 *   <li><code>LFUCache(int capacity)</code> Initializes the object with the capacity of the data structure.</li>
 *   <li><code>int get(int key)</code> Gets the value of the key if the key exists in the cache. Otherwise, returns -1.</li>
 *   <li><code>void put(int key, int value)</code> Update the value of the key if present, or inserts the key if not already present. When the cache reaches its capacity, it should invalidate and remove the least frequently used key before inserting a new item. For this problem, when there is a tie (i.e., two or more keys with the same frequency), the least recently used key would be invalidated.</li>
 * </ul>
 *
 * <p>To determine the least frequently used key, a use counter is maintained for each key in the cache. The key with the smallest use counter is the least frequently used key.</p>
 *
 * <p>When a key is first inserted into the cache, its use counter is set to 1 (due to the put operation). The use counter for a key in the cache is incremented either a get or put operation is called on it.</p>
 *
 * <p>The functions <code>get</code> and <code>put</code> must each run in O(1) average time complexity.</p>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= capacity <= 10^4</li>
 *   <li>0 <= key <= 10^5</li>
 *   <li>0 <= value <= 10^9</li>
 *   <li>At most 2 * 10^5 calls will be made to get and put.</li>
 * </ul>
 *
 * @tags Hash Table, Linked List, Design, Doubly-Linked List
 */
public class LFUCache {

    // Tier 2: Senior (Two Maps + Doubly Linked List)
    // Time Complexity: O(1) for both get and put
    // Space Complexity: O(Capacity)
    
    private static class Node {
        int key, val, count;
        Node prev, next;
        Node(int key, int val) {
            this.key = key;
            this.val = val;
            this.count = 1;
        }
    }

    private static class DLList {
        Node head, tail;
        int size;
        DLList() {
            head = new Node(0, 0);
            tail = new Node(0, 0);
            head.next = tail;
            tail.prev = head;
        }
        void addFirst(Node node) {
            Node nextNode = head.next;
            head.next = node;
            node.prev = head;
            node.next = nextNode;
            nextNode.prev = node;
            size++;
        }
        void remove(Node node) {
            Node prevNode = node.prev;
            Node nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            size--;
        }
        Node removeLast() {
            if (size > 0) {
                Node lastNode = tail.prev;
                remove(lastNode);
                return lastNode;
            }
            return null;
        }
    }

    private final int capacity;
    private int size;
    private int minFreq;
    private final java.util.Map<Integer, Node> nodeMap;
    private final java.util.Map<Integer, DLList> countMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.minFreq = 0;
        this.nodeMap = new java.util.HashMap<>();
        this.countMap = new java.util.HashMap<>();
    }
    
    public int get(int key) {
        if (!nodeMap.containsKey(key)) return -1;
        Node node = nodeMap.get(key);
        updateNode(node);
        return node.val;
    }
    
    public void put(int key, int value) {
        if (capacity == 0) return;

        if (nodeMap.containsKey(key)) {
            Node node = nodeMap.get(key);
            node.val = value;
            updateNode(node);
        } else {
            if (size == capacity) {
                DLList minFreqList = countMap.get(minFreq);
                Node lastNode = minFreqList.removeLast();
                nodeMap.remove(lastNode.key);
                size--;
            }
            size++;
            minFreq = 1;
            Node newNode = new Node(key, value);
            nodeMap.put(key, newNode);
            DLList newList = countMap.getOrDefault(1, new DLList());
            newList.addFirst(newNode);
            countMap.put(1, newList);
        }
    }

    private void updateNode(Node node) {
        int freq = node.count;
        DLList list = countMap.get(freq);
        list.remove(node);
        if (freq == minFreq && list.size == 0) {
            minFreq++;
        }
        node.count++;
        DLList newList = countMap.getOrDefault(node.count, new DLList());
        newList.addFirst(node);
        countMap.put(node.count, newList);
    }

    public static void main(String[] args) {
        System.out.println("Running Optimized (O(1) LFU):");
        LFUCache lfu = new LFUCache(2);
        lfu.put(1, 1);
        lfu.put(2, 2);
        System.out.println(lfu.get(1)); // 1
        lfu.put(3, 3); // evicts 2
        System.out.println(lfu.get(2)); // -1
        System.out.println(lfu.get(3)); // 3
        lfu.put(4, 4); // evicts 1
        System.out.println(lfu.get(1)); // -1
        System.out.println(lfu.get(3)); // 3
        System.out.println(lfu.get(4)); // 4
    }
}
