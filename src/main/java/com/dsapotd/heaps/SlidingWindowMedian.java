package com.dsapotd.heaps;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * <h1>480. Sliding Window Median</h1>
 *
 * <p>The median is the middle value in an ordered integer list. If the size of the list is even,
 * there is no middle value. So the median is the mean of the two middle values.</p>
 *
 * <ul>
 *   <li>For example, <code>arr = [2,3,4]</code>, the median is <code>3</code>.</li>
 *   <li>For example, <code>arr = [2,3]</code>, the median is <code>(2 + 3) / 2 = 2.5</code>.</li>
 * </ul>
 *
 * <p>You are given an integer array <code>nums</code> and an integer <code>k</code>. There is a sliding window
 * of size <code>k</code> which is moving from the very left of the array to the very right. You can only see the
 * <code>k</code> numbers in the window. Each time the sliding window moves right by one position.</p>
 *
 * <p>Return the median array for each window in the original array. Answers within <code>10<sup>-5</sup></code>
 * of the actual value will be accepted.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [1.00000,-1.00000,-1.00000,3.00000,5.00000,6.00000]
 * Explanation:
 * Window position                Median
 * ---------------                -----
 * [1  3  -1] -3  5  3  6  7        1
 *  1 [3  -1  -3] 5  3  6  7       -1
 *  1  3 [-1  -3  5] 3  6  7       -1
 *  1  3  -1 [-3  5  3] 6  7        3
 *  1  3  -1  -3 [5  3  6] 7        5
 *  1  3  -1  -3  5 [3  6  7]       6
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: nums = [1,2,3,4,2,3,1,4,2], k = 3
 * Output: [2.00000,3.00000,3.00000,3.00000,2.00000,3.00000,2.00000]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= k <= nums.length <= 10<sup>5</sup></li>
 *   <li>-2<sup>31</sup> <= nums[i] <= 2<sup>31</sup> - 1</li>
 * </ul>
 *
 * @tags Array, Hash Table, Sliding Window, Heap (Priority Queue)
 */
public class SlidingWindowMedian {

    // Tier 2: Senior (Two TreeSets for O(log K) operations)
    // Time Complexity: O(N * log K) where N is array length, K is window size
    // Space Complexity: O(K)
    public double[] medianSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        double[] result = new double[n - k + 1];
        
        // TreeSet with custom comparator to handle duplicates (compare by value, then by index)
        Comparator<int[]> comp = (a, b) -> {
            if (nums[a[0]] != nums[b[0]]) {
                return Integer.compare(nums[a[0]], nums[b[0]]);
            }
            return Integer.compare(a[0], b[0]);
        };
        
        TreeSet<int[]> small = new TreeSet<>(comp); // Max-heap equivalent (lower half)
        TreeSet<int[]> large = new TreeSet<>(comp); // Min-heap equivalent (upper half)
        
        for (int i = 0; i < n; i++) {
            // Add new element
            int[] elem = new int[]{i};
            large.add(elem);
            
            // Balance: move largest from large to small
            small.add(large.pollFirst());
            
            // If small has more elements than large, rebalance
            if (small.size() > large.size()) {
                large.add(small.pollLast());
            }
            
            // Remove element going out of window
            if (i >= k) {
                int[] toRemove = new int[]{i - k};
                if (!small.remove(toRemove)) {
                    large.remove(toRemove);
                }
                
                // Rebalance after removal
                if (small.size() < large.size() - 1) {
                    small.add(large.pollFirst());
                } else if (small.size() > large.size()) {
                    large.add(small.pollLast());
                }
            }
            
            // Calculate median when window is full
            if (i >= k - 1) {
                double median;
                if (k % 2 == 0) {
                    median = ((long) nums[small.last()[0]] + (long) nums[large.first()[0]]) / 2.0;
                } else {
                    median = nums[large.first()[0]];
                }
                result[i - k + 1] = median;
            }
        }
        
