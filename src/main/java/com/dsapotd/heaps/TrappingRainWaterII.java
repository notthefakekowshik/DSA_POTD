package com.dsapotd.heaps;

import java.util.PriorityQueue;

/**
 * <h1>407. Trapping Rain Water II</h1>
 *
 * <p>Given an <code>m x n</code> integer matrix <code>heightMap</code> representing the height of each unit cell
 * in a 2D elevation map, return the volume of water it can trap after raining.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
 * Output: 4
 * Explanation: After the rain, water is trapped between the blocks.
 * We have 4 units of water trapped.
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: heightMap = [[3,3,3,3,3],[3,2,2,2,3],[3,2,1,2,3],[3,2,2,2,3],[3,3,3,3,3]]
 * Output: 10
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>m == heightMap.length</li>
 *   <li>n == heightMap[i].length</li>
 *   <li>1 <= m, n <= 200</li>
 *   <li>0 <= heightMap[i][j] <= 2 * 10<sup>4</sup></li>
 * </ul>
 *
 * @tags Array, Breadth-First Search, Heap (Priority Queue), Matrix
 */
public class TrappingRainWaterII {

    // Tier 2: Senior (Min-Heap BFS from Boundaries)
    // Time Complexity: O(M * N * log(M * N)) where M, N are dimensions
    // Space Complexity: O(M * N) for heap and visited array
    public int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0].length == 0) {
            return 0;
        }
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        
        if (m < 3 || n < 3) {
            return 0; // No water can be trapped
        }
        
        // Min-heap: [height, row, col]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        boolean[][] visited = new boolean[m][n];
        
        // Add all boundary cells to heap
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    minHeap.offer(new int[]{heightMap[i][j], i, j});
                    visited[i][j] = true;
                }
            }
        }
        
        int water = 0;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        // Process cells from lowest boundary inward
        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int height = curr[0];
            int row = curr[1];
            int col = curr[2];
            
            // Check all 4 neighbors
            for (int[] dir : dirs) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    
                    // Water level is determined by the minimum boundary height seen so far
                    water += Math.max(0, height - heightMap[newRow][newCol]);
                    
                    // Add neighbor with updated height (max of its own height and current water level)
                    minHeap.offer(new int[]{Math.max(height, heightMap[newRow][newCol]), newRow, newCol});
                }
            }
        }
        
        return water;
    }

    /**
     * Tier 1: Mid-Level (Brute Force - For Each Cell, Find Min Boundary)
     * Time Complexity: O((M * N)²) - For each cell, BFS to find minimum boundary
     * Space Complexity: O(M * N) for BFS queue
     * 
     * Note: This approach is too slow for the given constraints and is shown for conceptual understanding only.
     */
    static class Intermediate {
        public int trapRainWater(int[][] heightMap) {
            if (heightMap == null || heightMap.length < 3 || heightMap[0].length < 3) {
                return 0;
            }
            
            int m = heightMap.length;
            int n = heightMap[0].length;
            int water = 0;
            
            // For each interior cell, find the minimum boundary height
            for (int i = 1; i < m - 1; i++) {
                for (int j = 1; j < n - 1; j++) {
                    int minBoundary = findMinBoundary(heightMap, i, j);
                    water += Math.max(0, minBoundary - heightMap[i][j]);
                }
            }
            
            return water;
        }
        
        private int findMinBoundary(int[][] heightMap, int startRow, int startCol) {
            int m = heightMap.length;
            int n = heightMap[0].length;
            
            // BFS to find minimum height on path to boundary
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            boolean[][] visited = new boolean[m][n];
            
            pq.offer(new int[]{heightMap[startRow][startCol], startRow, startCol});
            visited[startRow][startCol] = true;
            
            int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            
            while (!pq.isEmpty()) {
                int[] curr = pq.poll();
                int height = curr[0];
                int row = curr[1];
                int col = curr[2];
                
                // If reached boundary, return the maximum height seen on this path
                if (row == 0 || row == m - 1 || col == 0 || col == n - 1) {
                    return height;
                }
                
                for (int[] dir : dirs) {
                    int newRow = row + dir[0];
                    int newCol = col + dir[1];
                    
                    if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && !visited[newRow][newCol]) {
                        visited[newRow][newCol] = true;
                        pq.offer(new int[]{Math.max(height, heightMap[newRow][newCol]), newRow, newCol});
                    }
                }
            }
            
            return 0;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Key Insight - Water Flows Outward:**
     *    - Water trapped at a cell is determined by the minimum barrier height to the boundary
     *    - Process from boundary inward using min-heap ensures we always know the "water level"
     *    - Similar to Dijkstra's algorithm: process cells in order of increasing boundary height
     * 
     * 2. **Why Start from Boundaries?**
     *    - Boundary cells cannot trap water (water flows out)
     *    - For interior cells, water level = min(all paths to boundary, max height on path)
     *    - Processing from outside-in with min-heap gives us this minimum efficiently
     *    - Each cell is processed once when its minimum boundary is determined
     * 
     * 3. **Algorithm Correctness:**
     *    - Invariant: When processing a cell, all cells with lower boundary heights are already processed
     *    - Water at current cell = max(0, min_boundary_height - cell_height)
     *    - Min-heap ensures we process in correct order (lowest boundary first)
     *    - Similar to Prim's MST or Dijkstra's shortest path
     * 
     * 4. **Comparison to 1D Trapping Rain Water:**
     *    - 1D: Two pointers from both ends, O(N) time
     *    - 2D: Can't use two pointers (4 directions, not 2)
     *    - 2D: Need heap to process all boundary cells simultaneously
     *    - 1D is special case where boundary is just two endpoints
     * 
     * 5. **Why Min-Heap, Not Max-Heap?**
     *    - We want to process cells with lowest boundary height first
     *    - This ensures when we reach a cell, we know its minimum escape route
     *    - Max-heap would process highest boundaries first, giving incorrect water levels
     * 
     * 6. **Optimization: Early Termination:**
     *    - If all remaining cells in heap have same height, no more water can be trapped
     *    - Can check if heap.peek().height == current_max_height
     *    - Rare in practice but useful for flat terrains
     * 
     * 7. **Distributed System Considerations:**
     *    - For massive grids (satellite terrain data):
     *      a) Partition grid into tiles with overlapping boundaries
     *      b) Each worker processes its tile independently
     *      c) Coordinator resolves boundary conflicts
     *      d) Iterate until convergence (water levels stabilize)
     *    - Challenge: Boundary cells affect interior of adjacent tiles
     *    - Solution: Multi-pass algorithm with boundary exchange
     * 
     * 8. **Memory Optimization:**
     *    - Current: O(M*N) for visited array
     *    - Alternative: Mark visited by setting heightMap[i][j] to -1 (if allowed to modify input)
     *    - Saves O(M*N) boolean array, uses existing int array
     *    - Trade-off: Modifies input, but acceptable if specified
     * 
     * 9. **Real-World Applications:**
     *    - Urban planning: Flood risk assessment
     *    - Agriculture: Irrigation system design
     *    - Civil engineering: Drainage system planning
     *    - Video games: Realistic water physics simulation
     * 
     * 10. **Extension: 3D Trapping Rain Water:**
     *     - Extend to 3D grid (voxels)
     *     - Same algorithm: Start from 2D boundary (all faces of cube)
     *     - Process inward with min-heap
     *     - 6 directions instead of 4
     *     - Applications: 3D printing, medical imaging
     * 
     * 11. **Connection to Other Problems:**
     *     - **Trapping Rain Water I:** 1D version, two pointers
     *     - **Pacific Atlantic Water Flow:** Similar boundary-inward BFS
     *     - **Shortest Path in Grid:** Dijkstra's algorithm pattern
     *     - **Swim in Rising Water:** Binary search + BFS variant
     * 
     * 12. **Cache-Line Optimization:**
     *     - Heap operations have poor spatial locality
     *     - For small grids (< 20x20), DFS might be faster despite worse complexity
     *     - For large grids, heap is necessary
     *     - Consider blocked matrix layout for better cache utilization
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Min-Heap BFS):");
        TrappingRainWaterII solver = new TrappingRainWaterII();
        
        // Test case 1
        int[][] heightMap1 = {
            {1, 4, 3, 1, 3, 2},
            {3, 2, 1, 3, 2, 4},
            {2, 3, 3, 2, 3, 1}
        };
        System.out.println("Test 1: " + solver.trapRainWater(heightMap1)); // Expected: 4
        
        // Test case 2
        int[][] heightMap2 = {
            {3, 3, 3, 3, 3},
            {3, 2, 2, 2, 3},
            {3, 2, 1, 2, 3},
            {3, 2, 2, 2, 3},
            {3, 3, 3, 3, 3}
        };
        System.out.println("Test 2: " + solver.trapRainWater(heightMap2)); // Expected: 10
        
        // Test case 3: No water trapped
        int[][] heightMap3 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        System.out.println("Test 3: " + solver.trapRainWater(heightMap3)); // Expected: 0
        
        // Test case 4: Simple bowl
        int[][] heightMap4 = {
            {5, 5, 5},
            {5, 1, 5},
            {5, 5, 5}
        };
        System.out.println("Test 4: " + solver.trapRainWater(heightMap4)); // Expected: 4
        
        // Test case 5: Asymmetric
        int[][] heightMap5 = {
            {12, 13, 1, 12},
            {13, 4, 13, 12},
            {13, 8, 10, 12},
            {12, 13, 12, 12},
            {13, 13, 13, 13}
        };
        System.out.println("Test 5: " + solver.trapRainWater(heightMap5)); // Expected: 14
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Brute Force - SLOW):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        int[][] heightMap1 = {
            {1, 4, 3, 1, 3, 2},
            {3, 2, 1, 3, 2, 4},
            {2, 3, 3, 2, 3, 1}
        };
        System.out.println("Test 1: " + intermediate.trapRainWater(heightMap1)); // Expected: 4
        
        // Test case 2: Skip (too slow)
        // int[][] heightMap2 = {...};
        
        // Test case 3
        int[][] heightMap3 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        System.out.println("Test 3: " + intermediate.trapRainWater(heightMap3)); // Expected: 0
        
        // Test case 4
        int[][] heightMap4 = {
            {5, 5, 5},
            {5, 1, 5},
            {5, 5, 5}
        };
        System.out.println("Test 4: " + intermediate.trapRainWater(heightMap4)); // Expected: 4
    }
}
