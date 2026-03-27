The user will provide a problem name, LeetCode number, difficulty, and target package (e.g. "LC 373, Find K Pairs with Smallest Sums, Medium, com.dsapotd.heaps").

Follow the mandatory structure from INSTRUCTIONS.md exactly:

1. Create a Java file in the correct sub-package under src/main/java/com/dsapotd/.
   - Use PascalCase for the filename matching the problem name.
   - Use deeply nested packages where appropriate (e.g. com.dsapotd.graphs.dsu).

2. The file must contain:
   - A full JavaDoc block with: problem description, constraints, examples, and @tags.
   - Tier 2 (Senior/Optimized) as the outer class implementation.
   - Tier 1 (Intermediate/Brute Force) as a static inner class named `Intermediate`.
   - A Tier 3 Staff discussion block as a block comment covering scalability / JVM internals.
   - Every method implementation preceded by exactly two comment lines: Time Complexity and Space Complexity.
   - A main() method with runOptimized() and runIntermediate() private static methods.
   - The one-click toggle comment pattern in main().
   - Populated test cases in both run methods — no empty stubs.

3. After creating the file, update JOURNAL.md:
   - Append a new row with the next serial number, today's date, problem name, difficulty, pattern, and a specific key insight (the "Aha!" moment).
   - Add a day separator row (| --- | ... |) if this is the first problem of a new day.

4. Update PATTERNS.md:
   - Add the problem name under the relevant pattern's "Problems Solved" list.
   - If this is a new pattern not yet in PATTERNS.md, add a full new section using the template at the bottom of that file.

Do all three steps (file + JOURNAL.md + PATTERNS.md) in sequence without asking for confirmation between steps.
