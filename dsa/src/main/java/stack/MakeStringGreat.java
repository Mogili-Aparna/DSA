package stack; /**
 # Make The String Good

 **Date:** 2026-07-15
 **Pattern:** Stack
 **Difficulty:** Easy-Medium
 **Tags:** Stack, String, Simulation

 ---

 # Problem Statement

 A string is considered "good" if there are no two adjacent
 characters that:

 1. Represent the same alphabet letter
 2. But have different cases

 Examples:

 'a' and 'A'  -> remove
 'b' and 'B'  -> remove

 But:

 'a' and 'a'  -> keep
 'A' and 'A'  -> keep

 Continue removing such adjacent pairs until no more removals
 can be performed.

 Return the final string.

 An empty string is also considered good.

 ---

 # Examples

 ## Example 1

 Input:

 "AaBCcdEeff"

 Process:

 AaBCcdEeff
 (remove Aa)

 BCcdEeff
 (remove cC)

 BdEeff
 (remove Ee)

 Bdff

 Output:

 "Bdff"

 ---

 ## Example 2

 Input:

 "abBA"

 Process:

 abBA
 (remove bB)

 aA
 (remove aA)

 ""

 Output:

 ""

 ---

 ## Example 3

 Input:

 "s"

 Output:

 "s"

 ---

 # Observations

 Removing one pair may create another removable pair.

 Example:

 abBA

 After removing:

 bB

 String becomes:

 aA

 which can also be removed.

 This means we need the ability to:

 1. Remove previous character
 2. Re-check newly adjacent characters

 This naturally suggests a Stack.

 ---

 # Key Insight

 For every character:

 If current character and previous character:

 1. Represent same letter
 2. Have different cases

 then remove previous character.

 Otherwise keep current character.

 Stack perfectly models this behavior.

 ---

 # Dry Run

 Input:

 "abBA"

 ---

 'a'

 Stack:

 [a]

 ---

 'b'

 Stack:

 [a, b]

 ---

 'B'

 Same letter, different case.

 Remove:

 [a]

 ---

 'A'

 Same letter, different case.

 Remove:

 []

 Result:

 ""

 ---

 # Approach

 1. Traverse string.
 2. If current character can remove stack top:

 pop()

 3. Otherwise:

 push()

 4. Convert stack into answer.

 ---

 # Correctness Intuition

 Stack always contains the current valid string.

 Whenever:

 Current Character + Previous Character

 form an invalid pair,

 they should disappear.

 Removing previous character from stack naturally
 creates the newly adjacent relationship.

 Thus repeated removals happen automatically.

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

 Reverse:

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

 "a"

 Output:

 "a"

 ---

 ## Case 2

 Input:

 "aA"

 Output:

 ""

 ---

 ## Case 3

 Input:

 "aa"

 Output:

 "aa"

 Same case.

 Cannot remove.

 ---

 ## Case 4

 Input:

 "AA"

 Output:

 "AA"

 Same case.

 Cannot remove.

 ---

 ## Case 5

 Input:

 "abBA"

 Output:

 ""

 Chain removals happen.

 ---

 ## Case 6

 Input:

 "leEeetcode"

 Process:

 leEeetcode
 lEetcode
 leetcode

 Output:

 "leetcode"

 ---

 # Pattern Recognition

 Questions to ask:

 1. Can current element invalidate previous element?
 2. Can removals create new removals?
 3. Do I need undo/backtracking behavior?

 If YES:

 Think Stack.

 ---

 # Similar Problems

 1. Remove Adjacent Duplicates
 2. Remove Stars From String
 3. Asteroid Collision
 4. Backspace String Compare
 5. Valid Parentheses
 6. Simplify Unix Path

 ---

 # Initial Solution

 Used:

 Stack<String>

 with:

 split("")

 and comparison:

 equalsIgnoreCase()

 This works correctly.

 public String makeGood(String str) {
     if(str == null || str.isEmpty()) return "";
     Stack<String> st = new Stack<>();
     for(String ch:str.split("")){
        if(!st.isEmpty() && ch.equalsIgnoreCase(st.peek()) && !ch.equals(st.peek())){
            st.pop();
        }
        else{
            st.push(ch);
        }
     }
     StringBuilder result=new StringBuilder();
     while(!st.isEmpty()){
        result.append(st.pop());
     }
     return result.reverse().toString();
 }
 ---

 # Better Modeling

 Since every element is a single character:

 Prefer:

 Stack<Character>

 instead of:

 Stack<String>

 This avoids:

 - regex split
 - extra String objects
 - unnecessary String comparisons

 ---

 # Interesting Observation (ASCII)

 ASCII values:

 'A' = 65
 'a' = 97

 Difference:

 97 - 65 = 32

 Similarly:

 'B' and 'b'
 'C' and 'c'

 always differ by:

 32

 Therefore condition can be simplified to:

 Math.abs(ch - stack.peek()) == 32

 ---

 # ASCII Trick

 Examples:

 'a' - 'A' = 32
 'b' - 'B' = 32
 'Z' - 'z' = -32

 Absolute difference:

 32

 This is a neat interview optimization.

 ---

 # Learning Notes

 Pattern:

 Current element modifies previous state.

 ↓

 Need efficient removal of previous element.

 ↓

 Use Stack.

 This problem felt easier after solving:

 1. Remove Adjacent Duplicates
 2. Remove Stars

 because the Stack pattern became familiar.

 ---

 # Solution
 */

import java.util.Stack;

public class MakeStringGreat {

    public String makeGood(String str) {

        if (str == null || str.isEmpty())
            return "";

        Stack<Character> stack = new Stack<>();

        for (char ch : str.toCharArray()) {

            if (!stack.isEmpty()
                    && Math.abs(ch - stack.peek()) == 32) {

                stack.pop();
            }
            else {
                stack.push(ch);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.reverse().toString();
    }
}
