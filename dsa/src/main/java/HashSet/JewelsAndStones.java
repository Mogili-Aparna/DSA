package HashSet;
/**
 =========================================================
 PROBLEM
 =========================================================

 Jewels and Stones (LC771)

 You are given:

 1. jewels
 2. stones

 Every character in jewels represents a jewel type.

 Return how many characters in stones
 are also jewels.

 ---------------------------------------------------------

 Example:

 jewels = "aA"
 stones = "aAAbbbb"

 Jewels:

 a
 A

 Stones:

 a ✔
 A ✔
 A ✔
 b ✖
 b ✖
 b ✖
 b ✖

 Answer:

 3

 =========================================================
 OBSERVATION
 =========================================================

 Question asks:

 For every stone:

 "Is this stone a jewel?"

 This is simply a membership query.

 ---------------------------------------------------------

 We are NOT asked:

 How many times each jewel occurs.

 We only need:

 Existence.

 Therefore:

 HashMap is unnecessary.

 HashSet is sufficient.

 =========================================================
 INTUITION
 =========================================================

 Brute Force:

 For every stone:

 Search entire jewels string.

 ---------------------------------------------------------

 Example:

 stones = n

 jewels = m

 Time:

 O(n*m)

 ---------------------------------------------------------

 Instead:

 Store all jewel types inside
 a HashSet.

 Then:

 Checking:

 contains()

 becomes:

 O(1)

 Average case.

 =========================================================
 MY INITIAL APPROACH
 =========================================================

 You correctly identified:

 Need fast membership checking.

 So:

 1. Store all jewels inside HashSet.

 2. Traverse stones.

 3. If stone exists in set:

 count++

 This is the standard optimal solution.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Brute Force
 ---------------------------------------------------------

 For every stone:

 Search jewels string.

 Time Complexity:

 O(m*n)

 Space Complexity:

 O(1)

 ---------------------------------------------------------
 APPROACH 2
 HashSet (Optimal)
 ---------------------------------------------------------

 Store jewels inside HashSet.

 For every stone:

 Check:

 contains()

 Time Complexity:

 O(m+n)

 Space Complexity:

 O(m)

 =========================================================
 VISUALIZATION
 =========================================================

 jewels:

 "aA"

 Create:

 Set:

 {'a','A'}

 ---------------------------------------------------------

 stones:

 "aAAbbbb"

 Check one by one.

 a

 exists?

 YES

 count = 1

 ---------------------------------------------------------

 A

 exists?

 YES

 count = 2

 ---------------------------------------------------------

 A

 exists?

 YES

 count = 3

 ---------------------------------------------------------

 b

 exists?

 NO

 =========================================================
 DRY RUN
 =========================================================

 Input:

 jewels = "abc"

 stones = "aabbccd"

 ---------------------------------------------------------

 Set:

 {'a','b','c'}

 ---------------------------------------------------------

 stone = 'a'

 exists

 count = 1

 ---------------------------------------------------------

 stone = 'a'

 exists

 count = 2

 ---------------------------------------------------------

 stone = 'b'

 exists

 count = 3

 ---------------------------------------------------------

 stone = 'b'

 exists

 count = 4

 ---------------------------------------------------------

 stone = 'c'

 exists

 count = 5

 ---------------------------------------------------------

 stone = 'c'

 exists

 count = 6

 ---------------------------------------------------------

 stone = 'd'

 does not exist.

 Return:

 6

 =========================================================
 CODE EXPLANATION
 =========================================================

 Step 1:

 Store all jewel types.

 for(char ch : jewels.toCharArray())
 {
 jewelsSet.add(ch);
 }

 ---------------------------------------------------------

 Step 2:

 Traverse all stones.

 For every stone:

 if(jewelsSet.contains(ch))
 {
 count++;
 }

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 Let:

 m = jewels.length()

 n = stones.length()

 ---------------------------------------------------------

 Building HashSet:

 m insertions.

 Time:

 O(m)

 ---------------------------------------------------------

 Traversing stones:

 n lookups.

 Each lookup:

 O(1)

 Average.

 Time:

 O(n)

 ---------------------------------------------------------

 Total:

 O(m+n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 HashSet stores all jewel types.

 Worst case:

 all jewels are unique.

 Space:

 O(m)

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Not every string problem needs HashMap.

 ---------------------------------------------------------

 2.

 Ask:

 Do I need frequencies?

 If NO:

 HashSet may be enough.

 ---------------------------------------------------------

 3.

 Repeated membership checking:

 ↓

 HashSet.

 ---------------------------------------------------------

 4.

 HashSet converts:

 Repeated searching

 O(n²)

 ↓

 Fast lookup

 O(n)

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Using nested loops.

 for every stone
 search every jewel.

 Complexity:

 O(m*n)

 ---------------------------------------------------------

 Mistake 2:

 Using HashMap.

 Frequency information is unnecessary.

 ---------------------------------------------------------

 Mistake 3:

 Thinking duplicates inside stones
 should be removed.

 Wrong.

 Every stone contributes separately.

 Example:

 jewels = "a"

 stones = "aaa"

 Answer:

 3

 not

 1

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Question asks:

 "Does this element belong
 to another collection?"

 ↓

 Membership Query

 ↓

 HashSet

 ---------------------------------------------------------

 Need only existence.

 ↓

 HashSet.

 ---------------------------------------------------------

 Need frequency.

 ↓

 HashMap.

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Why HashSet and not HashMap?

 Because only membership checking
 is required.

 ---------------------------------------------------------

 Q2.

 Can sorting solve this?

 Yes.

 But complexity becomes:

 O(n log n)

 HashSet is better.

 ---------------------------------------------------------

 Q3.

 What if jewels contain duplicates?

 HashSet automatically removes them.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC217 Contains Duplicate

 LC349 Intersection of Two Arrays

 LC128 Longest Consecutive Sequence

 Counting Elements

 Valid Sudoku

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 Need counts?

 ↓

 HashMap

 ---------------------------------------------------------

 Need existence?

 ↓

 HashSet

 ---------------------------------------------------------

 Need duplicate removal?

 ↓

 HashSet

 ---------------------------------------------------------

 Need membership query?

 ↓

 HashSet

 ---------------------------------------------------------

 Need visited nodes?

 ↓

 HashSet

 =========================================================
 BIG IDEA
 =========================================================

 Jewels = Allowed Elements

 Stones = Elements being checked.

 For every stone:

 Ask:

 "Is this allowed?"

 This becomes:

 Membership Checking

 ↓

 Store allowed elements in HashSet.

 Pattern:

 Fast Lookup
 +
 Existence Query
 +
 Membership Testing

 ↓

 HashSet

 =========================================================
 */

import java.util.HashSet;

public class JewelsAndStones {

    public int numJewelsInStones(
            String jewels,
            String stones)
    {
        HashSet<Character> jewelsSet =
                new HashSet<>();

        for(char ch : jewels.toCharArray())
        {
            jewelsSet.add(ch);
        }

        int count = 0;

        for(char ch : stones.toCharArray())
        {
            if(jewelsSet.contains(ch))
                count++;
        }

        return count;
    }
}
