package com.dsapotd.heaps;

import java.util.PriorityQueue;
import java.util.Arrays;

/**
 * <h2>973. K Closest Points to Origin</h2>
 *
 * <p>Given an array of points where points[i] = [xi, yi] and an integer k, return the k closest points to the origin (0, 0).</p>
 * <p>The distance between two points on the X-Y plane is the Euclidean distance (√(x1 - x2)² + (y1 - y2)²).</p>
 * <p>You may return the answer in any order. The answer is guaranteed to be unique (except for the order that it is in).</p>
 *
 * <h3>Example 1:</h3>
 * <pre>
 * Input: points = [[1,3],[-2,2]], k = 1
 * Output: [[-2,2]]
 * Explanation:
 * The distance from (1, 3) to the origin is sqrt(1^2 + 3^2) = sqrt(10).
 * The distance from (-2, 2) to the origin is sqrt((-2)^2 + 2^2) = sqrt(8).
 * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
 * We only want the closest k = 1 points from the origin, so the answer is just [[-2,2]].
 * </pre>
 *
 * <h3>Constraints:</h3>
 * <ul>
 *   <li>1 <= k <= points.length <= 10,000</li>
 *   <li>-10,000 <= xi, yi <= 10,000</li>
 * </ul>
 *
 * @tags Heap, Math, Sorting
 */
public class KClosestPointsToOrigin {

    /**
     * Tier 1: Intermediate (Max-Heap of size K)
     * Time Complexity: O(N log K)
     * Space Complexity: O(K)
     */
    public static class Intermediate {
        public int[][] kClosest(int[][] points, int k) {
            // Max-Heap: stores K closest points. When full, removes the farthest of the K.
            PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> 
                (b[0] * b[0] + b[1] * b[1]) - (a[0] * a[0] + a[1] * a[1])
            );

            for (int[] point : points) {
                maxHeap.offer(point);
                if (maxHeap.size() > k) {
                    maxHeap.poll();
                }
            }

            int[][] result = new int[k][2];
            while (k > 0) {
                result[--k] = maxHeap.poll();
            }
            return result;
        }
    }

    /**
     * Tier 2: Senior (Quickselect Approach)
     * Time Complexity: O(N) average, O(N^2) worst case.
     * Space Complexity: O(1) (excluding output array)
     * Goal: Partition points around a pivot until the first K elements are the K closest.
     */
    public static class Optimized {
        public int[][] kClosest(int[][] points, int k) {
            int left = 0, right = points.length - 1;
            while (left <= right) {
                int pivotIndex = partition(points, left, right);
                if (pivotIndex == k) break;
                if (pivotIndex < k) left = pivotIndex + 1;
                else right = pivotIndex - 1;
            }
            return Arrays.copyOfRange(points, 0, k);
        }

        private int partition(int[][] points, int left, int right) {
            int[] pivot = points[right];
            int pivotDist = dist(pivot);
            int i = left;
            for (int j = left; j < right; j++) {
                if (dist(points[j]) <= pivotDist) {
                    swap(points, i++, j);
                }
            }
            swap(points, i, right);
            return i;
        }

        private int dist(int[] p) {
            return p[0] * p[0] + p[1] * p[1];
        }

        private void swap(int[][] points, int i, int j) {
            int[] temp = points[i];
            points[i] = points[j];
            points[j] = temp;
        }
    }

    public static void main(String[] args) {
        int[][] points = {{3, 3}, {5, -1}, {-2, 4}};
        int k = 2;

        // Toggle solution
        // Intermediate solver = new Intermediate();
        Optimized solver = new Optimized();

        int[][] result = solver.kClosest(points, k);
        System.out.println("K Closest Points: " + Arrays.deepToString(result));
    }
}
