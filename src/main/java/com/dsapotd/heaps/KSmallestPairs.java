package com.dsapotd.heaps;

import java.util.*;

/**
 * <h1>373. Find K Pairs with Smallest Sums</h1>
 *
 * <p>You are given two integer arrays {@code nums1} and {@code nums2} sorted in
 * non-decreasing order, and an integer {@code k}.</p>
 *
 * <p>Define a pair {@code (u, v)} which consists of one element from the first array
 * and one element from the second array.</p>
 *
 * <p>Return the {@code k} pairs {@code (u1, v1), (u2, v2), ..., (uk, vk)} with the
 * smallest sums. The answer can be returned in any order.</p>
 *
 * <h2>Examples:</h2>
 * <pre>
 * Input:  nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 * Output: [[1,2],[1,4],[1,6]]
 * Explanation: The first 3 pairs are [1,2]=3, [1,4]=5, [1,6]=7.
 *
 * Input:  nums1 = [1,1,2], nums2 = [1,2,3], k = 2
 * Output: [[1,1],[1,1]]
 *
 * Input:  nums1 = [1,2], nums2 = [3], k = 3
 * Output: [[1,3],[2,3]]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= nums1.length, nums2.length <= 10^5</li>
 *   <li>-10^9 <= nums1[i], nums2[i] <= 10^9</li>
 *   <li>nums1 and nums2 are sorted in non-decreasing order.</li>
 *   <li>1 <= k <= 10^4</li>
 *   <li>k <= nums1.length * nums2.length</li>
 * </ul>
 *
 * @tags Heap (Priority Queue), Array, Two Pointers
 */
public class KSmallestPairs {

    // -------------------------------------------------------------------------
    // Tier 2: Senior — Min-Heap with column-advance pointer
    // -------------------------------------------------------------------------
    // Key insight: seed the heap with (nums1[i], nums2[0]) for i = 0..min(k, M)-1.
    // Since both arrays are sorted, for each row i in nums1, the natural ordering
    // of candidates is nums2[0], nums2[1], nums2[2], ...
    // When we extract (nums1[i], nums2[j]), we push (nums1[i], nums2[j+1]) — advancing
    // only within that row. No visited set needed; each (i, j) is unique by construction.
    //
    // This is identical to "Merge K Sorted Lists" where each "list" is:
    //   (nums1[i] + nums2[0]), (nums1[i] + nums2[1]), (nums1[i] + nums2[2]), ...

    // Time Complexity: O(min(k, M) * log(min(k, M)) + k * log(min(k, M)))
    //                 ≈ O(k log k) since heap size never exceeds min(k, M)
    // Space Complexity: O(min(k, M)) for the heap
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        // Min-heap entry: [sum, indexInNums1, indexInNums2]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // Seed: pair every nums1[i] with nums2[0] — the cheapest partner for each row
        for (int i = 0; i < Math.min(k, nums1.length); i++) {
            minHeap.offer(new int[]{nums1[i] + nums2[0], i, 0});
        }

        List<List<Integer>> result = new ArrayList<>();
        while (!minHeap.isEmpty() && result.size() < k) {
            int[] top = minHeap.poll();
            int i = top[1], j = top[2];
            result.add(Arrays.asList(nums1[i], nums2[j]));

            // Advance to the next element in nums2 for this row
            if (j + 1 < nums2.length) {
                minHeap.offer(new int[]{nums1[i] + nums2[j + 1], i, j + 1});
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Tier 1: Intermediate — Generate all pairs, sort by sum, take top k
    // -------------------------------------------------------------------------
    static class Intermediate {

        // Time Complexity: O(M * N * log(M * N)) — sort of all pairs
        // Space Complexity: O(M * N) — storing all pairs
        public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
            List<int[]> allPairs = new ArrayList<>();
            for (int u : nums1) {
                for (int v : nums2) {
                    allPairs.add(new int[]{u, v, u + v});
                }
            }
            allPairs.sort((a, b) -> a[2] - b[2]);

            List<List<Integer>> result = new ArrayList<>();
            for (int idx = 0; idx < Math.min(k, allPairs.size()); idx++) {
                result.add(Arrays.asList(allPairs.get(idx)[0], allPairs.get(idx)[1]));
            }
            return result;
        }
    }

    /*
     * Tier 3: Staff Discussion
     * -------------------------
     * 1. WHY SEED WITH nums2[0] ONLY?
     *    Since nums2 is sorted, (nums1[i], nums2[0]) is always the best starting
     *    pair for row i. Seeding with all (i, j) combinations would blow up heap size.
     *    Limiting seeds to min(k, M) rows is safe: nums1[k] + nums2[0] can never
     *    appear in the k smallest pairs if nums1 has ≥ k elements with smaller values.
     *
     * 2. OVERFLOW — nums1[i] and nums2[j] can be ±10^9. Their sum can exceed int range.
     *    In production, use long for the sum stored in the heap entry.
     *    (Omitted here for clarity since LeetCode constraints guarantee int is safe.)
     *
     * 3. COMPARISON TO DESIGN TWITTER:
     *    This is the same K-way merge pattern. "Rows" here = users there.
     *    Each row advances independently via the j pointer — identical to the
     *    nextOlderIndex pointer in Twitter.getNewsFeed.
     *
     * 4. ALTERNATIVE — Binary search on answer value: binary search on the sum S,
     *    count pairs with sum ≤ S using two pointers in O(M + N). O((M+N) log(max_sum))
     *    total — better for very large k but complex to implement correctly.
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        KSmallestPairs solver = new KSmallestPairs();

        int[] nums1a = {1, 7, 11}, nums2a = {2, 4, 6};
        System.out.println(solver.kSmallestPairs(nums1a, nums2a, 3));
        // Expected: [[1,2],[1,4],[1,6]]

        int[] nums1b = {1, 1, 2}, nums2b = {1, 2, 3};
        System.out.println(solver.kSmallestPairs(nums1b, nums2b, 2));
        // Expected: [[1,1],[1,1]]

        int[] nums1c = {1, 2}, nums2c = {3};
        System.out.println(solver.kSmallestPairs(nums1c, nums2c, 3));
        // Expected: [[1,3],[2,3]]
    }

    private static void runIntermediate() {
        Intermediate solver = new Intermediate();

        int[] nums1a = {1, 7, 11}, nums2a = {2, 4, 6};
        System.out.println(solver.kSmallestPairs(nums1a, nums2a, 3));

        int[] nums1b = {1, 1, 2}, nums2b = {1, 2, 3};
        System.out.println(solver.kSmallestPairs(nums1b, nums2b, 2));
    }
}
