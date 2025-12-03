package recursion.recursiveTree;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PermutationWithCaseChange {
    /*
        i/p : ab
        o/p : ab
              aB
              Ab
              AB
               two branches per character (lower/upper), base at `index == ip.length()`. Works great.

A few small, useful refinements:

* **Handle non-letters**: for inputs like `a1b`, digit `'1'` shouldn’t branch; otherwise both branches add `'1'` and  relied on `Set` to dedupe. Better: only branch when `isLetter`.
* **Predictable output order**: use `LinkedHashSet` (or a `List`) if you want to preserve generation order instead of hashing order.
* **Avoid `index++` in the same line**: small readability win.
* **Close the scanner**.
* **Perf nit**: `StringBuilder` avoids lots of string allocations (nice if strings get longer).

Here’s a polished version with those tweaks:

```java
package recursion.recursiveTree;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class PermutationWithCaseChange {
    /*
      Input:  a1b
      Output: a1b, A1b, a1B, A1B
      Rule: only letters toggle case; non-letters are copied as-is.
    *
static void main() {
    System.out.print("Enter String: ");
    try (Scanner scanner = new Scanner(System.in)) {
        String ip = scanner.next();
        Set<String> results = new LinkedHashSet<>(); // preserves order

        // Use StringBuilder for efficiency
        getPermutationsWithCaseChange(ip, new StringBuilder(), 0, results);

        results.forEach(System.out::println);
    }
}

private static void getPermutationsWithCaseChange(
        String ip, StringBuilder op, int index, Set<String> out) {

    if (index == ip.length()) {
        out.add(op.toString());
        return;
    }

    char ch = ip.charAt(index);

    if (Character.isLetter(ch)) {
        // branch 1: lower
        op.append(Character.toLowerCase(ch));
        getPermutationsWithCaseChange(ip, op, index + 1, out);
        op.setLength(op.length() - 1);

        // branch 2: upper
        op.append(Character.toUpperCase(ch));
        getPermutationsWithCaseChange(ip, op, index + 1, out);
        op.setLength(op.length() - 1);
    } else {
        // single path for non-letters
        op.append(ch);
        getPermutationsWithCaseChange(ip, op, index + 1, out);
        op.setLength(op.length() - 1);
    }
}
}
        ```

### Quick notes

* **Correctness**: Same logic; just guards branching for non-letters.
* **Complexity**: `O(2^L)` time, `O(L)` recursion depth, where `L =` number of letters (non-letters don’t branch).
* **If input may already contain upper/lower mix**: this still toggles per character;  to *preserve original case as one branch*,
add a third path (but typical problem is exactly lower/upper).
follow-up variations next:

* **Letter Case Permutation with spaces allowed** (`a b`, `A b`, …),
* **Permutations skipping vowels**, or
* **Backtracking version that writes results directly to a list (no Set) but handles duplicates by sorting + skip-equal technique**.
 solved : true
 difficulty : medium
     */
    static void main() {
        System.out.println("Enter String :");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        Set<String> permutationsWithCaseChange = new HashSet<>();
        getPermutationsWithCaseChange(ip,"",0,permutationsWithCaseChange);
        for (String permutation:permutationsWithCaseChange){
            System.out.println(permutation);
        }
    }

    /*
                "","ab"
        "a","b"          "A","b"
    "ab","" "aB",""  "Ab","" "AB",""

     */
    private static void getPermutationsWithCaseChange(String ip, String op, int index, Set<String> permutationsWithCaseChange) {
        if(index==ip.length()){
            permutationsWithCaseChange.add(op);
            return;
        }
        char ch = ip.charAt(index++);
        getPermutationsWithCaseChange(ip,op+Character.toLowerCase(ch),index,permutationsWithCaseChange);
        getPermutationsWithCaseChange(ip,op+Character.toUpperCase(ch),index,permutationsWithCaseChange);
    }
}
/*
*/

