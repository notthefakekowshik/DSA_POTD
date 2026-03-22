package com.dsapotd.heaps;

/**
 * <h1>347. Top K Frequent Elements</h1>
 *
 * <p>Given an integer array <code>nums</code> and an integer <code>k</code>, return the <code>k</code> most frequent elements. You may return the answer in <strong>any order</strong>.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: nums = [1,1,1,2,2,3], k = 2
 * Output: [1,2]
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: nums = [1], k = 1
 * Output: [1]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= nums.length <= 10^5</li>
 *   <li>-10^4 <= nums[i] <= 10^4</li>
 *   <li>k is in the range [1, the number of unique elements in the array].</li>
 *   <li>It is <strong>guaranteed</strong> that the answer is <strong>unique</strong>.</li>
 * </ul>
 *
 * <p><strong>Follow up:</strong> Your algorithm's time complexity must be better than O(n log n), where n is the array's size.</p>
 *
 * @tags Heap (Priority Queue), Hash Table, Bucket Sort, Counting
 */
import java.util.*;

public class TopKFrequentElements {

    // Optimized Solution (Bucket Sort)
    // Time Complexity: O(N)
    // Space Complexity: O(N)
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }

        List<Integer>[] buckets = new List[nums.length + 1];
        for (int key : freqMap.keySet()) {
            int frequency = freqMap.get(key);
            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }
            buckets[frequency].add(key);
        }

        int[] result = new int[k];
        int index = 0;
        for (int i = buckets.length - 1; i >= 0 && index < k; i--) {
            if (buckets[i] != null) {
                for (int num : buckets[i]) {
                    result[index++] = num;
                    if (index == k)
                        return result;
                }
            }
        }
        return result;
    }

    /**
     * Tier 1: Mid-Level (Min-Heap)
     * Time Complexity: O(N log K)
     * Space Complexity: O(N + K)
     */
    static class Intermediate {
        public int[] topKFrequent(int[] nums, int k) {
            Map<Integer, Integer> freqMap = new HashMap<>();
            for (int num : nums) {
                freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
            }

            PriorityQueue<Integer> minHeap = new PriorityQueue<>(Comparator.comparingInt(freqMap::get));
            for (int num : freqMap.keySet()) {
                minHeap.offer(num);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }

            int[] result = new int[k];
            for (int i = 0; i < k; i++) {
                result[i] = minHeap.poll();
            }
            return result;
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Bucket Sort):");
        TopKFrequentElements solution = new TopKFrequentElements();
        int[] nums = {1, 1, 1, 2, 2, 3};
        int k = 2;
        System.out.println("Result: " + Arrays.toString(solution.topKFrequent(nums, k)));
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Min-Heap):");
        Intermediate intermediate = new Intermediate();
        int[] nums = {1, 1, 1, 2, 2, 3};
        int k = 2;
        System.out.println("Result: " + Arrays.toString(intermediate.topKFrequent(nums, k)));
    }
}
