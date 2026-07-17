/**
 # Implement Stack using Two Queues

 **Date:** 2026-07-15
 **Pattern:** Queue Simulation / Data Structure Design
 **Difficulty:** Easy-Medium
 **Tags:** Queue, Stack, Design

 ---

 # Problem Statement

 Implement a Stack using only Queue operations.

 Supported operations:

 1. push(x)
 2. pop()
 3. top()
 4. empty()

 Stack follows:

 LIFO
 (Last In First Out)

 Queues follow:

 FIFO
 (First In First Out)

 ---

 # Example

 Operations:

 push(5)
 push(10)

 Stack:

 Top
 10
 5

 pop()

 returns:

 10

 top()

 returns:

 5

 ---

 # Observations

 Queue naturally removes elements in insertion order.

 Stack requires removing the most recently inserted element.

 Therefore, after every insertion,
 we must somehow place the newest element at the front.

 ---

 # Approach 1 (Implemented)

 ## Costly Push

 Idea:

 Whenever a new element is pushed:

 1. Add it to queue2.
 2. Move all elements from queue1 → queue2.
 3. Swap queue references.

 This ensures:

 queue1 always stores elements in stack order.

 ---

 # Dry Run

 Initially:

 q1 = []
 q2 = []

 ---

 push(5)

 q2:

 [5]

 Swap:

 q1:

 [5]

 ---

 push(10)

 q2:

 [10]

 Move q1 → q2

 q2:

 [10,5]

 Swap:

 q1:

 [10,5]

 Top of stack is now at front of q1.

 ---

 pop()

 remove front:

 10

 Remaining:

 [5]

 Correct stack behavior.

 ---

 # Why Swap?

 Instead of copying elements back:

 q2 → q1

 simply swap references.

 Example:

 Queue temp = q1;
 q1 = q2;
 q2 = temp;

 This is an important optimization technique.

 ---

 # Invariant

 After every push:

 Front of q1 always represents
 Top of Stack.

 Therefore:

 pop()
 top()

 become O(1).

 ---

 # Complexity Analysis

 Let:

 n = number of elements

 ---

 ## push()

 Move all previous elements.

 Worst Case:

 O(n)

 ---

 ## pop()

 Remove front.

 O(1)

 ---

 ## top()

 Peek front.

 O(1)

 ---

 ## empty()

 O(1)

 ---

 # Space Complexity

 Two queues together store n elements.

 O(n)

 ---

 # Alternative Approach

 ## Costly Pop

 Idea:

 Push directly into q1.

 During pop():

 Move first n-1 elements
 to second queue.

 Remove last element.

 ---

 ### Complexities

 | Operation | Cost |
 |-----------|------|
 | push | O(1) |
 | pop | O(n) |
 | top | O(n) |
 | empty | O(1) |

 ---

 # Comparison

 ## Approach 1 (This Solution)

 | Operation | Complexity |
 |-----------|-------------|
 | push | O(n) |
 | pop | O(1) |
 | top | O(1) |
 | empty | O(1) |

 ---

 ## Approach 2

 | Operation | Complexity |
 |-----------|-------------|
 | push | O(1) |
 | pop | O(n) |
 | top | O(n) |
 | empty | O(1) |

 ---

 # Which Approach to Choose?

 Depends on usage pattern.

 ### Many push operations

 Prefer:

 Costly Pop

 ---

 ### Many pop/top operations

 Prefer:

 Costly Push

 (Current implementation)

 ---

 # Edge Cases

 ## Case 1

 push(5)

 top()

 Output:

 5

 ---

 ## Case 2

 push(5)
 push(10)

 pop()

 Output:

 10

 ---

 ## Case 3

 empty queue

 empty()

 Output:

 true

 ---

 # Interview Follow-up Questions

 ### Why use two queues?

 A single queue cannot directly provide:

 LIFO behavior.

 Two queues help rearrange order.

 ---

 ### Why does swapping work?

 Queues are reference variables.

 Swapping references avoids copying elements.

 ---

 ### Can we implement Stack using one Queue?

 Yes.

 Idea:

 1. Push new element.
 2. Rotate existing elements.

 Complexities:

 push : O(n)
 pop  : O(1)

 ---

 # Similar Problems

 1. Implement Queue using Stacks
 2. Min Stack
 3. Browser History
 4. Design Circular Queue
 5. Design Circular Deque

 ---

 # Pattern Recognition

 This belongs to:

 Implement one data structure using another.

 Questions to ask:

 1. Which operations need optimization?
 2. Can I rearrange elements?
 3. Can I trade push cost for pop cost?

 ---

 # Learning Notes

 Queue:

 FIFO

 Stack:

 LIFO

 To simulate stack:

 Newest element must always appear at front.

 Therefore:

 After every push:

 New Element
 +
 Previous Elements

 ↓

 Rearrange order.

 This is a common interview design question.

 ---

 # Solution
 */

import java.util.LinkedList;
import java.util.Queue;

class Solution {

    Queue<Integer> queue1;
    Queue<Integer> queue2;

    public Solution() {

        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
    }

    public void push(int x) {

        queue2.add(x);

        while (!queue1.isEmpty()) {
            queue2.add(queue1.remove());
        }

        Queue<Integer> temp = queue1;
        queue1 = queue2;
        queue2 = temp;
    }

    public int pop() {
        return queue1.remove();
    }

    public int top() {
        return queue1.peek();
    }

    public boolean empty() {
        return queue1.isEmpty();
    }
}
