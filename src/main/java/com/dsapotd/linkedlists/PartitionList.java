package com.dsapotd.linkedlists;

/**
 * <h1>86. Partition List</h1>
 *
 * <p>Given the <code>head</code> of a linked list and a value <code>x</code>, partition it such that all nodes less than <code>x</code> come before nodes greater than or equal to <code>x</code>.</p>
 * <p>You should preserve the original relative order of the nodes in each of the two partitions.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,4,3,2,5,2], x = 3
 * Output: [1,2,2,4,3,5]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 200].</li>
 *   <li>-100 <= Node.val <= 100</li>
 *   <li>-200 <= x <= 200</li>
 * </ul>
 *
 * @tags Linked List, Two Pointers
 */
public class PartitionList {

    // Tier 2: Senior (Two-Chain Stitching)
    // Time Complexity: O(N) where N is the number of nodes
    // Space Complexity: O(1) - re-linking existing nodes
    public ListNode partition(ListNode head, int x) {
        ListNode lessDummy = new ListNode(0);
        ListNode greaterDummy = new ListNode(0);
        ListNode less = lessDummy, greater = greaterDummy;

        while (head != null) {
            if (head.val < x) {
                less.next = head;
                less = less.next;
            } else {
                greater.next = head;
                greater = greater.next;
            }
            head = head.next;
        }

        // Crucial: Cut off the end of the greater chain to prevent cycles
        greater.next = null;
        
        // Stitch the two chains
        less.next = greaterDummy.next;

        return lessDummy.next;
    }

    public static void main(String[] args) {
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Two-Chain):");
        ListNode head = createList(new int[]{1, 4, 3, 2, 5, 2});
        printList(new PartitionList().partition(head, 3)); // Expected: [1, 2, 2, 4, 3, 5]
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