        return result;
    }

    /**
     * Tier 1: Mid-Level (Brute Force - Sort Each Window)
     * Time Complexity: O((N - K + 1) * K log K) = O(N * K log K)
     * Space Complexity: O(K) for sorting window
     */
    static class Intermediate {
        public double[] medianSlidingWindow(int[] nums, int k) {
            int n = nums.length;
            double[] result = new double[n - k + 1];
            
            for (int i = 0; i <= n - k; i++) {
                // Copy window elements
                int[] window = new int[k];
                for (int j = 0; j < k; j++) {
                    window[j] = nums[i + j];
                }
                
                // Sort the window
                Arrays.sort(window);
                
                // Calculate median
                if (k % 2 == 0) {
                    // Use long to avoid overflow
                    result[i] = ((long) window[k / 2 - 1] + (long) window[k / 2]) / 2.0;
                } else {
                    result[i] = window[k / 2];
                }
            }
            
            return result;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Why TreeSet over PriorityQueue?**
     *    - PriorityQueue doesn't support O(log N) removal of arbitrary elements
     *    - PriorityQueue.remove() is O(N) because it requires linear scan
     *    - TreeSet provides O(log N) insertion, deletion, and access to min/max
     *    - Trade-off: TreeSet has higher constant factors but better asymptotic complexity
     * 
     * 2. **Handling Duplicates:**
     *    - TreeSet by default doesn't allow duplicates
     *    - Solution: Store indices and compare by (value, index) pair
     *    - This ensures uniqueness while maintaining value-based ordering
     *    - Alternative: Use TreeMap<Integer, Integer> to count frequencies
     * 
     * 3. **Integer Overflow Prevention:**
     *    - When calculating median of two integers, (a + b) / 2 can overflow
     *    - Solution: Cast to long before addition: ((long)a + (long)b) / 2.0
     *    - Alternative: Use a + (b - a) / 2, but this gives integer division issues
     * 
     * 4. **Two-Heap Invariant:**
     *    - Maintain: small.size() == large.size() OR small.size() == large.size() - 1
     *    - For odd k: large has one more element, median is large.min()
     *    - For even k: both equal size, median is average of small.max() and large.min()
     *    - After removal, rebalancing is critical to maintain invariant
     * 
     * 5. **Alternative: Multiset with Lazy Deletion:**
     *    - Instead of immediate removal, mark elements as "deleted" in a HashSet
     *    - Only physically remove when accessing top elements
     *    - Reduces TreeSet operations but increases space complexity
     *    - Trade-off: O(K) extra space for potentially better constant factors
     * 
     * 6. **Distributed Streaming Scenario:**
     *    - For infinite streams with sliding windows:
     *      a) Use circular buffer to store last K elements
     *      b) Maintain two heaps as described
     *      c) When buffer wraps, remove oldest element from heaps
     *    - For distributed systems:
     *      a) Partition stream by time ranges
     *      b) Each partition maintains local median
     *      c) Coordinator merges medians using weighted average
     * 
     * 7. **Cache-Line Optimization:**
     *    - TreeSet nodes are scattered in memory (poor cache locality)
     *    - For small K (< 64), consider using sorted array with binary search
     *    - Array provides better cache performance: O(K) insertion but O(1) median access
     *    - Crossover point depends on K and hardware cache characteristics
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Two TreeSets):");
        SlidingWindowMedian swm = new SlidingWindowMedian();
        
        // Test case 1
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        double[] result1 = swm.medianSlidingWindow(nums1, k1);
        System.out.println("Test 1: " + Arrays.toString(result1));
        // Expected: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]
        
        // Test case 2
        int[] nums2 = {1, 2, 3, 4, 2, 3, 1, 4, 2};
        int k2 = 3;
        double[] result2 = swm.medianSlidingWindow(nums2, k2);
        System.out.println("Test 2: " + Arrays.toString(result2));
        // Expected: [2.0, 3.0, 3.0, 3.0, 2.0, 3.0, 2.0]
        
        // Test case 3: Even window size
        int[] nums3 = {1, 4, 2, 3};
        int k3 = 4;
        double[] result3 = swm.medianSlidingWindow(nums3, k3);
        System.out.println("Test 3: " + Arrays.toString(result3));
        // Expected: [2.5]
        
        // Test case 4: Window size 1
        int[] nums4 = {1, 2, 3};
        int k4 = 1;
        double[] result4 = swm.medianSlidingWindow(nums4, k4);
        System.out.println("Test 4: " + Arrays.toString(result4));
        // Expected: [1.0, 2.0, 3.0]
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Brute Force):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        double[] result1 = intermediate.medianSlidingWindow(nums1, k1);
        System.out.println("Test 1: " + Arrays.toString(result1));
        
        // Test case 2
        int[] nums2 = {1, 2, 3, 4, 2, 3, 1, 4, 2};
        int k2 = 3;
        double[] result2 = intermediate.medianSlidingWindow(nums2, k2);
        System.out.println("Test 2: " + Arrays.toString(result2));
        
        // Test case 3
        int[] nums3 = {1, 4, 2, 3};
        int k3 = 4;
        double[] result3 = intermediate.medianSlidingWindow(nums3, k3);
        System.out.println("Test 3: " + Arrays.toString(result3));
        
        // Test case 4
        int[] nums4 = {1, 2, 3};
        int k4 = 1;
        double[] result4 = intermediate.medianSlidingWindow(nums4, k4);
        System.out.println("Test 4: " + Arrays.toString(result4));
    }
}
