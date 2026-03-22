package com.dsapotd.linkedlists;

/**
 * <h1>19. Remove Nth Node From End of List</h1>
 *
 * <p>Given the <code>head</code> of a linked list, remove the <code>nth</code> node from the end of the list and return its head.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,3,4,5], n = 2
 * Output: [1,2,3,5]
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: head = [1], n = 1
 * Output: []
 * </pre>
 *
 * <h2>Example 3:</h2>
 * <pre>
 * Input: head = [1,2], n = 1
 * Output: [1]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is <code>sz</code>.</li>
 *   <li>1 <= sz <= 30</li>
 *   <li>0 <= Node.val <= 100</li>
 *   <li>1 <= n <= sz</li>
 * </ul>
 *
 * <p><strong>Follow up:</strong> Could you do this in one pass?</p>
 *
 * @tags Linked List, Two Pointers
 */
public class RemoveNthFromEnd {

    // Optimized Solution (One Pass - Two Pointers)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode first = dummy;
        ListNode second = dummy;
        
        // Move first pointer n+1 steps ahead
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        
        // Move both pointers until first reaches the end
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        
        second.next = second.next.next;
        return dummy.next;
    }

    /**
     * Tier 1: Mid-Level (Two Pass - Length calculation)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    static class Intermediate {
        public ListNode removeNthFromEnd(ListNode head, int n) {
            int length = 0;
            ListNode curr = head;
            while (curr != null) {
                length++;
                curr = curr.next;
            }
            
            ListNode dummy = new ListNode(0, head);
            curr = dummy;
            for (int i = 0; i < length - n; i++) {
                curr = curr.next;
            }
            
            curr.next = curr.next.next;
            return dummy.next;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (One Pass):");
        ListNode head = createList(new int[]{1, 2, 3, 4, 5});
        ListNode result = new RemoveNthFromEnd().removeNthFromEnd(head, 2);
        printList(result); // Expected: [1, 2, 3, 5]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Two Pass):");
        ListNode head = createList(new int[]{1});
        ListNode result = new Intermediate().removeNthFromEnd(head, 1);
        printList(result); // Expected: []
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
