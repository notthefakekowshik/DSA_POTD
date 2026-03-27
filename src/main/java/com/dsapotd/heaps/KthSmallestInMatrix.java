package com.dsapotd.heaps;

import java.util.PriorityQueue;

/**
 * <h1>378. Kth Smallest Element in a Sorted Matrix</h1>
 *
 * <p>Given an <code>n x n</code> matrix where each of the rows and columns is sorted in ascending order,
 * return the <code>k<sup>th</sup></code> smallest element in the matrix.</p>
 *
 * <p>Note that it is the <code>k<sup>th</sup></code> smallest element in the sorted order,
 * not the <code>k<sup>th</sup></code> distinct element.</p>
 *
 * <p>You must find a solution with a memory complexity better than <code>O(n<sup>2</sup>)</code>.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
 * Output: 13
 * Explanation: The elements in the matrix are [1,5,9,10,11,12,13,13,15], and the 8th smallest number is 13
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: matrix = [[-5]], k = 1
 * Output: -5
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>n == matrix.length == matrix[i].length</li>
 *   <li>1 <= n <= 300</li>
 *   <li>-10<sup>9</sup> <= matrix[i][j] <= 10<sup>9</sup></li>
 *   <li>All the rows and columns of matrix are guaranteed to be sorted in non-decreasing order.</li>
 *   <li>1 <= k <= n<sup>2</sup></li>
 * </ul>
 *
 * <h2>Follow up:</h2>
 * <ul>
 *   <li>Could you solve the problem with a constant memory (i.e., <code>O(1)</code> memory complexity)?</li>
 *   <li>Could you solve the problem in <code>O(n)</code> time complexity? The solution may be too advanced for an interview but you may find reading <a href="http://www.cse.yorku.ca/~andy/pubs/X+Y.pdf">this paper</a> fun.</li>
 * </ul>
 *
 * @tags Array, Binary Search, Sorting, Heap (Priority Queue), Matrix
 */
public class KthSmallestInMatrix {

    // Tier 2: Senior (Min-Heap - K-way Merge Pattern)
    // Time Complexity: O(K log N) where N is number of rows, K is the kth element
    // Space Complexity: O(N) for heap storing at most N elements
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        
        // Min-heap: stores [value, row, col]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Initialize heap with first element of each row
        for (int i = 0; i < Math.min(n, k); i++) {
            minHeap.offer(new int[]{matrix[i][0], i, 0});
        }
        
        // Extract min K times
        int result = 0;
        for (int i = 0; i < k; i++) {
            int[] curr = minHeap.poll();
            result = curr[0];
            int row = curr[1];
            int col = curr[2];
            
            // Add next element from the same row
            if (col + 1 < n) {
                minHeap.offer(new int[]{matrix[row][col + 1], row, col + 1});
            }
        }
        
