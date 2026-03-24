package com.dsapotd.linkedlists;

import java.util.HashSet;
import java.util.Set;

/**
 * <h2>160. Intersection of Two Linked Lists</h2>
 * <p>Given the heads of two singly linked-lists headA and headB, return the node at which the two lists intersect.
 * If the two linked lists have no intersection at all, return null.</p>
 *
 * <p>For example, the following two linked lists begin to intersect at node c1:</p>
 * <pre>
 * A:          a1 → a2
 *                    ↘
 *                      c1 → c2 → c3
 *                    ↗
 * B:     b1 → b2 → b3
 * </pre>
 *
 * <p><b>Constraints:</b></p>
 * <ul>
 *     <li>The number of nodes in listA is in the range [0, 10^4].</li>
 *     <li>The number of nodes in listB is in the range [0, 10^4].</li>
 *     <li>1 <= Node.val <= 10^5</li>
 *     <li>listA and listB do not have any cycles.</li>
 * </ul>
 *
 * @tags Linked List, Two Pointers, Hash Table
 */
public class IntersectionOfTwoLinkedLists {

    /**
     * TIER 1: Mid-Level (Intuitive HashSet Approach)
     * Focus: "Just make it work" using O(N) space.
     */
    public static class Intermediate {
        // Time Complexity: O(M + N)
        // Space Complexity: O(M) or O(N)
        public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            Set<ListNode> visited = new HashSet<>();
            ListNode curr = headA;
            while (curr != null) {
                visited.add(curr);
                curr = curr.next;
            }

            curr = headB;
            while (curr != null) {
                if (visited.contains(curr)) {
                    return curr;
                }
                curr = curr.next;
            }
            return null;
        }
    }

    /**
     * TIER 2: Senior (Optimized Two Pointers)
     * Focus: O(1) space using the "alignment" trick.
     * Logic: After one pointer reaches the end of its list, redirect it to the head of the OTHER list.
     * They will meet at the intersection point after at most two passes.
     */
    public static class Optimized {
        // Time Complexity: O(M + N)
        // Space Complexity: O(1)
        public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            if (headA == null || headB == null) return null;

            ListNode pA = headA;
            ListNode pB = headB;

            // They will either meet at the intersection node or both will be null (at the end)
            while (pA != pB) {
                pA = (pA == null) ? headB : pA.next;
                pB = (pB == null) ? headA : pB.next;
            }

            return pA;
        }
    }

    /**
     * TIER 3: Staff (Deep Internals & Scalability)
     * Focus: Memory Layout and Object Identity.
     * Discussion:
     * 1. In JVM, object references (pointers) are what we compare. The "identity" is based on memory location.
     * 2. If these lists were persisted in a database, we'd compare Primary Keys, not memory addresses.
     * 3. For massive lists (millions of nodes) that don't fit in memory, we could use Bloom Filters or
     *    external sort-merge techniques to find the intersection ID without loading both full lists.
     */
    public static void staffDiscussion() {
        System.out.println("Staff Insight: The Two-Pointer approach is mathematically elegant (L1+c+L2 = L2+c+L1).");
        System.out.println("In high-concurrency Java, if these lists were volatile, we'd need memory barriers.");
    }

    public static void main(String[] args) {
        // Setup: Intersection at node 8
        // A: 4 -> 1 -> 8 -> 4 -> 5
        // B: 5 -> 6 -> 1 -> 8 -> 4 -> 5
        ListNode intersection = new ListNode(8, new ListNode(4, new ListNode(5)));
        ListNode headA = new ListNode(4, new ListNode(1, intersection));
        ListNode headB = new ListNode(5, new ListNode(6, new ListNode(1, intersection)));

        System.out.println("--- Intersection of Two Linked Lists ---");

        // TOGGLE VERSION HERE
        ListNode result = Optimized.getIntersectionNode(headA, headB);
        // ListNode result = Intermediate.getIntersectionNode(headA, headB);

        if (result != null) {
            System.out.println("Intersection found at value: " + result.val);
        } else {
            System.out.println("No intersection found.");
        }
    }
}
