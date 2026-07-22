package HashTable;
/**
 =========================================================
 PROBLEM
 =========================================================

 Ransom Note (LC383)

 Given two strings:

 1. ransomNote
 2. magazine

 Return true if ransomNote can be constructed
 using characters from magazine.

 Each character in magazine can be used ONLY ONCE.

 Examples:

 ransomNote = "aa"
 magazine   = "ab"

 Answer:

 false

 ---------------------------------------------------------

 ransomNote = "aa"
 magazine   = "aab"

 Answer:

 true

 =========================================================
 OBSERVATION
 =========================================================

 This is a frequency counting problem.

 Every character required in ransomNote
 must be available in magazine.

 Important:

 Characters may repeat.

 So merely checking presence is not enough.

 Example:

 ransom = "aa"
 magazine = "ab"

 'a' exists in magazine.

 But only once.

 Answer should be false.

 Thus we need COUNTS.

 =========================================================
 INTUITION
 =========================================================

 Think of magazine as an inventory.

 Example:

 magazine = "aab"

 Inventory:

 a -> 2
 b -> 1

 Every character used in ransom note
 consumes inventory.

 Example:

 ransom = "aa"

 Use first 'a':

 a -> 1

 Use second 'a':

 a -> 0

 Inventory never becomes negative.

 Hence answer = true.

 ---------------------------------------------------------

 Example:

 ransom = "aaa"

 Use third 'a':

 a -> -1

 Inventory becomes negative.

 Hence answer = false.

 =========================================================
 My INITIAL APPROACH
 =========================================================

 1. Store frequency of magazine characters.

 2. Traverse ransom note.

 3. Consume inventory one by one.

 4. If frequency becomes negative:

 return false.

 Code:

 for(char ch:ransomNote.toCharArray())
 {
 int val =
 fc.getOrDefault(ch,0)-1;

 if(val < 0)
 return false;

 fc.put(ch,val);
 }

 This is already optimal.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Brute Force
 ---------------------------------------------------------

 For every character in ransom note:

 Search magazine for unused character.

 Mark it as used.

 Time Complexity:

 O(n*m)

 Space Complexity:

 O(m)

 ---------------------------------------------------------
 APPROACH 2
 Frequency Map (Optimal)
 ---------------------------------------------------------

 Build frequency map.

 Consume characters.

 If count becomes negative:

 return false.

 Time Complexity:

 O(m+n)

 Space Complexity:

 O(k)

 k = unique characters.

 ---------------------------------------------------------
 APPROACH 3
 Frequency Array (Best Practical Solution)
 ---------------------------------------------------------

 Since only lowercase letters exist:

 Use:

 int[] freq = new int[26];

 Time Complexity:

 O(m+n)

 Space Complexity:

 O(1)

 =========================================================
 VISUALIZATION
 =========================================================

 magazine:

 "aab"

 Inventory:

 a -> 2
 b -> 1

 ransom:

 "aa"

 Take first 'a'

 a -> 1

 Take second 'a'

 a -> 0

 Inventory never becomes negative.

 Answer:

 true

 ---------------------------------------------------------

 ransom:

 "aaa"

 Take first 'a'

 a -> 1

 Take second 'a'

 a -> 0

 Take third 'a'

 a -> -1

 Inventory exhausted.

 Answer:

 false

 =========================================================
 DRY RUN
 =========================================================

 Input:

 ransom = "aac"

 magazine = "abca"

 Frequency:

 a -> 2
 b -> 1
 c -> 1

 --------------------------------

 Take 'a'

 a -> 1

 Take 'a'

 a -> 0

 Take 'c'

 c -> 0

 All characters satisfied.

 Return true.

 =========================================================
 CODE EXPLANATION
 =========================================================

 Step 1:

 Build frequency map.

 for(char ch : magazine.toCharArray())
 {
 fc.put(
 ch,
 fc.getOrDefault(ch,0)+1
 );
 }

 --------------------------------

 Step 2:

 Consume characters.

 int val =
 fc.getOrDefault(ch,0)-1;

 If:

 val < 0

 Means inventory exhausted.

 Return false.

 Otherwise:

 Store updated frequency.

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 Let:

 m = magazine.length()

 n = ransomNote.length()

 ---------------------------------------------------------

 Building frequency map:

 O(m)

 ---------------------------------------------------------

 Traversing ransom note:

 O(n)

 ---------------------------------------------------------

 HashMap operations:

 get()
 put()

 Average:

 O(1)

 ---------------------------------------------------------

 Overall:

 O(m+n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 HashMap stores frequencies.

 At most:

 k unique characters.

 General:

 O(k)

 For lowercase English letters:

 k <= 26

 Practical Complexity:

 O(1)

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Presence checking is insufficient.

 Need frequencies.

 ---------------------------------------------------------

 2.

 Inventory must be updated after consumption.

 Repeated characters depend on this.

 ---------------------------------------------------------

 3.

 Negative frequency means:

 Resource exhausted.

 ---------------------------------------------------------

 4.

 Magazine acts as inventory.

 Ransom note acts as orders.

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Checking only:

 contains()

 Fails for repeated characters.

 ---------------------------------------------------------

 Mistake 2:

 Not decrementing frequencies.

 Example:

 ransom = "aa"

 magazine = "a"

 Would incorrectly return true.

 ---------------------------------------------------------

 Mistake 3:

 Forgetting:

 getOrDefault()

 Missing characters should contribute 0.

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Question asks:

 "Can A be formed using B?"

 ↓

 Frequency Counting

 ↓

 Inventory Consumption Pattern

 ---------------------------------------------------------

 Question asks:

 "Can we build something using resources?"

 ↓

 Count resources

 ↓

 Consume resources

 ↓

 If resource < 0

 Impossible.

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Why decrement frequencies?

 Because characters may repeat.

 ---------------------------------------------------------

 Q2.

 Can we use two maps?

 Yes.

 One for magazine.

 One for ransom note.

 Compare frequencies.

 But one-map inventory approach
 is cleaner.

 ---------------------------------------------------------

 Q3.

 Can this be optimized further?

 Yes.

 Use:

 int[26]

 instead of HashMap.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC242  Valid Anagram

 LC409  Longest Palindrome

 LC1189 Maximum Number of Balloons

 LC1160 Find Words That Can Be Formed

 LC2287 Rearrange Characters to Make Target String

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 Need counts?

 ↓

 Frequency Map

 --------------------------------

 Need to build one string from another?

 ↓

 Frequency Comparison

 --------------------------------

 Need repeated usage tracking?

 ↓

 Inventory Consumption

 --------------------------------

 Frequency becomes negative?

 ↓

 Impossible.

 =========================================================
 BIG IDEA
 =========================================================

 Magazine = Inventory

 Ransom Note = Orders

 Every character used:

 inventory--

 If inventory becomes negative:

 Construction impossible.

 Pattern:

 Frequency Counting
 +
 Inventory Consumption

 =========================================================
 */

import java.util.HashMap;

public class CanRansomNoteBeConstructedFromMagzine {

    public boolean canConstruct(
            String ransomNote,
            String magazine) {

        HashMap<Character,Integer> freq =
                new HashMap<>();

        for(char ch : magazine.toCharArray()) {
            freq.put(
                    ch,
                    freq.getOrDefault(ch,0)+1
            );
        }

        for(char ch : ransomNote.toCharArray()) {

            int remaining =
                    freq.getOrDefault(ch,0)-1;

            if(remaining < 0)
                return false;

            freq.put(ch,remaining);
        }

        return true;
    }
}
