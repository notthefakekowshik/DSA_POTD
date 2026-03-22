package com.dsapotd.heaps;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

/**
 * <h2>621. Task Scheduler</h2>
 *
 * <p>Given a characters array tasks, representing the tasks a CPU needs to do, where each letter represents a different task. Tasks could be done in any order. Each task is done in one unit of time. For each unit of time, the CPU could complete either one task or just be idle.</p>
 * <p>However, there is a non-negative integer n that represents the cooldown period between two <b>same tasks</b> (with the same letter), that is that there must be at least n units of time between any two same tasks.</p>
 * <p>Return the least number of units of time that the CPU will take to finish all the given tasks.</p>
 *
 * <h3>Example 1:</h3>
 * <pre>
 * Input: tasks = ["A","A","A","B","B","B"], n = 2
 * Output: 8
 * Explanation:
 * A -> B -> idle -> A -> B -> idle -> A -> B
 * There is at least 2 units of time between any two same tasks.
 * </pre>
 *
 * <h3>Constraints:</h3>
 * <ul>
 *   <li>1 <= tasks.length <= 10^4</li>
 *   <li>tasks[i] is upper-case English letter.</li>
 *   <li>0 <= n <= 100</li>
 * </ul>
 *
 * @tags Heap, Greedy, Simulation
 */
public class TaskScheduler {

    /**
     * Tier 1: Intermediate (Heap + Queue Simulation)
     * Time Complexity: O(T * log 26) where T is total time. Effectively O(T).
     * Space Complexity: O(1) - alphabet size is constant.
     */
    public static class Intermediate {
        public int leastInterval(char[] tasks, int n) {
            int[] counts = new int[26];
            for (char c : tasks) counts[c - 'A']++;

            PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
            for (int count : counts) {
                if (count > 0) maxHeap.offer(count);
            }

            Queue<int[]> queue = new LinkedList<>(); // {count, availableAtTime}
            int time = 0;

            while (!maxHeap.isEmpty() || !queue.isEmpty()) {
                time++;

                if (!maxHeap.isEmpty()) {
                    int count = maxHeap.poll() - 1;
                    if (count > 0) {
                        queue.offer(new int[]{count, time + n});
                    }
                }

                if (!queue.isEmpty() && queue.peek()[1] == time) {
                    maxHeap.offer(queue.poll()[0]);
                }
            }

            return time;
        }
    }

    /**
     * Tier 2: Senior (Mathematical Approach)
     * Goal: Calculate the minimum time without simulation.
     * Pattern: (Max Frequency - 1) * (n + 1) + (Number of tasks with Max Frequency)
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    public static class Optimized {
        public int leastInterval(char[] tasks, int n) {
            int[] counts = new int[26];
            int maxFreq = 0;
            for (char c : tasks) {
                counts[c - 'A']++;
                maxFreq = Math.max(maxFreq, counts[c - 'A']);
            }

            int maxFreqCount = 0;
            for (int count : counts) {
                if (count == maxFreq) maxFreqCount++;
            }

            int partCount = maxFreq - 1;
            int partLength = n - (maxFreqCount - 1);
            int emptySlots = partCount * partLength;
            int availableTasks = tasks.length - (maxFreq * maxFreqCount);
            int idles = Math.max(0, emptySlots - availableTasks);

            return tasks.length + idles;
        }
    }

    public static void main(String[] args) {
        char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n = 2;

        // Toggle solution
        // Intermediate solver = new Intermediate();
        Optimized solver = new Optimized();

        System.out.println("Least Interval: " + solver.leastInterval(tasks, n));
    }
}
