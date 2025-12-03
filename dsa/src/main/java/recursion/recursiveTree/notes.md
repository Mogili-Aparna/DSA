# 🧩 Problem: List All Subsets of a String

**Example:**  
Input: `"abc"`  
Output: `["", "a", "b", "c", "ab", "ac", "bc", "abc"]`
---

## 📜 Problem Description

---

## 🧠 Key Learnings

### 1️⃣ Recursive Tree Thinking
If I can draw the recursion tree, I can write the recursive code.  
Each level represents a decision: **include or exclude** the current character.

### 2️⃣ Iterative vs Recursive Mindset
| Type | How it works | Notes |
|------|---------------|-------|
| Iterative | Builds subsets level by level, expanding the list. | Easier to implement but needs careful list management. |
| Recursive | Explores all include/exclude paths using a recursion tree. | Mirrors problem structure, easier to reason about. |

### 3️⃣ Bug & Debug Lessons
| Issue | Root Cause | Fix |
|-------|-------------|-----|
| `ConcurrentModificationException` | Modified `substrings` list while iterating. | Use a new temporary list (`newSubStrings`) and merge at end. |
| Infinite Recursion | Forgot base condition (`index == length`). | Always write base condition before recursion logic. |
| Jumped to fix too early | Googled instead of dry-running. | Pause and mentally simulate 1–2 iterations before coding. |

---

## 🌲 Recursive Tree Diagram

Below is the conceptual tree for `"abc"`:

```arduino
                      ""
               /                  \
             "a"                  ""
           /     \              /     \
       "ab"      "a"         "b"       ""
       /  \      / \        /  \      / \
  "abc" "ab" "ac" "a"   "bc" "b"   "c"  ""


```

Each branch → include or exclude the current character.  
Base case: when index == string.length() → add `current` to result.

---

## 🧮 Final Recursive Solution (Clean & Interview-Style)

```java
private static void generateSubsets(String input, int index, String current, List<String> result) {
    if (index == input.length()) {
        result.add(current);
        return;
    }
    // Exclude current character
    generateSubsets(input, index + 1, current, result);
    // Include current character
    generateSubsets(input, index + 1, current + input.charAt(index), result);
}

// Usage:
List<String> result = new ArrayList<>();
generateSubsets("abc", 0, "", result);
System.out.println(result);
```

---

## ⚙️ Iterative Solution (for comparison)
```java
private static List<String> getSubSets(String input) {
    List<String> substrings = new ArrayList<>();
    substrings.add("");
    for (char ch : input.toCharArray()) {
        List<String> newSubStrings = new ArrayList<>();
        for (String subString : substrings) {
            newSubStrings.add(subString + ch);
        }
        substrings.addAll(newSubStrings);
    }
    return substrings;
}

```
---

## ⏱️ Time and Space Complexity
| Approach  | Time Complexity                | Space Complexity                                          | Explanation                                                                                                                        |
| --------- | ------------------------------ | --------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------- |
| Recursive | **O(2ⁿ)**                      | **O(n × 2ⁿ)**                                             | Each of the `n` characters can either be included or excluded → `2ⁿ` subsets. Each subset may take up to `O(n)` space when stored. |
| Iterative | **O(2ⁿ)**                      | **O(n × 2ⁿ)**                                             | Every iteration doubles the subset list (same growth as recursion).                                                                |
| Optimal   | Can’t be better than **O(2ⁿ)** | Generating all subsets inherently needs exponential time. |                                                                                                                                    |
---

## 🔀 Alternative Approaches

1. **Bitmasking Approach (Iterative, Non-Recursive):**

    * Use bits from `0` to `(1 << n) - 1`.
    * For each number, if bit `j` is set → include `input[j]`.
    * Good for compact, low-level control.

   ```java
   for (int mask = 0; mask < (1 << n); mask++) {
       StringBuilder subset = new StringBuilder();
       for (int j = 0; j < n; j++) {
           if ((mask & (1 << j)) != 0)
               subset.append(input.charAt(j));
       }
       result.add(subset.toString());
   }
   ```

