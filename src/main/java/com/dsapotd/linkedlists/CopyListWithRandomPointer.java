package com.dsapotd.linkedlists;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>138. Copy List with Random Pointer</h2>
 * <p>A linked list of length n is given such that each node contains an additional random pointer,
 * which could point to any node in the list, or null.</p>
 * <p>Construct a deep copy of the list. The deep copy should consist of exactly n brand new nodes,
 * where each new node has its value set to the value of its corresponding original node.</p>
 *
 * <p><b>Constraints:</b></p>
 * <ul>
 *     <li>0 <= n <= 1000</li>
 *     <li>-10^4 <= Node.val <= 10^4</li>
 *     <li>Node.random is null or is pointing to some node in the linked list.</li>
 * </ul>
 *
 * @tags Linked List, Hash Table, Design
 */
public class CopyListWithRandomPointer {

    public static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    /**
     * TIER 1: Mid-Level (HashMap Approach)
     * Focus: Standard 2-pass strategy with O(N) space.
     */
    public static class Intermediate {
        // Time Complexity: O(N)
        // Space Complexity: O(N)
        public static Node copyRandomList(Node head) {
            if (head == null) return null;

            Map<Node, Node> oldToNew = new HashMap<>();

            // Pass 1: Create all nodes
            Node curr = head;
            while (curr != null) {
                oldToNew.put(curr, new Node(curr.val));
                curr = curr.next;
            }

            // Pass 2: Connect next and random pointers
            curr = head;
            while (curr != null) {
                Node copy = oldToNew.get(curr);
                copy.next = oldToNew.get(curr.next);
                copy.random = oldToNew.get(curr.random);
                curr = curr.next;
            }

            return oldToNew.get(head);
        }
    }

    /**
     * TIER 2: Senior (In-place Interweaving)
     * Focus: O(1) extra space (excluding the result list).
     * Logic:
     * 1. Create interleaved nodes: A -> A' -> B -> B' -> C -> C'
     * 2. Set random pointers for A', B', C' based on A, B, C.
     * 3. Separate the two lists.
     */
    public static class Optimized {
        // Time Complexity: O(N)
        // Space Complexity: O(1)
        public static Node copyRandomList(Node head) {
            if (head == null) return null;

            // Step 1: Interleave
            Node curr = head;
            while (curr != null) {
                Node next = curr.next;
                Node copy = new Node(curr.val);
                curr.next = copy;
                copy.next = next;
                curr = next;
            }

            // Step 2: Assign Randoms
            curr = head;
            while (curr != null) {
                if (curr.random != null) {
                    curr.next.random = curr.random.next;
                }
                curr = curr.next.next;
            }

            // Step 3: Extract the copy
            curr = head;
            Node dummy = new Node(0);
            Node copyCurr = dummy;
            while (curr != null) {
                Node next = curr.next.next;

                // Extract copy
                copyCurr.next = curr.next;
                copyCurr = copyCurr.next;

                // Restore original
                curr.next = next;
                curr = next;
            }

            return dummy.next;
        }
    }

    /**
     * TIER 3: Staff (Memory Management & Graph Theory)
     * Discussion:
     * 1. IdentityHashMap vs HashMap: In the intermediate tier, we use Node as a key.
     *    In Java, Node objects don't override hashCode/equals, so it defaults to identity.
     *    IdentityHashMap would be more explicit and slightly faster.
     * 2. Serialization: This problem is essentially a graph serialization problem.
     *    In a distributed system, you'd use a UUID or Address as a key to map nodes across the network.
     */
    public static void staffDiscussion() {
        System.out.println("Staff Insight: The in-place interweaving avoids the overhead of a HashMap.");
        System.out.println("This is critical for low-memory environments (embedded) or massive graphs.");
    }

    public static void main(String[] args) {
        // Setup: [[7,null],[13,0],[11,4],[10,2],[1,0]]
        Node n1 = new Node(7);
        Node n2 = new Node(13);
        Node n3 = new Node(11);
        Node n4 = new Node(10);
        Node n5 = new Node(1);

        n1.next = n2; n2.next = n3; n3.next = n4; n4.next = n5;
        n2.random = n1; n3.random = n5; n4.random = n3; n5.random = n1;

        System.out.println("--- Copy List with Random Pointer ---");

        // TOGGLE VERSION HERE
        Node copy = Optimized.copyRandomList(n1);
        // Node copy = Intermediate.copyRandomList(n1);

        System.out.println("Original head val: " + n1.val + ", Copy head val: " + copy.val);
        System.out.println("Copy n2 random points to n1? " + (copy.next.random == copy));
    }
}
