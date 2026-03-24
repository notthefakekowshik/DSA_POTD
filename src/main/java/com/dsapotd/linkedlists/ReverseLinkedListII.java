package com.dsapotd.linkedlists;

/**
 * <h1>92. Reverse Linked List II</h1>
 *
 * <p>Given the <code>head</code> of a singly linked list and two integers <code>left</code> and <code>right</code> where <code>left <= right</code>, reverse the nodes of the list from position <code>left</code> to position <code>right</code>, and return the reversed list.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,3,4,5], left = 2, right = 4
 * Output: [1,4,3,2,5]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is n.</li>
 *   <li>1 <= n <= 500</li>
 *   <li>-500 <= Node.val <= 500</li>
 *   <li>1 <= left <= right <= n</li>
 * </ul>
 *
 * @tags Linked List
 */
public class ReverseLinkedListII {

    // Tier 2: Senior (Single Pass In-place Reversal)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || left == right) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;

        // 1. Move 'pre' to the node just before the sub-range
        for (int i = 0; i < left - 1; i++) pre = pre.next;

        // 2. Reverse sub-range using pointer-shifting dance
        // Example: pre -> 1 -> 2 -> 3 -> 4, left=2, right=4
        ListNode start = pre.next; // '1'
        ListNode then = start.next; // '2'

        for (int i = 0; i < right - left; i++) {
            start.next = then.next;
            then.next = pre.next;
            pre.next = then;
            then = start.next;
        }

        return dummy.next;
    }

    public static void main(String[] args) {
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (One Pass):");
        ListNode head = createList(new int[]{1, 2, 3, 4, 5});
        printList(new ReverseLinkedListII().reverseBetween(head, 2, 4)); // Expected: [1, 4, 3, 2, 5]
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
