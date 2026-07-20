package HashTable;


import java.util.HashMap;
import java.util.LinkedHashMap;

public class FirstNonRepeatingCharacter {
    public int firstUniqChar(String s) {
        if(s == null) return -1;
        HashMap<Character,Integer> fc = new LinkedHashMap<>();
        HashMap<Character,Integer> firstIndiciesMap = new LinkedHashMap<>();

        for(int i=0;i<s.length();i++){
            fc.put(s.charAt(i),fc.getOrDefault(s.charAt(i), 0)+1);
            if(!firstIndiciesMap.containsKey(s.charAt(i))){
                firstIndiciesMap.put(s.charAt(i),i);
            }
        }
        System.out.println(fc);
        System.out.println(firstIndiciesMap);
        for(Character ch : fc.keySet()){
            if(fc.get(ch)==1){
                return firstIndiciesMap.get(ch);
            }
        }
        return -1;
    }
}

/*
* =========================================================
PROBLEM: First Unique Character in a String (LC387)
=========================================================

Given a string s, return the index of the first non-repeating
character. If no such character exists, return -1.

Example:

Input:
s = "leetcode"

Output:
0

Explanation:
'l' occurs only once and appears first.

---------------------------------------------------------
OBSERVATION
---------------------------------------------------------

To know whether a character is unique, we need its frequency.

Questions:

1. How many times did a character occur?
2. Which unique character appears first?

This immediately suggests:

Frequency Counting + Order Preservation

---------------------------------------------------------
INTUITION
---------------------------------------------------------

Suppose:

s = "loveleetcode"

Characters:

l -> 2
o -> 2
v -> 1
e -> 4
t -> 1
c -> 1
d -> 1

The first character whose frequency is 1 is:

v

Its index:

2

So the solution becomes:

STEP 1:
Count frequency of every character.

STEP 2:
Traverse string from left to right.

The first character whose frequency is 1
is the answer.

---------------------------------------------------------
MULTIPLE APPROACHES
---------------------------------------------------------

APPROACH 1:
Brute Force

For every character:
    count occurrences again.

Time:
O(n²)

Space:
O(1)

---------------------------------------------------------

APPROACH 2:
HashMap Frequency + Second Pass

1. Count frequencies.
2. Traverse string again.
3. Return first character having count = 1.

Time:
O(n)

Space:
O(k)

(k = unique characters)

---------------------------------------------------------

APPROACH 3:
Array Frequency (Best if lowercase letters)

Since:

'a' to 'z'

Use:

int[] freq = new int[26];

Time:
O(n)

Space:
O(1)

---------------------------------------------------------
VISUALIZATION
---------------------------------------------------------

String:

"loveleetcode"

Pass 1:

l -> 2
o -> 2
v -> 1
e -> 4
t -> 1
c -> 1
d -> 1

Pass 2:

l -> freq = 2 ❌
o -> freq = 2 ❌
v -> freq = 1 ✅

Answer:

2

---------------------------------------------------------
DRY RUN
---------------------------------------------------------

Input:

s = "leetcode"

----------------------------------
Pass 1
----------------------------------

l -> 1
e -> 3
t -> 1
c -> 1
o -> 1
d -> 1

----------------------------------
Pass 2
----------------------------------

i=0

s.charAt(0) = 'l'

freq['l'] = 1

Return:

0

---------------------------------------------------------
MY SOLUTION
---------------------------------------------------------

HashMap<Character,Integer> fc
HashMap<Character,Integer> firstIndicesMap

Idea:

1. Store frequencies.
2. Store first occurrence index.
3. Iterate through insertion order.
4. Return first character whose count is 1.

This is CORRECT.

Time:
O(n)

Space:
O(k)

---------------------------------------------------------
OPTIMAL INTERVIEW SOLUTION
---------------------------------------------------------

public int firstUniqChar(String s)
{
    Map<Character,Integer> freq =
            new HashMap<>();

    for(char ch : s.toCharArray())
    {
        freq.put(
                ch,
                freq.getOrDefault(ch,0)+1
        );
    }

    for(int i=0;i<s.length();i++)
    {
        if(freq.get(s.charAt(i)) == 1)
            return i;
    }

    return -1;
}

---------------------------------------------------------
BEST SOLUTION (LOWERCASE LETTERS)
---------------------------------------------------------

public int firstUniqChar(String s)
{
    int[] freq = new int[26];

    for(char ch : s.toCharArray())
    {
        freq[ch-'a']++;
    }

    for(int i=0;i<s.length();i++)
    {
        if(freq[s.charAt(i)-'a'] == 1)
            return i;
    }

    return -1;
}

---------------------------------------------------------
COMPLEXITY
---------------------------------------------------------

HashMap Solution:

Time:
O(n)

Space:
O(k)

------------------------------------------

Array Solution:

Time:
O(n)

Space:
O(1)

---------------------------------------------------------
IMPORTANT LEARNINGS
---------------------------------------------------------

1.

Need occurrence count?

→ Frequency Map

------------------------------------------

2.

Need first unique element?

→ Frequency +
Second Left-to-Right Traversal

------------------------------------------

3.

Need order preservation?

→ LinkedHashMap

------------------------------------------

4.

Sometimes storing extra information
(first index map) can simplify thinking,
even if it isn't necessary.

---------------------------------------------------------
COMMON MISTAKES
---------------------------------------------------------

1.

Returning first frequency = 1
without preserving original order.

Wrong:

for(char ch : freq.keySet())

if HashMap is used.

HashMap order is not guaranteed.

---------------------------------------------------------

2.

Doing:

s.indexOf(ch)

inside loop.

This may increase complexity.

---------------------------------------------------------

3.

Using brute force counting repeatedly.

Leads to:

O(n²)

---------------------------------------------------------

4.

Forgetting edge cases:

s = ""

s = "aabb"

Answer:

-1

---------------------------------------------------------
PATTERN RECOGNITION
---------------------------------------------------------

Question contains:

"count"
"frequency"
"duplicates"
"occurs once"

↓

Think:

Frequency Map

---------------------------------------------------------

Question contains:

"first"
"leftmost"

↓

Think:

Second Traversal
OR
Order Preservation

---------------------------------------------------------
INTERVIEW QUESTIONS
---------------------------------------------------------

1.

What if string is huge?

Still O(n).

------------------------------------------

2.

What if characters are lowercase only?

Use array instead of HashMap.

------------------------------------------

3.

What if stream of characters arrives continuously?

Need:

Frequency +
Queue

(First Non-Repeating Character in Stream)

------------------------------------------

4.

Can we solve in one pass?

Possible with:

LinkedHashMap / Queue approach.

---------------------------------------------------------
RELATED PROBLEMS
---------------------------------------------------------

LC217  Contains Duplicate
LC242  Valid Anagram
LC49   Group Anagrams
LC219  Contains Duplicate II
LC451  Sort Characters By Frequency
First Non-Repeating Character in Stream

---------------------------------------------------------
RECOGNITION CHEAT SHEET
---------------------------------------------------------

Need counts?

→ Frequency Map

Need duplicate detection?

→ HashSet

Need counts + order?

→ Frequency + Second Pass

Need real-time first unique?

→ Frequency + Queue

Need only lowercase letters?

→ int[26]

=========================================================
BIG IDEA
=========================================================

First Unique Character

=

Frequency Counting

+

Preserve Original Ordering

(or traverse string again)

=========================================================
* */
