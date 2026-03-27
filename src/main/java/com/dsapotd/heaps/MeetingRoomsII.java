package com.dsapotd.heaps;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * <h1>253. Meeting Rooms II</h1>
 *
 * <p>Given an array of meeting time intervals <code>intervals</code> where
 * <code>intervals[i] = [start<sub>i</sub>, end<sub>i</sub>]</code>, return the minimum number of
 * conference rooms required.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: intervals = [[0,30],[5,10],[15,20]]
 * Output: 2
 * Explanation:
 * - Meeting 1: [0, 30]
 * - Meeting 2: [5, 10] overlaps with Meeting 1, needs a second room
 * - Meeting 3: [15, 20] overlaps with Meeting 1, can use the room freed by Meeting 2
 * Maximum rooms needed at any time: 2
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: intervals = [[7,10],[2,4]]
 * Output: 1
 * Explanation: The two meetings don't overlap, so only 1 room is needed.
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= intervals.length <= 10<sup>4</sup></li>
 *   <li>0 <= start<sub>i</sub> < end<sub>i</sub> <= 10<sup>6</sup></li>
 * </ul>
 *
 * @tags Array, Two Pointers, Greedy, Sorting, Heap (Priority Queue), Prefix Sum
 */
public class MeetingRoomsII {

    // Tier 2: Senior (Min-Heap for End Times)
    // Time Complexity: O(N log N) for sorting + heap operations
    // Space Complexity: O(N) for heap
    public int minMeetingRooms(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return 0;
        }
        
