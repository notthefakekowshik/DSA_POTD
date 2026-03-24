package com.dsapotd.linkedlists.cycle;

import com.dsapotd.linkedlists.ListNode;

/**
 * <h1>142. Linked List Cycle II</h1>
 *
 * <p>Given the <code>head</code> of a linked list, return the node where the cycle begins. If there is no cycle, return <code>null</code>.</p>
 *
 * <p>There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the <code>next</code> pointer. Internally, <code>pos</code> is used to denote the index of the node that tail's <code>next</code> pointer is connected to (0-indexed). It is -1 if there is no cycle. <b>Note that <code>pos</code> is not passed as a parameter.</b></p>
 *
 * <p><b>Do not modify</b> the linked list.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: head = [3,2,0,-4], pos = 1
 * Output: tail connects to node index 1
 * Explanation: There is a cycle in the linked list, where tail connects to the second node.
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>The number of the nodes in the list is in the range [0, 10^4].</li>
 *   <li>-10^5 <= Node.val <= 10^5</li>
 *   <li>pos is -1 or a valid index in the linked-list.</li>
 * </ul>
 *
 * <p><b>Follow up:</b> Can you solve it using O(1) (i.e. constant) memory?</p>
 *
 * @tags Linked List, Two Pointers, Math
 */
public class LinkedListCycleII {

    // Tier 2: Senior (Tortoise and Hare Math Proof)
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    // Mathematical Proof: Distance(Fast) = 2 * Distance(Slow).
    // 2(L1 + L2) = L1 + L2 + n*C => L1 + L2 = n*C => L1 = n*C - L2.
    // L1 is the distance to cycle start. C - L2 is the distance from meeting point to cycle start.
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) return null;

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                // Cycle detected. Find the start node.
                ListNode start = head;
                while (start != slow) {
                    start = start.next;
                    slow = slow.next;
                }
                return start;
            }
        }

        return null;
    }

    /**
     * Tier 1: Mid-Level (HashSet Approach)
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     */
    static class Intermediate {
        public ListNode detectCycle(ListNode head) {
            java.util.HashSet<ListNode> visited = new java.util.HashSet<>();
            ListNode curr = head;
            while (curr != null) {
                if (visited.contains(curr)) {
                    return curr;
                }
                visited.add(curr);
                curr = curr.next;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (O(1) Space):");
        // Create [3, 2, 0, -4] with cycle at index 1 (node with val 2)
        ListNode head = new ListNode(3);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(0);
        ListNode node3 = new ListNode(-4);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node1; // cycle

        ListNode result = new LinkedListCycleII().detectCycle(head);
        System.out.println("Cycle starts at node with value: " + (result != null ? result.val : "null"));
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (HashSet):");
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        head.next = node1;
        node1.next = head; // cycle at index 0
        
        ListNode result = new Intermediate().detectCycle(head);
        System.out.println("Cycle starts at node with value: " + (result != null ? result.val : "null"));
    }
}
