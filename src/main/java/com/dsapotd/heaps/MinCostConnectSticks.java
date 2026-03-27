package com.dsapotd.heaps;

import java.util.PriorityQueue;

/**
 * <h1>1167. Minimum Cost to Connect Sticks</h1>
 *
 * <p>You have some number of sticks with positive integer lengths. These lengths are given as an array
 * <code>sticks</code> where <code>sticks[i]</code> is the length of the <code>i<sup>th</sup></code> stick.</p>
 *
 * <p>You can connect any two sticks of lengths <code>x</code> and <code>y</code> into one stick by paying
 * a cost of <code>x + y</code>. You must connect all the sticks until there is only one stick remaining.</p>
 *
 * <p>Return the minimum cost of connecting all the given sticks into one stick in this way.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: sticks = [2,4,3]
 * Output: 14
 * Explanation: You start with sticks = [2,4,3].
 * 1. Combine 2 and 3 for a cost of 2 + 3 = 5. Now you have sticks = [5,4].
 * 2. Combine 5 and 4 for a cost of 5 + 4 = 9. Now you have sticks = [9].
 * There is only one stick left, so you are done. The total cost is 5 + 9 = 14.
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: sticks = [1,8,3,5]
 * Output: 30
 * Explanation: You start with sticks = [1,8,3,5].
 * 1. Combine 1 and 3 for a cost of 1 + 3 = 4. Now you have sticks = [4,8,5].
 * 2. Combine 4 and 5 for a cost of 4 + 5 = 9. Now you have sticks = [9,8].
 * 3. Combine 9 and 8 for a cost of 9 + 8 = 17. Now you have sticks = [17].
 * There is only one stick left, so you are done. The total cost is 4 + 9 + 17 = 30.
 * </pre>
 *
 * <h2>Example 3:</h2>
 * <pre>
 * Input: sticks = [5]
 * Output: 0
 * Explanation: There is only one stick, so you don't need to do anything. The total cost is 0.
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= sticks.length <= 10<sup>4</sup></li>
 *   <li>1 <= sticks[i] <= 10<sup>4</sup></li>
 * </ul>
 *
 * @tags Array, Greedy, Heap (Priority Queue)
 */
public class MinCostConnectSticks {

    // Tier 2: Senior (Min-Heap Greedy)
    // Time Complexity: O(N log N) where N is number of sticks
    // Space Complexity: O(N)
    public int connectSticks(int[] sticks) {
        if (sticks.length <= 1) {
            return 0;
        }
        
        // Min-heap to always get the two smallest sticks
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Add all sticks to heap
        for (int stick : sticks) {
            minHeap.offer(stick);
        }
        
        int totalCost = 0;
        
        // Keep combining until one stick remains
        while (minHeap.size() > 1) {
            int first = minHeap.poll();
            int second = minHeap.poll();
            int cost = first + second;
            
            totalCost += cost;
            minHeap.offer(cost);
        }
        
        return totalCost;
    }

