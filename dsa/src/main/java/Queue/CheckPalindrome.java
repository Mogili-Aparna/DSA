/*
========================================================
PALINDROME CHECKING
========================================================

Definition:
A palindrome reads the same forward and backward.

Examples:

"madam"       -> true
"racecar"     -> true
"hello"       -> false
"A man a plan a canal Panama"
(ignore spaces/punctuation)
               -> true


========================================================
APPROACH 1 : TWO POINTERS (BEST)
========================================================

IDEA:
Compare first and last characters.
Move inward.

l -> beginning
r -> end

If any mismatch occurs,
it is not a palindrome.


Dry Run:

m a d a m
L       R

m==m

  a d a
  L   R

a==a

    d

Palindrome.


--------------------------------------------------------
CODE
--------------------------------------------------------

public static boolean checkPalindrome(String s)
{
    if(s == null)
        return false;

    s = s.replaceAll("\\s+","");

    int l = 0;
    int r = s.length()-1;

    while(l < r)
    {
        if(Character.toLowerCase(s.charAt(l))
                !=
           Character.toLowerCase(s.charAt(r)))
        {
            return false;
        }

        l++;
        r--;
    }

    return true;
}


--------------------------------------------------------
COMPLEXITY
--------------------------------------------------------

Time  : O(n)
Space : O(1)

--------------------------------------------------------
TRADE OFF
--------------------------------------------------------

✔ Best solution.
✔ Interview preferred.
✔ Constant extra space.
✘ Does not demonstrate Queue/Stack usage.


========================================================
APPROACH 2 : RECURSION
========================================================

IDEA:

Compare first and last.

Then recursively solve:

middle substring.


--------------------------------------------------------
BAD VERSION
--------------------------------------------------------

return checkPalindrome(
            s.substring(1,s.length()-1))
       &&
       s.charAt(0)==
       s.charAt(s.length()-1);


Problem:

substring() creates new strings.


--------------------------------------------------------
COMPLEXITY
--------------------------------------------------------

Time  : O(n²)
Space : O(n)


--------------------------------------------------------
BETTER RECURSIVE VERSION
--------------------------------------------------------

public static boolean helper(
        String s,
        int l,
        int r)
{
    if(l >= r)
        return true;

    if(Character.toLowerCase(s.charAt(l))
            !=
       Character.toLowerCase(s.charAt(r)))
    {
        return false;
    }

    return helper(s,l+1,r-1);
}


public static boolean checkPalindrome(String s)
{
    if(s == null)
        return false;

    s = s.replaceAll("\\s+","");

    return helper(
            s,
            0,
            s.length()-1);
}


--------------------------------------------------------
COMPLEXITY
--------------------------------------------------------

Time  : O(n)
Space : O(n)
(recursion stack)


--------------------------------------------------------
TRADE OFF
--------------------------------------------------------

✔ Elegant solution.
✔ Good for recursion practice.
✘ Extra recursion stack.
✘ Risk of stack overflow for huge strings.


========================================================
APPROACH 3 : STACK
========================================================

IDEA:

Push all characters.

Traverse string again.

Compare current character
with popped character.


--------------------------------------------------------
CODE
--------------------------------------------------------

public static boolean checkPalindrome(String s)
{
    if(s == null)
        return false;

    s = s.replaceAll("\\s+","");

    Deque<Character> st =
            new ArrayDeque<>();

    for(char ch : s.toCharArray())
    {
        st.push(
            Character.toLowerCase(ch));
    }

    for(char ch : s.toCharArray())
    {
        if(Character.toLowerCase(ch)
                != st.pop())
        {
            return false;
        }
    }

    return true;
}


--------------------------------------------------------
COMPLEXITY
--------------------------------------------------------

Time  : O(n)
Space : O(n)


--------------------------------------------------------
TRADE OFF
--------------------------------------------------------

✔ Demonstrates reverse order using stack.
✔ Good DS practice.
✘ Extra memory.
✘ Not optimal.


========================================================
APPROACH 4 : QUEUE + STACK
========================================================

IDEA:

Queue gives characters in
FORWARD order.

Stack gives characters in
REVERSE order.

Compare both.


--------------------------------------------------------
CODE
--------------------------------------------------------

public static boolean checkPalindrome(String s)
{
    if(s == null)
        return false;

    s = s.replaceAll("\\s+","");

    Queue<Character> q =
            new LinkedList<>();

    Deque<Character> st =
            new ArrayDeque<>();

    for(char ch : s.toCharArray())
    {
        ch = Character.toLowerCase(ch);

        q.offer(ch);
        st.push(ch);
    }

    while(!q.isEmpty())
    {
        if(q.poll() != st.pop())
            return false;
    }

    return true;
}


--------------------------------------------------------
COMPLEXITY
--------------------------------------------------------

Time  : O(n)
Space : O(n)


--------------------------------------------------------
TRADE OFF
--------------------------------------------------------

✔ Demonstrates Queue + Stack concepts.
✔ Useful educational example.
✘ Uses twice the memory.
✘ Not interview optimal.


========================================================
APPROACH 5 : DEQUE ONLY
========================================================

IDEA:

Palindrome means:

front == back

Deque naturally supports both.


--------------------------------------------------------
CODE
--------------------------------------------------------

public static boolean checkPalindrome(String s)
{
    if(s == null)
        return false;

    s = s.replaceAll("\\s+","");

    Deque<Character> dq =
            new ArrayDeque<>();

    for(char ch : s.toCharArray())
    {
        dq.addLast(
            Character.toLowerCase(ch));
    }

    while(dq.size() > 1)
    {
        if(dq.removeFirst()
                !=
           dq.removeLast())
        {
            return false;
        }
    }

    return true;
}


--------------------------------------------------------
COMPLEXITY
--------------------------------------------------------

Time  : O(n)
Space : O(n)


--------------------------------------------------------
TRADE OFF
--------------------------------------------------------

✔ Nice use of Deque.
✔ Cleaner than Queue+Stack.
✘ Still extra space.


========================================================
INTERVIEW DISCUSSION
========================================================

If interviewer asks:

"Can you optimize?"

Mention:

1. Queue + Stack -> O(n) space
2. Recursion -> O(n) stack
3. Optimal -> Two Pointers O(1)


========================================================
COMPARISON TABLE
========================================================

Approach              Time      Space

Two Pointers          O(n)      O(1)   ✅
Recursion             O(n)      O(n)
Substring Recursion   O(n²)     O(n)
Stack                 O(n)      O(n)
Queue + Stack         O(n)      O(n)
Deque                 O(n)      O(n)


========================================================
PATTERN RECOGNITION
========================================================

Need reverse order?
→ Stack

Need front and rear operations?
→ Deque

Need optimal palindrome checking?
→ Two Pointers

Need recursion practice?
→ Recursive indices solution


========================================================
FOLLOW UPS
========================================================

LC 125 : Valid Palindrome
LC 680 : Valid Palindrome II
LC 234 : Palindrome Linked List
LC 647 : Palindromic Substrings
LC 5   : Longest Palindromic Substring
*/