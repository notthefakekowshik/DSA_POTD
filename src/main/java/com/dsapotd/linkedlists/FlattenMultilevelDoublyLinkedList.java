package com.dsapotd.linkedlists;

/**
 * <h1>430. Flatten a Multilevel Doubly Linked List</h1>
 *
 * <p>You are given a doubly linked list, which contains nodes that have a next pointer, a previous pointer, and an additional child pointer. This child pointer may or may not point to a separate doubly linked list, also containing these special nodes. These child lists may have one or more children of their own, and so on, to produce a multilevel data structure as shown in the example below.</p>
 *
 * <p>Given the <code>head</code> of the first level of the list, flatten the list so that all the nodes appear in a single-level, doubly linked list. Let <code>curr</code> be a node with a child list. The nodes in the child list should appear after <code>curr</code> and before <code>curr.next</code> in the flattened list.</p>
 *
 * <p>Return the <code>head</code> of the flattened list. The nodes in the list must have all of their child pointers set to <code>null</code>.</p>
 *
 * @tags Linked List, Doubly-Linked List, Depth-First Search
 */
public class FlattenMultilevelDoublyLinkedList {

    static class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
        public Node(int val) { this.val = val; }
    }

    // Tier 2: Senior (Iterative Stack-less Merging)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public Node flatten(Node head) {
        if (head == null) return head;

        Node curr = head;
        while (curr != null) {
            if (curr.child != null) {
                Node next = curr.next;
                Node child = curr.child;
                
                curr.next = child;
                child.prev = curr;
                curr.child = null; // Clear child pointer
                
                // Find tail of the child level
                Node tail = child;
                while (tail.next != null) {
                    tail = tail.next;
                }
                
                tail.next = next;
                if (next != null) {
                    next.prev = tail;
                }
            }
            curr = curr.next;
        }
        return head;
    }

    public static void main(String[] args) {
        System.out.println("Running Optimized (Iterative Flatten):");
        Node head = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        head.next = node2; node2.prev = head;
        head.child = node3;
        
        Node res = new FlattenMultilevelDoublyLinkedList().flatten(head);
        System.out.print("Flattened: ");
        while(res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
        System.out.println();
    }
}
