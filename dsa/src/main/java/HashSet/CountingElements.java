package HashSet;
/**
 =========================================================
 PROBLEM
 =========================================================

 Counting Elements (Easy)

 Given an integer array arr, count how many
 elements x have:

 x + 1

 also present in the array.

 Duplicates should be counted separately.

 ---------------------------------------------------------

 Example 1:

 arr = [1,2,3]

 1 -> 2 exists ✔
 2 -> 3 exists ✔
 3 -> 4 does not exist ❌

 Answer = 2

 ---------------------------------------------------------

 Example 2:

 arr = [1,1,2]

 first 1 -> 2 exists ✔
 second 1 -> 2 exists ✔
 2 -> 3 does not exist ❌

 Answer = 2

 =========================================================
 OBSERVATION
 =========================================================

 Question does NOT ask:

 "How many times does x+1 occur?"

 It only asks:

 "Does x+1 exist?"

 This immediately suggests:

 Existence Checking.

 ---------------------------------------------------------

 Brute force:

 For every element:

 Search entire array for x+1.

 This becomes:

 O(n²)

 which is too slow.

 =========================================================
 INTUITION
 =========================================================

 Searching repeatedly inside an array
 is expensive.

 We need:

 Fast lookup.

 Question:

 Can we store all numbers somewhere
 such that checking:

 x+1 exists?

 becomes O(1)?

 Yes.

 HashSet.

 ---------------------------------------------------------

 HashSet allows:

 add()      → O(1)
 contains() → O(1)

 Average case.

 =========================================================
 MY INITIAL APPROACH
 =========================================================

 You correctly identified:

 Need only existence.

 Therefore:

 HashSet is enough.

 HashMap is unnecessary.

 ---------------------------------------------------------

 You created:

 HashSet<Integer> set =
 Arrays.stream(arr)
 .boxed()
 .collect(Collectors.toCollection(HashSet::new));

 Then:

 for(int num : arr)
 {
 if(set.contains(num+1))
 count++;
 }

 Perfect.

 ---------------------------------------------------------

 Important observation:

 You iterated over ARR and not SET.

 This is correct because duplicates
 must contribute separately.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Brute Force
 ---------------------------------------------------------

 For every number:

 Search array for:

 num+1

 Time Complexity:

 O(n²)

 Space Complexity:

 O(1)

 ---------------------------------------------------------
 APPROACH 2
 HashSet (Optimal)
 ---------------------------------------------------------

 Insert all elements into HashSet.

 For every number:

 Check:

 set.contains(num+1)

 Time Complexity:

 O(n)

 Space Complexity:

 O(n)

 =========================================================
 VISUALIZATION
 =========================================================

 arr:

 [1,1,2]

 ---------------------------------------------------------

 Create Set:

 {1,2}

 ---------------------------------------------------------

 First 1:

 Does 2 exist?

 YES

 count = 1

 ---------------------------------------------------------

 Second 1:

 Does 2 exist?

 YES

 count = 2

 ---------------------------------------------------------

 2:

 Does 3 exist?

 NO

 Final Answer:

 2

 =========================================================
 DRY RUN
 =========================================================

 Input:

 arr = [1,2,3,5]

 ---------------------------------------------------------

 Set:

 {1,2,3,5}

 ---------------------------------------------------------

 num = 1

 contains(2)

 YES

 count = 1

 ---------------------------------------------------------

 num = 2

 contains(3)

 YES

 count = 2

 ---------------------------------------------------------

 num = 3

 contains(4)

 NO

 ---------------------------------------------------------

 num = 5

 contains(6)

 NO

 Return:

 2

 =========================================================
 CODE EXPLANATION
 =========================================================

 Step 1:

 Insert every element into HashSet.

 This gives:

 O(1)

 average lookup.

 ---------------------------------------------------------

 Step 2:

 Traverse original array.

 For each number:

 Check:

 num+1

 exists.

 If yes:

 count++

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 Let:

 n = arr.length

 ---------------------------------------------------------

 Building HashSet:

 n insertions.

 Time:

 O(n)

 ---------------------------------------------------------

 Traversal:

 n lookups.

 Each lookup:

 O(1)

 Average.

 Time:

 O(n)

 ---------------------------------------------------------

 Total:

 O(n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 HashSet stores all unique elements.

 Worst case:

 all elements distinct.

 Extra Space:

 O(n)

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Repeated searching in array usually
 indicates:

 HashSet.

 ---------------------------------------------------------

 2.

 Question asks:

 "Does it exist?"

 NOT:

 "How many exist?"

 ---------------------------------------------------------

 3.

 Existence problems usually become:

 HashSet problems.

 ---------------------------------------------------------

 4.

 Duplicates may still matter even when
 using HashSet.

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Using nested loops.

 Complexity:

 O(n²)

 ---------------------------------------------------------

 Mistake 2:

 Using HashMap.

 Frequency information is unnecessary.

 ---------------------------------------------------------

 Mistake 3:

 Iterating over SET.

 Example:

 arr = [1,1,2]

 Set:

 {1,2}

 If we iterate over set:

 count = 1

 Wrong.

 Correct answer:

 count = 2

 ---------------------------------------------------------

 Always iterate over original array
 when duplicates matter.

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Question asks:

 Does something exist?

 ↓

 Fast lookup needed.

 ↓

 HashSet.

 ---------------------------------------------------------

 Need:

 Unique elements only.

 ↓

 HashSet.

 ---------------------------------------------------------

 Need:

 Frequency counts.

 ↓

 HashMap.

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Why HashSet instead of HashMap?

 Because we only need existence,
 not frequencies.

 ---------------------------------------------------------

 Q2.

 Can sorting solve this?

 Yes.

 Sort array and search.

 Time:

 O(n log n)

 HashSet is better.

 ---------------------------------------------------------

 Q3.

 Why iterate over arr and not set?

 Because duplicates should contribute
 multiple times.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC217 Contains Duplicate

 LC202 Happy Number

 LC128 Longest Consecutive Sequence

 LC349 Intersection of Two Arrays

 LC36 Valid Sudoku

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 Need frequency?

 ↓

 HashMap

 ---------------------------------------------------------

 Need only existence?

 ↓

 HashSet

 ---------------------------------------------------------

 Need duplicate removal?

 ↓

 HashSet

 ---------------------------------------------------------

 Need visited tracking?

 ↓

 HashSet

 =========================================================
 BIG IDEA
 =========================================================

 This problem introduces the first
 major HashSet pattern:

 Existence Checking.

 Repeated searching:

 O(n²)

 ↓

 Store elements in HashSet.

 ↓

 Search becomes:

 O(1)

 Pattern:

 Fast Lookup
 +
 Existence Query

 ↓

 HashSet

 =========================================================
 */

import java.util.HashSet;

public class CountingElements {

    public int countElements(int[] arr) {

        HashSet<Integer> set =
                new HashSet<>();

        for(int num : arr)
            set.add(num);

        int count = 0;

        for(int num : arr)
        {
            if(set.contains(num + 1))
                count++;
        }

        return count;
    }
}
