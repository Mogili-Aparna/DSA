package recursion.IBH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
problem : We build a table of n rows (1-indexed). We start by writing 0 in the 1st row. Now in every subsequent row, we look at the previous row and replace each occurrence of 0 with 01, and each occurrence of 1 with 10.

For example, for n = 3, the 1st row is 0, the 2nd row is 01, and the 3rd row is 0110.
Given two integer n and k, return the kth (1-indexed) symbol in the nth row of a table of n rows.

Examples:
1)
Input: n = 1, k = 1
Output: 0
Explanation: row 1: 0
Example 2:
2)
Input: n = 2, k = 1
Output: 0
Explanation:
row 1: 0
row 2: 01
Example 3:
3)
Input: n = 2, k = 2
Output: 1
Explanation:
row 1: 0
row 2: 01

Constraints:
1 <= n <= 30
1 <= k <= 2n - 1

https://leetcode.com/problems/k-th-symbol-in-grammar/description/

Notes:

Approach 1: "built the whole forest to find one leaf." 🌳

Approach 2 : Optimized version:
"Now I know how trees grow, so I can walk directly to the leaf I need." 🍃

Approach1 :

This version runs and prints the rows correctly for very small n,
but it isn’t what the LeetCode #779 problem is really asking you to do — and
for n = 30 it will overflow memory or hang because the full table has 2ⁿ – 1 ≈ a billion entries.

🧩 What Approach1 code is doing:
    Builds every row up to n using List<List<Integer>>.
    Each recursive call expands the previous row by duplicating all 0 → 01 and 1 → 10.
    Then prints the whole grammar and finally returns grammar[n-1][k-1].

✅ Logic is fine for tiny n.
❌ Space = O(2ⁿ), time = O(2ⁿ) — it’s exponential.
The problem statement only asks for the k-th symbol, so no need to materialize all rows.

.

🧠 Recursive pattern for the k-th symbol
Each row doubles the previous one:
row1: 0
row2: 01
row3: 0110
row4: 01101001

Notice:
    a row has double no. of digits compared to the previous row.
    Length of row n = 2ⁿ⁻¹
    The first half of row n is identical to row n – 1.
    The second half is the bitwise NOT of row n – 1.

So the k-th symbol of row n depends only on the position of k:
if k <= mid      → same as kthSymbolInGrammar(n-1, k)
else              → complement of kthSymbolInGrammar(n-1, k-mid)
where mid = 2^(n-1).

🧩 IBH Breakdown
| Step           | Concept                                                        | Explanation                          |
| -------------- | -------------------------------------------------------------- | ------------------------------------ |
| **Base**       | Smallest valid case                                            | Row 1 → 0                            |
| **Hypothesis** | Assume function works for smaller row (n-1)                    | Recursive call                       |
| **Induction**  | Decide if k lies in first half (same) or second half (flipped) | `1 - kthSymbolInGrammar(n-1, k-mid)` |

🧠 Intuitive Understanding
Row 1: 0
Row 2: 01
Row 3: 0110
Row 4: 01101001

🟩 First half → copy previous row
🟥 Second half → flip previous row
So, you never actually need to build these rows —
just keep tracing where your k lies in the recursion tree!

⏱ Complexity
| Metric                  | Value                     |
| ----------------------- | ------------------------- |
| **Time**                | O(n) — depth of recursion |
| **Space**               | O(n) — recursion stack    |
| **No Extra Structures** | ✅                         |

✅ Key Insight:

The kth symbol depends on whether k lies in the first half (same)
or second half (complement) of the previous row.

---

## 💡 Step 1: Row structure

| Row (n) | Content          | Length (2ⁿ⁻¹) |
| ------- | ---------------- | ------------- |
| 1       | 0                | 1             |
| 2       | 01               | 2             |
| 3       | 0110             | 4             |
| 4       | 01101001         | 8             |
| 5       | 0110100110010110 | 16            |

So, for **row n**,
👉 length = `2^(n-1)`

---

## 💡 Step 2: Finding the middle index

For each new row, we split the previous row into **two halves**:

```
row n = [first half same as row n-1] + [second half = flipped version]
```

That means the **middle index** is exactly the *length of the previous row*, because that’s where the split happens.

👉 middle = length of row (n - 1)
👉 middle = `2^( (n - 1) - 1 ) = 2^(n - 2)`

---

## 💡 Step 3: How it looks for real rows

