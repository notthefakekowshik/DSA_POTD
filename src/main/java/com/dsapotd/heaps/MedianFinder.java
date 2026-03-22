package com.dsapotd.heaps;

/**
 * <h1>295. Find Median from Data Stream</h1>
 *
 * <p>
 * The <strong>median</strong> is the middle value in an ordered integer list.
 * If the size of the list is even, there is no middle value, and the median is
 * the mean of the two middle values.
 * </p>
 *
 * <ul>
 * <li>For example, for <code>arr = [2,3,4]</code>, the median is
 * <code>3</code>.</li>
 * <li>For example, for <code>arr = [2,3]</code>, the median is
 * <code>(2 + 3) / 2 = 2.5</code>.</li>
 * </ul>
 *
 * <p>
 * Implement the <code>MedianFinder</code> class:
 * </p>
 * <ul>
 * <li><code>MedianFinder()</code> initializes the <code>MedianFinder</code>
 * object.</li>
 * <li><code>void addNum(int num)</code> adds the integer <code>num</code> from
 * the data stream to the data structure.</li>
 * <li><code>double findMedian()</code> returns the median of all elements so
 * far. Answers within 10^-5 of the actual answer will be accepted.</li>
 * </ul>
 *
 * <h2>Example 1:</h2>
 * 
 * <pre>
 * Input
 * ["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
 * [[], [1], [2], [], [3], []]
 * Output
 * [null, null, null, 1.5, null, 2.0]
 *
 * Explanation
 * MedianFinder medianFinder = new MedianFinder();
 * medianFinder.addNum(1);    // arr = [1]
 * medianFinder.addNum(2);    // arr = [1, 2]
 * medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
 * medianFinder.addNum(3);    // arr[1, 2, 3]
 * medianFinder.findMedian(); // return 2.0
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 * <li>-10^5 <= num <= 10^5</li>
 * <li>At most 5 * 10^4 calls will be made to addNum and findMedian.</li>
 * </ul>
 *
 * @tags Heap (Priority Queue), Two Heaps, Design, Data Stream
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class MedianFinder {
    private final PriorityQueue<Integer> small; // Max-Heap
    private final PriorityQueue<Integer> large; // Min-Heap

    public MedianFinder() {
        small = new PriorityQueue<>(Collections.reverseOrder());
        large = new PriorityQueue<>();
    }

    // Optimized Solution (Two Heaps)
    // Time Complexity: O(log N) to add and balance heaps
    // Space Complexity: O(N) to store all elements
    public void addNum(int num) {
        if (small.isEmpty() || num <= small.peek()) {
            small.offer(num);
        } else {
            large.offer(num);
        }

        // Balance the heaps
        if (small.size() > large.size() + 1) {
            large.offer(small.poll());
        } else if (large.size() > small.size()) {
            small.offer(large.poll());
        }
    }

    // Time Complexity: O(1)
    // Space Complexity: O(1)
    public double findMedian() {
        if (small.size() == large.size()) {
            return (small.peek() + large.peek()) / 2.0;
        } else {
            return small.peek();
        }
    }

    /**
     * Tier 1: Mid-Level (Brute Force)
     * Time Complexity: O(N log N) per add
     * Space Complexity: O(N)
     */
    static class Intermediate {
        private final List<Integer> allNums = new ArrayList<>();

        public void addNum(int num) {
            allNums.add(num);
            Collections.sort(allNums);
        }

        public double findMedian() {
            int n = allNums.size();
            if (n % 2 == 0) {
                return (allNums.get(n / 2 - 1) + allNums.get(n / 2)) / 2.0;
            } else {
                return allNums.get(n / 2);
            }
        }
    }

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Two Heaps):");
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println("Median after [1, 2]: " + medianFinder.findMedian()); // Output: 1.5
        medianFinder.addNum(3);
        System.out.println("Median after [1, 2, 3]: " + medianFinder.findMedian()); // Output: 2.0
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Brute Force):");
        Intermediate intermediate = new Intermediate();
        intermediate.addNum(1);
        intermediate.addNum(2);
        System.out.println("Median after [1, 2]: " + intermediate.findMedian()); // Output: 1.5
        intermediate.addNum(3);
        System.out.println("Median after [1, 2, 3]: " + intermediate.findMedian()); // Output: 2.0
    }
}
