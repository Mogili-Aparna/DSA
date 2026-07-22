package HashTable;
/**
 =========================================================
 PROBLEM
 =========================================================

 Longest Palindrome (LC409)

 Given a string s consisting of uppercase/lowercase
 letters, return the length of the longest palindrome
 that can be built using those letters.

 Characters are case-sensitive.

 Examples:

 Input:
 "abccccdd"

 Output:
 7

 Explanation:

 One possible palindrome:

 "dccaccd"

 ---------------------------------------------------------
 OBSERVATION
 ---------------------------------------------------------

 Palindrome Property:

 1. Characters always appear in PAIRS.

 Examples:

 aa
 bb
 cccc

 can be placed symmetrically.

 ---------------------------------------------------------

 2. At most ONE character may appear odd number
 of times.

 That odd character sits in the CENTER.

 Examples:

 abcba
 racecar

 Only one center character is allowed.

 ---------------------------------------------------------
 KEY INSIGHT
 ---------------------------------------------------------

 Even frequency:

 All characters can be used.

 Example:

 aaaa

 Contribution:

 4

 ---------------------------------------------------------

 Odd frequency:

 Only the largest even part can be used.

 Example:

 aaa

 Contribution:

 2

 One character remains unused.

 ---------------------------------------------------------

 Example:

 aaaaa

 Contribution:

 4

 One 'a' remains.

 ---------------------------------------------------------
 MY INITIAL THOUGHT PROCESS
 =========================================================

 You correctly noticed:

 1. Repeating characters contribute to palindrome.

 2. Non-repeating characters need special handling.

 You tried handling:

 1. No repeating characters.
 2. Exactly two characters.

 This works for some cases but introduces
 special-case logic.

 Usually when we start writing:

 if(case1)
 if(case2)
 if(case3)

 it indicates that there is a deeper invariant.

 The invariant here is:

 A palindrome can have:

 0 OR 1 odd frequency character.

 Everything follows from this property.

 ---------------------------------------------------------
 INTUITION
 ---------------------------------------------------------

 For every frequency:

 Take all possible PAIRS.

 Pairs:

 freq / 2

 Characters contributed:

 (freq / 2) * 2

 ---------------------------------------------------------

 Examples:

 freq = 1

 (1/2)*2

 0*2

 0

 ---------------------------------------------------------

 freq = 3

 (3/2)*2

 1*2

 2

 ---------------------------------------------------------

 freq = 5

 (5/2)*2

 2*2

 4

 ---------------------------------------------------------

 freq = 8

 (8/2)*2

 4*2

 8

 ---------------------------------------------------------
 IMPORTANT JAVA OBSERVATION
 ---------------------------------------------------------

 This works because Java integer division
 removes decimal values.

 Examples:

 5 / 2 = 2

 3 / 2 = 1

 1 / 2 = 0

 So:

 (freq / 2) * 2

 gives:

 Largest Even Number <= freq

 ---------------------------------------------------------

 Examples:

 1 → 0
 2 → 2
 3 → 2
 4 → 4
 5 → 4
 6 → 6
 7 → 6

 Exactly what we need.

 ---------------------------------------------------------
 MATHEMATICAL EQUIVALENTS
 ---------------------------------------------------------

 These are all equivalent:

 1.

 (freq / 2) * 2

 ---------------------------------------------------------

 2.

 freq - (freq % 2)

 ---------------------------------------------------------

 3.

 if(freq % 2 == 0)
 use freq
 else
 use freq - 1

 ---------------------------------------------------------
 BIG IDEA
 ---------------------------------------------------------

 Palindrome wants:

 PAIRS

 freq / 2

 gives number of pairs.

 Multiply by 2

 to get number of characters participating
 in those pairs.

 ---------------------------------------------------------
 MULTIPLE APPROACHES
 ---------------------------------------------------------

 APPROACH 1 : Simulation

 Try constructing palindrome manually.

 Complicated.

 ---------------------------------------------------------

 APPROACH 2 : Frequency Map (Optimal)

 1. Count frequencies.

 2. Use all even parts.

 3. If any odd frequency exists,
 add one center character.

 ---------------------------------------------------------

 APPROACH 3 : HashSet Trick

 Traverse characters:

 If character not present:

 add to set.

 Else:

 remove from set and answer += 2.

 Finally:

 if set not empty:

 answer += 1.

 Very elegant approach.

 ---------------------------------------------------------
 VISUALIZATION
 ---------------------------------------------------------

 Input:

 "abccccdd"

 Frequency:

 a -> 1
 b -> 1
 c -> 4
 d -> 2

 Contributions:

 a:

 0

 b:

 0

 c:

 4

 d:

 2

 Length:

 6

 Odd characters exist?

 YES

 Add center:

 +1

 Answer:

 7

 ---------------------------------------------------------
 DRY RUN
 ---------------------------------------------------------

 Input:

 "aaaabbbcc"

 Frequency:

 a -> 4
 b -> 3
 c -> 2

 Contribution:

 a:

 4

 b:

 2

 c:

 2

 Length:

 8

 Odd exists?

 YES

 Answer:

 9

 Possible palindrome:

 abcbabacb

 ---------------------------------------------------------
 OPTIMAL ALGORITHM
 ---------------------------------------------------------

 1.

 Count frequencies.

 2.

 For every frequency:

 length += (freq/2)*2

 3.

 If frequency is odd:

 oddPresent = true

 4.

 If oddPresent:

 length++

 ---------------------------------------------------------
 COMPLEXITY
 ---------------------------------------------------------

 Time:

 O(n)

 Space:

 O(k)

 k = unique characters

 ---------------------------------------------------------
 COMMON MISTAKES
 ---------------------------------------------------------

 1.

 Adding entire odd frequency.

 Example:

 aaa

 Contribution is:

 2

 NOT

 3

 ---------------------------------------------------------

 2.

 Handling special cases separately.

 The invariant automatically handles all cases.

 ---------------------------------------------------------

 3.

 Forgetting that only ONE odd frequency
 can contribute its extra character.

 ---------------------------------------------------------

 4.

 Thinking all odd frequencies can contribute
 their extra character.

 Wrong:

 aaa + bbb

 Cannot contribute:

 3 + 3

 Only one extra character can be used.

 ---------------------------------------------------------
 PATTERN RECOGNITION
 ---------------------------------------------------------

 Question mentions:

 Palindrome

 ↓

 Think:

 Pairs

 ↓

 Frequency Counting

 ↓

 At most one odd center.

 ---------------------------------------------------------
 INTERVIEW QUESTIONS
 ---------------------------------------------------------

 Q1.

 Why only one odd frequency?

 Because palindrome has only one center.

 ---------------------------------------------------------

 Q2.

 Why:

 (freq/2)*2 ?

 Because:

 freq/2

 gives number of complete pairs.

 Multiply by 2 to get participating characters.

 ---------------------------------------------------------

 Q3.

 Can we avoid HashMap?

 Yes.

 Use:

 int[128]

 for ASCII characters.

 ---------------------------------------------------------

 Q4.

 Can this be solved using HashSet?

 Yes.

 Set stores currently unpaired characters.

 ---------------------------------------------------------
 HASHSET APPROACH
 ---------------------------------------------------------

 Idea:

 Every pair contributes:

 +2

 Set stores unmatched characters.

 Example:

 abccccdd

 a → add
 b → add
 c → add
 c → remove (+2)
 c → add
 c → remove (+2)
 d → add
 d → remove (+2)

 Length:

 6

 Set:

 [a,b]

 Not empty:

 +1

 Answer:

 7

 ---------------------------------------------------------
 RELATED PROBLEMS
 ---------------------------------------------------------

 LC266  Palindrome Permutation
 LC49   Group Anagrams
 LC242  Valid Anagram
 LC409  Longest Palindrome
 LC2131 Longest Palindrome by Concatenating

 ---------------------------------------------------------
 RECOGNITION CHEAT SHEET
 ---------------------------------------------------------

 Need counts?

 → Frequency Map

 Need palindrome?

 → Think Pairs

 Odd frequencies?

 → Use freq-1

 Any odd exists?

 → Add one center character

 Formula:

 length += (freq/2)*2

 if(anyOdd)
 length++

 =========================================================
 BIG IDEA
 =========================================================

 Longest Palindrome

 =

 Use all possible PAIRS

 +

 At most ONE odd character in center.

 Palindrome Property:

 0 or 1 odd frequencies allowed.

 =========================================================
 */

import java.util.HashMap;

public class LengthOfLongestPalindrome {

    public int longestPalindrome(String s) {

        if (s == null || s.isEmpty())
            return 0;

        HashMap<Character, Integer> freq =
                new HashMap<>();

        for (char ch : s.toCharArray()) {
            freq.put(
                    ch,
                    freq.getOrDefault(ch, 0) + 1
            );
        }

        int length = 0;
        boolean oddPresent = false;

        for (int count : freq.values()) {

            length += (count / 2) * 2;

            if (count % 2 == 1)
                oddPresent = true;
        }

        if (oddPresent)
            length++;

        return length;
    }
}
/**
 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 Building Frequency Map:

 for(char ch : s.toCharArray())

 Time:

 O(n)

 ---------------------------------------------------------

 Traversing frequency map:

 for(int freq : map.values())

 Time:

 O(k)

 k = unique characters.

 Since:

 k <= 52
 (uppercase + lowercase)

 Overall:

 O(n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 HashMap stores frequency of unique characters.

 Worst Case:

 Every character is unique.

 Space:

 O(k)

 For English letters:

 k <= 52

 Thus practically:

 O(1)

 Generalized:

 O(k)
 */