        return result;
    }

    /**
     * Tier 1: Mid-Level (Binary Search on Value Range)
     * Time Complexity: O(N * log(max - min)) where N is matrix dimension
     * Space Complexity: O(1)
     */
    static class Intermediate {
        public int kthSmallest(int[][] matrix, int k) {
            int n = matrix.length;
            int low = matrix[0][0];
            int high = matrix[n - 1][n - 1];
            
            while (low < high) {
                int mid = low + (high - low) / 2;
                int count = countLessOrEqual(matrix, mid);
                
                if (count < k) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            
            return low;
        }
        
        private int countLessOrEqual(int[][] matrix, int target) {
            int n = matrix.length;
            int count = 0;
            int row = n - 1;
            int col = 0;
            
            // Start from bottom-left corner
            while (row >= 0 && col < n) {
                if (matrix[row][col] <= target) {
                    count += row + 1; // All elements in this column up to row are <= target
                    col++;
                } else {
                    row--;
                }
            }
            
            return count;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Pattern Recognition - K-way Merge:**
     *    - Each row is a sorted list
     *    - Finding Kth smallest = merging K sorted lists and stopping at Kth element
     *    - Identical to Merge K Sorted Lists, but we only need Kth element, not full merge
     *    - This is why heap approach is O(K log N) not O(N² log N)
     * 
     * 2. **Binary Search Approach (Tier 1):**
     *    - Search space: [matrix[0][0], matrix[n-1][n-1]]
     *    - For each mid value, count how many elements <= mid
     *    - If count < k, answer is in upper half; else in lower half
     *    - Counting uses "staircase" traversal from bottom-left: O(N)
     *    - Total: O(N * log(max - min))
     *    - Trade-off: Better when K is large (close to N²), worse when K is small
     * 
     * 3. **When to Use Which Approach:**
     *    - Heap (Tier 2): Best when K << N²
     *      - Example: K = 10, N = 1000 → O(10 log 1000) ≈ 100 operations
     *    - Binary Search (Tier 1): Best when K is large or value range is small
     *      - Example: K = 500000, N = 1000 → O(1000 * log(10⁹)) ≈ 30000 operations
     *    - Crossover point: approximately K ≈ N * log(max - min) / log(N)
     * 
     * 4. **Memory Optimization:**
     *    - Current heap approach: O(N) space for N row pointers
     *    - Could reduce to O(min(N, K)) by only adding rows as needed
     *    - Binary search approach: O(1) space - better for memory-constrained systems
     * 
     * 5. **Distributed System Considerations:**
     *    - For massive matrices (millions x millions):
     *      a) Partition matrix into blocks (e.g., 1000x1000 blocks)
     *      b) Each worker maintains local min-heap for its block
     *      c) Coordinator runs global min-heap over worker heaps
     *      d) Pull elements from workers on-demand
     *    - Network I/O becomes bottleneck; consider batch fetching
     * 
     * 6. **Cache-Line Optimization:**
     *    - Matrix stored row-major in Java
     *    - Heap approach accesses columns sequentially (good cache locality)
     *    - Binary search "staircase" traversal has poor cache locality (jumps between rows)
     *    - For small matrices that fit in L1 cache, this difference is negligible
     *    - For large matrices, heap approach benefits from better cache utilization
     * 
     * 7. **Alternative: Young Tableau:**
     *    - Specialized data structure for sorted matrix
     *    - Extract-min in O(N) time, insert in O(N) time
     *    - Not practical for this problem, but interesting for repeated queries
     * 
     * 8. **Follow-up: O(1) Space Solution:**
     *    - Binary search approach achieves O(1) space
     *    - No better space complexity possible while maintaining reasonable time
     * 
     * 9. **Follow-up: O(N) Time Solution:**
     *    - Frederickson & Johnson's algorithm (1982)
     *    - Uses selection algorithm on implicit heap structure
     *    - Extremely complex, not practical for interviews
     *    - Theoretical interest only
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Min-Heap K-way Merge):");
        KthSmallestInMatrix solver = new KthSmallestInMatrix();
        
        // Test case 1
        int[][] matrix1 = {
            {1, 5, 9},
            {10, 11, 13},
            {12, 13, 15}
        };
        System.out.println("Test 1 (k=8): " + solver.kthSmallest(matrix1, 8)); // Expected: 13
        System.out.println("Test 1 (k=1): " + solver.kthSmallest(matrix1, 1)); // Expected: 1
        System.out.println("Test 1 (k=5): " + solver.kthSmallest(matrix1, 5)); // Expected: 11
        
        // Test case 2
        int[][] matrix2 = {{-5}};
        System.out.println("Test 2 (k=1): " + solver.kthSmallest(matrix2, 1)); // Expected: -5
        
        // Test case 3: Duplicates
        int[][] matrix3 = {
            {1, 2},
            {1, 3}
        };
        System.out.println("Test 3 (k=2): " + solver.kthSmallest(matrix3, 2)); // Expected: 1
        System.out.println("Test 3 (k=3): " + solver.kthSmallest(matrix3, 3)); // Expected: 2
        
        // Test case 4: Negative numbers
        int[][] matrix4 = {
            {-5, -4},
            {-3, -2}
        };
        System.out.println("Test 4 (k=2): " + solver.kthSmallest(matrix4, 2)); // Expected: -4
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Binary Search):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        int[][] matrix1 = {
            {1, 5, 9},
            {10, 11, 13},
            {12, 13, 15}
        };
        System.out.println("Test 1 (k=8): " + intermediate.kthSmallest(matrix1, 8)); // Expected: 13
        System.out.println("Test 1 (k=1): " + intermediate.kthSmallest(matrix1, 1)); // Expected: 1
        System.out.println("Test 1 (k=5): " + intermediate.kthSmallest(matrix1, 5)); // Expected: 11
        
        // Test case 2
        int[][] matrix2 = {{-5}};
        System.out.println("Test 2 (k=1): " + intermediate.kthSmallest(matrix2, 1)); // Expected: -5
        
        // Test case 3
        int[][] matrix3 = {
            {1, 2},
            {1, 3}
        };
        System.out.println("Test 3 (k=2): " + intermediate.kthSmallest(matrix3, 2)); // Expected: 1
        System.out.println("Test 3 (k=3): " + intermediate.kthSmallest(matrix3, 3)); // Expected: 2
        
        // Test case 4
        int[][] matrix4 = {
            {-5, -4},
            {-3, -2}
        };
        System.out.println("Test 4 (k=2): " + intermediate.kthSmallest(matrix4, 2)); // Expected: -4
    }
}
