package com.dsapotd.linkedlists;

/**
 * <h1>21. Merge Two Sorted Lists</h1>
 *
 * <p>
 * You are given the heads of two sorted linked lists {@code list1} and
 * {@code list2}. Merge the two lists into one sorted list. The list should
 * be made by splicing together the nodes of the first two lists.
 * Return the head of the merged linked list.
 * </p>
 *
 * <h2>Example 1:</h2>
 *
 * <pre>
 * Input:  list1 = [1, 2, 4], list2 = [1, 3, 4]
 * Output: [1, 1, 2, 3, 4, 4]
 * </pre>
 *
 * <h2>Example 2:</h2>
 *
 * <pre>
 * Input:  list1 = [], list2 = []
 * Output: []
 * </pre>
 *
 * <h2>Example 3:</h2>
 *
 * <pre>
 * Input:  list1 = [], list2 = [0]
 * Output: [0]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 * <li>The number of nodes in both lists is in the range [0, 50].</li>
 * <li>-100 <= Node.val <= 100</li>
 * <li>Both list1 and list2 are sorted in non-decreasing order.</li>
 * </ul>
 *
 * @tags Linked List, Recursion
 */
public class MergeTwoSortedLists {

    // Tier 1: Mid-Level — recursive; clean to read but O(M+N) call-stack space
    static class Intermediate {
        // Time Complexity: O(M + N)
        // Space Complexity: O(M + N) — recursive call stack depth
        public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
            if (list1 == null) return list2;
            if (list2 == null) return list1;
            if (list1.val <= list2.val) {
                list1.next = mergeTwoLists(list1.next, list2);
                return list1;
            } else {
                list2.next = mergeTwoLists(list1, list2.next);
                return list2;
            }
        }
    }

    // Tier 2: Senior — iterative with dummy head; splices existing nodes, O(1) extra space
    static class Optimized {
        // Time Complexity: O(M + N)
        // Space Complexity: O(1)
        public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
            ListNode dummy = new ListNode(0);
            ListNode curr = dummy;
            while (list1 != null && list2 != null) {
                if (list1.val <= list2.val) {
                    curr.next = list1;
                    list1 = list1.next;
                } else {
                    curr.next = list2;
                    list2 = list2.next;
                }
                curr = curr.next;
            }
            curr.next = (list1 != null) ? list1 : list2;
            return dummy.next;
        }
    }

    // Tier 3: Staff — same O(M+N) / O(1) as Optimized but worth noting:
    // The dummy-node pattern eliminates a conditional branch on first-node selection,
    // which reduces branch mispredictions in tight loops. For bulk merges (e.g., external
    // sort on disk), this extends to a K-way merge with a min-heap (see MergeKSortedLists).

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized:");
        ListNode l1 = createList(new int[]{1, 2, 4});
        ListNode l2 = createList(new int[]{1, 3, 4});
        ListNode result = new Optimized().mergeTwoLists(l1, l2);
        printList(result); // Expected: [1, 1, 2, 3, 4, 4]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate:");
        ListNode l1 = createList(new int[]{1, 2, 4});
        ListNode l2 = createList(new int[]{1, 3, 4});
        ListNode result = new Intermediate().mergeTwoLists(l1, l2);
        printList(result); // Expected: [1, 1, 2, 3, 4, 4]
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
