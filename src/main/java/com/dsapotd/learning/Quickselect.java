package com.dsapotd.learning;

import java.util.Arrays;
import java.util.Random;

/**
 * <h1>Quickselect Algorithm</h1>
 *
 * <p>
 * <b>Quickselect</b> is a selection algorithm to find the <i>k</i>-th smallest
 * or largest element in an unordered list.
 * It is related to the Quicksort sorting algorithm and was developed by Tony
 * Hoare.
 * </p>
 *
 * <h2>Theory & Mechanism</h2>
 * <ul>
 * <li><b>Partitioning:</b> Like Quicksort, it chooses a 'pivot' element from
 * the array and partitions the other elements into two sub-arrays, according to
 * whether they are less than or greater than the pivot.</li>
 * <li><b>Selective Recursion:</b> Unlike Quicksort, which recurses into both
 * sub-arrays, Quickselect determines which sub-array contains the desired
 * <i>k</i>-th element and only recurses into that single sub-array.</li>
 * <li>This "single-branch" approach drastically reduces the work done compared
 * to a full sort.</li>
 * </ul>
 * 
 * 
 * 
 *
 * <h2>Complexity</h2>
 * <ul>
 * <li><b>Time Complexity (Average):</b> {@code O(N)}. The search space halves
 * on average each step: {@code N + N/2 + N/4 + ... ≈ 2N}.</li>
 * <li><b>Time Complexity (Worst Case):</b> {@code O(N^2)}. If the pivot chosen
 * is consistently the smallest or largest element (e.g., array is already
 * sorted and we always pick the last element as pivot).</li>
 * <li><b>Mitigation:</b> The worst-case is commonly mitigated by using a
 * <b>Randomized Pivot</b> or the "Median of Medians" approach. Randomization
 * makes the {@code O(N^2)} case practically impossible.</li>
 * <li><b>Space Complexity:</b> {@code O(1)} auxiliary space if implemented
 * iteratively, or {@code O(log N)} for the recursive call stack.</li>
 * </ul>
 */
public class Quickselect {

    private static final Random RANDOM = new Random();

    /**
     * Finds the k-th smallest element in the array.
     * 
     * @param nums The input array
     * @param k    The 1-based index (1 = smallest, 2 = second smallest, etc.)
     * @return The k-th smallest element
     */
    public int findKthSmallest(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input array or k");
        }
        // k-th smallest is at index (k - 1) in a 0-indexed sorted array
        return quickSelect(nums, 0, nums.length - 1, k - 1);
    }

    /**
     * Finds the k-th largest element in the array.
     * 
     * @param nums The input array
     * @param k    The 1-based index (1 = largest, 2 = second largest, etc.)
     * @return The k-th largest element
     */
    public int findKthLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input array or k");
        }
        // k-th largest is the (N - k + 1)-th smallest, which is at index (N - k)
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }

    /**
     * The core Quickselect recursive function.
     */
    private int quickSelect(int[] nums, int left, int right, int targetIndex) {
        // Base case: If the sub-array has only one element, it must be the target
        if (left == right) {
            return nums[left];
        }

        // 1. Choose a random pivot index between left and right (inclusive)
        int pivotIndex = left + RANDOM.nextInt(right - left + 1);

        // 2. Partition the array around the pivot and get the final sorted index of the
        // pivot
        pivotIndex = partition(nums, left, right, pivotIndex);

        // 3. Decide which half to recurse into
        if (targetIndex == pivotIndex) {
            // Found the exact target element
            return nums[targetIndex];
        } else if (targetIndex < pivotIndex) {
            // The target must be in the left sub-array
            return quickSelect(nums, left, pivotIndex - 1, targetIndex);
        } else {
            // The target must be in the right sub-array
            return quickSelect(nums, pivotIndex + 1, right, targetIndex);
        }
    }

    /**
     * Standard Lomuto partition scheme.
     */
    private int partition(int[] nums, int left, int right, int pivotIndex) {
        int pivotValue = nums[pivotIndex];

        // Move pivot to the end temporarily to keep it out of the way
        swap(nums, pivotIndex, right);

        int storeIndex = left;

        // Move all elements smaller than pivotValue to the left of storeIndex
        for (int i = left; i < right; i++) {
            if (nums[i] < pivotValue) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }

        // Move the pivot back to its final sorted position
        swap(nums, right, storeIndex);

        return storeIndex;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        Quickselect qs = new Quickselect();

        int[] arr1 = { 3, 2, 1, 5, 6, 4 };
        int k1 = 2;
        System.out.println("Array: " + Arrays.toString(arr1));
        System.out.println("2nd Largest element: " + qs.findKthLargest(arr1.clone(), k1)); // Expected: 5
        System.out.println("2nd Smallest element: " + qs.findKthSmallest(arr1.clone(), k1)); // Expected: 2

        System.out.println("--------------------------------------------------");

        int[] arr2 = { 3, 2, 3, 1, 2, 4, 5, 5, 6 };
        int k2 = 4;
        System.out.println("Array: " + Arrays.toString(arr2));
        System.out.println("4th Largest element: " + qs.findKthLargest(arr2.clone(), k2)); // Expected: 4
        System.out.println("4th Smallest element: " + qs.findKthSmallest(arr2.clone(), k2)); // Expected: 3
    }
}
