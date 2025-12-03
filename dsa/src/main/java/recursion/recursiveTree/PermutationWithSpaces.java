package recursion.recursiveTree;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PermutationWithSpaces {
    /*
        i/p : ABC
        o/p : A B C
              A BC
              AB C
              ABC


## ✅ What Went Right

### 1. **Correct recursive branching**

For each character → 2 choices:

* Add with a space
* Add without a space

This is exactly the recursive tree for *Permutation with Spaces*.

```
op + ch + " "   → include space
op + ch         → exclude space
```

### 2. Using `index` to shrink input ✔

### 3. Base case when `index == length` ✔

### 4. Using a Set to avoid duplicates ✔ (good because input like `"AAB"` would duplicate)

---

## 🔍 Small Improvements

### 1️⃣ There is an *extra leading space* issue if `op` starts empty.

Your first recursive call produces `"A "` but intended first branch should start as `"A"`, not `" A"`.

### 2️⃣ The problem always starts **without space before the first character**.

So we should initialize `op` with the *first character already placed*.

### 3️⃣ Avoid `.trim()`, it hides logic mistakes — better to *prevent* the extra space.

---

## ✨ Corrected and Clean Version

```java
package recursion.recursiveTree;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PermutationWithSpaces {

    static void main() {
        System.out.print("Enter String: ");
        Scanner sc = new Scanner(System.in);
        String ip = sc.next();
        sc.close();

        Set<String> result = new HashSet<>();

        // Initialize output with first character (no space before it)
        getPermutationWithSpaces(ip, "" + ip.charAt(0), 1, result);

        for (String s : result) {
            System.out.println(s);
        }
    }

    private static void getPermutationWithSpaces(String ip, String op, int index, Set<String> result) {
        if (index == ip.length()) {
            result.add(op);
            return;
        }

        char ch = ip.charAt(index);

        // Choice 1: Add space before next char
        getPermutationWithSpaces(ip, op + " " + ch, index + 1, result);

        // Choice 2: Add char without space
        getPermutationWithSpaces(ip, op + ch, index + 1, result);
    }
}
```

---

## 🧩 Example for `"ABC"`

```
Start: op = "A", index = 1

Branches:
"A B C"
"A BC"
"AB C"
"ABC"
```

**Output:**

```
A B C
A BC
AB C
ABC
```

Perfect ✔✔

---

## 🎯 Complexity

| Measure | Value                                |
| ------- | ------------------------------------ |
| Time    | O(2ⁿ) — each character has 2 choices |
| Space   | O(n) recursion stack + O(2ⁿ) results |

---

## 🌱 Key Learning

```
Any problem with "insert space or not" → 2 decisions per character → recursion tree.
```

This is **another pure “decision space recursion”** problem — same pattern as:

* Subsets
* Subsequences
* Balanced parentheses
* Binary representation trees

---

**next logical progression problem**:

### 🔥 *Permutation with Case Change* (`aBc`, `AbC`, etc.)

It’s the same recursion tree but instead of `" "` you branch on **uppercase vs lowercase**.
A beautiful pattern-next-step.

solved : true
difficulty :medium

     */
    static void main() {
        System.out.println("Enter String :");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        Set<String> permutationWithSpaces = new HashSet<>();
         getPermutationWithSpaces(ip,"",0,permutationWithSpaces);
        for (String permutation:permutationWithSpaces){
            System.out.println(permutation);
        }
    }
    /*
                                                "ABC",""
                         "BC","A "                                        "BC","A"
            "C","A B "                 "C","A B"               "C","AB "              "C""AB"
    "","A B C "  "","A B C"   "","A BC "  "","A BC"   "","AB C "  "","AB C"    "","ABC "  "","ABC"
     */
    private static void getPermutationWithSpaces(String ip, String op, int index, Set<String> permutationWithSpaces) {
        if(index == ip.length()) {
            permutationWithSpaces.add(op.trim());
            return;
        }
        char ch = ip.charAt(index);
        index+=1;
        getPermutationWithSpaces(ip,op+ch+" ",index,permutationWithSpaces);
        getPermutationWithSpaces(ip,op+ch,index,permutationWithSpaces);
    }
}
/*

 */