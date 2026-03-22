package com.dsapotd.heaps;

import java.util.PriorityQueue;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * <h2>632. Smallest Range Covering Elements from K Lists</h2>
 *
 * <p>You have k lists of sorted integers in non-decreasing order. Find the smallest range that includes at least one number from each of the k lists.</p>
 * <p>We define the range [a, b] is smaller than range [c, d] if b - a < d - c or a < c if b - a == d - c.</p>
 *
 * <h3>Example 1:</h3>
 * <pre>
 * Input: nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]
 * Output: [20,24]
 * Explanation:
 * List 1: [4, 10, 15, 24, 26], 24 is in range [20,24].
 * List 2: [0, 9, 12, 20], 20 is in range [20,24].
 * List 3: [5, 18, 22, 30], 22 is in range [20,24].
 * </pre>
 *
 * <h3>Constraints:</h3>
 * <ul>
 *   <li>nums.length == k</li>
 *   <li>1 <= k <= 3500</li>
 *   <li>1 <= nums[i].length <= 50</li>
 *   <li>-10^5 <= nums[i][j] <= 10^5</li>
 *   <li>nums[i] is sorted in non-decreasing order.</li>
 * </ul>
 *
 * @tags Heap, Greedy, Sliding Window, N-Pointer
 */
public class SmallestRangeCoveringKLists {

    /**
     * Tier 1: Intermediate (Min-Heap + Max Tracking)
     * Time Complexity: O(N log K) where N is total number of elements.
     * Space Complexity: O(K) for the heap.
     */
    public static class Intermediate {
        public int[] smallestRange(List<List<Integer>> nums) {
            int k = nums.size();
            // Heap stores: {value, listIndex, elementIndex}
            PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < k; i++) {
                int val = nums.get(i).get(0);
                minHeap.offer(new int[]{val, i, 0});
                max = Math.max(max, val);
            }

            int rangeStart = 0, rangeEnd = Integer.MAX_VALUE;

            while (minHeap.size() == k) {
                int[] curr = minHeap.poll();
                int min = curr[0];
                int listIdx = curr[1];
                int elemIdx = curr[2];

                // Update result if current range is smaller
                if (max - min < rangeEnd - rangeStart) {
                    rangeStart = min;
                    rangeEnd = max;
                }

                // Move to next element in the same list
                if (elemIdx + 1 < nums.get(listIdx).size()) {
                    int nextVal = nums.get(listIdx).get(elemIdx + 1);
                    minHeap.offer(new int[]{nextVal, listIdx, elemIdx + 1});
                    max = Math.max(max, nextVal);
                } else {
                    // One list exhausted, cannot cover all lists anymore
                    break;
                }
            }

            return new int[]{rangeStart, rangeEnd};
        }
    }

    /**
     * Tier 3: Staff (Deep Internals - Memory Locality & Sliding Window Alternative)
     * Discussion: 
     * - The Heap approach is optimal for K lists.
     * - For massive datasets, we could use a Sliding Window on a combined sorted list of all elements.
     * - This would be O(N log N) to sort + O(N) window, but the window might be better for cache locality.
     * - In JVM, memory locality of List<List<Integer>> is poor due to object overhead. 
     * - Using primitive arrays and a custom Min-Heap implementation (avoiding PriorityQueue's object churn) would be Staff-level optimization.
     */
    public static class Optimized extends Intermediate {
        // Standard Heap is already the best complexity for this problem.
    }

    public static void main(String[] args) {
        List<List<Integer>> nums = new ArrayList<>();
        nums.add(Arrays.asList(4, 10, 15, 24, 26));
        nums.add(Arrays.asList(0, 9, 12, 20));
        nums.add(Arrays.asList(5, 18, 22, 30));

        // Toggle solution
        Intermediate solver = new Intermediate();
        
        int[] result = solver.smallestRange(nums);
        System.out.println("Smallest Range: " + Arrays.toString(result));
    }
}
