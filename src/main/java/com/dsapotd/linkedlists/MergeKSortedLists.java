package com.dsapotd.linkedlists;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * <h1>23. Merge k Sorted Lists</h1>
 *
 * <p>You are given an array of <code>k</code> linked-lists <code>lists</code>, each linked-list is sorted in ascending order.</p>
 *
 * <p>Merge all the linked-lists into one sorted linked-list and return it.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: lists = [[1,4,5],[1,3,4],[2,6]]
 * Output: [1,1,2,3,4,4,5,6]
 * Explanation: The linked-lists are:
 * [
 *   1->4->5,
 *   1->3->4,
 *   2->6
 * ]
 * merging them into one sorted list:
 * 1->1->2->3->4->4->5->6
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: lists = []
 * Output: []
 * </pre>
 *
 * <h2>Example 3:</h2>
 * <pre>
 * Input: lists = [[]]
 * Output: []
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>k == lists.length</li>
 *   <li>0 <= k <= 10^4</li>
 *   <li>0 <= lists[i].length <= 500</li>
 *   <li>-10^4 <= lists[i][j] <= 10^4</li>
 *   <li>lists[i] is sorted in ascending order.</li>
 *   <li>The sum of lists[i].length will not exceed 10^4.</li>
 * </ul>
 *
 * @tags Linked List, Divide and Conquer, Heap (Priority Queue), Merge Sort
 */
public class MergeKSortedLists {

    // Optimized Solution (Divide and Conquer)
    // Time Complexity: O(N log K) where N is the total number of nodes and K is the number of linked lists
    // Space Complexity: O(log K) recursion stack
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeKLists(lists, 0, lists.length - 1);
    }

    private ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) return lists[start];
        int mid = start + (end - start) / 2;
        ListNode left = mergeKLists(lists, start, mid);
        ListNode right = mergeKLists(lists, mid + 1, end);
        return mergeTwoLists(left, right);
    }

    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
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

    /**
     * Tier 1: Mid-Level (Min-Heap)
     * Time Complexity: O(N log K)
     * Space Complexity: O(K) for the heap
     */
    static class Intermediate {
        public ListNode mergeKLists(ListNode[] lists) {
            if (lists == null || lists.length == 0) return null;
            
            PriorityQueue<ListNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
            for (ListNode node : lists) {
                if (node != null) {
                    minHeap.offer(node);
                }
            }
            
            ListNode dummy = new ListNode(0);
            ListNode curr = dummy;
            while (!minHeap.isEmpty()) {
                ListNode smallest = minHeap.poll();
                curr.next = smallest;
                curr = curr.next;
                if (smallest.next != null) {
                    minHeap.offer(smallest.next);
                }
            }
            return dummy.next;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Divide and Conquer):");
        ListNode[] lists = {
            createList(new int[]{1, 4, 5}),
            createList(new int[]{1, 3, 4}),
            createList(new int[]{2, 6})
        };
        ListNode result = new MergeKSortedLists().mergeKLists(lists);
        printList(result); // Expected: [1, 1, 2, 3, 4, 4, 5, 6]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Min-Heap):");
        ListNode[] lists = {
            createList(new int[]{1, 4, 5}),
            createList(new int[]{1, 3, 4}),
            createList(new int[]{2, 6})
        };
        ListNode result = new Intermediate().mergeKLists(lists);
        printList(result); // Expected: [1, 1, 2, 3, 4, 4, 5, 6]
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
