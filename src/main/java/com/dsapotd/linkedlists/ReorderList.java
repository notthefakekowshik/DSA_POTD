package com.dsapotd.linkedlists;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>143. Reorder List</h1>
 *
 * <p>
 * You are given the head of a singly linked-list. The list can be represented
 * as:
 * </p>
 * 
 * <pre>
 * L0 → L1 → … → Ln - 1 → Ln
 * </pre>
 *
 * <p>
 * Reorder the list to be on the following form:
 * </p>
 * 
 * <pre>
 * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
 * </pre>
 *
 * <p>
 * You may not modify the values in the list's nodes. Only nodes themselves may
 * be changed.
 * </p>
 *
 * <h2>Example 1:</h2>
 * 
 * <pre>
 * Input: head = [1,2,3,4]
 * Output: [1,4,2,3]
 * </pre>
 *
 * <h2>Example 2:</h2>
 * 
 * <pre>
 * Input: head = [1,2,3,4,5]
 * Output: [1,5,2,4,3]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 * <li>The number of nodes in the list is in the range [1, 5 * 10^4].</li>
 * <li>1 <= Node.val <= 1000</li>
 * </ul>
 *
 * @tags Linked List, Two Pointers, Stack, Recursion
 */
public class ReorderList {

    // Optimized Solution (Find Middle, Reverse Second Half, Merge)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public void reorderList(ListNode head) {
        if (head == null || head.next == null)
            return;

        // 1. Find the middle
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2. Reverse the second half
        ListNode prev = null, curr = slow.next;
        slow.next = null; // Split the list
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        // 3. Merge the two halves
        ListNode first = head, second = prev;
        while (second != null) {
            ListNode next1 = first.next;
            ListNode next2 = second.next;
            first.next = second;
            second.next = next1;
            first = next1;
            second = next2;
        }
    }

    /**
     * Tier 1: Mid-Level (Using ArrayList for Random Access)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    static class Intermediate {
        public void reorderList(ListNode head) {
            if (head == null)
                return;
            List<ListNode> nodes = new ArrayList<>();
            ListNode curr = head;
            while (curr != null) {
                nodes.add(curr);
                curr = curr.next;
            }

            int left = 0, right = nodes.size() - 1;
            while (left < right) {
                nodes.get(left).next = nodes.get(right);
                left++;
                if (left == right)
                    break;
                nodes.get(right).next = nodes.get(left);
                right--;
            }
            nodes.get(left).next = null;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Find Middle + Reverse + Merge):");
        ListNode head = createList(new int[] { 1, 2, 3, 4, 5 });
        new ReorderList().reorderList(head);
        printList(head); // Expected: [1, 5, 2, 4, 3]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (ArrayList storage):");
        ListNode head = createList(new int[] { 1, 2, 3, 4 });
        new Intermediate().reorderList(head);
        printList(head); // Expected: [1, 4, 2, 3]
    }

    private static ListNode createList(int[] vals) {
        if (vals.length == 0)
            return null;
        ListNode head = new ListNode(vals[0]);
        ListNode curr = head;
        for (int i = 1; i < vals.length; i++) {
            curr.next = new ListNode(vals[i]);
            curr = curr.next;
        }
        return head;
    }

    private static void printList(ListNode head) {
        System.out.print("[");
        while (head != null) {
            System.out.print(head.val + (head.next != null ? ", " : ""));
            head = head.next;
        }
        System.out.println("]");
    }
}
