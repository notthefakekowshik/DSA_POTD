package com.dsapotd.linkedlists;

/**
 * <h1>234. Palindrome Linked List</h1>
 *
 * <p>Given the <code>head</code> of a singly linked list, return <code>true</code> if it is a palindrome or <code>false</code> otherwise.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [1,2,2,1]
 * Output: true
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: head = [1,2]
 * Output: false
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of nodes in the list is in the range [1, 10^5].</li>
 *   <li>0 <= Node.val <= 9</li>
 * </ul>
 *
 * <p><b>Follow up:</b> Could you do it in O(n) time and O(1) space?</p>
 *
 * @tags Linked List, Two Pointers, Stack, Recursion
 */
public class PalindromeLinkedList {

    // Tier 2: Senior (In-place Reversal)
    // Time Complexity: O(N) where N is the number of nodes
    // Space Complexity: O(1)
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;

        // 1. Find middle (slow will be at start of second half or center)
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // 2. Reverse second half
        ListNode secondHalfHead = reverse(slow);
        ListNode firstHalfHead = head;

        // 3. Compare halves
        ListNode p1 = firstHalfHead, p2 = secondHalfHead;
        boolean result = true;
        while (p2 != null) {
            if (p1.val != p2.val) {
                result = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }

        // 4. Restore list (Senior practice: don't leave side effects)
        reverse(secondHalfHead);

        return result;
    }

    private ListNode reverse(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    /**
     * Tier 1: Mid-Level (Stack Approach)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    static class Intermediate {
        public boolean isPalindrome(ListNode head) {
            java.util.Stack<Integer> stack = new java.util.Stack<>();
            ListNode curr = head;
            while (curr != null) {
                stack.push(curr.val);
                curr = curr.next;
            }
            curr = head;
            while (curr != null) {
                if (curr.val != stack.pop()) return false;
                curr = curr.next;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (In-place):");
        ListNode head1 = createList(new int[]{1, 2, 2, 1});
        System.out.println("1,2,2,1: " + new PalindromeLinkedList().isPalindrome(head1));
        ListNode head2 = createList(new int[]{1, 2});
        System.out.println("1,2: " + new PalindromeLinkedList().isPalindrome(head2));
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Stack):");
        ListNode head1 = createList(new int[]{1, 2, 2, 1});
        System.out.println("1,2,2,1: " + new Intermediate().isPalindrome(head1));
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
}
