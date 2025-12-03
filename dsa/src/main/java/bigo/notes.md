# ⚡ Big-O Notes — Time & Space Complexity (Intuitive + Visual Guide)

---

## 🧠 What Is Big-O?

**Big-O notation** describes **how the time or space used by a program grows** as the size of input (**n**) increases.

It tells you *how fast things blow up or shrink* — not the exact speed.

> 💬 Think of Big-O as “How many steps will my code roughly take if I double the input?”

---

## 🌱 1. Big-O Means "Growth Pattern"

| Input Size (n) | O(1) | O(n) | O(n²) | O(2ⁿ) |
|----------------|------|------|--------|--------|
| 1 | 1 | 1 | 1 | 2 |
| 2 | 1 | 2 | 4 | 4 |
| 3 | 1 | 3 | 9 | 8 |
| 4 | 1 | 4 | 16 | 16 |
| 10 | 1 | 10 | 100 | 1024 |

➡️ Notice how **O(2ⁿ)** explodes, while **O(1)** stays flat.

---

## 🌾 2. Visual Intuition for Common Complexities

### ⚙️ Constant Time — **O(1)**
> “Always the same number of steps.”

Example:
```java
int x = arr[5];
```

📈 Grows flat — doesn’t depend on input size.

---

### 🪴 Linear Time — **O(n)**

> “Work grows in direct proportion to input size.”

Example:

```java
for(int i=0; i<n; i++)
   print(arr[i]);
```

If you double n, time doubles.
📈 Straight diagonal line.

![Linear](dsa/src/main/java/bigo/images/linear.png)

---

### 🌻 Quadratic Time — **O(n²)**

> “Work grows as the square of input size.”

Example:

```java
for(i=0;i<n;i++)
  for(j=0;j<n;j++)
    print(i,j);
```

If n=10 → 100 steps; if n=100 → 10,000 steps.
📈 Steep parabola — nested loops.

![Quadratic](dsa/src/main/java/bigo/images/quadratic.png)

---

### 🌲 Logarithmic Time — **O(log n)**

> “Dividing the work in half each time.”

Example: Binary Search

```java
int binarySearch(int arr[], int target){
    int l=0, r=arr.length-1;
    while(l<=r){
        int mid = (l+r)/2;
        if(arr[mid]==target) return mid;
        else if(arr[mid]<target) l=mid+1;
        else r=mid-1;
    }
}
```

Each step halves the array size:
n → n/2 → n/4 → n/8 → … → 1
📈 Grows slowly — even for huge n.

| n         | Steps |
| --------- | ----- |
| 8         | 3     |
| 16        | 4     |
| 1,024     | 10    |
| 1,000,000 | 20    |

🧩 Opposite of exponentials — *shrinks fast*.

![Logarithmic](dsa/src/main/java/bigo/images/logarithmic.png)

---

### 🌸 Exponential Time — **O(2ⁿ)**

> “Work doubles at each step.”

Example: Subset generation or recursion with two decisions (include/exclude).

```
At each char → two choices: ✅ include / ❌ exclude
```

| Input length | Subsets generated |
| ------------ | ----------------- |
| 1            | 2                 |
| 2            | 4                 |
| 3            | 8                 |
| 4            | 16                |
| n            | 2ⁿ                |

🌀 Tree expands like a fractal — doubles each level.

If each subset also takes O(n) to build → **O(n·2ⁿ)** total.

![Exponential](dsa/src/main/java/bigo/images/exponential.png)

---

### 🌺 Factorial Time — **O(n!)**

> “Explore all possible orderings.”

Example: generating all permutations of n elements.
First position: n choices → next: (n−1) → next: (n−2)… → 1
Total = **n! = n × (n−1) × … × 1**

| n  | n!        | Approx Steps |
| -- | --------- | ------------ |
| 3  | 6         | small        |
| 5  | 120       | ok           |
| 10 | 3,628,800 | 😱 huge      |

📈 Explodes faster than exponential.

![Factorial Complexity](dsa/src/main/java/bigo/images/factorial.png)

---

## 🧩 3. How to Guess Complexity by Looking

| What You See in Code                        | Likely Complexity |
| ------------------------------------------- | ----------------- |
| Single loop                                 | O(n)              |
| Two nested loops                            | O(n²)             |
| Three nested loops                          | O(n³)             |
| Divide array in half each step              | O(log n)          |
| Divide and process both halves (merge sort) | O(n log n)        |
| Explore all subsets                         | O(2ⁿ)             |
| Explore all permutations                    | O(n!)             |
| Constant operations only                    | O(1)              |

---

## 🪄 4. Connecting Visual → Math

