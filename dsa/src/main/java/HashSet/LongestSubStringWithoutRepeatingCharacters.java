package HashSet;
/**
 =========================================================
 PROBLEM
 =========================================================

 Longest Substring Without Repeating Characters
 (LC3)

 Given a string s, return the length of
 the longest substring without repeating
 characters.

 Example:

 s = "abcabcbb"

 Answer = 3

 Longest substrings:

 "abc"
 "bca"
 "cab"

 =========================================================
 OBSERVATION
 =========================================================

 Brute Force:

 Generate every substring.

 For every substring:

 Check duplicates.

 Time:

 O(n³)

 Even with optimization:

 O(n²)

 Too slow.

 ---------------------------------------------------------

 Question contains keywords:

 Longest
 Substring
 Condition on window

 ↓

 Sliding Window.

 =========================================================
 INTUITION
 =========================================================

 We maintain a window:

 [L ... R]

 Condition:

 All characters inside window
 must be unique.

 ---------------------------------------------------------

 If next character is NOT duplicate:

 Expand window.

 ---------------------------------------------------------

 If duplicate appears:

 Shrink from left until window
 becomes valid again.

 =========================================================
 MY INITIAL APPROACH
 =========================================================

 You used:

 Sliding Window
 +
 HashSet

 Excellent observation:

 HashSet can tell:

 "Does duplicate exist?"

 but cannot tell:

 "Where was duplicate seen?"

 Therefore:

 HashSet solution shrinks
 one character at a time.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Brute Force
 ---------------------------------------------------------

 Generate all substrings.

 Check uniqueness.

 Time Complexity:

 O(n³)

 Optimized brute:

 O(n²)

 Space:

 O(1)

 ---------------------------------------------------------
 APPROACH 2
 Sliding Window + HashSet
 ---------------------------------------------------------

 Maintain current window.

 HashSet stores characters
 currently inside window.

 If duplicate appears:

 remove from left until valid.

 Time:

 O(n)

 Space:

 O(min(n, charset))

 ---------------------------------------------------------
 APPROACH 3
 Sliding Window + HashMap
 (Better Practical Optimization)
 ---------------------------------------------------------

 Store:

 character → last index

 Instead of removing one by one,
 jump directly.

 Time:

 O(n)

 Space:

 O(min(n, charset))

 =========================================================
 VISUALIZATION
 =========================================================

 String:

 abcabcbb

 ---------------------------------------------------------

 L=0 R=0

 [a]

 max=1

 ---------------------------------------------------------

 L=0 R=1

 [ab]

 max=2

 ---------------------------------------------------------

 L=0 R=2

 [abc]

 max=3

 ---------------------------------------------------------

 R=3

 Duplicate:

 a

 HashSet approach:

 remove a

 Window:

 [bca]

 ---------------------------------------------------------

 HashMap approach:

 Jump directly.

 lastIndex(a)=0

 L=0 → 1

 =========================================================
 DRY RUN
 =========================================================

 Input:

 "pwwkew"

 ---------------------------------------------------------

 p

 window = [p]

 max=1

 ---------------------------------------------------------

 w

 window = [pw]

 max=2

 ---------------------------------------------------------

 w again

 duplicate.

 Remove:

 p

 window=[w]

 Still duplicate.

 Remove:

 w

 window=[]

 Insert w.

 window=[w]

 ---------------------------------------------------------

 k

 window=[wk]

 max=2

 ---------------------------------------------------------

 e

 window=[wke]

 max=3

 =========================================================
 CODE EXPLANATION
 =========================================================

 ---------------------------------------------------------
 HASHSET SOLUTION
 ---------------------------------------------------------

 HashSet contains all characters
 inside current window.

 While duplicate exists:

 remove left character.

 Then expand right.

 ---------------------------------------------------------

 Important realization:

 Every character enters and leaves
 window at most once.

 Therefore:

 Overall complexity remains O(n).

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 HASHSET APPROACH

 ---------------------------------------------------------

 At first glance:

 Nested loop exists.

 Looks like:

 O(n²)

 But actually:

 Every character:

 1. inserted once
 2. removed once

 Therefore:

 Total operations:

 ≤ 2n

 Time:

 O(n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 HashSet stores characters
 inside current window.

 Worst case:

 all characters distinct.

 Space:

 O(min(n, charset))

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Keywords:

 Longest / Shortest
 Substring / Subarray

 ↓

 Think Sliding Window.

 ---------------------------------------------------------

 2.

 Need duplicate detection?

 ↓

 HashSet.

 ---------------------------------------------------------

 3.

 Need duplicate POSITION?

 ↓

 HashMap.

 ---------------------------------------------------------

 4.

 Main pattern:

 Sliding Window.

 HashSet/HashMap are helper
 data structures.

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Thinking nested loop means O(n²).

 Not here.

 Pointers only move forward.

 ---------------------------------------------------------

 Mistake 2:

 Using indexOf repeatedly.

 Complexity becomes O(n²).

 ---------------------------------------------------------

 Mistake 3:

 Thinking HashSet can jump
 to duplicate index.

 Impossible.

 HashSet stores only existence.

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Question contains:

 Longest
 Substring
 Without Repeating

 ↓

 Variable Sliding Window.

 ---------------------------------------------------------

 Need duplicate detection.

 ↓

 HashSet.

 ---------------------------------------------------------

 Need duplicate position.

 ↓

 HashMap.

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Why is HashSet solution O(n)?

 Because every character enters
 and leaves window once.

 ---------------------------------------------------------

 Q2.

 Why HashMap?

 Because it stores:

 character → last index

 allowing direct jumps.

 ---------------------------------------------------------

 Q3.

 Which solution is better?

 Asymptotically both:

 O(n)

 Practically:

 HashMap is slightly better.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC904 Fruit Into Baskets

 LC424 Longest Repeating Character Replacement

 LC76 Minimum Window Substring

 LC438 Find All Anagrams

 LC567 Permutation in String

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 Longest/Shortest
 +
 Substring/Subarray
 +
 Condition

 ↓

 Sliding Window

 ---------------------------------------------------------

 Need existence only

 ↓

 HashSet

 ---------------------------------------------------------

 Need positions/frequencies

 ↓

 HashMap

 =========================================================
 BIG IDEA
 =========================================================

 This problem is NOT primarily
 a HashSet problem.

 It is:

 Variable Sliding Window

 implemented using:

 HashSet

 or

 HashMap.

 ---------------------------------------------------------

 HashSet Version:

 Duplicate exists?

 ↓

 Shrink one step at a time.

 ---------------------------------------------------------

 HashMap Version:

 Duplicate exists?

 ↓

 Jump directly to duplicate's
 next position.

 =========================================================
 */

import java.util.HashMap;
import java.util.HashSet;

class LongestSubStringWithoutRepeatingCharacters {

    /*
    =====================================================
    APPROACH 1
    Sliding Window + HashSet
    =====================================================
    */

    public int lengthOfLongestSubstringSet(
            String s)
    {
        HashSet<Character> set =
                new HashSet<>();

        int max = 0;

        for(int l=0,r=0;r<s.length();)
        {
            while(!set.add(s.charAt(r)))
            {
                set.remove(s.charAt(l++));
            }

            max =
                    Math.max(
                            max,
                            r-l+1
                    );

            r++;
        }

        return max;
    }


    /*
    =====================================================
    APPROACH 2
    Sliding Window + HashMap
    =====================================================
    */

    public int lengthOfLongestSubstringMap(
            String s)
    {
        HashMap<Character,Integer> map =
                new HashMap<>();

        int max = 0;

        for(int l=0,r=0;r<s.length();r++)
        {
            char ch = s.charAt(r);

            if(map.containsKey(ch))
            {
                l =
                        Math.max(
                                l,
                                map.get(ch)+1
                        );
            }

            map.put(ch,r);

            max =
                    Math.max(
                            max,
                            r-l+1
                    );
        }

        return max;
    }
}
