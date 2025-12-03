package recursion.IBH;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
/*
 * Problem: Reverse a stack using only recursion.
 * No extra stack or loop allowed.
 *
 * Example:
 * Input  : [2, 3, 7, 6, 4, 5, 9]
 * Output : [9, 5, 4, 6, 7, 3, 2]
 *
 *        6 5 4 3 2 1 --> 1 2 3 4 5 6
        5 4 3 2 1   --> 1 2 3 4 5
        4 3 2 1     --> 1 2 3 4
        3 2 1       --> 1 2 3
        2 1         --> 1 2
        1           --> 1

💡 Complexity
| Measure              | Value                                                      |
| -------------------- | ---------------------------------------------------------- |
| **Time Complexity**  | O(N²) → each `insert()` call can traverse up to N elements |
| **Space Complexity** | O(N) → due to recursion stack                              |

🧠 Intuitive IBH Recap

| Step               | Meaning                         |      Code                            |
| ------------------ | ------------------------------- | ------------------------------------ |
| **Base Condition** | Empty stack → stop              | `if (stack.isEmpty()) return stack;` |
| **Hypothesis**     | Assume smaller stack reversed   | `reverseStack(stack);`               |
| **Induction**      | Place removed element at bottom | `insert(stack, peek);`               |

✅ Key Idea:
Use recursion to remove all elements, and while unwinding, insert each element at the bottom.

✅ What i did:
1. Used the IBH pattern
    Base Condition: stop when the stack is empty.
    Hypothesis: assume reverseStack() reverses the smaller stack.
    Induction: insert the removed element (peek) at the bottom of that reversed stack.
2. Handled insertion correctly
    You used a helper insert() that recursively pushes the element at the bottom of the stack.
    The order of pop → recurse → push back is exactly what makes it reverse properly.
3. Didn’t use extra data structures beyond the recursion call stack.
    ✅ This is a pure recursion solution.

🧠 Intuition Trick
🧩 "Reverse a stack" = “Reverse smaller part + put the removed element at bottom.”
The recursion’s call stack itself acts as the temporary structure.

🪶 Notes for Your GitHub (Markdown Summary)
| Concept              | Explanation                              |
| -------------------- | ---------------------------------------- |
| **Approach**         | IBH (Base → Hypothesis → Induction)      |
| **Base Condition**   | Empty or single-element stack            |
| **Hypothesis**       | Smaller stack is reversed                |
| **Induction**        | Insert removed top element at bottom     |
| **Time Complexity**  | O(N²) — each insertAtBottom is O(N)      |
| **Space Complexity** | O(N) — due to recursion stack            |
| **Category**         | Recursion using call stack (no extra DS) |
solved : true
* difficulty : medium
 */
public class ReverseAStack {
    static void main() {
        sort(Arrays.asList(2,3,7,6,4,5,9));
        sort(Arrays.asList(2));
        sort(Arrays.asList(2,3));
        sort(Arrays.asList(2,2,2,2,2));
        sort(Arrays.asList(2,3,4,5,6));
        sort(Arrays.asList(6,5,4,3,2,1));
        sort(Arrays.asList(5,2,2,7,9,9,9,2,2,4,4,1,0,0,0,0));
    }

    private static void sort(List<Integer> list) {
        Stack<Integer> stack = new Stack<>();
        for (Integer ele:list){
            stack.push(ele);
        }
        Stack<Integer> reversedStack = reverseStack(stack);
        while(!reversedStack.isEmpty()){
            System.out.print(reversedStack.pop()+" ");
        }
        System.out.println();
    }
    // IBH Method:
    // Base: stack with 0 or 1 element is already reversed
    // Hypothesis: assume reverse() reverses the smaller stack
    // Induction: remove top, reverse smaller stack, and insert top at bottom
    private static Stack<Integer> reverseStack(Stack<Integer> stack) {
        if(stack.isEmpty())
            return stack;
        int peek = stack.pop();
        reverseStack(stack);// Hypothesis: reverse remaining stack
        insert(stack,peek);// Induction: place top at bottom
        return stack;
    }

    private static void insert(Stack<Integer> stack, int peek) {
        if(stack.isEmpty()) {
            stack.push(peek);
            return;
        }
        int top = stack.pop();
        insert(stack,peek);
        stack.push(top);
    }

    /*

*/


}
