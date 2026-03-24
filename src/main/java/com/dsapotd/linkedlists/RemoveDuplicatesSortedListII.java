package com.dsapotd.linkedlists;

/**
 * <h1>82. Remove Duplicates from Sorted List II</h1>
 *
 * <p>Given the <code>head</code> of a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list. Return the linked list sorted as well.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,3,3,4,4,5]
 * Output: [1,2,5]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is in the range [0, 300].</li>
 *   <li>-100 <= Node.val <= 100</li>
 *   <li>The list is guaranteed to be sorted in ascending order.</li>
 * </ul>
 *
 * @tags Linked List, Two Pointers
 */
public class RemoveDuplicatesSortedListII {

    // Tier 2: Senior (Iterative with Dummy Node)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        
        while (head != null) {
            if (head.next != null && head.val == head.next.val) {
                // Move head to the last duplicate
                while (head.next != null && head.val == head.next.val) {
                    head = head.next;
                }
                // Skip all duplicates
                prev.next = head.next;
            } else {
                prev = prev.next;
            }
            head = head.next;
        }
        
        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println("Running Optimized (Iterative with Dummy):");
        ListNode head = createList(new int[]{1, 2, 3, 3, 4, 4, 5});
        printList(new RemoveDuplicatesSortedListII().deleteDuplicates(head)); // [1, 2, 5]
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
