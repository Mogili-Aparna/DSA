package stack; /**
 # Simplify Unix File Path

 **Date:** 2026-07-15
 **Pattern:** Stack
 **Difficulty:** Medium
 **Tags:** Stack, String Parsing, Simulation

 ---

 # Problem Statement

 Given an absolute Unix-style file path, simplify it.

 Rules:

 1. Multiple consecutive slashes `//` should be treated as a single slash `/`.
 2. `"."` refers to the current directory and should be ignored.
 3. `".."` refers to the parent directory.
 4. If already at root `/`, `".."` has no effect.
 5. Return the shortest canonical absolute path.

 ---

 # Examples

 ### Example 1

 Input:

 /a//b////c/d//././/..

 Output:

 /a/b/c

 Explanation:

 Tokens:

 a, b, c, d, ., ., ..

 Process:

 /a
 /a/b
 /a/b/c
 /a/b/c/d
 (ignore .)
 (ignore .)
 (.. removes d)

 Result:

 /a/b/c

 ---

 ### Example 2

 Input:

 /../

 Output:

 /

 Explanation:

 Already at root.

 `..` cannot go above root.

 ---

 ### Example 3

 Input:

 /home//foo/

 Output:

 /home/foo

 ---

 # Initial Wrong Thought Process

 I initially assumed directory names were single characters because
 examples only contained:

 a, b, c, d

 So I modeled the problem using:

 Stack<Character>

 This complicated the solution because I had to:

 - remove previous folder character by character
 - manually handle folder boundaries
 - deal with slashes and dots individually

 Example that exposed the flaw:

 Input:

 /home//foo/../.

 Character-based thinking breaks because:

 "home" and "foo" are directory names, not characters.

 ---

 # Important Lesson

 Do NOT overfit to examples.

 Think about the real-world domain.

 Filesystem hierarchy consists of:

 - directories
 - folder names

 Therefore:

 ❌ Stack<Character>

 ✅ Stack<String>

 One stack element should represent one directory.

 ---

 # Correct Abstraction

 Path:

 /a/b/c

 Hierarchy:

 /
 └── a
 └── b
 └── c

 Current location is simply represented by directories stored in stack.

 ---

 # Approach

 1. Split path using "/"
 2. Iterate through tokens.

 Cases:

 ### token == ""

 Represents duplicate slashes.

 Ignore.

 Example:

 /home//foo

 produces:

 ["", "home", "", "foo"]

 ---

 ### token == "."

 Current directory.

 Ignore.

 ---

 ### token == ".."

 Move to parent directory.

 Pop from stack if stack is not empty.

 ---

 ### Otherwise

 Token is a valid directory name.

 Push onto stack.

 ---

 # Building Final Answer

 After processing:

 Stack:

 [a, b, c]

 Need result:

 /a/b/c

 Since stack top contains last directory,
 use another stack to reverse order.

 ---

 # Complexity

 Let:

 n = length of path

 k = number of directories

 ## Time Complexity

 Splitting path:
 O(n)

 Processing tokens:
 O(n)

 Reversing stack:
 O(k)

 Building answer:
 O(n)

 Overall:

 O(n)

 ---

 ## Space Complexity

 Directory stack:
 O(k)

 Reverse stack:
 O(k)

 StringBuilder:
 O(n)

 Overall:

 O(n)

 ---

 # Edge Cases

 ### Case 1

 Input:

 "/../"

 Output:

 "/"

 ---

 ### Case 2

 Input:

 "/home//foo/../."

 Output:

 "/home"

 ---

 ### Case 3

 Input:

 "/a/../../b/../c//.//"

 Output:

 "/c"

 ---

 ### Case 4

 Input:

 "/"

 Output:

 "/"

 ---

 # Interview Notes

 ### Main Learning

 Always ask:

 "What should one element in my stack represent?"

 Correct abstraction often simplifies the entire problem.

 ---

 ### Common Mistakes

 1. Using Character stack instead of String stack.
 2. Overfitting to examples.
 3. Forgetting that folder names may contain multiple characters.
 4. Forgetting that ".." at root has no effect.
 5. Building answer using:

 insert(0,...)

 which becomes O(n²).

 ---

 # Similar Problems

 - Browser History
 - Remove Invalid Parentheses
 - Decode String
 - Evaluate Reverse Polish Notation
 - Simplify Linux Commands
 - Design File System
 */

import java.util.*;

public class SimplifyPath {

    public String simplifyPath(String path) {

        Stack<String> stack = new Stack<>();

        if (path == null || path.isEmpty())
            return "/";

        String[] tokens = path.split("/");

        for (String token : tokens) {

            if (token.isEmpty() || token.equals(".")) {
                continue;
            }
            else if (token.equals("..")) {

                if (!stack.isEmpty()) {
                    stack.pop();
                }
            }
            else {
                stack.push(token);
            }
        }

        Stack<String> reverse = new Stack<>();

        while (!stack.isEmpty()) {
            reverse.push(stack.pop());
        }

        StringBuilder result = new StringBuilder();

        while (!reverse.isEmpty()) {
            result.append("/");
            result.append(reverse.pop());
        }

        return result.length() == 0
                ? "/"
                : result.toString();
    }
}
