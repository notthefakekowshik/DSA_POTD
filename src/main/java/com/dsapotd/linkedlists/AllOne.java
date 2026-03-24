package com.dsapotd.linkedlists;

/**
 * <h1>432. All O`one Data Structure</h1>
 *
 * <p>Design a data structure to store the strings' count with the ability to return the strings with minimum and maximum counts.</p>
 *
 * <p>Implement the <code>AllOne</code> class:</p>
 * <ul>
 *   <li><code>AllOne()</code> Initializes the object of the data structure.</li>
 *   <li><code>inc(String key)</code> Increments the count of the string <code>key</code> by 1. If <code>key</code> does not exist in the data structure, insert it with count 1.</li>
 *   <li><code>dec(String key)</code> Decrements the count of the string <code>key</code> by 1. If the count of <code>key</code> becomes 0, remove it from the data structure. It is guaranteed that <code>key</code> exists in the data structure before the decrement.</li>
 *   <li><code>getMaxKey()</code> Returns one of the keys with the maximal count. If no element exists, return an empty string <code>""</code>.</li>
 *   <li><code>getMinKey()</code> Returns one of the keys with the minimum count. If no element exists, return an empty string <code>""</code>.</li>
 * </ul>
 *
 * <p><b>Note</b> that each function must run in O(1) average time complexity.</p>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= key.length <= 10</li>
 *   <li>key consists of lowercase English letters.</li>
 *   <li>It is guaranteed that for each call to dec, key is existing in the data structure.</li>
 *   <li>At most 5 * 10^4 calls will be made to inc, dec, getMaxKey, and getMinKey.</li>
 * </ul>
 *
 * @tags Hash Table, Linked List, Design, Doubly-Linked List
 */
public class AllOne {

    // Tier 2: Senior (Doubly Linked List of Frequencies + HashMap)
    // Time Complexity: O(1) for all operations
    // Space Complexity: O(N)
    private static class Node {
        int count;
        java.util.Set<String> keys;
        Node prev, next;
        Node(int c) {
            count = c;
            keys = new java.util.HashSet<>();
        }
    }

    private final Node head;
    private final Node tail;
    private final java.util.Map<String, Node> map;

    public AllOne() {
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
        map = new java.util.HashMap<>();
    }

    private Node insertAfter(Node prevNode, int count) {
        Node newNode = new Node(count);
        newNode.next = prevNode.next;
        newNode.prev = prevNode;
        prevNode.next.prev = newNode;
        prevNode.next = newNode;
        return newNode;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    public void inc(String key) {
        if (!map.containsKey(key)) {
            if (head.next == tail || head.next.count != 1) {
                insertAfter(head, 1);
            }
            head.next.keys.add(key);
            map.put(key, head.next);
        } else {
            Node currNode = map.get(key);
            int newCount = currNode.count + 1;
            if (currNode.next == tail || currNode.next.count != newCount) {
                insertAfter(currNode, newCount);
            }
            currNode.next.keys.add(key);
            map.put(key, currNode.next);
            currNode.keys.remove(key);
            if (currNode.keys.isEmpty()) {
                removeNode(currNode);
            }
        }
    }
    
    public void dec(String key) {
        if (!map.containsKey(key)) return;
        Node currNode = map.get(key);
        int newCount = currNode.count - 1;
        
        if (newCount == 0) {
            map.remove(key);
        } else {
            if (currNode.prev == head || currNode.prev.count != newCount) {
                insertAfter(currNode.prev, newCount);
            }
            currNode.prev.keys.add(key);
            map.put(key, currNode.prev);
        }
        
        currNode.keys.remove(key);
        if (currNode.keys.isEmpty()) {
            removeNode(currNode);
        }
    }
    
    public String getMaxKey() {
        return tail.prev == head ? "" : tail.prev.keys.iterator().next();
    }
    
    public String getMinKey() {
        return head.next == tail ? "" : head.next.keys.iterator().next();
    }

    public static void main(String[] args) {
        System.out.println("Running Optimized (O(1) AllOne):");
        AllOne allOne = new AllOne();
        allOne.inc("hello");
        allOne.inc("hello");
        System.out.println(allOne.getMaxKey()); // "hello"
        System.out.println(allOne.getMinKey()); // "hello"
        allOne.inc("leet");
        System.out.println(allOne.getMaxKey()); // "hello"
        System.out.println(allOne.getMinKey()); // "leet"
    }
}
