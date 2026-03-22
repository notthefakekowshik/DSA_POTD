package com.dsapotd.linkedlists.cycle;

import java.util.HashSet;
import java.util.Set;

/**
 * <h1>141. Linked List Cycle</h1>
 *
 * <p>Given <code>head</code>, the head of a linked list, determine if the linked list has a cycle in it.</p>
 *
 * <p>There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the <code>next</code> pointer. Internally, <code>pos</code> is used to denote the index of the node that tail's <code>next</code> pointer is connected to. <strong>Note that <code>pos</code> is not passed as a parameter.</strong></p>
 *
 * <p>Return <code>true</code> if there is a cycle in the linked list. Otherwise, return <code>false</code>.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [3,2,0,-4], pos = 1
 * Output: true
 * Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: head = [1,2], pos = 0
 * Output: true
 * Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 10^4].</li>
 *   <li>-10^5 <= Node.val <= 10^5</li>
 *   <li>pos is -1 or a valid index in the linked-list.</li>
 * </ul>
 *
 * <p><strong>Follow up:</strong> Can you solve it using O(1) (i.e. constant) memory?</p>
 *
 * @tags Linked List, Two Pointers, Hash Table, Cycle Detection
 */
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class LinkedListCycle {

    // Optimized Solution (Floyd's Tortoise and Hare)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) return false;
        
        ListNode slow = head;
        ListNode fast = head.next;
        
        while (slow != fast) {
            if (fast == null || fast.next == null) return false;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        return true;
    }

    /**
     * Tier 1: Mid-Level (Hash Set)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    static class Intermediate {
        public boolean hasCycle(ListNode head) {
            Set<ListNode> seen = new HashSet<>();
            while (head != null) {
                if (seen.contains(head)) return true;
                seen.add(head);
                head = head.next;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Floyd's Cycle-Finding Algorithm):");
        ListNode head = new ListNode(3);
        ListNode cycleNode = new ListNode(2);
        head.next = cycleNode;
        head.next.next = new ListNode(0);
        head.next.next.next = new ListNode(-4);
        head.next.next.next.next = cycleNode; // Create cycle

        LinkedListCycle solution = new LinkedListCycle();
        System.out.println("Has Cycle: " + solution.hasCycle(head)); // Expected: true
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Hash Set):");
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = head; // Create cycle

        Intermediate intermediate = new Intermediate();
        System.out.println("Has Cycle: " + intermediate.hasCycle(head)); // Expected: true
    }
}
