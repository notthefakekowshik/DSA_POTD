package com.dsapotd.linkedlists;

import java.util.concurrent.locks.StampedLock;

/**
 * Thread-safe version of AllOne using StampedLock.
 * Optimistic reads are used for getMaxKey and getMinKey to allow for maximum
 * concurrency when no writes are occurring, falling back to a full read lock if necessary.
 */
public class AllOneThreadSafe {

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
    private final StampedLock lock;

    public AllOneThreadSafe() {
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
        map = new java.util.HashMap<>();
        lock = new StampedLock();
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
        long stamp = lock.writeLock();
        try {
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
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    public void dec(String key) {
        long stamp = lock.writeLock();
        try {
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
        } finally {
            lock.unlockWrite(stamp);
        }
    }
    
    public String getMaxKey() {
        long stamp = lock.tryOptimisticRead();
        String result = "";
        boolean valid = false;
        try {
            Node p = tail.prev;
            if (p != head) {
                java.util.Iterator<String> it = p.keys.iterator();
                if (it.hasNext()) {
                    result = it.next();
                }
            }
            valid = lock.validate(stamp);
        } catch (Exception e) {
            valid = false;
        }
        
        if (!valid) {
            stamp = lock.readLock();
            try {
                result = tail.prev == head ? "" : tail.prev.keys.iterator().next();
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return result;
    }
    
    public String getMinKey() {
        long stamp = lock.tryOptimisticRead();
        String result = "";
        boolean valid = false;
        try {
            Node n = head.next;
            if (n != tail) {
                java.util.Iterator<String> it = n.keys.iterator();
                if (it.hasNext()) {
                    result = it.next();
                }
            }
            valid = lock.validate(stamp);
        } catch (Exception e) {
            valid = false;
        }
        
        if (!valid) {
            stamp = lock.readLock();
            try {
                result = head.next == tail ? "" : head.next.keys.iterator().next();
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return result;
    }
}
