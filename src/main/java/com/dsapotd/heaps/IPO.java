package com.dsapotd.heaps;

import java.util.PriorityQueue;

/**
 * <h1>502. IPO</h1>
 *
 * <p>Suppose LeetCode will start its IPO soon. In order to sell a good price of its shares to Venture Capital,
 * LeetCode would like to work on some projects to increase its capital before the IPO. Since it has limited resources,
 * it can only finish at most <code>k</code> distinct projects before the IPO. Help LeetCode design the best way to
 * maximize its total capital after finishing at most <code>k</code> distinct projects.</p>
 *
 * <p>You are given <code>n</code> projects where the <code>i<sup>th</sup></code> project has a pure profit
 * <code>profits[i]</code> and a minimum capital of <code>capital[i]</code> is needed to start it.</p>
 *
 * <p>Initially, you have <code>w</code> capital. When you finish a project, you will obtain its pure profit and
 * the profit will be added to your total capital.</p>
 *
 * <p>Pick a list of at most <code>k</code> distinct projects from given projects to maximize your final capital,
 * and return the final maximized capital.</p>
 *
 * <p>The answer is guaranteed to fit in a 32-bit signed integer.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: k = 2, w = 0, profits = [1,2,3], capital = [0,1,1]
 * Output: 4
 * Explanation: Since your initial capital is 0, you can only start the project indexed 0.
 * After finishing it you will obtain profit 1 and your capital becomes 1.
 * With capital 1, you can either start the project indexed 1 or the project indexed 2.
 * Since you can choose at most 2 projects, you need to finish the project indexed 2 to get the maximum capital.
 * Therefore, output the final maximized capital, which is 0 + 1 + 3 = 4.
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: k = 3, w = 0, profits = [1,2,3], capital = [0,1,2]
 * Output: 6
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= k <= 10<sup>5</sup></li>
 *   <li>0 <= w <= 10<sup>9</sup></li>
 *   <li>n == profits.length</li>
 *   <li>n == capital.length</li>
 *   <li>1 <= n <= 10<sup>5</sup></li>
 *   <li>0 <= profits[i] <= 10<sup>4</sup></li>
 *   <li>0 <= capital[i] <= 10<sup>9</sup></li>
 * </ul>
 *
 * @tags Array, Greedy, Sorting, Heap (Priority Queue)
 */
public class IPO {

    // Tier 2: Senior (Two Heaps - Capital Min-Heap + Profit Max-Heap)
    // Time Complexity: O(N log N + K log N) where N is number of projects
    // Space Complexity: O(N)
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        
        // Min-heap sorted by capital requirement (project index stored)
        PriorityQueue<int[]> minCapitalHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Max-heap sorted by profit (for affordable projects)
        PriorityQueue<Integer> maxProfitHeap = new PriorityQueue<>((a, b) -> b - a);
        
        // Add all projects to capital min-heap
        for (int i = 0; i < n; i++) {
            minCapitalHeap.offer(new int[]{capital[i], profits[i]});
        }
        
        // Perform at most k projects
        for (int i = 0; i < k; i++) {
            // Move all affordable projects to profit max-heap
            while (!minCapitalHeap.isEmpty() && minCapitalHeap.peek()[0] <= w) {
                int[] project = minCapitalHeap.poll();
                maxProfitHeap.offer(project[1]);
            }
            
            // If no affordable projects, break
            if (maxProfitHeap.isEmpty()) {
                break;
            }
            
            // Pick the most profitable project
            w += maxProfitHeap.poll();
        }
        
