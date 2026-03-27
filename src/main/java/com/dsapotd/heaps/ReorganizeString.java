package com.dsapotd.heaps;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <h1>767. Reorganize String</h1>
 *
 * <p>Given a string <code>s</code>, rearrange the characters of <code>s</code> so that any two adjacent
 * characters are not the same.</p>
 *
 * <p>Return any possible rearrangement of <code>s</code> or return <code>""</code> if not possible.</p>
 *
 * <h2>Example 1:</h2>
 * <pre>
 * Input: s = "aab"
 * Output: "aba"
 * </pre>
 *
 * <h2>Example 2:</h2>
 * <pre>
 * Input: s = "aaab"
 * Output: ""
 * Explanation: It's impossible to rearrange the string so no two adjacent characters are the same.
 * </pre>
 *
 * <h2>Constraints:</h2>
 * <ul>
 *   <li>1 <= s.length <= 500</li>
 *   <li>s consists of lowercase English letters.</li>
 * </ul>
 *
 * @tags Hash Table, String, Greedy, Sorting, Heap (Priority Queue), Counting
 */
public class ReorganizeString {

    // Tier 2: Senior (Max-Heap Greedy)
    // Time Complexity: O(N log K) where N is string length, K is unique characters (at most 26)
    // Space Complexity: O(K) for heap and frequency map
    public String reorganizeString(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        
        // Count character frequencies
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        
        // Max-heap by frequency
        PriorityQueue<Map.Entry<Character, Integer>> maxHeap = 
            new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        maxHeap.addAll(freq.entrySet());
        
        // Early termination: if max frequency > (n + 1) / 2, impossible
        if (maxHeap.peek().getValue() > (s.length() + 1) / 2) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        Map.Entry<Character, Integer> prev = null;
        
        while (!maxHeap.isEmpty()) {
            // Get most frequent character
            Map.Entry<Character, Integer> curr = maxHeap.poll();
            result.append(curr.getKey());
            curr.setValue(curr.getValue() - 1);
            
            // Add back previous character if it still has remaining count
            if (prev != null && prev.getValue() > 0) {
                maxHeap.offer(prev);
            }
            
            prev = curr;
        }
        
        return result.length() == s.length() ? result.toString() : "";
    }

    /**
     * Tier 1: Mid-Level (Greedy with Array Sorting)
     * Time Complexity: O(N log N) due to repeated sorting
     * Space Complexity: O(K) for frequency array
     */
    static class Intermediate {
        public String reorganizeString(String s) {
            if (s == null || s.length() <= 1) {
                return s;
            }
            
            // Count frequencies
            int[] freq = new int[26];
            for (char c : s.toCharArray()) {
                freq[c - 'a']++;
            }
            
            // Find max frequency
            int maxFreq = 0;
            for (int f : freq) {
                maxFreq = Math.max(maxFreq, f);
            }
            
            // Early check: impossible if max frequency too high
            if (maxFreq > (s.length() + 1) / 2) {
                return "";
            }
            
            StringBuilder result = new StringBuilder();
            char lastChar = '\0';
            
            while (result.length() < s.length()) {
                // Find most frequent character that's different from last
                int maxIdx = -1;
                int maxCount = 0;
                
                for (int i = 0; i < 26; i++) {
                    if (freq[i] > maxCount && (char)('a' + i) != lastChar) {
                        maxCount = freq[i];
                        maxIdx = i;
                    }
                }
                
                if (maxIdx == -1) {
                    return ""; // No valid character found
                }
                
                char ch = (char)('a' + maxIdx);
                result.append(ch);
                freq[maxIdx]--;
                lastChar = ch;
            }
            
            return result.toString();
        }
    }

