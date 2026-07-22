package HashSet;
/**
 =========================================================
 PROBLEM
 =========================================================

 Unique Number of Occurrences (LC1207)

 Given an integer array arr,
 return true if the number of occurrences
 of each value in the array is unique.

 ---------------------------------------------------------

 Example 1:

 arr = [1,2,2,1,1,3]

 Frequencies:

 1 -> 3
 2 -> 2
 3 -> 1

 Frequency values:

 [3,2,1]

 All frequencies are unique.

 Answer:

 true

 ---------------------------------------------------------

 Example 2:

 arr = [1,2]

 Frequencies:

 1 -> 1
 2 -> 1

 Frequency values:

 [1,1]

 Duplicate frequency exists.

 Answer:

 false

 =========================================================
 OBSERVATION
 =========================================================

 This problem has TWO parts.

 Part 1:

 Find frequencies.

 ↓

 HashMap.

 ---------------------------------------------------------

 Part 2:

 Check if frequencies themselves
 are unique.

 ↓

 HashSet.

 ---------------------------------------------------------

 This is one of the first problems where
 HashMap and HashSet are used together.

 =========================================================
 INTUITION
 =========================================================

 Example:

 arr =

 [1,2,2,1,1,3]

 ---------------------------------------------------------

 Step 1:

 Count frequencies.

 1 -> 3
 2 -> 2
 3 -> 1

 ---------------------------------------------------------

 Step 2:

 Extract frequency values.

 [3,2,1]

 ---------------------------------------------------------

 Step 3:

 Check if any frequency repeats.

 If yes:

 return false.

 Else:

 return true.

 =========================================================
 MY INITIAL APPROACH
 =========================================================

 You correctly identified:

 Need frequencies.

 ↓

 HashMap.

 Then:

 Need to detect duplicate frequencies.

 ↓

 HashSet.

 ---------------------------------------------------------

 Code:

 for(int num : fc.values())
 {
 if(!hashSet.add(num))
 return false;
 }

 Excellent usage.

 Because:

 HashSet.add()

 returns:

 true  -> inserted successfully
 false -> already exists

 This avoids an additional contains() call.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Brute Force
 ---------------------------------------------------------

 1. Count frequencies.

 2. Compare every frequency
 with every other frequency.

 Time Complexity:

 O(k²)

 where:

 k = number of unique elements.

 Space Complexity:

 O(k)

 ---------------------------------------------------------
 APPROACH 2
 HashMap + HashSet (Optimal)
 ---------------------------------------------------------

 1. Build frequency map.

 2. Insert frequencies into set.

 3. If insertion fails:

 duplicate frequency found.

 Time Complexity:

 O(n)

 Space Complexity:

 O(k)

 =========================================================
 VISUALIZATION
 =========================================================

 arr:

 [1,2,2,1,1,3]

 ---------------------------------------------------------

 Frequency Map:

 {
 1 : 3,
 2 : 2,
 3 : 1
 }

 ---------------------------------------------------------

 Insert frequencies:

 Insert 3

 Set:

 {3}

 ---------------------------------------------------------

 Insert 2

 Set:

 {2,3}

 ---------------------------------------------------------

 Insert 1

 Set:

 {1,2,3}

 No duplicates.

 Return:

 true

 =========================================================
 DRY RUN
 =========================================================

 Input:

 arr = [1,2]

 ---------------------------------------------------------

 Frequency Map:

 {
 1 : 1,
 2 : 1
 }

 ---------------------------------------------------------

 Insert:

 1

 Set:

 {1}

 ---------------------------------------------------------

 Insert:

 1

 Already exists.

 add() returns false.

 Return:

 false

 =========================================================
 CODE EXPLANATION
 =========================================================

 Step 1:

 Build frequency map.

 for(int num : arr)
 {
 fc.put(
 num,
 fc.getOrDefault(num,0)+1
 );
 }

 ---------------------------------------------------------

 Map becomes:

 number -> frequency

 ---------------------------------------------------------

 Step 2:

 Traverse all frequencies.

 for(int freq : fc.values())
 {
 if(!set.add(freq))
 return false;
 }

 If insertion fails:

 Duplicate frequency exists.

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 Let:

 n = arr.length

 k = number of unique elements.

 ---------------------------------------------------------

 Building HashMap:

 n insertions.

 Time:

 O(n)

 ---------------------------------------------------------

 Traversing frequencies:

 k insertions.

 Each insertion:

 O(1)

 Average.

 Time:

 O(k)

 ---------------------------------------------------------

 Since:

 k <= n

 Total:

 O(n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 HashMap:

 Stores k entries.

 Space:

 O(k)

 ---------------------------------------------------------

 HashSet:

 Stores at most k frequencies.

 Space:

 O(k)

 ---------------------------------------------------------

 Total:

 O(k)

 Worst Case:

 k = n

 Space:

 O(n)

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Need frequencies.

 ↓

 HashMap.

 ---------------------------------------------------------

 2.

 Need uniqueness checking.

 ↓

 HashSet.

 ---------------------------------------------------------

 3.

 Many medium problems combine
 multiple data structures.

 ---------------------------------------------------------

 4.

 HashSet.add()

 can itself be used to detect duplicates.

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Checking frequencies using nested loops.

 Complexity:

 O(k²)

 ---------------------------------------------------------

 Mistake 2:

 Using only HashMap.

 Map gives frequencies but does not
 tell whether frequencies repeat.

 ---------------------------------------------------------

 Mistake 3:

 Writing:

 if(set.contains(freq))
 return false;

 set.add(freq);

 Works.

 But:

 if(!set.add(freq))
 return false;

 is cleaner.

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Question asks:

 Count occurrences.

 ↓

 HashMap.

 ---------------------------------------------------------

 Question then asks:

 Are these counts unique?

 ↓

 HashSet.

 ---------------------------------------------------------

 Combined Pattern:

 HashMap
 +
 HashSet

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Why use HashSet?

 Because duplicate detection becomes:

 O(1)

 Average.

 ---------------------------------------------------------

 Q2.

 Can sorting solve this?

 Yes.

 Store frequencies in array,
 sort them,
 check adjacent values.

 Complexity:

 O(k log k)

 HashSet is better.

 ---------------------------------------------------------

 Q3.

 Why does add() help?

 Because:

 set.add(x)

 returns:

 false

 if x already exists.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC217 Contains Duplicate

 LC242 Valid Anagram

 LC49 Group Anagrams

 LC771 Jewels and Stones

 LC451 Sort Characters By Frequency

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 Need counts?

 ↓

 HashMap

 ---------------------------------------------------------

 Need uniqueness?

 ↓

 HashSet

 ---------------------------------------------------------

 Need counts AND uniqueness?

 ↓

 HashMap
 +
 HashSet

 ---------------------------------------------------------

 Need duplicate detection?

 ↓

 HashSet

 =========================================================
 BIG IDEA
 =========================================================

 Array

 ↓

 Frequency Counting

 ↓

 HashMap

 ↓

 Frequency Values

 ↓

 Duplicate Detection

 ↓

 HashSet

 Pattern:

 HashMap
 +
 HashSet

 This combination appears frequently
 in medium-level interview questions.

 =========================================================
 */

import java.util.HashMap;
import java.util.HashSet;

public class UniqueNumberOfOccurrences {

    public boolean uniqueOccurrences(int[] arr) {

        HashMap<Integer,Integer> freq =
                new HashMap<>();

        for(int num : arr)
        {
            freq.put(
                    num,
                    freq.getOrDefault(num,0)+1
            );
        }

        HashSet<Integer> set =
                new HashSet<>();

        for(int count : freq.values())
        {
            if(!set.add(count))
                return false;
        }

        return true;
    }
}
