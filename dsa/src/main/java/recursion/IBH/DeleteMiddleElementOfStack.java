package recursion.IBH;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
/*
 * Problem: Delete the middle element of a stack using recursion.
 *
 * Example:
 *  Input : [2, 3, 7, 6, 4, 5, 9]
 *  Output: [9, 7, 6, 4, 3, 2]   (middle element 5 removed)
 *
 🧠 IBH Breakdown (Intuition)
| Step               | Meaning                                                | In this problem                  |
| ------------------ | ------------------------------------------------------ | -------------------------------- |
| **Base Condition** | When we reach middle element                           | `if (index == 1)`                |
| **Hypothesis**     | Assume recursion deletes mid element for smaller stack | `deleteMiddle(stack, index - 1)` |
| **Induction**      | After deleting, restore the top elements               | `stack.push(top)`                |

🪶 Summary for Notes
| Concept            | Value                                                                                                                        |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------- |
| **Approach**       | IBH (Base → Hypothesis → Induction)                                                                                          |
| **Base Condition** | When `index == 1`, pop the middle                                                                                            |
| **Hypothesis**     | Assume recursion deletes mid from smaller stack                                                                              |
| **Induction**      | Push back popped elements after recursive call                                                                               |
| **Complexity**     | O(N) time, O(N) stack space                                                                                                  |
| **Key Idea**       | Recursion naturally keeps track of “depth,” letting us delete the middle without explicit counting or extra data structures. |

* solved : true
* difficulty : easy
 */
public class DeleteMiddleElementOfStack {
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
        Stack<Integer> deletedStack = deleteMiddleElement(stack,((stack.size()/2) +1));
        while(!deletedStack.isEmpty()){
            System.out.print(deletedStack.pop()+" ");
        }
        System.out.println();
    }
    // IBH Method:
    // Base: when index == 1 → we reached the middle → pop it
    // Hypothesis: assume deleteMiddle() removes middle from smaller stack
    // Induction: after recursive call, push popped element back
    private static Stack<Integer> deleteMiddleElement(Stack<Integer> stack,int index) {
        if(stack.isEmpty()){//when index of mid element is passed wrong or invaild case in which it leads to empty stack.
            return stack;
        }
        if(index == 1){//base condition , when we found mid element
            stack.pop();// delete middle element
            return stack;
        }
        int peek = stack.pop();// remove top
        deleteMiddleElement(stack,--index);//recursive call (Hypothesis)
        stack.push(peek);//restore (Induction)
        return stack;
    }

}