    /**
     * Tier 3: Staff (Deep Internals & Scalability)
     * 
     * Discussion:
     * 1. **Greedy Correctness Proof:**
     *    - Always pick the most frequent remaining character (that's different from previous)
     *    - Why? If we don't use the most frequent character, it accumulates and might become impossible later
     *    - Mathematical proof: If max_freq <= (n+1)/2, a valid arrangement always exists
     *    - Proof: Place max_freq character at even indices (0, 2, 4...), others fill remaining
     *    - This guarantees no two adjacent characters are the same
     * 
     * 2. **Why Max-Heap?**
     *    - Need to repeatedly find most frequent character: O(log K) with heap
     *    - Alternative: Linear scan through frequency array: O(K) per iteration
     *    - For K=26 (fixed alphabet), linear scan might be faster due to cache locality
     *    - Heap approach generalizes better to larger alphabets
     * 
     * 3. **Alternative: Fill Even Positions First:**
     *    - Sort characters by frequency
     *    - Place most frequent at positions 0, 2, 4, ..., n-1
     *    - Then fill positions 1, 3, 5, ..., n-2 with remaining characters
     *    - Same O(N log K) complexity but simpler logic
     *    - Trade-off: Less flexible for extensions (e.g., k-distance apart)
     * 
     * 4. **Extension: K-Distance Apart:**
     *    - Problem variant: No two same characters within distance K
     *    - Solution: Maintain a queue of size K for "cooldown" characters
     *    - After using a character, add to queue; only re-add to heap after K steps
     *    - Similar to Task Scheduler problem
     * 
     * 5. **Distributed System Considerations:**
     *    - For massive strings (GB-scale):
     *      a) First pass: Count frequencies in parallel (MapReduce)
     *      b) Coordinator runs greedy algorithm (sequential, but small state)
     *      c) Result is O(N) size, can't be parallelized further
     *    - Bottleneck: Final assembly is inherently sequential
     * 
     * 6. **Memory Optimization:**
     *    - Current approach: O(K) for heap + map
     *    - Can use fixed-size array[26] instead of HashMap for lowercase letters
     *    - Heap can store (char, count) as primitive arrays instead of Map.Entry
     *    - Reduces GC pressure and memory overhead
     * 
     * 7. **Real-World Applications:**
     *    - CPU task scheduling (avoid consecutive same tasks for cache efficiency)
     *    - Network packet scheduling (avoid burst from same source)
     *    - Playlist generation (avoid consecutive songs from same artist)
     *    - DNA sequence design (avoid repeating nucleotides)
     * 
     * 8. **Connection to Other Problems:**
     *    - **Task Scheduler:** Same greedy + heap pattern with cooldown
     *    - **Rearrange String k Distance Apart:** Direct generalization
     *    - **Valid Parentheses String:** Different constraint but similar greedy approach
     * 
     * 9. **Edge Cases:**
     *    - Empty string: Return ""
     *    - Single character: Return as-is
     *    - All same character: Return "" if length > 1
     *    - Two characters alternating: Always possible if counts differ by at most 1
     * 
     * 10. **Optimization: Early Termination:**
     *     - Check max_freq > (n+1)/2 before building heap
     *     - Saves heap construction time for impossible cases
     *     - Critical for adversarial inputs (e.g., "aaaaaab")
     */

    public static void main(String[] args) {
        // --- ONE CLICK TOGGLE ---
        // runIntermediate();
        runOptimized();
    }

    private static void runOptimized() {
        System.out.println("Running Optimized (Max-Heap Greedy):");
        ReorganizeString solver = new ReorganizeString();
        
        // Test case 1
        System.out.println("Test 1: " + solver.reorganizeString("aab")); // Expected: "aba" or "baa"
        
        // Test case 2: Impossible
        System.out.println("Test 2: " + solver.reorganizeString("aaab")); // Expected: ""
        
        // Test case 3: All different
        System.out.println("Test 3: " + solver.reorganizeString("abc")); // Expected: any permutation
        
        // Test case 4: Complex case
        System.out.println("Test 4: " + solver.reorganizeString("aaabbc")); // Expected: "ababac" or similar
        
        // Test case 5: Single character
        System.out.println("Test 5: " + solver.reorganizeString("a")); // Expected: "a"
        
        // Test case 6: Two characters
        System.out.println("Test 6: " + solver.reorganizeString("ab")); // Expected: "ab" or "ba"
        
        // Test case 7: Long string with repeats
        System.out.println("Test 7: " + solver.reorganizeString("vvvlo")); // Expected: "vovlv" or similar
        
        // Test case 8: Edge of impossible
        System.out.println("Test 8: " + solver.reorganizeString("aaab")); // Expected: ""
        System.out.println("Test 9: " + solver.reorganizeString("aab")); // Expected: valid rearrangement
    }

    private static void runIntermediate() {
        System.out.println("Running Intermediate (Greedy Array):");
        Intermediate intermediate = new Intermediate();
        
        // Test case 1
        System.out.println("Test 1: " + intermediate.reorganizeString("aab"));
        
        // Test case 2
        System.out.println("Test 2: " + intermediate.reorganizeString("aaab"));
        
        // Test case 3
        System.out.println("Test 3: " + intermediate.reorganizeString("abc"));
        
        // Test case 4
        System.out.println("Test 4: " + intermediate.reorganizeString("aaabbc"));
        
        // Test case 5
        System.out.println("Test 5: " + intermediate.reorganizeString("a"));
        
        // Test case 6
        System.out.println("Test 6: " + intermediate.reorganizeString("ab"));
        
        // Test case 7
        System.out.println("Test 7: " + intermediate.reorganizeString("vvvlo"));
        
        // Test case 8
        System.out.println("Test 8: " + intermediate.reorganizeString("aaab"));
        System.out.println("Test 9: " + intermediate.reorganizeString("aab"));
    }
}
