package com.dsapotd.heaps;

import java.util.PriorityQueue;
import java.util.Collections;

/**
 * <h2>1046. Last Stone Weight</h2>
 *
 * <p>You are given an array of integers stones where stones[i] is the weight of the ith stone.</p>
 * <p>We are playing a game with the stones. On each turn, we choose the heaviest two stones and smash them together. Suppose the heaviest two stones have weights x and y with x <= y. The result of this smash is:</p>
 * <ul>
 *   <li>If x == y, both stones are destroyed.</li>
 *   <li>If x != y, the stone of weight x is destroyed, and the stone of weight y has new weight y - x.</li>
 * </ul>
 * <p>At the end of the game, there is at most one stone left. Return the weight of the last remaining stone. If there are no stones left, return 0.</p>
 *
 * <h3>Example 1:</h3>
 * <pre>
 * Input: stones = [2,7,4,1,8,1]
 * Output: 1
 * Explanation:
 * We combine 7 and 8 to get 1 so the array converts to [2,4,1,1,1] then,
 * we combine 2 and 4 to get 2 so the array converts to [2,1,1,1] then,
 * we combine 2 and 1 to get 1 so the array converts to [1,1,1] then,
 * we combine 1 and 1 to get 0 so the array converts to [1] then that's the value of the last stone.
 * </pre>
 *
 * <h3>Constraints:</h3>
 * <ul>
 *   <li>1 <= stones.length <= 30</li>
 *   <li>1 <= stones[i] <= 1000</li>
 * </ul>
 *
 * @tags Heap, Simulation, Greedy
 */
public class LastStoneWeight {

    /**
     * Tier 1: Intermediate (Basic Heap Simulation)
     * Time Complexity: O(N log N) - Each extraction and insertion takes log N.
     * Space Complexity: O(N) - To store stones in the heap.
     */
    public static class Intermediate {
        public int lastStoneWeight(int[] stones) {
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            for (int stone : stones) {
                maxHeap.offer(stone);
            }

            while (maxHeap.size() > 1) {
                int stone1 = maxHeap.poll();
                int stone2 = maxHeap.poll();
                if (stone1 != stone2) {
                    maxHeap.offer(stone1 - stone2);
                }
            }

            return maxHeap.isEmpty() ? 0 : maxHeap.poll();
        }
    }

    /**
     * Tier 2: Senior (In-place or optimized if possible)
     * Note: For N=30, the Heap is already optimal. 
     * However, we can discuss Bucket Sort if weights were small.
     * Time Complexity: O(N log N)
     * Space Complexity: O(N)
     */
    public static class Optimized {
        public int lastStoneWeight(int[] stones) {
            // Using PriorityQueue is the standard "Senior" approach for this problem.
            // We'll use the same logic but encapsulated for Tier 2.
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
            for (int stone : stones) {
                maxHeap.offer(stone);
            }

            while (maxHeap.size() > 1) {
                int first = maxHeap.poll();
                int second = maxHeap.poll();
                if (first > second) {
                    maxHeap.offer(first - second);
                }
            }
            return maxHeap.isEmpty() ? 0 : maxHeap.peek();
        }
    }

    public static void main(String[] args) {
        int[] stones = {2, 7, 4, 1, 8, 1};
        
        // Toggle solution here
        // Intermediate solver = new Intermediate();
        Optimized solver = new Optimized();
        
        System.out.println("Last Stone Weight: " + solver.lastStoneWeight(stones));
    }
}
