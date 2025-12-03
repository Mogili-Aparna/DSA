package recursion.IBH;

import java.util.Scanner;
/*
### 🧠 Problem
Move N disks from rod A → rod C using rod B, following rules:
1. Only one disk can be moved at a time.
2. Larger disk can’t be placed on smaller disk.
3. Only top disk can be moved.

🧠 IBH Recursion Breakdown (for your code)
| Step                 | Meaning                                | Example (n=3)               |
| -------------------- | -------------------------------------- | --------------------------- |
| **Base**             | Move 1 disk from source → destination  | `move 1 from s → d`         |
| **Hypothesis**       | Move top n-1 disks source → helper     | Move top 2 disks from s → h |
| **Induction**        | Move largest disk source → destination | Move disk 3 from s → d      |
| **Final Hypothesis** | Move n-1 disks helper → destination    | Move 2 disks from h → d     |

🧮 Moves
| N | Moves  | Formula |
| - | ------ | ------- |
| 1 | 1      | 2¹ - 1  |
| 2 | 3      | 2² - 1  |
| 3 | 7      | 2³ - 1  |
| n | 2ⁿ - 1 | ✅       |

🧮 Complexity
| Measure              | Value                    |
| -------------------- | ------------------------ |
| **Moves**            | `2^n - 1`                |
| **Time Complexity**  | `O(2^n)`                 |
| **Space Complexity** | `O(n)` (recursion stack) |

🪶 Summary for Notes
| Concept              | Explanation                                                                                |
| -------------------- | ------------------------------------------------------------------------------------------ |
| **Base Condition**   | If 1 disk → move directly                                                                  |
| **Hypothesis**       | Assume we can move n-1 disks                                                               |
| **Induction**        | Move nth disk → then move smaller ones again                                               |
| **Moves**            | 2ⁿ - 1                                                                                     |
| **Time Complexity**  | O(2ⁿ)                                                                                      |
| **Space Complexity** | O(n)                                                                                       |
| **Key Idea**         | The recursion solves smaller subproblems twice — before and after moving the largest disk. |
solved : true
difficulty : medium
 */
public class TowersOfHanoi {
    static void main() {
        System.out.println("Enter the no. of disks to be moved : ");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        char source = 's';
        char destination = 'd';
        char helper = 'h';
        towersOfHanoi(n,source,destination,helper);
        scanner.close();

    }

    private static void towersOfHanoi(int n, char source, char destination, char helper) {
        // Base condition
        if(n == 1) {
            System.out.println("moving disk no " + n + " from " + source + " to " + destination);
            return;
        }
        // Hypothesis: move top n-1 disks from source to helper using destination
        towersOfHanoi(n-1,source,helper,destination);
        // Induction: move nth (largest) disk from source to destination
        System.out.println("moving disk no "+n+" from "+source+" to "+destination);
        // Move n-1 disks from helper to destination using source
        towersOfHanoi(n-1,helper,destination,source);
    }
}
/*

 */
