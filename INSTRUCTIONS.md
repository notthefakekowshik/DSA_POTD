# Problem-Solving Protocol (The Brain)

This document defines the mandatory workflow for every DSA problem addressed in this repository. Concentration on both high-impact "Must-Know" and comprehensive "Nice-to-Know" problems to ensure exhaustive coverage of each topic, leaving no stone unturned for top-tier PBC interviews.

## 1. Initial Critique

Before providing a solution, analyze the proposed approach. Specifically, explain why a naive or standard implementation does not meet "Senior" (SDE-2/SDE-3) standards. Focus on:

- Edge case handling.
- Code readability and maintainability.
- Resource management (memory/CPU).
- Lack of idiomatic Java features.

## 2. Solution Progression

For every problem, provide solutions in three distinct tiers:

### Tier 1: Mid-Level (Brute Force/Intuitive)

- Focus: "Just make it work."
- Goal: Establish a baseline and understand the constraints.

### Tier 2: Senior (Optimized & Clean)

- Focus: Optimal Time/Space complexity.
- Goal: Production-ready code using clean abstractions, proper naming, and idiomatic Java.

### Tier 3: Staff (Deep Internals & Scalability)

- Focus: High-level optimizations, JVM internals, or distributed system implications.
- Goal: Discuss how the problem scales, potential cache-line optimizations, or alternative data structures for specific hardware constraints.

### Structural Separation & Execution Flow

To ensure solutions are clean and non-conflicting:
- **Separation of Concerns:** Tier 1 (Intermediate) and Tier 2 (Optimized) solutions MUST be implemented as distinct, non-overlapping logic paths. They should not share mutable state.
    - For **Design Problems** (e.g., `MedianFinder`), use separate static inner classes for different tiers.
    - For **Functional Problems**, use separate static methods or static inner classes if distinct state is required.
- **Toggleable Execution:** The `main` method must facilitate "one-click" toggling between solutions. This is achieved by:
    - Instantiating the desired version (Intermediate or Optimized) explicitly in `main`.
    - Using simple comments to swap which version is active for testing.

## 3. Mandatory Metadata

Every solution must include:

- **Language:** Java (Default).
- **Complexity:** Big O notation for Time and Space.
- **Related Topics/Patterns:** Suggestions for further study (e.g., Sliding Window, Monotonic Stack).

## 4. Interview Simulation & Workspace Setup

To simulate a real interview environment:

- **Problem Provider:** Gemini CLI will provide the problems daily:
  - Selection Logic: Before suggesting problems, refer to `JOURNAL.md` to avoid daily repetition. High-impact "Must-Know" problems or complex patterns can be repeated occasionally for reinforcement (spaced repetition). Following the "Must-Know" set, Gemini will suggest "Nice-to-Know" problems (complex variants, edge cases) to ensure 100% topic mastery.
  - **Weekdays:** 1 Easy, 2 Medium.
  - **Weekends:** 1 Easy, 2 Medium, 1 Hard.
- **File Creation:** Create a dedicated Java class for each problem in the appropriate sub-package of `com.dsapotd`. Use deeply nested packages where appropriate (e.g., `com.dsapotd.graphs.dsu`, `com.dsapotd.trees.segment`). And also, populate necessary data to execute the code directly.
- **Problem Statement:** Include the following in a **JavaDoc** at the top of the file:
  - Full problem description, constraints, and examples.
  - **Tags:** A dedicated `@tags` section listing relevant topics/patterns.
- **Initial State:** The class should initially contain only the JavaDoc and a method signature, providing a "blank canvas" for implementation.
- **Implementation Rules:**
  - **Complexity Headers:** Every method implementation MUST be preceded by exactly two lines of comments: one for Time Complexity and one for Space Complexity.
  - **Optimization Path:** When a problem can be solved with multiple distinct patterns (e.g., Heap vs Bucket Sort), implement both methods in the same file to demonstrate the optimization progression.

## 5. Topic Progression Rule

**Complete one topic before moving to the next.**

- Do not start a new pattern/topic until all standard interview-relevant problems for the current topic are solved.
- Use PATTERNS.md, the standard SDE-2 problem set, and comprehensive "Nice-to-Know" variants as the checklist for "complete."
- Only exception: if a problem explicitly requires a secondary pattern (e.g., Heaps inside a Graph problem), that is incidental and does not count as starting a new topic.

## 6. Persistence & Tracking (JOURNAL.md)

The `JOURNAL.md` file is the primary source for long-term retention and revision.

- **Daily Updates:** Update the journal immediately upon completing a problem.
- **Serial Numbering:** Use a `S.No` column as the first column.
    - **New Problems:** Assign a unique, incremental serial number.
    - **Repeated Problems:** If a problem is solved again for reinforcement, log it under the current date but leave the `S.No` column empty. This distinguishes new progress from spaced repetition.
- **Day Differentiation:** Use a separator row (e.g., `| --- | --- | --- | --- | --- | --- |`) between entries of different days to clearly distinguish them.
- **Key Insights:** Focus the "Key Insight" column on the "Aha!" moment or the specific reason a certain pattern was chosen. Be specific and detailed - this is the most valuable part of the journal.
- **Revision Reference:** This file must be treated as a living document to be reviewed monthly to reinforce concepts that are easily forgotten.

## 7. Pattern Recognition Index (PATTERNS.md)

The `PATTERNS.md` file is your mental index for recognizing which pattern to apply during interviews.

- **Update After Each Problem:** When completing a problem, add it to the "Problems Solved" list under the relevant pattern section.
- **New Pattern Introduction:** When starting a new pattern category (e.g., Dynamic Programming, Graphs):
  - Add a new section following the template provided in PATTERNS.md
  - Include: When to use, Key Recognition Triggers, Common Patterns, Time Complexity
  - Update the Quick Reference Table at the bottom
- **Pattern Insights:** As you solve more problems, refine the "Key Recognition Triggers" based on what helped you identify the pattern
- **Weekly Review:** Every Sunday, review PATTERNS.md to reinforce pattern recognition skills