        return w;
    }

    /**
     * Tier 1: Mid-Level (Brute Force - Repeated Linear Scan)
     * Time Complexity: O(K * N) where K is number of projects to complete, N is total projects
     * Space Complexity: O(N) for tracking used projects
     */
    static class Intermediate {
        public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
            int n = profits.length;
            boolean[] used = new boolean[n];
            
            // Perform at most k projects
            for (int i = 0; i < k; i++) {
                int maxProfitIdx = -1;
                int maxProfit = -1;
                
                // Find the most profitable affordable project
                for (int j = 0; j < n; j++) {
                    if (!used[j] && capital[j] <= w && profits[j] > maxProfit) {
                        maxProfit = profits[j];
                        maxProfitIdx = j;
                    }
                }
                
                // If no affordable project found, break
                if (maxProfitIdx == -1) {
                    break;
                }
                
                // Complete the project
                w += maxProfit;
                used[maxProfitIdx] = true;
            }
            
            return w;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Heap Choice Rationale:**
     *    - Min-heap by capital: Allows efficient retrieval of all affordable projects as capital grows
     *    - Max-heap by profit: Greedy selection of highest profit among affordable projects
     *    - This two-heap pattern is optimal for "dynamic eligibility with greedy selection"
     * 
     * 2. **Why Not Sort + Binary Search?**
     *    - Sorting by capital: O(N log N)
     *    - For each of K iterations, binary search for affordable range: O(log N)
     *    - Finding max profit in range: O(N) worst case
     *    - Total: O(N log N + K*N) which is worse than two-heap approach
     * 
     * 3. **Distributed System Considerations:**
     *    - For massive project databases (millions of projects):
     *      a) Partition projects by capital ranges across shards
     *      b) Each shard maintains local min-heap by capital
     *      c) Coordinator queries relevant shards based on current capital
     *      d) Merge results using distributed max-heap
     *    - Trade-off: Network latency vs. memory constraints
     * 
     * 4. **Memory Optimization:**
     *    - Current approach stores full project data in heaps
     *    - For memory-constrained systems: Store indices in heaps, keep projects in array
     *    - Reduces heap memory from O(N * (capital_size + profit_size)) to O(N * index_size)
     * 
     * 5. **Early Termination:**
     *    - If at any point minCapitalHeap is empty and maxProfitHeap is empty, no more projects possible
     *    - Could track "minimum capital needed for next project" to predict impossibility
     * 
     * 6. **Integer Overflow:**
     *    - Problem guarantees answer fits in 32-bit signed integer
     *    - In real systems with larger values, use long for capital accumulation
     *    - Or implement BigInteger for arbitrary precision
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Two Heaps):");
        IPO ipo = new IPO();
        
        // Test case 1
        int k1 = 2, w1 = 0;
        int[] profits1 = {1, 2, 3};
        int[] capital1 = {0, 1, 1};
        System.out.println("Test 1: " + ipo.findMaximizedCapital(k1, w1, profits1, capital1)); // Expected: 4
        
        // Test case 2
        int k2 = 3, w2 = 0;
        int[] profits2 = {1, 2, 3};
        int[] capital2 = {0, 1, 2};
        System.out.println("Test 2: " + ipo.findMaximizedCapital(k2, w2, profits2, capital2)); // Expected: 6
        
        // Test case 3: Can't afford any project initially
        int k3 = 1, w3 = 0;
        int[] profits3 = {1, 2, 3};
        int[] capital3 = {1, 1, 2};
        System.out.println("Test 3: " + ipo.findMaximizedCapital(k3, w3, profits3, capital3)); // Expected: 0
        
        // Test case 4: More k than projects
        int k4 = 10, w4 = 0;
        int[] profits4 = {1, 2, 3};
        int[] capital4 = {0, 1, 1};
        System.out.println("Test 4: " + ipo.findMaximizedCapital(k4, w4, profits4, capital4)); // Expected: 6
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Brute Force):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        int k1 = 2, w1 = 0;
        int[] profits1 = {1, 2, 3};
        int[] capital1 = {0, 1, 1};
        System.out.println("Test 1: " + intermediate.findMaximizedCapital(k1, w1, profits1, capital1)); // Expected: 4
        
        // Test case 2
        int k2 = 3, w2 = 0;
        int[] profits2 = {1, 2, 3};
        int[] capital2 = {0, 1, 2};
        System.out.println("Test 2: " + intermediate.findMaximizedCapital(k2, w2, profits2, capital2)); // Expected: 6
        
        // Test case 3
        int k3 = 1, w3 = 0;
        int[] profits3 = {1, 2, 3};
        int[] capital3 = {1, 1, 2};
        System.out.println("Test 3: " + intermediate.findMaximizedCapital(k3, w3, profits3, capital3)); // Expected: 0
        
        // Test case 4
        int k4 = 10, w4 = 0;
        int[] profits4 = {1, 2, 3};
        int[] capital4 = {0, 1, 1};
        System.out.println("Test 4: " + intermediate.findMaximizedCapital(k4, w4, profits4, capital4)); // Expected: 6
    }
}