        // Sort meetings by start time
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        // Min-heap to track end times of ongoing meetings
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();
        
        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];
            
            // If earliest ending meeting finishes before this one starts, reuse the room
            if (!endTimes.isEmpty() && endTimes.peek() <= start) {
                endTimes.poll();
            }
            
            // Allocate room for current meeting
            endTimes.offer(end);
        }
        
        // Heap size = number of rooms needed
        return endTimes.size();
    }

    /**
     * Tier 1: Mid-Level (Two Separate Arrays - Chronological Ordering)
     * Time Complexity: O(N log N) for sorting
     * Space Complexity: O(N) for separate arrays
     */
    static class Intermediate {
        public int minMeetingRooms(int[][] intervals) {
            if (intervals == null || intervals.length == 0) {
                return 0;
            }
            
            int n = intervals.length;
            int[] starts = new int[n];
            int[] ends = new int[n];
            
            for (int i = 0; i < n; i++) {
                starts[i] = intervals[i][0];
                ends[i] = intervals[i][1];
            }
            
            Arrays.sort(starts);
            Arrays.sort(ends);
            
            int rooms = 0;
            int maxRooms = 0;
            int endPtr = 0;
            
            for (int start : starts) {
                // If a meeting starts before the earliest ending meeting finishes
                if (start < ends[endPtr]) {
                    rooms++; // Need a new room
                } else {
                    endPtr++; // Reuse a room
                }
                maxRooms = Math.max(maxRooms, rooms);
            }
            
            return maxRooms;
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Why Min-Heap?**
     *    - We only care about the earliest ending meeting (to check if we can reuse a room)
     *    - Min-heap gives O(1) access to minimum, O(log N) insertion/deletion
     *    - Heap size at any point = number of concurrent meetings = rooms needed
     * 
     * 2. **Two-Array Approach (Tier 1) Insight:**
     *    - Separating starts and ends, then sorting both
     *    - Process chronologically: each start increments rooms, each end decrements
     *    - This is a "sweep line" algorithm - imagine a vertical line sweeping left to right
     *    - At any point, rooms = (starts processed) - (ends processed)
     *    - Same O(N log N) complexity but avoids heap overhead
     * 
     * 3. **When to Use Which Approach:**
     *    - Heap: More intuitive, easier to extend (e.g., track which room is used)
     *    - Two-array: Slightly better constant factors, pure mathematical elegance
     *    - For interviews: Heap approach is safer and more extensible
     * 
     * 4. **Extension: Assign Specific Room Numbers:**
     *    - Modify heap to store (endTime, roomId) pairs
     *    - When reusing, track which room is freed
     *    - Build a mapping: meeting -> room assignment
     *    - Useful for real scheduling systems
     * 
     * 5. **Distributed System Considerations:**
     *    - For millions of meetings across data centers:
     *      a) Partition by time ranges (e.g., by day)
     *      b) Each partition runs algorithm independently
     *      c) Global coordinator aggregates peak requirements
     *    - Challenge: Meetings spanning partition boundaries
     *    - Solution: Overlap windows between partitions
     * 
     * 6. **Real-Time Streaming Scenario:**
     *    - Meetings arrive in real-time, not all at once
     *    - Maintain heap of active meetings (those not yet ended)
     *    - On new meeting arrival:
     *      a) Remove all meetings that ended before current time
     *      b) Add new meeting to heap
     *      c) Track max heap size seen so far
     *    - This is an online algorithm variant
     * 
     * 7. **Optimization: Early Termination:**
     *    - If at any point heap.size() == intervals.length, all meetings overlap
     *    - Can't do better than N rooms, return early
     *    - Rare in practice but good for worst-case inputs
     * 
     * 8. **Connection to Other Problems:**
     *    - **Interval Scheduling Maximization:** Greedy by end time
     *    - **Merge Intervals:** Sort and merge overlapping intervals
     *    - **Employee Free Time:** Invert this problem (find gaps)
     *    - All use similar sorting + sweep line / heap patterns
     * 
     * 9. **Cache-Line Optimization:**
     *    - Heap operations have poor cache locality (tree structure)
     *    - For small N (< 100), linear scan for minimum might be faster
     *    - Trade-off: O(N²) worst case vs O(N log N) with poor cache
     *    - Profile on target hardware to determine crossover point
     * 
     * 10. **Follow-up: What if meetings have priorities?**
     *     - Higher priority meetings can preempt lower priority
     *     - Use max-heap by (priority, endTime)
     *     - On conflict, evict lowest priority meeting
     *     - Track evicted meetings for rescheduling
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Min-Heap):");
        MeetingRoomsII solver = new MeetingRoomsII();
        
        // Test case 1
        int[][] intervals1 = {{0, 30}, {5, 10}, {15, 20}};
        System.out.println("Test 1: " + solver.minMeetingRooms(intervals1)); // Expected: 2
        
        // Test case 2
        int[][] intervals2 = {{7, 10}, {2, 4}};
        System.out.println("Test 2: " + solver.minMeetingRooms(intervals2)); // Expected: 1
        
        // Test case 3: All meetings overlap
        int[][] intervals3 = {{1, 5}, {2, 6}, {3, 7}, {4, 8}};
        System.out.println("Test 3: " + solver.minMeetingRooms(intervals3)); // Expected: 4
        
        // Test case 4: No overlap
        int[][] intervals4 = {{1, 2}, {3, 4}, {5, 6}};
        System.out.println("Test 4: " + solver.minMeetingRooms(intervals4)); // Expected: 1
        
        // Test case 5: Back-to-back meetings (can reuse room)
        int[][] intervals5 = {{1, 5}, {5, 10}, {10, 15}};
        System.out.println("Test 5: " + solver.minMeetingRooms(intervals5)); // Expected: 1
        
        // Test case 6: Complex overlap
        int[][] intervals6 = {{0, 30}, {5, 10}, {15, 20}, {20, 25}};
        System.out.println("Test 6: " + solver.minMeetingRooms(intervals6)); // Expected: 2
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Two Arrays):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        int[][] intervals1 = {{0, 30}, {5, 10}, {15, 20}};
        System.out.println("Test 1: " + intermediate.minMeetingRooms(intervals1)); // Expected: 2
        
        // Test case 2
        int[][] intervals2 = {{7, 10}, {2, 4}};
        System.out.println("Test 2: " + intermediate.minMeetingRooms(intervals2)); // Expected: 1
        
        // Test case 3
        int[][] intervals3 = {{1, 5}, {2, 6}, {3, 7}, {4, 8}};
        System.out.println("Test 3: " + intermediate.minMeetingRooms(intervals3)); // Expected: 4
        
        // Test case 4
        int[][] intervals4 = {{1, 2}, {3, 4}, {5, 6}};
        System.out.println("Test 4: " + intermediate.minMeetingRooms(intervals4)); // Expected: 1
        
        // Test case 5
        int[][] intervals5 = {{1, 5}, {5, 10}, {10, 15}};
        System.out.println("Test 5: " + intermediate.minMeetingRooms(intervals5)); // Expected: 1
        
        // Test case 6
        int[][] intervals6 = {{0, 30}, {5, 10}, {15, 20}, {20, 25}};
        System.out.println("Test 6: " + intermediate.minMeetingRooms(intervals6)); // Expected: 2
    }
}
