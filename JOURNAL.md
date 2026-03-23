# DSA Progress Journal

| Date | Problem | Difficulty | Pattern | Key Insight |
|------|---------|------------|---------|-------------|
| 2026-03-21 | Kth Largest Element in a Stream | Easy | Heap (Min-Heap) | |
| 2026-03-21 | Kth Largest Element in an Array | Medium | Heap / Quickselect | |
| 2026-03-21 | Top K Frequent Elements | Medium | Heap + Map | |
| 2026-03-21 | Find Median from Data Stream | Hard | Two Heaps | |
| --- | --- | --- | --- | --- |
| 2026-03-22 | Linked List Cycle | Easy | Two Pointers | Floyd's Cycle detection |
| 2026-03-22 | Reorder List | Medium | Two Pointers | Middle find + Reverse + Merge |
| 2026-03-22 | Remove Nth Node From End of List | Medium | Two Pointers | Two pointer spacing by N |
| 2026-03-22 | Merge k Sorted Lists | Hard | Divide and Conquer | Pairwise merging is O(N log K) |
| 2026-03-22 | Last Stone Weight | Easy | Max-Heap | Standard simulation using PriorityQueue |
| 2026-03-22 | K Closest Points to Origin | Medium | Max-Heap / Quickselect | Use Max-Heap of size K for O(N log K) |
| 2026-03-22 | Task Scheduler | Medium | Greedy + Heap | Math formula vs Simulation |
| 2026-03-22 | Smallest Range Covering K Lists | Hard | Min-Heap + N-Pointer | Track min/max across K sorted lists |
| --- | --- | --- | --- | --- |
| 2026-03-23 | Reverse Linked List | Easy | Linked List | Stack uses O(N) space; iterative prev/curr pointer dance achieves O(1) — JVM has no TCO so recursive is strictly worse |
| 2026-03-23 | Merge Two Sorted Lists | Easy | Linked List | Dummy head eliminates first-node branch; recursive is elegant but O(M+N) call-stack — iterative splices existing nodes at O(1) |
| 2026-03-23 | Add Two Numbers | Medium | Linked List, Math | Digits stored in reverse = carry flows naturally left-to-right; `|| carry != 0` in loop condition handles the final overflow digit |
