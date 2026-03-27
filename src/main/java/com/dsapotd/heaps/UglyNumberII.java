package com.dsapotd.heaps;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * <h1>264. Ugly Number II</h1>
 *
 * <p>An ugly number is a positive integer whose prime factors are limited to <code>2</code>, <code>3</code>, and <code>5</code>.</p>
 *
 * <p>Given an integer <code>n</code>, return the <code>n<sup>th</sup></code> ugly number.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: n = 10
 * Output: 12
 * Explanation: [1, 2, 3, 4, 5, 6, 8, 9, 10, 12] is the sequence of the first 10 ugly numbers.
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: n = 1
 * Output: 1
 * Explanation: 1 has no prime factors, therefore all of its prime factors are limited to 2, 3, and 5.
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= n <= 1690</li>
 * </ul>
 *
 * @tags Hash Table, Math, Dynamic Programming, Heap (Priority Queue)
 */
public class UglyNumberII {

    // Tier 2: Senior (Dynamic Programming - Three Pointers)
    // Time Complexity: O(N)
    // Space Complexity: O(N)
    public int nthUglyNumber(int n) {
        int[] ugly = new int[n];
        ugly[0] = 1;
        
        int i2 = 0, i3 = 0, i5 = 0;
        
        for (int i = 1; i < n; i++) {
            int next2 = ugly[i2] * 2;
            int next3 = ugly[i3] * 3;
            int next5 = ugly[i5] * 5;
            
            int nextUgly = Math.min(next2, Math.min(next3, next5));
            ugly[i] = nextUgly;
            
            // Advance pointers (handle duplicates by advancing all matching)
            if (nextUgly == next2) i2++;
            if (nextUgly == next3) i3++;
            if (nextUgly == next5) i5++;
        }
        
        return ugly[n - 1];
    }