| Visual Intuition         | Math Form | What It Means             |
| ------------------------ | --------- | ------------------------- |
| **Doubles every step**   | 2ⁿ        | “2 choices per item”      |
| **Triples every step**   | 3ⁿ        | “3 choices per step”      |
| **Halves each step**     | log₂ n    | “How many times until 1?” |
| **Adds one each step**   | n         | “Linear growth”           |
| **Adds layers of loops** | n², n³    | “Nested passes”           |

💬 Think of **log** as *“how many times can I cut it in half?”*
and **2ⁿ** as *“how many times does it double?”*

---

## 🌿 5. Space Complexity (Memory Growth)

| Code Behavior             | Space              |
| ------------------------- | ------------------ |
| Use few variables         | O(1)               |
| Store an array of size n  | O(n)               |
| Nested lists (n×n matrix) | O(n²)              |
| Recursion depth = n       | O(n) (stack space) |
| Store all subsets         | O(n·2ⁿ)            |

🧠 Tip: Space often = size of data you keep at once (arrays, recursion calls, or output).

---

## 🧭 6. Rule of Thumb for Recursive Problems

1️⃣ **Count the branches**
 How many calls are made per level? (branching factor **b**)
2️⃣ **Count the depth**
 How deep does recursion go? (levels **d**)
3️⃣ **Total calls ≈ bᵈ**

> 💬 Example: subsets → 2 branches (✅/❌) × n levels → 2ⁿ calls.

If each call costs O(1): total O(2ⁿ).
If each call builds a string of length n: O(n·2ⁿ).

---

## 🌼 7. Why Exponential and Factorial Feel "Impossible"

* O(2ⁿ) → doubles every new element
* O(n!) → grows even faster; try listing all 10! permutations — impossible manually.

That’s why we look for **DP, pruning, or greedy** tricks to cut branches early.

---

## 💫 8. Comparing Growth Visually

```
Time ↑
│                        O(n!)
│                     O(2ⁿ)
│                O(n³)
│            O(n²)
│        O(n log n)
│     O(n)
│  O(log n)
│O(1)
└─────────────────────────────→ n (input)
```

![All Complexities Annotated](dsa/src/main/java/bigo/images/all_complexities_annotated.png)

📈 Notice exponential/factorial skyrocket, logarithmic hardly moves.

---

## 🔍 9. Quick Complexity Recognition Guide

| Pattern Type                        | Typical Complexity | Example                          |
| ----------------------------------- | ------------------ | -------------------------------- |
| **Simple loop**                     | O(n)               | Sum array                        |
| **Nested loops**                    | O(n²)              | Matrix traversal                 |
| **Divide & conquer**                | O(n log n)         | Merge sort, Quick sort           |
| **Binary search**                   | O(log n)           | Searching in sorted array        |
| **Subsets / Backtracking**          | O(2ⁿ)              | Power set                        |
| **Permutations / DFS of all paths** | O(n!)              | Traveling salesman, permutations |
| **Constant ops**                    | O(1)               | Accessing index, swapping        |

---

## 🧮 10. Math to Intuition Bridge

| Mathematical View | Visual Meaning                           |
| ----------------- | ---------------------------------------- |
| 2ⁿ                | Every element doubles output count       |
| n log n           | Slightly more than linear, e.g., sorting |
| log₂ n            | Keep halving input (binary search)       |
| n²                | Two nested passes through data           |
| n³                | Three nested passes                      |
| n!                | All orderings of elements                |

---

## 🪶 11. Tiny Summary Table (Time vs Space)

| Category    | Time       | Space   | Example           |
| ----------- | ---------- | ------- | ----------------- |
| Constant    | O(1)       | O(1)    | Swap 2 numbers    |
| Linear      | O(n)       | O(1)    | Find max in array |
| Logarithmic | O(log n)   | O(1)    | Binary Search     |
| Linear-Log  | O(n log n) | O(n)    | Merge Sort        |
| Quadratic   | O(n²)      | O(1)    | Bubble Sort       |
| Exponential | O(2ⁿ)      | O(n·2ⁿ) | Subset Generation |
| Factorial   | O(n!)      | O(n·n!) | Permutations      |

---

## 🧭 12. How to Estimate Complexity in Practice

Ask yourself:

1. 🔹 **How many choices** do I have at each step?
2. 🔹 **How deep** does it go (loop/recursion)?
3. 🔹 **What work** happens inside each step?
   → Multiply them together.

That’s your rough Big-O.

---

## 🧘 Final Thought 

> You don’t need to love formulas — you need to **see patterns**.
> Big-O is just a way to **describe how patterns grow** when inputs increase.

Once you visualize doubling, halving, or nesting,
you already understand complexity better than most.

---

🪷 *Written for intuition-based learners. Visuals over math.*

---

