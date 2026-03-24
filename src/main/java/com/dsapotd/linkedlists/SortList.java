package com.dsapotd.linkedlists;

/**
 * <h2>148. Sort List</h2>
 * <p>Given the head of a linked list, return the list after sorting it in ascending order.</p>
 * <p>Can you sort the linked list in O(n logn) time and O(1) memory (i.e. constant space)?</p>
 *
 * <p><b>Constraints:</b></p>
 * <ul>
 *     <li>The number of nodes in the list is in the range [0, 5 * 10^4].</li>
 *     <li>-10^5 <= Node.val <= 10^5</li>
 * </ul>
 *
 * @tags Linked List, Divide and Conquer, Merge Sort, Sorting
 */
public class SortList {

    /**
     * TIER 1: Mid-Level (Top-Down Merge Sort)
     * Focus: Recursive Divide and Conquer.
     */
    public static class Intermediate {
        // Time Complexity: O(N log N)
        // Space Complexity: O(log N) for recursion stack
        public static ListNode sortList(ListNode head) {
            if (head == null || head.next == null) return head;

            // Split
            ListNode mid = getMidAndSplit(head);
            ListNode left = sortList(head);
            ListNode right = sortList(mid);

            // Merge
            return merge(left, right);
        }

        private static ListNode getMidAndSplit(ListNode head) {
            ListNode slow = head, fast = head, prev = null;
            while (fast != null && fast.next != null) {
                prev = slow;
                slow = slow.next;
                fast = fast.next.next;
            }
            prev.next = null; // Sever the connection
            return slow;
        }

        private static ListNode merge(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0);
            ListNode curr = dummy;
            while (l1 != null && l2 != null) {
                if (l1.val < l2.val) {
                    curr.next = l1;
                    l1 = l1.next;
                } else {
                    curr.next = l2;
                    l2 = l2.next;
                }
                curr = curr.next;
            }
            curr.next = (l1 != null) ? l1 : l2;
            return dummy.next;
        }
    }

    /**
     * TIER 2: Senior (Bottom-Up Merge Sort)
     * Focus: True O(1) space complexity by avoiding recursion.
     * Logic: Iterate with step sizes of 1, 2, 4, 8... merging sub-lists iteratively.
     */
    public static class Optimized {
        // Time Complexity: O(N log N)
        // Space Complexity: O(1)
        public static ListNode sortList(ListNode head) {
            if (head == null || head.next == null) return head;

            int length = getLength(head);
            ListNode dummy = new ListNode(0, head);

            for (int step = 1; step < length; step *= 2) {
                ListNode curr = dummy.next;
                ListNode tail = dummy;

                while (curr != null) {
                    ListNode left = curr;
                    ListNode right = split(left, step);
                    curr = split(right, step); // Next iteration starting point

                    tail.next = merge(left, right);
                    while (tail.next != null) {
                        tail = tail.next;
                    }
                }
            }
            return dummy.next;
        }

        private static int getLength(ListNode head) {
            int len = 0;
            while (head != null) {
                len++;
                head = head.next;
            }
            return len;
        }

        // Splits list after 'n' nodes, returns head of second part
        private static ListNode split(ListNode head, int n) {
            for (int i = 1; head != null && i < n; i++) {
                head = head.next;
            }
            if (head == null) return null;
            ListNode second = head.next;
            head.next = null; // Break connection
            return second;
        }

        private static ListNode merge(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0);
            ListNode curr = dummy;
            while (l1 != null && l2 != null) {
                if (l1.val < l2.val) {
                    curr.next = l1;
                    l1 = l1.next;
                } else {
                    curr.next = l2;
                    l2 = l2.next;
                }
                curr = curr.next;
            }
            curr.next = (l1 != null) ? l1 : l2;
            return dummy.next;
        }
    }

    /**
     * TIER 3: Staff (Stability, JVM, and Alternatives)
     * Discussion:
     * 1. Stability: Merge Sort is stable, which is crucial for sorting records by multiple criteria.
     * 2. Why Merge Sort for Lists? Unlike Arrays (where QuickSort is often preferred), Merge Sort
     *    doesn't require random access.
     * 3. Parallelism: On a multi-core CPU, the Merge step can be parallelized (ForkJoin framework),
     *    though this is harder for linked lists than arrays due to sequential access.
     */
    public static void staffDiscussion() {
        System.out.println("Staff Insight: Bottom-up merge sort is the gold standard for O(1) space sorting on lists.");
        System.out.println("In the real world, you might just dump the list into an array, Timsort it, and rebuild.");
    }

    public static void main(String[] args) {
        // Setup: 4 -> 2 -> 1 -> 3
        ListNode head = new ListNode(4, new ListNode(2, new ListNode(1, new ListNode(3))));

        System.out.println("--- Sort List ---");

        // TOGGLE VERSION HERE
        ListNode sorted = Optimized.sortList(head);
        // ListNode sorted = Intermediate.sortList(head);

        System.out.print("Sorted List: ");
        while (sorted != null) {
            System.out.print(sorted.val + (sorted.next != null ? " -> " : ""));
            sorted = sorted.next;
        }
        System.out.println();
    }
}
