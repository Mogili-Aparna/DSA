package Queue; /**
 # Reverse Queue

 **Date:** 2026-07-15
 **Pattern:** Queue + Stack
 **Difficulty:** Easy
 **Tags:** Queue, Stack, Reversal

 ---

 # Portal Observation

 Problem statement mentioned:

 "Reverse a singly linked list"

 But boilerplate provided:

 Queue<Integer> reverseQueue(Queue<Integer> q)

 This is a portal mismatch.

 Since method signature cannot be changed,
 solution should follow the boilerplate and solve:

 "Reverse a Queue"

 ---

 # Problem Statement

 Given a queue, reverse its elements.

 Example:

 Input:

 Front -> 3 5 2 -> Rear

 Output:

 Front -> 2 5 3 -> Rear

 ---

 # Observations

 Queue follows:

 FIFO

 Stack follows:

 LIFO

 If we move:

 Queue → Stack → Queue

 the order naturally gets reversed.

 ---

 # Dry Run

 Initial Queue:

 Front

 3 5 2

 Rear

 ---

 Move to Stack:

 Top

 2
 5
 3

 Bottom

 ---

 Move back to Queue:

 Front

 2 5 3

 Rear

 Result:

 Queue reversed.

 ---

 # Approach

 1. Remove all elements from queue.
 2. Push them into stack.
 3. Pop from stack and add back to queue.
 4. Return queue.

 ---

 # Correctness Intuition

 Queue removes elements in FIFO order.

 Stack stores them in reverse order.

 When inserted back into queue,
 elements appear in reversed sequence.

 ---

 # Complexity Analysis

 Let:

 n = number of elements

 ---

 ## Time Complexity

 Removing from queue:

 O(n)

 Adding back:

 O(n)

 Overall:

 O(n)

 ---

 ## Space Complexity

 Stack stores all elements.

 O(n)

 ---

 # Edge Cases

 ## Case 1

 Input:

 []

 Output:

 []

 ---

 ## Case 2

 Input:

 [7]

 Output:

 [7]

 ---

 ## Case 3

 Input:

 [1,2,3,4]

 Output:

 [4,3,2,1]

 ---

 # Interview Notes

 ### Why does Stack work?

 Queue:

 FIFO

 Stack:

 LIFO

 Using Stack converts:

 First element removed from queue

 ↓

 becomes last element inserted back.

 Hence reversal happens naturally.

 ---

 # Follow-up

 Can this be solved without explicit stack?

 Yes.

 Using recursion.

 Idea:

 1. Remove front element.
 2. Reverse remaining queue recursively.
 3. Add removed element at rear.

 Complexities:

 Time  : O(n)
 Space : O(n)

 ---

 # Pattern Recognition

 Questions to ask:

 1. Need reversal?
 2. Input is FIFO?
 3. Need opposite ordering?

 Think:

 Queue + Stack

 ---

 # Similar Problems

 1. Reverse Stack
 2. Reverse Linked List
 3. Implement Queue using Stacks
 4. Implement Stack using Queues
 5. Reverse First K Elements of Queue

 ---

 # Learning Notes

 This problem reinforces:

 Queue + Stack together can naturally reverse order.

 Portal had a mismatch between:

 Problem Statement:
 Reverse Linked List

 Boilerplate:
 Reverse Queue

 Always trust the method signature if portal content is inconsistent.

 ---

 # Solution
 */

import java.util.Queue;
import java.util.Stack;

public class ReverseAQueue {

    public Queue<Integer> reverseQueue(Queue<Integer> q) {

        if (q == null || q.isEmpty())
            return q;

        Stack<Integer> stack = new Stack<>();

        while (!q.isEmpty()) {
            stack.push(q.poll());
        }

        while (!stack.isEmpty()) {
            q.add(stack.pop());
        }

        return q;
    }
    public Queue<Integer> reverseQueueRecursive(Queue<Integer> q) {

        if (q == null || q.isEmpty())
            return q;

        int currELe = q.poll();
        reverseQueueRecursive(q);
        q.add(currELe);

        return q;
    }
}
