# Pattern Recognition Guide

This document serves as a mental index for recognizing which pattern to apply when encountering a new problem.

---

## Heaps (Priority Queue)

**When to use:**
- Kth largest/smallest element in a stream or array
- Top K elements (most/least frequent, closest, etc.)
- Merge K sorted structures (lists, arrays)
- Running median or percentile calculations
- Scheduling problems with priorities

**Key Recognition Triggers:**
- Problem mentions "Kth largest" or "Kth smallest"
- Need to maintain top/bottom K elements efficiently
- Merging multiple sorted sequences
- Need quick access to min/max while adding elements

**Common Patterns:**
- **Min-Heap for Kth Largest:** Keep K largest elements, root is Kth largest
- **Max-Heap for Kth Smallest:** Keep K smallest elements, root is Kth smallest
- **Two Heaps (Max + Min):** For running median (small half in max-heap, large half in min-heap)
- **Min-Heap for Merging:** Track smallest unprocessed element across K lists

**Time Complexity:** O(log N) for insert/delete, O(1) for peek

**Problems Solved:**
- Kth Largest Element in a Stream
- Kth Largest Element in an Array
- Top K Frequent Elements
- Find Median from Data Stream
- Last Stone Weight
- K Closest Points to Origin
- Task Scheduler
- Smallest Range Covering K Lists

---

## Linked Lists

**When to use:**
- In-place manipulation with O(1) space required
- Problems involving cycles or loops
- Reversing sequences
- Merging sorted sequences

**Key Recognition Triggers:**
- Problem explicitly uses linked list structure
- Need to reverse a list or sublist
- Detect or find cycle
- Remove nodes based on position
- Merge multiple sorted lists

**Common Patterns:**
- **Two Pointers (Fast/Slow):** Cycle detection, finding middle, Nth from end
- **Reverse in Place:** Iterative reversal using prev/curr/next pointers
- **Dummy Node:** Simplifies edge cases when head might change
- **Runner Technique:** Fast pointer moves 2x speed of slow pointer

**Time Complexity:** O(N) traversal, O(1) space for in-place operations

**Problems Solved:**
- Linked List Cycle
- Reorder List
- Remove Nth Node From End of List
- Merge k Sorted Lists
- Reverse Linked List
- Merge Two Sorted Lists
- Add Two Numbers
- Intersection of Two Linked Lists
- Copy List with Random Pointer
- Sort List

---

## Pattern Addition Template

When adding a new pattern, use this structure:

```markdown
## [Pattern Name]

**When to use:**
- [Scenario 1]
- [Scenario 2]

**Key Recognition Triggers:**
- [Trigger phrase or constraint]
- [Another trigger]

**Common Patterns:**
- **[Sub-pattern]:** [Brief explanation]

**Time Complexity:** [Typical complexity]

**Problems Solved:**
- [Problem 1]
- [Problem 2]
```

---

## Quick Reference Table

| Pattern | Time | Space | Key Indicator |
|---------|------|-------|---------------|
| Heaps | O(log N) | O(K) or O(N) | "Kth largest/smallest", "Top K" |
| Linked Lists (Two Pointers) | O(N) | O(1) | Cycle detection, middle finding |
| Linked Lists (Reverse) | O(N) | O(1) | Reorder, reverse sublist |

---

**Last Updated:** 2026-03-23
