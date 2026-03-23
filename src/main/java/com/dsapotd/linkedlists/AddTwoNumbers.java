package com.dsapotd.linkedlists;

/**
 * <h1>2. Add Two Numbers</h1>
 *
 * <p>
 * You are given two non-empty linked lists representing two non-negative
 * integers. The digits are stored in reverse order, and each of their nodes
 * contains a single digit. Add the two numbers and return the sum as a
 * linked list.
 * </p>
 *
 * <p>
 * You may assume the two numbers do not have leading zeros, except the
 * number 0 itself.
 * </p>
 *
 * <h2>Example 1:</h2>
 *
 * <pre>
 * Input:  l1 = [2, 4, 3], l2 = [5, 6, 4]
 * Output: [7, 0, 8]
 * Explanation: 342 + 465 = 807
 * </pre>
 *
 * <h2>Example 2:</h2>
 *
 * <pre>
 * Input:  l1 = [0], l2 = [0]
 * Output: [0]
 * </pre>
 *
 * <h2>Example 3:</h2>
 *
 * <pre>
 * Input:  l1 = [9, 9, 9, 9, 9, 9, 9], l2 = [9, 9, 9, 9]
 * Output: [8, 9, 9, 9, 0, 0, 0, 1]
 * Explanation: 9999999 + 9999 = 10009998
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 * <li>The number of nodes in each linked list is in the range [1, 100].</li>
 * <li>0 <= Node.val <= 9</li>
 * <li>It is guaranteed that the list represents a number that does not have leading zeros.</li>
 * </ul>
 *
 * @tags Linked List, Math, Recursion
 */
public class AddTwoNumbers {

    // Tier 1: Mid-Level — convert each list to a BigInteger, add, then build result list.
    // Handles arbitrary precision but involves string/object allocation overhead.
    static class Intermediate {
        // Time Complexity: O(M + N)
        // Space Complexity: O(max(M, N)) — for BigInteger internal storage
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            java.math.BigInteger num1 = toNumber(l1);
            java.math.BigInteger num2 = toNumber(l2);
            java.math.BigInteger sum = num1.add(num2);

            if (sum.equals(java.math.BigInteger.ZERO)) return new ListNode(0);

            ListNode dummy = new ListNode(0);
            ListNode curr = dummy;
            while (sum.compareTo(java.math.BigInteger.ZERO) > 0) {
                curr.next = new ListNode(sum.mod(java.math.BigInteger.TEN).intValue());
                sum = sum.divide(java.math.BigInteger.TEN);
                curr = curr.next;
            }
            return dummy.next;
        }

        private java.math.BigInteger toNumber(ListNode node) {
            java.math.BigInteger result = java.math.BigInteger.ZERO;
            java.math.BigInteger place = java.math.BigInteger.ONE;
            while (node != null) {
                result = result.add(place.multiply(java.math.BigInteger.valueOf(node.val)));
                place = place.multiply(java.math.BigInteger.TEN);
                node = node.next;
            }
            return result;
        }
    }

    // Tier 2: Senior — simulate grade-school addition digit-by-digit with an explicit carry.
    // Single pass, no extra objects; the carry condition `|| carry != 0` handles final overflow.
    static class Optimized {
        // Time Complexity: O(max(M, N))
        // Space Complexity: O(max(M, N)) — output list length
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode dummy = new ListNode(0);
            ListNode curr = dummy;
            int carry = 0;
            while (l1 != null || l2 != null || carry != 0) {
                int sum = carry;
                if (l1 != null) { sum += l1.val; l1 = l1.next; }
                if (l2 != null) { sum += l2.val; l2 = l2.next; }
                carry = sum / 10;
                curr.next = new ListNode(sum % 10);
                curr = curr.next;
            }
            return dummy.next;
        }
    }

    // Tier 3: Staff — the iterative approach is already optimal for single-machine use.
    // At scale (distributed systems): if numbers are stored across shards, you need a
    // right-to-left traversal which is non-trivial for singly linked lists without reversal.
    // One approach: reverse both lists first (O(N)), add, reverse result — still O(N) total.
    // Alternatively store doubly linked or use a stack for right-to-left processing.
    // For "Add Two Numbers II" (digits stored forward, not reversed) this exact trade-off applies.

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized:");
        ListNode l1 = createList(new int[]{2, 4, 3});
        ListNode l2 = createList(new int[]{5, 6, 4});
        ListNode result = new Optimized().addTwoNumbers(l1, l2);
        printList(result); // Expected: [7, 0, 8]

        System.out.println("Edge case (carry propagation):");
        l1 = createList(new int[]{9, 9, 9, 9, 9, 9, 9});
        l2 = createList(new int[]{9, 9, 9, 9});
        result = new Optimized().addTwoNumbers(l1, l2);
        printList(result); // Expected: [8, 9, 9, 9, 0, 0, 0, 1]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate:");
        ListNode l1 = createList(new int[]{2, 4, 3});
        ListNode l2 = createList(new int[]{5, 6, 4});
        ListNode result = new Intermediate().addTwoNumbers(l1, l2);
        printList(result); // Expected: [7, 0, 8]
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
