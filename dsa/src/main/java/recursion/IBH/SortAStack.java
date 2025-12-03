package recursion.IBH;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
/*
* ps : sort a stack using recursion
* i/p :
        1) 2,3,7,6,4,5,9));
        2) 2
        3) 2,3
        4) 2,2,2,2,2
        5) 2,3,4,5,6
        6) 6,5,4,3,2,1
        7) 5,2,2,7,9,9,9,2,2,4,4,1,0,0,0,0
* o/p :
        1) 9 7 6 5 4 3 2
        2) 2
        3) 3 2
        4) 2 2 2 2 2
        5) 6 5 4 3 2
        6) 6 5 4 3 2 1
        7) 9 9 9 7 5 4 4 2 2 2 2 1 0 0 0 0
 🧠 Explanation — IBH Method Breakdown
| Step               | Concept                              | Meaning Here                           |
| ------------------ | ------------------------------------ | -------------------------------------- |
| **Base Condition** | stack has ≤1 element                 | Already sorted                         |
| **Hypothesis**     | assume smaller stack is sorted       | `sortStack()` sorts remaining elements |
| **Induction**      | insert last popped element correctly | Use recursion again for insertion      |
solved : true
* difficulty : medium
* */
public class SortAStack {
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
        Stack<Integer> sortedStack = sortAStack(stack);
        while(!sortedStack.isEmpty()){
            System.out.print(sortedStack.pop()+" ");
        }
        System.out.println();
    }
    // IBH method:
    // Base: stack with single element is already sorted
    // Hypothesis: assume sortStack() correctly sorts the smaller stack
    // Induction: insert popped element into sorted stack at correct position
    private static Stack<Integer> sortAStack(Stack<Integer> stack) {
        if(stack.size() == 1) return stack; //base condition
        int peek = stack.pop();
        Stack<Integer> sortedStack = sortAStack(stack);//hypothesis
        insert(sortedStack,peek);//induction
        return sortedStack;
    }
    // Insert element into correct position in descending order
    private static void insert(Stack<Integer> sortedStack, int peek) {
        if(sortedStack.isEmpty() || sortedStack.peek() <= peek){//base condition
            sortedStack.push(peek);
            return;
        }
        int temp  = sortedStack.pop();
        insert(sortedStack,peek);//hypothesis
        sortedStack.push(temp);//induction
    }
}
/*

 */

