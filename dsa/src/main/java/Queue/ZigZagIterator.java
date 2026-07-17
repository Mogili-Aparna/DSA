/**
 # Zigzag Iterator

 **Date:** 2026-07-15
 **Pattern:** Queue + Iterator Design
 **Difficulty:** Medium
 **Tags:** Queue, Iterator, Design, Round Robin

 ---

 # Problem Statement

 Given two vectors:

 v1
 v2

 Return their elements alternately.

 Example:

 v1 = [1,2]
 v2 = [3,4,5,6]

 Output:

 [1,3,2,4,5,6]

 ---

 # Examples

 ## Example 1

 Input:

 v1 = [1,2]
 v2 = [3,4,5,6]

 Output:

 [1,3,2,4,5,6]

 ---

 ## Example 2

 Input:

 v1 = [1,2,3,4]
 v2 = [5,6]

 Output:

 [1,5,2,6,3,4]

 ---

 ## Example 3

 Input:

 v1 = [1,2]
 v2 = []

 Output:

 [1,2]

 ---

 # Initial Thought Process

 My first idea was:

 Precompute the entire answer.

 1. Alternate elements.
 2. Store everything in queue.
 3. next() simply polls.

 This works.

 However:

 This is not the spirit of an iterator.

 ---

 # First Solution Issues

 I initially used:

 vector.remove(0)

 Problem:

 For ArrayList:

 remove(0)

 ↓

 All elements shift left.

 Complexity:

 O(n)

 Thus constructor became:

 O(n²)

 ---

 # Important Observation

 Iterator problems should usually be:

 LAZY.

 Meaning:

 Constructor should NOT compute everything.

 Instead:

 Generate values only when:

 next()

 is called.

 ---

 # Key Insight

 We only need to remember:

 Which vector should provide the next element.

 This naturally suggests:

 Queue of Iterators.

 ---

 # Elegant Idea

 Maintain:

 Queue<Iterator<Integer>>

 Queue contains only iterators
 that still have elements.

 ---

 # Invariant

 IMPORTANT:

 Queue always contains:

 Only non-empty iterators.

 Because of this:

 hasNext()

 becomes trivial.

 ---

 # Constructor

 Create iterators.

 If iterator still has elements:

 Add it into queue.

 Example:

 v1=[1,2]
 v2=[3,4]

 Queue:

 [it1,it2]

 ---

 # How next() Works

 1. Remove front iterator.
 2. Return its next element.
 3. If iterator still has elements:

 Put it back into queue.

 This naturally creates:

 Round Robin behavior.

 ---

 # Dry Run

 Input:

 v1=[1,2]
 v2=[3,4,5,6]

 ---

 Queue:

 [it1,it2]

 ---

 next()

 Take:

 it1

 Return:

 1

 it1 still has:

 2

 Reinsert.

 Queue:

 [it2,it1]

 ---

 next()

 Take:

 it2

 Return:

 3

 Queue:

 [it1,it2]

 ---

 Result:

 1,3,2,4,5,6

 Perfect.

 ---

 # Why Queue?

 Because after using one iterator:

 it should wait until
 other iterators get their turn.

 This is exactly:

 Round Robin Scheduling.

 Queue naturally models this behavior.

 ---

 # Complexity Analysis

 Let:

 n = total elements

 ---

 ## Constructor

 Creating iterators:

 O(1)

 ---

 ## next()

 poll()
 next()
 optional add()

 Everything:

 O(1)

 ---

 ## hasNext()

 Queue empty check:

 O(1)

 ---

 ## Space Complexity

 Store only iterators.

 O(1)

 (For only 2 vectors)

 Generalized:

 O(k)

 where:

 k = number of vectors.

 ---

 # Generalization

 This solution beautifully extends to:

 k vectors.

 Example:

 v1
 v2
 v3
 v4
 ...

 Simply:

 for each vector:

 if iterator has elements:

 add iterator into queue.

 No other code changes.

 ---

 # Pattern Recognition

 Questions to ask:

 1. Need alternate processing?
 2. Need fair ordering?
 3. Need round-robin execution?

 Think:

 Queue.

 ---

 # Similar Problems

 1. Round Robin CPU Scheduling
 2. Task Scheduling
 3. Merge K Sorted Lists
 4. Design Hit Counter
 5. Implement Stack using Queues
 6. Producer Consumer Problems

 ---

 # Learning Notes

 My progression:

 1. Precompute answer.
 2. Realized remove(0) issue.
 3. Thought of Queue<Iterator>.
 4. Fixed invariant.
 5. Arrived at elegant iterator solution.

 Important lesson:

 Strong invariants simplify code.

 Invariant:

 Queue contains only iterators
 that still have elements.

 ↓

 next() becomes simple.

 ↓

 hasNext() becomes:

 return !q.isEmpty();

 ---

 # Solution
 */

import java.util.*;

class Solution {

    Queue<Iterator<Integer>> q;

    public Solution(
            List<Integer> v1,
            List<Integer> v2) {

        q = new LinkedList<>();

        Iterator<Integer> iterator =
                v1.iterator();

        if (iterator.hasNext()) {
            q.add(iterator);
        }

        iterator = v2.iterator();

        if (iterator.hasNext()) {
            q.add(iterator);
        }
    }

    public int next() {

        Iterator<Integer> iterator =
                q.poll();

        int result = iterator.next();

        if (iterator.hasNext()) {
            q.add(iterator);
        }

        return result;
    }

    public boolean hasNext() {
        return !q.isEmpty();
    }
}
