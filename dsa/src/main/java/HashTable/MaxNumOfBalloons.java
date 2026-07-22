package HashTable;
import java.util.HashMap;
/**
 =========================================================
 PROBLEM
 =========================================================

 Given a string, determine the maximum number of times
 the word "balloon" can be formed using characters
 from the string.

 Each character can be used only once.

 Examples:

 Input:
 "balloonballoon"

 Output:
 2

 ---------------------------------------------------------
 OBSERVATION
 ---------------------------------------------------------

 The word "balloon" requires:

 b -> 1
 a -> 1
 l -> 2
 o -> 2
 n -> 1

 Notice that some characters are required
 multiple times.

 This is the most important observation.

 ---------------------------------------------------------
 INITIAL THOUGHT PROCESS (MY APPROACH)
 ---------------------------------------------------------

 You initially simulated making one balloon at a time.

 Example:

 Frequency:

 b -> 2
 a -> 2
 l -> 4
 o -> 4
 n -> 2

 Subtract:

 b--
 a--
 l -= 2
 o -= 2
 n--

 One balloon formed.

 Repeat again.

 This works correctly.

 Time Complexity:

 O(n + answer)

 where answer = number of balloons formed.

 ---------------------------------------------------------
 KEY REALIZATION
 ---------------------------------------------------------

 Repeated subtraction is mathematically equivalent to:

 availableCount / requiredCount

 Because eventually one character becomes insufficient
 first.

 That character becomes the bottleneck.

 ---------------------------------------------------------
 INTUITION
 ---------------------------------------------------------

 Suppose frequencies are:

 b = 5
 a = 3
 l = 8
 o = 4
 n = 2

 How many balloons can each character support?

 b:

 5 / 1 = 5

 a:

 3 / 1 = 3

 l:

 8 / 2 = 4

 o:

 4 / 2 = 2

 n:

 2 / 1 = 2

 The limiting character is:

 o or n

 Therefore answer:

 min(5,3,4,2,2)

 =

 2

 ---------------------------------------------------------
 BIG IDEA
 ---------------------------------------------------------

 Maximum copies of a word

 =

 Minimum of:

 available
 ----------
 required

 for every required character.

 ---------------------------------------------------------
 MULTIPLE APPROACHES
 ---------------------------------------------------------

 APPROACH 1 : Brute Force

 Try forming balloon repeatedly by searching
 characters every time.

 Time:

 O(n²)

 ---------------------------------------------------------

 APPROACH 2 : Simulation (MY APPROACH)

 Count frequencies.

 Repeatedly remove:

 b--
 a--
 l -= 2
 o -= 2
 n--

 until impossible.

 Time:

 O(n + answer)

 ---------------------------------------------------------

 APPROACH 3 : Frequency + Minimum Ratio
 (OPTIMAL)

 1. Count frequencies.
 2. Compute:

 available / required

 3. Return minimum.

 Time:

 O(n)

 ---------------------------------------------------------
 VISUALIZATION
 ---------------------------------------------------------

 Input:

 "balloonballoooon"

 Frequency:

 b -> 2
 a -> 2
 l -> 4
 o -> 6
 n -> 2

 Possible balloons:

 b:

 2/1 = 2

 a:

 2/1 = 2

 l:

 4/2 = 2

 o:

 6/2 = 3

 n:

 2/1 = 2

 Answer:

 2

 ---------------------------------------------------------
 DRY RUN
 ---------------------------------------------------------

 Input:

 "balloonballoon"

 Frequency:

 b -> 2
 a -> 2
 l -> 4
 o -> 4
 n -> 2

 Possible balloons:

 2
 2
 2
 2
 2

 Minimum:

 2

 Answer:

 2

 ---------------------------------------------------------
 COMPLEXITY
 ---------------------------------------------------------

 HashMap Solution:

 Time:

 O(n)

 Space:

 O(1)

 (Only lowercase characters exist.)

 ---------------------------------------------------------
 IMPORTANT LEARNINGS
 ---------------------------------------------------------

 1.

 Questions containing:

 "How many times can X be formed?"

 usually indicate:

 Frequency Counting
 +
 Minimum Ratio

 ---------------------------------------------------------

 2.

 Repeated subtraction and minimum ratio
 are equivalent.

 Repeated subtraction:

 b--
 a--
 l -= 2
 o -= 2
 n--

 Eventually one character becomes insufficient.

 That character determines the answer.

 ---------------------------------------------------------

 3.

 This is actually a Resource Allocation problem.

 Think of:

 Ingredients
 Recipes
 Teams
 Groups

 ---------------------------------------------------------
 COMMON MISTAKES
 ---------------------------------------------------------

 1.

 Forgetting:

 l requires TWO copies.

 o requires TWO copies.

 ---------------------------------------------------------

 2.

 Doing:

 min(a,b,l,o,n)

 instead of:

 min(a,b,l/2,o/2,n)

 ---------------------------------------------------------

 3.

 Using:

 text.length() < 5

 One balloon requires:

 7 characters.

 ---------------------------------------------------------

 4.

 Not using:

 getOrDefault()

 Missing characters should contribute 0.

 ---------------------------------------------------------
 PATTERN RECOGNITION
 ---------------------------------------------------------

 Question says:

 "How many copies can be formed?"

 ↓

 Frequency Counting

 ↓

 Resource Allocation

 ↓

 Minimum Ratio

 ---------------------------------------------------------
 INTERVIEW QUESTIONS
 ---------------------------------------------------------

 1.

 Why minimum?

 Because the least available required resource
 limits production.

 ---------------------------------------------------------

 2.

 Can simulation work?

 Yes.

 But minimum ratio is simpler and cleaner.

 ---------------------------------------------------------

 3.

 Where else does this pattern appear?

 - Ransom Note
 - Rearrange Characters To Make Target String
 - Recipe problems
 - Team formation problems
 - Inventory allocation problems

 ---------------------------------------------------------
 RELATED PROBLEMS
 ---------------------------------------------------------

 LC383  Ransom Note
 LC242  Valid Anagram
 LC1160 Find Words That Can Be Formed
 LC2287 Rearrange Characters to Make Target String

 ---------------------------------------------------------
 RECOGNITION CHEAT SHEET
 ---------------------------------------------------------

 Need counts?

 → Frequency Map

 Need to form another string?

 → Frequency Comparison

 Need maximum copies?

 → available / required

 ↓

 Take Minimum

 =========================================================
 BIG IDEA
 =========================================================

 Maximum Number Of Balloons

 =

 Frequency Counting

 +

 Resource Allocation

 +

 Minimum Ratio (Bottleneck Resource)

 Repeated Subtraction

 ⇔

 Minimum Ratio

 =========================================================
 */

public class MaxNumOfBalloons {

    public int maxNumberOfBalloons(String text) {

        if (text == null || text.length() < 7)
            return 0;

        HashMap<Character, Integer> freq =
                new HashMap<>();

        for (char ch : text.toCharArray()) {
            freq.put(
                    ch,
                    freq.getOrDefault(ch, 0) + 1
            );
        }

        int b = freq.getOrDefault('b', 0);
        int a = freq.getOrDefault('a', 0);
        int l = freq.getOrDefault('l', 0);
        int o = freq.getOrDefault('o', 0);
        int n = freq.getOrDefault('n', 0);

        return Math.min(
                Math.min(
                        Math.min(a, b),
                        Math.min(l / 2, o / 2)
                ),
                n
        );
    }
}