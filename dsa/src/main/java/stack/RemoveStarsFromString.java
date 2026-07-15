/**
 # Remove Stars From a String

 **Date:** 2026-07-15
 **Pattern:** Stack
 **Difficulty:** Easy-Medium
 **Tags:** Stack, String, Simulation

 ---

 # Problem Statement

 Given a string `s` containing lowercase English letters and `'*'`.

 Each `'*'` removes:

 1. Itself
 2. The closest non-star character to its left.

 Perform all possible removals and return the final string.

 The problem guarantees that every `'*'`
 has a valid character to remove.

 ---

 # Examples

 ## Example 1

 Input:

 "abc*de*f"

 Process:

 abc*de*f
 abde*f
 abdf

 Output:

 "abdf"

 ---

 ## Example 2

 Input:

 "a*b*c*d"

 Process:

 a*b*c*d
 b*c*d
 c*d
 d

 Output:

 "d"

 ---

 ## Example 3

 Input:

 "abcd"

 Output:

 "abcd"

 ---

 # Observations

 Every star affects the character immediately before it.

 This means:

 Current character depends on previously processed characters.

 Whenever we see:

 '*'

 we need to remove the most recent valid character.

 This is exactly LIFO behavior.

 Therefore:

 Use a Stack.

 ---

 # Key Insight

 Treat stack as:

 Current valid string built so far.

 For every character:

 ### If character is not '*'

 Push it.

 ### If character is '*'

 Remove previous character.

 Pop from stack.

 ---

 # Dry Run

 Input:

 "abc*de*f"

 ---

 'a'

 Stack:

 [a]

 ---

 'b'

 Stack:

 [a, b]

 ---

 'c'

 Stack:

 [a, b, c]

 ---

 '*'

 Remove previous character.

 Stack:

 [a, b]

 ---

 'd'

 Stack:

 [a, b, d]

 ---

 'e'

 Stack:

 [a, b, d, e]

 ---

 '*'

 Remove previous character.

 Stack:

 [a, b, d]

 ---

 'f'

 Stack:

 [a, b, d, f]

 Final Answer:

 "abdf"

 ---

 # Approach

 1. Traverse string from left to right.
 2. If current character is a letter:

 push()

 3. If current character is '*':

 pop()

 4. Build final answer from stack.

 ---

 # Correctness Intuition

 Stack always contains the valid characters
 remaining after processing the prefix.

 Whenever a '*' appears:

 The closest non-star character to the left
 is exactly the top of the stack.

 Thus:

 pop()

 correctly removes it.

 ---

 # Complexity Analysis

 Let:

 n = length of string

 ---

 ## Time Complexity

 Traverse string:

 O(n)

 Build answer:

 O(n)

 Reverse result:

 O(n)

 Overall:

 O(n)

 ---

 ## Space Complexity

 Stack:

 O(n)

 StringBuilder:

 O(n)

 Overall:

 O(n)

 ---

 # Edge Cases

 ## Case 1

 Input:

 "abcd"

 Output:

 "abcd"

 No stars.

 ---

 ## Case 2

 Input:

 "a*"

 Output:

 ""

 ---

 ## Case 3

 Input:

 "a*b*c*d"

 Output:

 "d"

 ---

 ## Case 4

 Input:

 "*****"

 Not possible according to constraints.

 Every star always has a valid character.

 ---

 ## Case 5

 Input:

 "leet**cod*e"

 Process:

 leet**cod*e
 lecod*e
 lecoe

 Output:

 "lecoe"

 ---

 # Interview Notes

 ### Pattern Recognition

 Questions to ask:

 1. Does current element affect previous elements?
 2. Do I need undo behavior?
 3. Do I need the most recent element?

 If YES:

 Think about Stack.

 ---

 # Similar Problems

 1. Remove Adjacent Duplicates
 2. Backspace String Compare
 3. Browser History
 4. Simplify Unix Path
 5. Asteroid Collision
 6. Valid Parentheses

 ---

 # Important Observation

 This problem is very similar to:

 Remove Adjacent Duplicates

 Difference:

 ### Adjacent Duplicates

 Current character removes previous character
 only if they are equal.

 ### Remove Stars

 Current '*' always removes previous character.

 Same underlying pattern:

 Current element modifies previous state.

 ---

 # Possible Optimization

 Instead of using Stack<Character>,

 StringBuilder can be used as a stack.

 Operations:

 append()            -> push
 deleteCharAt(last)  -> pop

 This avoids reversing the answer.

 Complexities remain:

 Time  : O(n)
 Space : O(n)

 ---

 # Learning Notes

 Pattern:

 Current element needs access to previous state.

 ↓

 Need efficient undo operation.

 ↓

 Stack is a natural choice.

 This problem became easier after solving:

 1. Remove Adjacent Duplicates
 2. Simplify Path

 because the Stack pattern became familiar.

 ---

 # Solution
 */

import java.util.Stack;

public class Solution {

    public String removeStars(String str) {

        if (str == null || str.isEmpty())
            return "";

        Stack<Character> stack = new Stack<>();

        for (char ch : str.toCharArray()) {

            if(!stack.isEmpty() && ch == '*'){
                stack.pop();
            }
            else if(ch!='*'){
                stack.push(ch);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.reverse().toString();
    }
