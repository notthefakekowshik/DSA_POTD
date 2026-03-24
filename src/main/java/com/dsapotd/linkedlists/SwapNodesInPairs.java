package com.dsapotd.linkedlists;

/**
 * <h1>24. Swap Nodes in Pairs</h1>
 *
 * <p>Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,3,4]
 * Output: [2,1,4,3]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 100].</li>
 *   <li>0 <= Node.val <= 100</li>
 * </ul>
 *
 * @tags Linked List, Recursion
 */
public class SwapNodesInPairs {

    // Tier 2: Senior (Iterative with Dummy Node)
    // Time Complexity: O(N) where N is the number of nodes
    // Space Complexity: O(1)
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;

        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = prev.next.next;

            // Swapping logic:
            // prev -> first -> second -> nextNode
            // becomes: prev -> second -> first -> nextNode
            first.next = second.next;
            second.next = first;
            prev.next = second;

            // Move prev to 'first' node which is now the second node in the pair
            prev = first;
        }

        return dummy.next;
    }

    /**
     * Tier 1: Mid-Level (Recursive)
     * Time Complexity: O(N)
     * Space Complexity: O(N) for recursion stack
     */
    static class Intermediate {
        public ListNode swapPairs(ListNode head) {
            if (head == null || head.next == null) return head;

            ListNode first = head;
            ListNode second = head.next;

            first.next = swapPairs(second.next);
            second.next = first;

            return second;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Iterative):");
        ListNode head = createList(new int[]{1, 2, 3, 4});
        printList(new SwapNodesInPairs().swapPairs(head)); // Expected: [2, 1, 4, 3]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Recursive):");
        ListNode head = createList(new int[]{1, 2, 3, 4});
        printList(new Intermediate().swapPairs(head)); // Expected: [2, 1, 4, 3]
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
