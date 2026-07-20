/*
* =========================================================
PROBLEM: Largest Unique Number
=========================================================

Given an integer array A, return the largest integer
that occurs exactly once.

If no such integer exists, return -1.

Example:

Input:
[5,7,3,9,4,9,8,3,1]

Output:
8

Explanation:

Frequency:

5 -> 1
7 -> 1
3 -> 2
9 -> 2
4 -> 1
8 -> 1
1 -> 1

Unique numbers:

5,7,4,8,1

Largest:

8

---------------------------------------------------------
OBSERVATION
---------------------------------------------------------

Question contains two important words:

1. Unique
2. Largest

---------------------------------------------------------

"Unique"

↓

Need frequency information.

---------------------------------------------------------

"Largest"

↓

Need to track maximum among valid candidates.

---------------------------------------------------------

This immediately suggests:

Frequency Map
+
Candidate Tracking

---------------------------------------------------------
INTUITION
---------------------------------------------------------

Step 1:

Count frequency of every number.

Example:

[2,2,1,5,5,7]

Frequency:

2 -> 2
1 -> 1
5 -> 2
7 -> 1

---------------------------------------------------------

Step 2:

Traverse frequency map.

Whenever:

frequency == 1

it becomes a candidate.

Keep updating:

maxUnique

---------------------------------------------------------
MULTIPLE APPROACHES
---------------------------------------------------------

APPROACH 1:
Brute Force

For every number:

Count occurrences by traversing entire array.

Time:

O(n²)

Space:

O(1)

---------------------------------------------------------

APPROACH 2:
Sorting

Sort array.

Unique numbers become easy to identify.

Time:

O(n log n)

Space:

Depends on sorting algorithm.

---------------------------------------------------------

APPROACH 3:
Frequency Map (Optimal)

1. Count frequencies.
2. Find largest number with frequency = 1.

Time:

O(n)

Space:

O(k)

(k = unique numbers)

---------------------------------------------------------
VISUALIZATION
---------------------------------------------------------

Input:

[5,7,3,9,4,9,8,3,1]

----------------------------------
Pass 1
----------------------------------

5 -> 1
7 -> 1
3 -> 2
9 -> 2
4 -> 1
8 -> 1
1 -> 1

----------------------------------
Pass 2
----------------------------------

Unique numbers:

5
7
4
8
1

maxUnique:

-1
↓

5
↓

7
↓

8

Answer:

8

---------------------------------------------------------
DRY RUN
---------------------------------------------------------

Input:

[2,2,3,4]

----------------------------------
Frequency Map
----------------------------------

2 -> 2
3 -> 1
4 -> 1

----------------------------------
Candidate Tracking
----------------------------------

maxUnique = -1

3 is unique

maxUnique = 3

4 is unique

maxUnique = 4

Return:

4

---------------------------------------------------------
CODE
---------------------------------------------------------

public int largestUniqueNumber(int[] A)
{
    int maxUnique = -1;

    HashMap<Integer,Integer> freq =
            new HashMap<>();

    for(int num : A)
    {
        freq.put(
                num,
                freq.getOrDefault(num,0)+1
        );
    }

    for(int num : freq.keySet())
    {
        if(freq.get(num) == 1)
        {
            maxUnique =
                    Math.max(maxUnique,num);
        }
    }

    return maxUnique;
}

---------------------------------------------------------
ALTERNATIVE USING ENTRYSET
---------------------------------------------------------

for(Map.Entry<Integer,Integer> entry
        : freq.entrySet())
{
    if(entry.getValue() == 1)
    {
        maxUnique =
                Math.max(
                        maxUnique,
                        entry.getKey()
                );
    }
}

---------------------------------------------------------
COMPLEXITY
---------------------------------------------------------

Time:

Building Frequency Map:

O(n)

Traversing Map:

O(k)

Overall:

O(n)

---------------------------------------------------------

Space:

O(k)

Worst Case:

O(n)

---------------------------------------------------------
IMPORTANT LEARNINGS
---------------------------------------------------------

1.

Need occurrence count?

↓

Frequency Map

---------------------------------------------------------

2.

Need min/max among filtered elements?

↓

Candidate Tracking

---------------------------------------------------------

3.

Do not immediately think of sorting.

Frequency Map often gives linear solution.

---------------------------------------------------------

4.

Candidate tracking pattern:

answer = Math.max(answer,candidate);

appears very frequently.

---------------------------------------------------------
COMMON MISTAKES
---------------------------------------------------------

1.

Returning first unique number instead of largest.

---------------------------------------------------------

2.

Forgetting to initialize:

maxUnique = -1

If no unique number exists,
answer must be -1.

---------------------------------------------------------

3.

Using brute force counting.

Leads to:

O(n²)

---------------------------------------------------------

4.

Sorting immediately.

Works but not optimal.

---------------------------------------------------------
PATTERN RECOGNITION
---------------------------------------------------------

Question says:

"occurs once"

↓

Frequency Map

---------------------------------------------------------

Question says:

"largest"
"smallest"
"maximum"
"minimum"

↓

Candidate Tracking

---------------------------------------------------------

Question says both:

"largest unique"

↓

Frequency Map
+
Candidate Tracking

---------------------------------------------------------
INTERVIEW QUESTIONS
---------------------------------------------------------

1.

Can we solve without extra space?

Yes.

Sort array first.

Time:

O(n log n)

Space:

Depends on sorting.

---------------------------------------------------------

2.

What if numbers are bounded?

Example:

0 <= A[i] <= 1000

Use frequency array.

---------------------------------------------------------

3.

What if stream of numbers arrives continuously?

Maintain:

Frequency Map
+
TreeSet of unique numbers

Largest unique can then be obtained quickly.

---------------------------------------------------------
RELATED PROBLEMS
---------------------------------------------------------

LC217  Contains Duplicate
LC219  Contains Duplicate II
LC242  Valid Anagram
LC387  First Unique Character
LC1207 Unique Number of Occurrences
First Non-Repeating Character in Stream

---------------------------------------------------------
RECOGNITION CHEAT SHEET
---------------------------------------------------------

Need duplicate detection?

→ HashSet

Need counts?

→ Frequency Map

Need largest/smallest among valid elements?

→ Candidate Tracking

Need largest unique?

→ Frequency Map
  +
  Candidate Tracking

Need first unique?

→ Frequency Map
  +
  Order Preservation

=========================================================
BIG IDEA
=========================================================

Largest Unique Number

=

Frequency Counting

+

Maximum Candidate Tracking

=========================================================
* */