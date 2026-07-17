/**
 # Generate Binary Numbers from 1 to N

 **Date:** 2026-07-15
 **Pattern:** Queue / BFS / State Generation
 **Difficulty:** Medium
 **Tags:** Queue, BFS, String Generation, Pattern Recognition

 ---

 # Problem Statement

 Given an integer N, generate binary representations of all
 numbers from 1 to N.

 Example:

 N = 5

 Output:

 [
 "1",
 "10",
 "11",
 "100",
 "101"
 ]

 ---

 # Initial Thought Process

 My first thought was:

 "Why is this under Queue?"

 A straightforward solution exists:

 For every number:

 1 → binary
 2 → binary
 3 → binary
 ...
 N → binary

 This can be done recursively using:

 num % 2
 num / 2

 ---

 # Recursive Binary Conversion Solution

 ## Idea

 Convert one number at a time.

 Example:

 13

 13 % 2 = 1
 6 % 2 = 0
 3 % 2 = 1
 1 % 2 = 1

 Result:

 1101

 ### Code

 private static String getBinaryRep(int num) {

 if (num <= 0)
 return "";

 int remainder = num % 2;

 String binary =
 getBinaryRep(num / 2);

 return binary + remainder;
 }

 Generate answer:

 for (int i = 1; i <= n; i++) {
 result[i - 1] = getBinaryRep(i);
 }

 ---

 ## Complexity

 Each number takes:

 O(log N)

 Total:

 O(N log N)

 Space:

 O(log N) recursion stack.

 ---

 # Important Observation

 Writing outputs:

 1
 10
 11
 100
 101
 110
 111
 1000

 They seem related.

 Question:

 Can one answer generate future answers?

 YES.

 From:

 1

 we can generate:

 10
 11

 From:

 10

 we can generate:

 100
 101

 From:

 11

 we can generate:

 110
 111

 This forms a tree.

 ---

 # Tree Representation

 1
 /     \
 10       11
 /   \     /   \
 100   101 110   111

 Reading this tree level-by-level gives:

 1
 10
 11
 100
 101
 110
 111
 ...

 Exactly the required output.

 ---

 # Key Insight

 This is an infinite tree.

 We only need:

 First N nodes.

 Whenever we need:

 1. Generate states
 2. Process level by level
 3. First K nodes

 Think:

 BFS

 ↓

 Queue

 ---

 # Queue Approach

 1. Start with:

 Queue = ["1"]

 2. Remove front.

 3. Add answer to result.

 4. Generate children:

 current + "0"
 current + "1"

 5. Repeat N times.

 ---

 # Dry Run

 N = 5

 Queue:

 [1]

 ---

 Remove:

 1

 Result:

 [1]

 Generate:

 10
 11

 Queue:

 [10,11]

 ---

 Remove:

 10

 Result:

 [1,10]

 Generate:

 100
 101

 Queue:

 [11,100,101]

 ---

 Remove:

 11

 Result:

 [1,10,11]

 Generate:

 110
 111

 Queue:

 [100,101,110,111]

 ...

 Answer:

 1
 10
 11
 100
 101

 ---

 # Initial Queue Solution

 I initially tried to avoid generating
 extra nodes.

 Code:

 while (resList.size() + q.size() < n) {

 String cur = q.poll();

 resList.add(cur);

 q.add(cur + "0");
 q.add(cur + "1");
 }

 Then consume remaining queue elements.

 This works correctly but becomes slightly
 harder to reason about.

 ---

 # Cleaner BFS Solution

 Generate exactly N nodes.

 Code:

 for (int i = 0; i < n; i++) {

 String cur = q.poll();

 resList.add(cur);

 q.add(cur + "0");
 q.add(cur + "1");
 }

 ---

 # Why Generate Extra Nodes?

 Example:

 N = 5

 To generate:

 101

 we may temporarily create:

 110
 111

 which are not needed.

 This is okay.

 The tree itself is infinite.

 BFS naturally keeps some future nodes
 inside queue.

 This small extra work keeps the code simple.

 ---

 # Complexity Analysis

 Let:

 N = number of answers required.

 ---

 ## Time Complexity

 Each iteration:

 1 poll
 2 adds

 O(1)

 Total:

 O(N)

 ---

 ## Space Complexity

 Queue stores future generated nodes.

 O(N)

 ---

 # Comparison

 ## Recursive Number Conversion

 Pros:

 - Easy to derive
 - Direct approach

 Complexity:

 Time:

 O(N log N)

 ---

 ## Queue BFS Solution

 Pros:

 - Elegant pattern
 - Demonstrates BFS thinking
 - Better complexity

 Complexity:

 Time:

 O(N)

 ---

 # Pattern Recognition Notes

 Whenever outputs look like:

 Current State

 ↓

 Generate Next States

 ↓

 Generate First K States

 Think:

 Tree / Graph

 ↓

 BFS

 ↓

 Queue

 ---

 # Similar Problems

 1. Generate Parentheses
 2. Word Ladder
 3. Open Lock
 4. Minimum Genetic Mutation
 5. State Space Search Problems
 6. Level Order Traversal

 ---

 # Learning Notes

 Initially I was thinking in:

 "Binary Conversion Mode"

 using:

 - powers of 2
 - division by 2
 - remainders

 But the intended solution requires:

 "Pattern Generation Mode"

 Important question:

 Can one answer generate future answers?

 If YES:

 Think:

 BFS / Queue.

 This was the main learning from this problem.

 ---

 # Solution 1
 # Recursive Binary Conversion
 */

public static String[] generateBinaryNumbers(int n) {

    String[] result = new String[n];

    for (int i = 1; i <= n; i++) {
        result[i - 1] = getBinaryRep(i);
    }

    return result;
}

private static String getBinaryRep(int num) {

    if (num <= 0)
        return "";

    return getBinaryRep(num / 2)
            + (num % 2);
}

/**
 -------------------------------------------------------

 # Solution 2
 # Queue BFS Solution
 */

public static String[] generateBinaryNumbers(int n) {

    Queue<String> q =
            new LinkedList<>();

    ArrayList<String> result =
            new ArrayList<>();

    q.add("1");

    for (int i = 0; i < n; i++) {

        String current = q.poll();

        result.add(current);

        q.add(current + "0");
        q.add(current + "1");
    }

    return result.toArray(String[]::new);
}
