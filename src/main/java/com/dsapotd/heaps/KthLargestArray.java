package com.dsapotd.heaps;

/**
 * <h1>215. Kth Largest Element in an Array</h1>
 *
 * <p>Given an integer array <code>nums</code> and an integer <code>k</code>, return the <code>kth</code> largest element in the array.</p>
 *
 * <p>Note that it is the <code>kth</code> largest element in the sorted order, not the <code>kth</code> distinct element.</p>
 *
 * <p>Can you solve it without sorting?</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: nums = [3,2,1,5,6,4], k = 2
 * Output: 5
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
 * Output: 4
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= k <= nums.length <= 10^5</li>
 *   <li>-10^4 <= nums[i] <= 10^4</li>
 * </ul>
 *
 * @tags Heap (Priority Queue), Divide and Conquer, Quickselect
 */
import java.util.PriorityQueue;
import java.util.Random;

public class KthLargestArray {
    private final Random random = new Random();

    // Optimized Solution (Quickselect)
    // Time Complexity: O(N) average, O(N^2) worst case
    // Space Complexity: O(1) in-place partition
    public int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    // Time Complexity: O(N) average
    // Space Complexity: O(log N) stack depth
    private int quickSelect(int[] nums, int left, int right, int kIndex) {
        if (left == right)
            return nums[left];

        int pivotIndex = left + random.nextInt(right - left + 1);
        pivotIndex = partition(nums, left, right, pivotIndex);

        if (pivotIndex == kIndex) {
            return nums[kIndex];
        } else if (pivotIndex < kIndex) {
            return quickSelect(nums, pivotIndex + 1, right, kIndex);
        } else {
            return quickSelect(nums, left, pivotIndex - 1, kIndex);
        }
    }

    private int partition(int[] nums, int left, int right, int pivotIndex) {
        int pivotValue = nums[pivotIndex];
        swap(nums, pivotIndex, right);
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }
        swap(nums, storeIndex, right);
        return storeIndex;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * Tier 1: Mid-Level (Min-Heap)
     * Time Complexity: O(N log K)
     * Space Complexity: O(K)
     */
    static class Intermediate {
        public int findKthLargest(int[] nums, int k) {
            PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
            for (int num : nums) {
                minHeap.offer(num);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }
            return minHeap.peek();
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Quickselect):");
        KthLargestArray solution = new KthLargestArray();
        int[] nums1 = { 3, 2, 1, 5, 6, 4 };
        System.out.println("Result: " + solution.findKthLargest(nums1, 2)); // Output: 5
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Min-Heap):");
        Intermediate intermediate = new Intermediate();
        int[] nums1 = { 3, 2, 1, 5, 6, 4 };
        System.out.println("Result: " + intermediate.findKthLargest(nums1, 2)); // Output: 5
    }
}
