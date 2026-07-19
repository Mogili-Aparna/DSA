package stack; /**
 # Remove All Adjacent Duplicates in String

 **Date:** 2026-07-15
 **Pattern:** Stack
 **Difficulty:** Easy-Medium
 **Tags:** Stack, String, Simulation

 ---

 # Problem Statement

 Given a string `s`, repeatedly remove adjacent duplicate characters.

 A duplicate removal consists of removing two adjacent equal characters.

 Continue the process until no adjacent duplicates remain.

 Return the final valid string.

 ---

 # Examples

 ## Example 1

 Input:

 "abbaca"

 Process:

 abbaca
 a(b b)aca
 aaca
 (a a)ca
 ca

 Output:

 "ca"

 ---

 ## Example 2

 Input:

 "azxxzy"

 Process:

 az(x x)zy
 azzy
 a(z z)y
 ay

 Output:

 "ay"

 ---

 ## Example 3

 Input:

 "abba"

 Process:

 a(b b)a
 aa
 (a a)

 Output:

 ""

 ---

 # Observations

 Removing a pair can create a new adjacent pair.

 Example:

 abba

 After removing:

 bb

 String becomes:

 aa

 which can also be removed.

 Therefore, simply removing duplicates once is not enough.

 We need a way to remember the previous character.

 This naturally suggests using a Stack.

 ---

 # Key Insight

 For every character:

 1. If current character matches previous character,
 remove the previous one.

 2. Otherwise, keep the current character.

 A stack perfectly models this behavior.

 ---

 # Dry Run

 Input:

 "abbaca"

 ---

 Process:

 'a'

 Stack:

 [a]

 ---

 'b'

 Stack:

 [a, b]

 ---

 'b'

 Current == Stack Top

 Pop:

 [a]

 ---

 'a'

 Current == Stack Top

 Pop:

 []

 ---

 'c'

 Push:

 [c]

 ---

 'a'

 Push:

 [c, a]

 Result:

 "ca"

 ---

 # Approach

 1. Traverse the string.
 2. If stack top equals current character:

 pop()

 3. Else:

 push()

 4. Convert stack into answer.

 ---

 # Correctness Intuition

 Stack always stores the current valid string.

 Whenever a duplicate appears:

 Current Character == Previous Character

 Both should disappear.

 Pop the previous character and ignore current.

 This automatically exposes newly formed adjacent pairs.

 ---

 # Complexity Analysis

 Let:

 n = length of string

 ---

 ## Time Complexity

 Traversing string:

 O(n)

 Reversing stack:

 O(n)

 Building answer:

 O(n)

 Overall:

 O(n)

 ---

 ## Space Complexity

 Stack:

 O(n)

 Reverse Stack:

 O(n)

 Overall:

 O(n)

 ---

 # Edge Cases

 ## Case 1

 Input:

 "a"

 Output:

 "a"

 ---

 ## Case 2

 Input:

 "aa"

 Output:

 ""

 ---

 ## Case 3

 Input:

 "abba"

 Output:

 ""

 ---

 ## Case 4

 Input:

 "abc"

 Output:

 "abc"

 ---

 ## Case 5

 Input:

 "aaaa"

 Process:

 aaaa
 aa
 ""

 Output:

 ""

 ---

 # Interview Notes

 ### Pattern Recognition

 This problem belongs to:

 Process current element based on previous element.

 Questions to ask:

 1. Do I need to remember previous elements?
 2. Can removals create new valid removals?
 3. Do I need backtracking behavior?

 If yes → Stack is usually useful.

 ---

 # Similar Problems

 1. Valid Parentheses
 2. Remove Adjacent Duplicates II
 3. Asteroid Collision
 4. Simplify Path
 5. Remove Stars From String
 6. Backspace String Compare

 ---

 # Possible Optimization

 Instead of Stack<Character>,
 StringBuilder can also be used as a stack.

 Example:

 append()  -> push
 deleteCharAt(last) -> pop

 This avoids creating another reverse stack.

 Complexities remain:

 Time : O(n)
 Space: O(n)

 ---

 # Learning Notes

 This problem became easier after solving:

 1. Sort Stack
 2. Simplify Unix Path

 because the Stack pattern became familiar.

 Pattern:

 Current character interacts with previous character.

 ↓

 Need to remember previous state.

 ↓

 Use Stack.

 ---

 # Solution
 */

import java.util.Stack;

public class RemoveAllAdjacentChars {

    public String removeDuplicates(String str) {

        Stack<Character> stack = new Stack<>();

        if (str == null || str.length() == 0)
            return "";

        for (char ch : str.toCharArray()) {

            if (!stack.isEmpty()
                    && stack.peek() == ch) {

                stack.pop();
            }
            else {
                stack.push(ch);
            }
        }

        Stack<Character> reverse = new Stack<>();

        while (!stack.isEmpty()) {
            reverse.push(stack.pop());
        }

        StringBuilder result = new StringBuilder();

        while (!reverse.isEmpty()) {
            result.append(reverse.pop());
        }

        return result.toString();
    }
}
