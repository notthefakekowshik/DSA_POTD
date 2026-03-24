# DSA Progress Journal

| S.No | Date | Problem | Difficulty | Pattern | Key Insight |
|------|------|---------|------------|---------|-------------|
| 1 | 2026-03-21 | Kth Largest Element in a Stream | Easy | Heap (Min-Heap) | Maintaining a min-heap of size K always keeps the Kth largest element at the root. |
| 2 | 2026-03-21 | Kth Largest Element in an Array | Medium | Heap / Quickselect | Quickselect offers O(N) average time vs O(N log K) for heap; critical for large N. |
| 3 | 2026-03-21 | Top K Frequent Elements | Medium | Heap + Map | Map for counts, then Min-Heap of size K to keep top frequencies. |
| 4 | 2026-03-21 | Find Median from Data Stream | Hard | Two Heaps | Max-heap for lower half, Min-heap for upper half; rebalance on every add. |
| --- | --- | --- | --- | --- | --- |
| 5 | 2026-03-22 | Linked List Cycle | Easy | Two Pointers | Floyd's Cycle detection (Tortoise and Hare) detects cycles in O(N) time and O(1) space. |
| 6 | 2026-03-22 | Reorder List | Medium | Two Pointers | 3-step: 1. Find Mid, 2. Reverse Second Half, 3. Interleave/Merge. |
| 7 | 2026-03-22 | Remove Nth Node From End of List | Medium | Two Pointers | Two-pointer delay of N nodes allows reaching the target in one pass. |
| 8 | 2026-03-22 | Merge k Sorted Lists | Hard | Divide and Conquer | Pairwise merging (bottom-up or recursive) is O(N log K) and more space-efficient than naive heap. |
| 9 | 2026-03-22 | Last Stone Weight | Easy | Max-Heap | Standard simulation using PriorityQueue; always pull the two largest stones. |
| 10 | 2026-03-22 | K Closest Points to Origin | Medium | Max-Heap / Quickselect | Use Max-Heap of size K to keep the K smallest distances; O(N log K). |
| 11 | 2026-03-22 | Task Scheduler | Medium | Greedy + Heap | Calculating idle slots based on max-frequency task: `(max_freq-1)*(n+1) + num_max_freq_tasks`. |
| 12 | 2026-03-22 | Smallest Range Covering K Lists | Hard | Min-Heap + N-Pointer | Track min/max across K sorted lists; pop min, update max, push next from the same list. |
| --- | --- | --- | --- | --- | --- |
| 13 | 2026-03-23 | Reverse Linked List | Easy | Linked List | Stack uses O(N) space; iterative prev/curr pointer dance achieves O(1) — JVM has no TCO so recursive is strictly worse. |
| 14 | 2026-03-23 | Merge Two Sorted Lists | Easy | Linked List | Dummy head eliminates first-node branch; recursive is elegant but O(M+N) call-stack — iterative splices existing nodes at O(1). |
| 15 | 2026-03-23 | Add Two Numbers | Medium | Linked List, Math | Digits stored in reverse = carry flows naturally left-to-right; `|| carry != 0` in loop condition handles the final overflow digit. |
| 16 | 2026-03-23 | Intersection of Two Linked Lists | Easy | Two Pointers | Two-pointer alignment trick (swapping heads) equalizes the distance to intersection; O(1) space. |
| 17 | 2026-03-23 | Copy List with Random Pointer | Medium | Linked List | In-place interweaving (A->A'->B->B') allows deep copy with O(1) extra space by leveraging the existing structure. |
| 18 | 2026-03-23 | Sort List | Medium | Merge Sort | Bottom-up merge sort (iterative with step 1, 2, 4...) is the only way to achieve O(1) space sorting on lists. |