| n | row length | middle (split) | left part length  |
| - | ---------- | -------------- | ----------------- |
| 2 | 2¹ = 2     | middle = 1     | left: 1, right: 1 |
| 3 | 2² = 4     | middle = 2     | left: 2, right: 2 |
| 4 | 2³ = 8     | middle = 4     | left: 4, right: 4 |

→ Notice the **middle index = half of total length** = `2^(n - 2)`.

---

## 💡 Step 4: How this looks in bitwise shift form

`1 << x` means “2 raised to the power x”.

So:

* `1 << (n - 1)` → total row length (`2^(n - 1)`)
* `1 << (n - 2)` → middle index (`2^(n - 2)`)

That’s why we use:

```java
int mid = 1 << (n - 2);
```

---

## ✅ Visual Intuition

For `n = 4`:

```
Row length = 2^(4-1) = 8
Indices:     1 2 3 4 | 5 6 7 8
             ← first half →|← second half →
Middle = 2^(4-2) = 4
```

So:

* `k <= mid (4)` → first half → same as row n-1
* `k > mid` → second half → complement of row n-1 (k - mid)

---

## ⚡ Shortcut Summary

| Purpose                  | Formula        | Meaning                          |
| ------------------------ | -------------- | -------------------------------- |
| Total symbols in nth row | `1 << (n - 1)` | 2^(n-1)                          |
| Midpoint (split index)   | `1 << (n - 2)` | 2^(n-2) = length of previous row |

---

✅ Therefore:

```java
int mid = 1 << (n - 2);  // correct midpoint
```

If you used `1 << (n - 1)`, it would become the **total length**, not the middle — and your recursion would never reach the base condition correctly (you’d always fall into the second half case).
---
solved : true
difficulty : medium
 */
public class KthSymbolInGrammarLeetCode779 {
    static void main() {
        System.out.println("Enter the Value of n : ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        if(n<1) throw new IllegalArgumentException("n>=1");
        System.out.println("Enter the Value of k : ");
        int k = sc.nextInt();
        if(k<1) throw new IllegalArgumentException("k>=1");
        System.out.println("Kth element of n lines grammar is : "+kthSymbolInGrammar(n,k));
        System.out.println("Kth element of n lines grammar is : "+kthSymbolInGrammarApproach2(n,k));

    }

    private static int kthSymbolInGrammar(int n, int k) {
        List<List<Integer>> grammar = getGrammar(n);
        System.out.println("grammar generated :");
        for (List<Integer> arr:grammar){
            for (Integer ele:arr){
                System.out.print(ele+" ");
            }
            System.out.println();
        }
        return grammar.get(n-1).get(k-1);//doing -1 minus as input is 1-indexed and arrays in java are zero indexed
    }

    private static List<List<Integer>> getGrammar(int n) {
        List<List<Integer>> list ;
        if(n==1){//base condition
            list = new ArrayList<>();
            list.add(Arrays.asList(0)); //add initial 0 where the grammar starts
            return list;
        }
        list = getGrammar(n-1); //hypothesis : recursive call on smaller input i.e, previous row
        List<Integer> row = nextRow(list.getLast()); // induction : generate nextRow based on prev row
        list.add(row); //add newly generated row to grammar.
        return list;
    }

    private static List<Integer> nextRow(List<Integer> list) {
        if(list.isEmpty()){//base condition
            return new ArrayList<Integer>();
        }
        int last = list.getLast(); //save last element for induction step
        List<Integer> row = nextRow(list.subList(0,list.size()-1));//hypothesis : recursive call -- smaller input returns-- correct grammar for previous all digits in a row except the last
        //induction : generate grammar for last digit in array.
        if(last ==0){
            row.add(0);
            row.add(1);
        }
        else if(last ==1 ){
            row.add(1);
            row.add(0);
        }
        return row;
    }
    private static int kthSymbolInGrammarApproach2(int n,int k){
        if(n == 1 && k ==1) return 0;
        int totalSymbolsInRow = 1 << (n-1) ;//Math.pow(2,n-1);
        if (k > totalSymbolsInRow) throw new IllegalArgumentException("k exceeds length of row");
        int mid = (totalSymbolsInRow/2);
        if(k<=mid){
            return kthSymbolInGrammarApproach2(n-1,k);
        }
        else{
            return 1-kthSymbolInGrammarApproach2(n-1,k-mid);
        }
    }
}
