package com.dsapotd.linkedlists;

/**
 * <h1>206. Reverse Linked List</h1>
 *
 * <p>
 * Given the head of a singly linked list, reverse the list, and return
 * the reversed list.
 * </p>
 *
 * <h2>Example 1:</h2>
 *
 * <pre>
 * Input:  head = [1, 2, 3, 4, 5]
 * Output: [5, 4, 3, 2, 1]
 * </pre>
 *
 * <h2>Example 2:</h2>
 *
 * <pre>
 * Input:  head = [1, 2]
 * Output: [2, 1]
 * </pre>
 *
 * <h2>Example 3:</h2>
 *
 * <pre>
 * Input:  head = []
 * Output: []
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 * <li>The number of nodes in the list is in the range [0, 5000].</li>
 * <li>-5000 <= Node.val <= 5000</li>
 * </ul>
 *
 * @tags Linked List, Recursion
 */
public class ReverseLinkedList {

    // Tier 1: Mid-Level — use a Stack to reverse by pushing all nodes, then relinking
    static class Intermediate {
        // Time Complexity: O(N)
        // Space Complexity: O(N)
        public ListNode reverseList(ListNode head) {
            java.util.Deque<ListNode> stack = new java.util.ArrayDeque<>();
            ListNode curr = head;
            while (curr != null) {
                stack.push(curr);
                curr = curr.next;
            }
            ListNode dummy = new ListNode(0);
            curr = dummy;
            while (!stack.isEmpty()) {
                curr.next = stack.pop();
                curr = curr.next;
            }
            curr.next = null; // prevent cycle on former tail
            return dummy.next;
        }
    }

    // Tier 2: Senior — in-place iterative reversal with prev/curr pointer dance; O(1) space
    static class Optimized {
        // Time Complexity: O(N)
        // Space Complexity: O(1)
        public ListNode reverseList(ListNode head) {
            ListNode prev = null, curr = head;
            while (curr != null) {
                ListNode next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }
            return prev;
        }
    }

    // Tier 3: Staff — recursive reversal; elegant but O(N) implicit call-stack space.
    // In JVM terms this can cause StackOverflowError for lists > ~5000 nodes (default stack ~512KB).
    // For production, prefer iterative. Tail-call optimization is NOT performed by the JVM,
    // so this is strictly worse than Optimized in memory-constrained environments.

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized:");
        ListNode head = createList(new int[]{1, 2, 3, 4, 5});
        ListNode result = new Optimized().reverseList(head);
        printList(result); // Expected: [5, 4, 3, 2, 1]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate:");
        ListNode head = createList(new int[]{1, 2, 3, 4, 5});
        ListNode result = new Intermediate().reverseList(head);
        printList(result); // Expected: [5, 4, 3, 2, 1]
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