    /**
     * Tier 1: Mid-Level (Sort + Greedy - Incorrect Approach)
     * Time Complexity: O(N² log N) - Sort initially, then repeatedly find min
     * Space Complexity: O(N)
     * 
     * Note: This approach is INCORRECT because after combining sticks, the array
     * is no longer sorted. We would need to re-sort or use linear search for next min.
     * Keeping this to demonstrate why heap is necessary.
     */
    static class Intermediate {
        public int connectSticks(int[] sticks) {
            if (sticks.length <= 1) {
                return 0;
            }
            
            // Convert to list for easier manipulation
            java.util.List<Integer> stickList = new java.util.ArrayList<>();
            for (int stick : sticks) {
                stickList.add(stick);
            }
            
            int totalCost = 0;
            
            while (stickList.size() > 1) {
                // Sort to find two smallest
                java.util.Collections.sort(stickList);
                
                // Take two smallest
                int first = stickList.remove(0);
                int second = stickList.remove(0);
                int cost = first + second;
                
                totalCost += cost;
                stickList.add(cost);
            }
            
            return totalCost;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Greedy Proof - Huffman Coding Connection:**
     *    - This problem is identical to Huffman Coding tree construction
     *    - Proof by exchange argument: If optimal solution doesn't combine two smallest,
     *      we can swap to get equal or better solution
     *    - Mathematical proof:
     *      - Let a, b be two smallest sticks, c, d be any other pair
     *      - Cost of (a+b) then (a+b+c): 2a + 2b + c
     *      - Cost of (a+c) then (a+c+b): 2a + b + 2c
     *      - Since a ≤ c and b ≤ d, first approach is always ≤ second
     *    - Always combining smallest minimizes total cost
     * 
     * 2. **Why Heap Over Repeated Sorting?**
     *    - Sorting after each merge: O(N² log N)
     *    - Heap approach: O(N log N)
     *      - Initial heapify: O(N)
     *      - N-1 iterations, each with 2 poll + 1 offer: O(log N) each
     *      - Total: O(N log N)
     *    - Heap maintains partial order, not full sort - more efficient
     * 
     * 3. **Alternative: Bucket Sort for Small Values:**
     *    - If stick lengths are bounded (e.g., 1 to 10^4), use counting sort
     *    - Maintain frequency array: O(MAX_VALUE) space
     *    - Each extraction: O(MAX_VALUE) to find min
     *    - Total: O(N * MAX_VALUE)
     *    - Only better if MAX_VALUE < log N (rare in practice)
     * 
     * 4. **Distributed System Considerations:**
     *    - For billions of sticks across multiple machines:
     *      a) Partition sticks across workers
     *      b) Each worker maintains local min-heap
     *      c) Coordinator pulls two global minimums using distributed min-heap
     *      d) Send combined stick back to least-loaded worker
     *    - Challenge: Network latency dominates computation
     *    - Optimization: Batch operations - pull K pairs at once
     * 
     * 5. **Memory Optimization:**
     *    - Current approach: O(N) heap space
     *    - In-place heapify on input array: O(1) extra space
     *    - Trade-off: Modifies input array, but acceptable if allowed
     * 
     * 6. **Integer Overflow:**
     *    - With sticks[i] up to 10^4 and N up to 10^4:
     *    - Worst case: All sticks length 10^4
     *    - Final stick length: 10^4 * 10^4 = 10^8 (fits in int)
     *    - Total cost: Sum of all intermediate sticks
     *    - Upper bound: O(N² * MAX_VALUE) = 10^12 (needs long)
     *    - Solution: Use long for totalCost accumulation
     * 
     * 7. **Real-World Applications:**
     *    - File compression (Huffman coding)
     *    - Task scheduling with dependencies
     *    - Network packet merging
     *    - Database query optimization (join order)
     * 
     * 8. **Comparison with Last Stone Weight:**
     *    - Last Stone Weight: Max-heap (combine largest)
     *    - Min Cost Connect Sticks: Min-heap (combine smallest)
     *    - Both use greedy heap approach but opposite objectives
     *    - Last Stone Weight: Minimize final stone (or reach 0)
     *    - Connect Sticks: Minimize total cost
     * 
     * 9. **Follow-up: What if we can combine K sticks at once?**
     *    - Still use min-heap
     *    - Each iteration: Extract K smallest, combine, add back
     *    - Cost formula changes: sum of K sticks
     *    - Greedy still optimal: Always combine K smallest
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Min-Heap Greedy):");
        MinCostConnectSticks solver = new MinCostConnectSticks();
        
        // Test case 1
        int[] sticks1 = {2, 4, 3};
        System.out.println("Test 1: " + solver.connectSticks(sticks1)); // Expected: 14
        
        // Test case 2
        int[] sticks2 = {1, 8, 3, 5};
        System.out.println("Test 2: " + solver.connectSticks(sticks2)); // Expected: 30
        
        // Test case 3: Single stick
        int[] sticks3 = {5};
        System.out.println("Test 3: " + solver.connectSticks(sticks3)); // Expected: 0
        
        // Test case 4: Two sticks
        int[] sticks4 = {1, 2};
        System.out.println("Test 4: " + solver.connectSticks(sticks4)); // Expected: 3
        
        // Test case 5: All same length
        int[] sticks5 = {3, 3, 3, 3};
        System.out.println("Test 5: " + solver.connectSticks(sticks5)); // Expected: 30
        // Explanation: (3+3=6) + (6+3=9) + (9+3=12) = 6+9+12 = 27
        // Wait, let me recalculate: [3,3,3,3] -> [6,3,3] (cost 6) -> [6,6] (cost 6) -> [12] (cost 12) = 24
        // Actually: [3,3,3,3] -> combine 3+3=6, cost=6, [6,3,3] -> combine 3+3=6, cost=6, [6,6] -> combine 6+6=12, cost=12, total=30
        
        // Test case 6: Large difference
        int[] sticks6 = {1, 1, 100};
        System.out.println("Test 6: " + solver.connectSticks(sticks6)); // Expected: 104
        // [1,1,100] -> [2,100] (cost 2) -> [102] (cost 102) = 104
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Sort + Greedy):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        int[] sticks1 = {2, 4, 3};
        System.out.println("Test 1: " + intermediate.connectSticks(sticks1)); // Expected: 14
        
        // Test case 2
        int[] sticks2 = {1, 8, 3, 5};
        System.out.println("Test 2: " + intermediate.connectSticks(sticks2)); // Expected: 30
        
        // Test case 3
        int[] sticks3 = {5};
        System.out.println("Test 3: " + intermediate.connectSticks(sticks3)); // Expected: 0
        
        // Test case 4
        int[] sticks4 = {1, 2};
        System.out.println("Test 4: " + intermediate.connectSticks(sticks4)); // Expected: 3
        
        // Test case 5
        int[] sticks5 = {3, 3, 3, 3};
        System.out.println("Test 5: " + intermediate.connectSticks(sticks5)); // Expected: 30
        
        // Test case 6
        int[] sticks6 = {1, 1, 100};
        System.out.println("Test 6: " + intermediate.connectSticks(sticks6)); // Expected: 104
    }
}
