package com.dsapotd.heaps;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * <h1>506. Relative Ranks</h1>
 *
 * <p>You are given an integer array {@code score} of size {@code n}, where {@code score[i]} is the
 * score of the ith athlete in a competition. All the scores are guaranteed to be unique.</p>
 *
 * <p>The athletes are placed based on their scores, where the 1st place athlete has the highest
 * score, the 2nd place athlete has the second highest score, and so on. The placement of each
 * athlete determines their rank:</p>
 * <ul>
 *   <li>The 1st place athlete's rank is "Gold Medal".</li>
 *   <li>The 2nd place athlete's rank is "Silver Medal".</li>
 *   <li>The 3rd place athlete's rank is "Bronze Medal".</li>
 *   <li>For the 4th place to the nth place athlete, their rank is their placement number
 *   (i.e., the xth place athlete's rank is "x").</li>
 * </ul>
 *
 * <p>Return an array {@code answer} of size {@code n} where {@code answer[i]} is the rank of the
 * ith athlete.</p>
 *
 * <h2>Examples:</h2>
 * <pre>
 * Input:  score = [5, 4, 3, 2, 1]
 * Output: ["Gold Medal","Silver Medal","Bronze Medal","4","5"]
 *
 * Input:  score = [10, 3, 8, 9, 4]
 * Output: ["Gold Medal","5","Bronze Medal","Silver Medal","4"]
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>n == score.length</li>
 *   <li>1 <= n <= 10^4</li>
 *   <li>0 <= score[i] <= 10^6</li>
 *   <li>All the values in score are unique.</li>
 * </ul>
 *
 * @tags Heap (Priority Queue), Array, Sorting
 */
public class RelativeRanks {

    // -------------------------------------------------------------------------
    // Tier 2: Senior (Max-Heap for idiomatic heap-based rank assignment)
    // -------------------------------------------------------------------------

    // Time Complexity: O(N log N) — heap construction and N extractions
    // Space Complexity: O(N) — heap + result array
    public String[] findRelativeRanks(int[] score) {
        // Max-heap: stores [score, originalIndex], sorted by score descending
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        for (int i = 0; i < score.length; i++) {
            maxHeap.offer(new int[]{score[i], i});
        }

        String[] result = new String[score.length];
        int rank = 1;
        while (!maxHeap.isEmpty()) {
            int originalIndex = maxHeap.poll()[1];
            result[originalIndex] = switch (rank) {
                case 1 -> "Gold Medal";
                case 2 -> "Silver Medal";
                case 3 -> "Bronze Medal";
                default -> String.valueOf(rank);
            };
            rank++;
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // Tier 1: Intermediate (Sort with index tracking — intuitive approach)
    // -------------------------------------------------------------------------
    static class Intermediate {

        // Time Complexity: O(N log N) — sort dominates
        // Space Complexity: O(N) — index array + result
        public String[] findRelativeRanks(int[] score) {
            int n = score.length;
            Integer[] indices = new Integer[n];
            for (int i = 0; i < n; i++) indices[i] = i;

            // Sort indices by their corresponding score, descending
            Arrays.sort(indices, (a, b) -> score[b] - score[a]);

            String[] result = new String[n];
            for (int rank = 0; rank < n; rank++) {
                result[indices[rank]] = switch (rank) {
                    case 0 -> "Gold Medal";
                    case 1 -> "Silver Medal";
                    case 2 -> "Bronze Medal";
                    default -> String.valueOf(rank + 1);
                };
            }
            return result;
        }
    }

    /*
     * Tier 3: Staff Discussion
     * -------------------------
     * 1. Both tiers are O(N log N) — the bottleneck is ordering, unavoidable without
     *    additional constraints.
     * 2. If scores are bounded (e.g., 0..10^6), a counting/bucket sort reduces to O(N + M)
     *    where M is the score range — useful for very large N in competitive systems.
     * 3. In a distributed leaderboard (e.g., top-K global scores), you'd use a
     *    distributed merge of per-shard top-K lists, then assign medals centrally.
     * 4. The heap approach here is preferred in the heaps learning track because it
     *    reinforces the "extract-max" idiom used in Task Scheduler, K Closest, etc.
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        RelativeRanks solver = new RelativeRanks();

        int[] score1 = {5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(solver.findRelativeRanks(score1)));
        // Expected: [Gold Medal, Silver Medal, Bronze Medal, 4, 5]

        int[] score2 = {10, 3, 8, 9, 4};
        System.out.println(Arrays.toString(solver.findRelativeRanks(score2)));
        // Expected: [Gold Medal, 5, Bronze Medal, Silver Medal, 4]

        int[] score3 = {1};
        System.out.println(Arrays.toString(solver.findRelativeRanks(score3)));
        // Expected: [Gold Medal]
    }

    private static void runIntermediate() {
        Intermediate solver = new Intermediate();

        int[] score1 = {5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(solver.findRelativeRanks(score1)));

        int[] score2 = {10, 3, 8, 9, 4};
        System.out.println(Arrays.toString(solver.findRelativeRanks(score2)));
    }
}
