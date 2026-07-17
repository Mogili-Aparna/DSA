import java.util.*;
/**
 # Sort a Stack Using Another Stack

 **Date:** 2026-07-13
 **Platform:** Grokking / Stack Pattern
 **Difficulty:** Medium
 **Tags:** Stack, Recursion, Simulation, Sorting

 ---

 ## Problem Statement

 Given a stack, sort it using only stack operations (`push`, `pop`, `peek`).

 You may use one additional temporary stack, but no other data structures
 (such as arrays, lists, etc.).

 Sort the stack such that the largest element is on top.

 ### Examples

 Input:
 [34, 3, 31, 98, 92, 23]

 Output:
 [3, 23, 31, 34, 92, 98]

 ---

 ## Observations

 - Stack only allows access to the top element.
 - This behaves similarly to **Insertion Sort**.
 - Maintain another stack in sorted order.
 - Insert each new element into its correct position.

 ---

 # Solution 1 : Recursive Insert

 ## Idea

 Maintain `tmpStack` in sorted order.

 For every element popped from input:

 1. Find its correct location inside `tmpStack`
 2. Temporarily remove larger elements recursively
 3. Insert current element
 4. Push removed elements back

 ---

 ## Complexity

 Time Complexity:
 O(N²)

 Space Complexity:
 O(N)
 (extra stack + recursion stack)

 ---

 ## Recursive Solution
 */
import java.util.Stack;

class RecursiveSolution {

    public static Stack<Integer> sortStack(Stack<Integer> input) {

        Stack<Integer> tmpStack = new Stack<>();

        if (input == null)
            return tmpStack;

        while (!input.isEmpty()) {
            sortedInsert(tmpStack, input.pop());
        }

        return tmpStack;
    }

    private static void sortedInsert(Stack<Integer> stack, int num) {

        if (stack.isEmpty() || stack.peek() <= num) {
            stack.push(num);
            return;
        }

        int top = stack.pop();

        sortedInsert(stack, num);

        stack.push(top);
    }
}

/**
 ---

 # Solution 2 : Iterative Two Stack Solution

 ## Idea

 Maintain `tmpStack` always sorted.

 For every element:

 1. Pop current element from input.
 2. While elements in tmpStack are greater than current element,
 move them back to input.
 3. Push current element into tmpStack.

 This is essentially insertion sort using stacks.

 ---

 ## Complexity

 Time Complexity:
 O(N²)

 Space Complexity:
 O(N)

 ---

 ## Interview Notes

 ### Invariant

 `tmpStack` is always sorted.

 ### Common Mistakes

 1. Wrong comparison (`<` vs `<=`)
 2. Confusion about stack representation:
 - Left → Right
 - Right → Left
 3. Forgetting to move elements back to input.
 4. Returning the wrong stack.

 ### Follow-up Questions

 1. Can this be done without recursion?
 2. Can you explain why it resembles insertion sort?
 3. What additional space is used by recursion?

 ---

 ## Iterative Solution
 */

class IterativeSolution {

    public static Stack<Integer> sortStack(Stack<Integer> input) {

        Stack<Integer> tmpStack = new Stack<>();

        if (input == null)
            return tmpStack;

        while (!input.isEmpty()) {

            int current = input.pop();

            while (!tmpStack.isEmpty()
                    && tmpStack.peek() > current) {

                input.push(tmpStack.pop());
            }

            tmpStack.push(current);
        }

        return tmpStack;
    }
}

/**
 ---

 # My Notes

 ### Thought Process

 1. Examples were ambiguous regarding which side represented
 the top of the stack.

 2. Verified by printing `peek()` and comparing against
 expected outputs.

 3. First intuition naturally led to recursion:

 "Insert an element into an already sorted stack."

 4. Recursive approach was easier to derive.

 5. Iterative version can be obtained by replacing the
 recursion stack with the original input stack.

 ---

 # Pattern Recognition

 This same recursive insertion idea is useful for:

 - Sort Stack
 - Reverse Stack
 - Delete Middle of Stack
 - Reverse Linked List
 - Backtracking problems
 - BST insertion/deletion

 ---
 */


class sortStack {
    /*public static Stack<Integer> sortStack(Stack<Integer> input) {
        Stack<Integer> tmpStack = new Stack<Integer>();
        // ToDo: Write Your Code Here.
        // loop untill the end of the stack. return empty stack if inuput is null or empty
        if(input == null) return tmpStack;
        while(!input.isEmpty()){
            int peek = input.pop();
            if(tmpStack.isEmpty() || tmpStack.peek() <= peek) {
                tmpStack.push(peek);
            }
            else
            {
                input.push(tmpStack.pop());
                input.push(peek);
            }
        }
        return tmpStack;
    }*/
    public static Stack<Integer> sortStack(Stack<Integer> input) {
        Stack<Integer> tmpStack = new Stack<Integer>();
        // ToDo: Write Your Code Here.
        // loop untill the end of the stack. return empty stack if inuput is null or empty
        while(input!=null && !input.isEmpty()){
            //recursive function to push the peek of input and rearrange exising elements of tmp stack
            rearrange(tmpStack,input.pop());
        }
        return tmpStack;
    }
    Queue
    public static void rearrange(Stack<Integer> stack,int number){
        //base cond : stack is empty or if peek element is less than number to be added , we will push the number onto the stack
        if(stack.isEmpty() || stack.peek() <= number) {
            stack.push(number);
            return;
        }
        else {
            //pop peek
            int peek = stack.pop();
            //recursively call rearrange to find where the number fits the best
            rearrange(stack,number);
            //then push back the peek element
            stack.push(peek);
            return;
        }
    }
}
