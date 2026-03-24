package com.dsapotd.linkedlists;

/**
 * <h1>25. Reverse Nodes in k-Group</h1>
 *
 * <p>Given the <code>head</code> of a linked list, reverse the nodes of the list <code>k</code> at a time, and return the modified list.</p>
 *
 * <p><code>k</code> is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of <code>k</code> then left-out nodes, in the end, should remain as it is.</p>
 *
 * <p>You may not alter the values in the list's nodes, only nodes themselves may be changed.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [2,1,4,3,5]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is <code>n</code>.</li>
 *   <li>1 <= k <= n <= 5000</li>
 *   <li>0 <= Node.val <= 1000</li>
 * </ul>
 *
 * <p><b>Follow-up:</b> Can you solve the problem in O(1) extra memory space?</p>
 *
 * @tags Linked List, Recursion
 */
public class ReverseKGroup {

    // Tier 2: Senior (Iterative O(1) space)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k == 1) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode curr = dummy, nex = dummy, pre = dummy;
        int count = 0;

        while (curr.next != null) {
            curr = curr.next;
            count++;
        }

        while (count >= k) {
            curr = pre.next;
            nex = curr.next;
            for (int i = 1; i < k; i++) {
                curr.next = nex.next;
                nex.next = pre.next;
                pre.next = nex;
                nex = curr.next;
            }
            pre = curr;
            count -= k;
        }

        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println("Running Optimized (Iterative O(1)):");
        ListNode head = createList(new int[]{1, 2, 3, 4, 5});
        printList(new ReverseKGroup().reverseKGroup(head, 2)); // [2, 1, 4, 3, 5]
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
