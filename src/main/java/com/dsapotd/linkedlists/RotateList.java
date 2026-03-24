package com.dsapotd.linkedlists;

/**
 * <h1>61. Rotate List</h1>
 *
 * <p>Given the <code>head</code> of a linked list, rotate the list to the right by <code>k</code> places.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [4,5,1,2,3]
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: head = [0,1,2], k = 4
 * Output: [2,0,1]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 500].</li>
 *   <li>-100 <= Node.val <= 100</li>
 *   <li>0 <= k <= 2 * 10^9</li>
 * </ul>
 *
 * @tags Linked List, Two Pointers
 */
public class RotateList {

    // Tier 2: Senior (Circular Link Approach)
    // Time Complexity: O(N) where N is the number of nodes
    // Space Complexity: O(1)
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;

        // 1. Find length and tail node
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
            length++;
        }

        // 2. Handle k >= length
        k = k % length;
        if (k == 0) return head;

        // 3. Connect tail to head to form a circle
        tail.next = head;

        // 4. Find the new tail: (length - k) nodes from the start
        ListNode newTail = head;
        for (int i = 0; i < length - k - 1; i++) {
            newTail = newTail.next;
        }

        // 5. Break the circle
        ListNode newHead = newTail.next;
        newTail.next = null;

        return newHead;
    }

    /**
     * Tier 1: Mid-Level (N-Pointer Approach)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    static class Intermediate {
        public ListNode rotateRight(ListNode head, int k) {
            if (head == null || head.next == null || k == 0) return head;

            int len = 0;
            ListNode curr = head;
            while (curr != null) {
                len++;
                curr = curr.next;
            }

            k %= len;
            if (k == 0) return head;

            ListNode fast = head;
            ListNode slow = head;

            for (int i = 0; i < k; i++) fast = fast.next;

            while (fast.next != null) {
                fast = fast.next;
                slow = slow.next;
            }

            ListNode newHead = slow.next;
            slow.next = null;
            fast.next = head;

            return newHead;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Circular Link):");
        ListNode head = createList(new int[]{1, 2, 3, 4, 5});
        printList(new RotateList().rotateRight(head, 2)); // Expected: [4, 5, 1, 2, 3]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Two Pointers):");
        ListNode head = createList(new int[]{0, 1, 2});
        printList(new Intermediate().rotateRight(head, 4)); // Expected: [2, 0, 1]
    }

    private static ListNode createList(int[] vals) {
        if (vals.length == 0) return null;
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