    /**
     * Tier 1: Mid-Level (Min-Heap with Set for Deduplication)
     * Time Complexity: O(N log N) for heap operations
     * Space Complexity: O(N) for heap and set
     */
    static class Intermediate {
        public int nthUglyNumber(int n) {
            PriorityQueue<Long> minHeap = new PriorityQueue<>();
            Set<Long> seen = new HashSet<>();
            
            minHeap.offer(1L);
            seen.add(1L);
            
            long ugly = 1;
            int[] factors = {2, 3, 5};
            
            for (int i = 0; i < n; i++) {
                ugly = minHeap.poll();
                
                // Generate next ugly numbers
                for (int factor : factors) {
                    long next = ugly * factor;
                    if (!seen.contains(next)) {
                        seen.add(next);
                        minHeap.offer(next);
                    }
                }
            }
            
            return (int) ugly;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Three-Pointer DP Insight:**
     *    - Ugly numbers form a sequence where each element is 2, 3, or 5 times a previous ugly number
     *    - Maintain three pointers (i2, i3, i5) tracking which ugly number to multiply next
     *    - At each step, pick minimum of (ugly[i2]*2, ugly[i3]*3, ugly[i5]*5)
     *    - Advance pointer(s) that produced the minimum (handle duplicates by advancing all)
     *    - This is essentially a "merge K sorted lists" where K=3 and lists are infinite
     * 
     * 2. **Why Three Pointers Beat Heap:**
     *    - Heap approach: O(N log N) with O(N) space for deduplication
     *    - Three-pointer DP: O(N) time with O(N) space, no deduplication needed
     *    - DP generates numbers in sorted order directly, heap requires sorting
     *    - For N=1690 (constraint max), difference is 1690 vs 1690*log(1690) ≈ 17,000 operations
     * 
     * 3. **Handling Duplicates:**
     *    - Example: ugly[2]=2, ugly[3]=3
     *      - 2*3 = 6 (from i2)
     *      - 3*2 = 6 (from i3)
     *    - Solution: Advance ALL pointers that produce the minimum
     *    - Using if (not else-if) ensures all matching pointers advance
     * 
     * 4. **Generalization: K Factors:**
     *    - For factors [2, 3, 5, 7, ...], use K pointers
     *    - At each step: nextUgly = min(ugly[i_k] * factor_k for all k)
     *    - Advance all pointers where ugly[i_k] * factor_k == nextUgly
     *    - Time complexity remains O(N * K) where K is number of factors
     * 
     * 5. **Mathematical Property:**
     *    - Ugly numbers are also called "Hamming numbers" or "regular numbers"
     *    - They form a multiplicative closure under {2, 3, 5}
     *    - Sequence: 1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, ...
     *    - Density: Approximately O(log³(n)) ugly numbers up to n
     * 
     * 6. **Overflow Considerations:**
     *    - For N=1690, largest ugly number fits in int (2^31 - 1)
     *    - For larger N or more factors, use long
     *    - Heap approach uses long to avoid overflow during multiplication
     *    - DP approach can use int for given constraints
     * 
     * 7. **Distributed System Considerations:**
     *    - For massive N (billions):
     *      a) Partition range into blocks
     *      b) Each worker computes ugly numbers in its range
     *      c) Challenge: Need all previous ugly numbers to compute next
     *      d) Solution: Broadcast small prefix, compute locally
     *    - Not easily parallelizable due to sequential dependency
     * 
     * 8. **Memory Optimization:**
     *    - Current DP stores all N ugly numbers: O(N) space
     *    - If only Nth number needed (not full sequence):
     *      - Still need O(N) to compute due to dependencies
     *      - Can't reduce space without changing algorithm
     *    - Heap approach has similar O(N) space requirement
     * 
     * 9. **Connection to Other Problems:**
     *    - **Super Ugly Number:** Generalization with K factors
     *    - **Ugly Number I:** Check if single number is ugly (different problem)
     *    - **Merge K Sorted Lists:** Three pointers merge 3 infinite sorted lists
     *    - **Kth Smallest in Multiplication Table:** Similar DP pattern
     * 
     * 10. **Real-World Applications:**
     *     - Smooth numbers in cryptography (RSA key generation)
     *     - Audio/video sampling rates (powers of 2, 3, 5)
     *     - FFT algorithm (requires smooth sizes for efficiency)
     *     - Computer graphics (texture sizes as powers of 2)
     * 
     * 11. **Follow-up: What if we need ugly numbers in range [L, R]?**
     *     - Generate all ugly numbers up to R using DP
     *     - Filter for those >= L
     *     - Can't skip to L directly due to sequential dependency
     *     - Alternative: Binary search on "count of ugly numbers <= X"
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Three-Pointer DP):");
        UglyNumberII solver = new UglyNumberII();
        
        // Test case 1
        System.out.println("Test 1 (n=10): " + solver.nthUglyNumber(10)); // Expected: 12
        
        // Test case 2
        System.out.println("Test 2 (n=1): " + solver.nthUglyNumber(1)); // Expected: 1
        
        // Test case 3
        System.out.println("Test 3 (n=15): " + solver.nthUglyNumber(15)); // Expected: 24
        
        // Test case 4: First few ugly numbers
        System.out.print("First 15 ugly numbers: ");
        for (int i = 1; i <= 15; i++) {
            System.out.print(solver.nthUglyNumber(i) + " ");
        }
        System.out.println(); // Expected: 1 2 3 4 5 6 8 9 10 12 15 16 18 20 24
        
        // Test case 5: Larger n
        System.out.println("Test 5 (n=100): " + solver.nthUglyNumber(100)); // Expected: 1536
        
        // Test case 6: Edge case
        System.out.println("Test 6 (n=1690): " + solver.nthUglyNumber(1690)); // Expected: 2123366400
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Min-Heap):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        System.out.println("Test 1 (n=10): " + intermediate.nthUglyNumber(10)); // Expected: 12
        
        // Test case 2
        System.out.println("Test 2 (n=1): " + intermediate.nthUglyNumber(1)); // Expected: 1
        
        // Test case 3
        System.out.println("Test 3 (n=15): " + intermediate.nthUglyNumber(15)); // Expected: 24
        
        // Test case 4
        System.out.print("First 15 ugly numbers: ");
        for (int i = 1; i <= 15; i++) {
            System.out.print(intermediate.nthUglyNumber(i) + " ");
        }
        System.out.println();
        
        // Test case 5
        System.out.println("Test 5 (n=100): " + intermediate.nthUglyNumber(100)); // Expected: 1536
        
        // Test case 6: Skip for intermediate (too slow)
        // System.out.println("Test 6 (n=1690): " + intermediate.nthUglyNumber(1690));
    }
}
