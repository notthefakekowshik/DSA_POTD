package com.dsapotd.heaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <h1>703. Kth Largest Element in a Stream</h1>
 *
 * <p>
 * Design a class to find the <code>kth</code> largest element in a stream. Note
 * that it is the <code>kth</code> largest element in the sorted order, not the
 * <code>kth</code> distinct element.
 * </p>
 *
 * <p>
 * Implement <code>KthLargest</code> class:
 * </p>
 * <ul>
 * <li><code>KthLargest(int k, int[] nums)</code> Initializes the object with
 * the integer <code>k</code> and the stream of integers <code>nums</code>.</li>
 * <li><code>int add(int val)</code> Appends the integer <code>val</code> to the
 * stream and returns the element representing the <code>kth</code> largest
 * element in the stream.</li>
 * </ul>
 *
 * <h2>Example 1:</h2>
 * 
 * <pre>
 * Input
 * ["KthLargest", "add", "add", "add", "add", "add"]
 * [[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]
 * Output
 * [null, 4, 5, 5, 8, 8]
 *
 * Explanation
 * KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
 * kthLargest.add(3);   // return 4
 * kthLargest.add(5);   // return 5
 * kthLargest.add(10);  // return 5
 * kthLargest.add(9);   // return 8
 * kthLargest.add(4);   // return 8
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 * <li>1 <= k <= 10^4</li>
 * <li>0 <= nums.length <= 10^4</li>
 * <li>-10^4 <= nums[i] <= 10^4</li>
 * <li>-10^4 <= val <= 10^4</li>
 * <li>At most 10^4 calls will be made to add.</li>
 * <li>It is guaranteed that there will be at least k elements in the array when
 * you search for the kth element.</li>
 * </ul>
 *
 * @tags Heap (Priority Queue), Design, Data Stream
 */
public class KthLargestStream {
    private final PriorityQueue<Integer> minHeap;
    private final int k;

    // Optimized Solution (Min-Heap)
    // Time Complexity: O(N log K) to initialize, O(log K) to add
    // Space Complexity: O(K)
    public KthLargestStream(int k, int[] nums) {
        this.k = k;
        this.minHeap = new PriorityQueue<>(k);
        for (int num : nums) {
            add(num);
        }
    }

    public int add(int val) {
        if (minHeap.size() < k) {
            minHeap.offer(val);
        } else if (val > minHeap.peek()) {
            minHeap.poll();
            minHeap.offer(val);
        }
        return minHeap.peek();
    }

    /**
     * Tier 1: Mid-Level (Brute Force)
     * Time Complexity: O(N log N) per add
     * Space Complexity: O(N)
     */
    static class Intermediate {
        private final List<Integer> allNums = new ArrayList<>();
        private final int k;

        public Intermediate(int k, int[] nums) {
            this.k = k;
            for (int num : nums) {
                allNums.add(num);
            }
        }

        public int add(int val) {
            allNums.add(val);
            Collections.sort(allNums);
            return allNums.get(allNums.size() - k);
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Min-Heap):");
        KthLargestStream kthLargest = new KthLargestStream(3, new int[] { 4, 5, 8, 2 });
        System.out.println("Add 3: " + kthLargest.add(3)); // Output: 4
        System.out.println("Add 5: " + kthLargest.add(5)); // Output: 5
        System.out.println("Add 10: " + kthLargest.add(10)); // Output: 5
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Brute Force):");
        Intermediate intermediate = new Intermediate(3, new int[] { 4, 5, 8, 2 });
        System.out.println("Add 3: " + intermediate.add(3)); // Output: 4
        System.out.println("Add 5: " + intermediate.add(5)); // Output: 5
        System.out.println("Add 10: " + intermediate.add(10)); // Output: 5
    }
}