2. **Backtracking (Same as Recursion, but Explicit Path Tracking):**

    * Similar to the recursive version, but emphasizes "state undo" (useful when subsets require additional constraints).

---

---

## 🪶 Reflections

* I understood recursion deeply — I just needed to slow down and trust my reasoning.
* Dry-running on paper before coding reveals 80% of bugs.
* This exercise re-activated my problem-solving flow and debugging awareness.
* I now consciously separate logic design (recursion tree) from implementation details (loops, collections).

---
## 🔍 Deep-Dive Thought Tracker

| Prompt                                  | My Answer                                   |
| --------------------------------------- | ------------------------------------------- |
| 🔹 What is the smallest solvable case?  | When index = length → add current string.   |
| 🔹 How does each level change state?    | Chooses to include or exclude current char. |
| 🔹 What’s the branching factor?         | 2 (include/exclude).                        |
| 🔹 How do I avoid shared mutation bugs? | Pass new strings or new lists in recursion. |

---
## 🔁 Next Practice (Follow-up Problems)
* [ ] Subset Sum Problem
* [ ] Power Set (LeetCode #78)
* [ ] Combination Sum (LeetCode #39)
* [ ] Permutations of a String (LeetCode #46)

---
## 🧾 Tracker Summary

| Category                    | Detail                                                             |
| --------------------------- |--------------------------------------------------------------------|
| **Topic**                   | Recursion / Subsets                                                |
| **Time Taken**              | ~30 mins including debugging                                       |
| **Initial Approach**        | Iterative (mutating list)                                          |
| **Final Approach**          | Recursive (include/exclude)                                        |
| **Complexity**              | Time: O(2ⁿ), Space: O(n × 2ⁿ)                                      |
| **New Concepts Reinforced** | Recursion tree, base case design, mutation awareness               |
| **Confidence Level**        | 🌕🌕🌕🌕⚪ (4/5)                                                    |
| **Next Focus**              | Improve speed in deriving optimal code after writing naive version |
| **Revisit Date**            | 14/11/25                                                           |
---

💎 Concept Clarity for Similar problems :

| Concept         | Definition                                        | Example (`abc`)                                                 | Notes                                 |
| --------------- | ------------------------------------------------- | --------------------------------------------------------------- | ------------------------------------- |
| **Substring**   | Continuous sequence of characters                 | `"a"`, `"b"`, `"c"`, `"ab"`, `"bc"`, `"abc"`                    | Must be contiguous. No skipping.      |
| **Subsequence** | Can skip characters, but order must stay the same | `"a"`, `"b"`, `"c"`, `"ab"`, `"ac"`, `"bc"`, `"abc"`            | Order matters but continuity doesn’t. |
| **Subset**      | From *set* perspective (ignores order)            | `{}`, `{a}`, `{b}`, `{c}`, `{a,b}`, `{b,c}`, `{a,c}`, `{a,b,c}` | Used for combinations / power set.    |

🧠 Concept Reinforcement: How to identify the three

| Pattern Type    | Clue in Problem                                         | Recursion Approach                                               |
| --------------- | ------------------------------------------------------- | ---------------------------------------------------------------- |
| **Substring**   | Words like *“continuous”*, *“contiguous”*               | Nested loops or sliding window (no recursion)                    |
| **Subsequence** | Words like *“skip allowed”* but *order matters*         | Use recursion with inclusion/exclusion (like your current code)  |
| **Subset**      | Words like *“set”, “power set”, “order doesn’t matter”* | Similar recursion but often requires sorting or unique filtering |

🔍 Subset vs Subsequence vs Substring

| Type            | Continuous? | Order Matters? | Example ("abc")                 |
| --------------- | ----------- | -------------- | ------------------------------- |
| **Substring**   | ✅ Yes       | ✅ Yes          | "a", "ab", "bc", "abc"          |
| **Subsequence** | ❌ No        | ✅ Yes          | "a", "ac", "bc", "abc"          |
| **Subset**      | ❌ No        | ❌ No           | {a}, {b}, {a,b}, {b,a}, {a,b,c} |

---
## 🏷️ Tags
#recursion #arrays #bitmasking #leetcode78
---
solved : true
difficulty :medium
