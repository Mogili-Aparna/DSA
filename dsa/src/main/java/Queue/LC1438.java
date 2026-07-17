/*
========================================================
LC 1438 : Longest Continuous Subarray With Absolute Diff
Less Than or Equal to Limit
========================================================

PATTERN:
Variable Size Sliding Window + Two Monotonic Deques

--------------------------------------------------------
OBSERVATION
--------------------------------------------------------

Question:

Absolute difference between ANY TWO elements
must be <= limit.

For any set of numbers:

maximum pair difference
=
maximum element - minimum element

Therefore:

abs(a-b) <= limit for all pairs

is equivalent to:

max(window) - min(window) <= limit


--------------------------------------------------------
PATTERN RECOGNITION
--------------------------------------------------------

"Longest Continuous Subarray"

→ Sliding Window

Need:

1. Current Maximum
2. Current Minimum

inside a moving window.

→ Monotonic Deques


--------------------------------------------------------
DEQUE USAGE
--------------------------------------------------------

maxDQ
-------
Monotonic Decreasing

Front -> Maximum element index


minDQ
-------
Monotonic Increasing

Front -> Minimum element index


Store INDICES instead of values because:

1. Need to know when elements leave window.
2. Duplicate values may exist.


--------------------------------------------------------
WINDOW LOGIC
--------------------------------------------------------

Expand Right.

If:

max - min > limit

Shrink Left until valid.


Template:

for(r=0;r<n;r++)
{
    add nums[r]

    while(window invalid)
    {
        remove expired indices
        l++;
    }

    ans=max(ans,r-l+1);
}


--------------------------------------------------------
MAX DEQUE
--------------------------------------------------------

while(!maxDQ.isEmpty()
        &&
      nums[maxDQ.peekLast()] <= nums[r])
{
    maxDQ.removeLast();
}

maxDQ.addLast(r);


--------------------------------------------------------
MIN DEQUE
--------------------------------------------------------

while(!minDQ.isEmpty()
        &&
      nums[minDQ.peekLast()] >= nums[r])
{
    minDQ.removeLast();
}

minDQ.addLast(r);


--------------------------------------------------------
INVALID WINDOW
--------------------------------------------------------

while(
    nums[maxDQ.peekFirst()]
    -
    nums[minDQ.peekFirst()]
    > limit
)
{
    if(maxDQ.peekFirst()==l)
        maxDQ.removeFirst();

    if(minDQ.peekFirst()==l)
        minDQ.removeFirst();

    l++;
}


--------------------------------------------------------
TIME COMPLEXITY
--------------------------------------------------------

O(n)

Each element is inserted once
and removed once.


--------------------------------------------------------
SPACE COMPLEXITY
--------------------------------------------------------

O(n)
Worst case for deques.


--------------------------------------------------------
RECOGNITION TEMPLATE
--------------------------------------------------------

Longest/Shortest Continuous Subarray
+
Need Maximum/Minimum repeatedly
inside moving window

→ Sliding Window
+
Monotonic Deque


Related Problems:

239  Sliding Window Maximum
862  Shortest Subarray with Sum >= K
1438 Longest Continuous Subarray...
1696 Jump Game VI
*/
public class LC1438 {
    public int longestSubarray(int[] nums, int limit) {
        int windowSize = 1;
        Deque<Integer> maxdq = new ArrayDeque<>();
        Deque<Integer> mindq = new ArrayDeque<>();
        for (int r = 0, l = 0; r < nums.length; r++) {
            // Maintain monotonic decreasing deque for max tracking
            while (!maxdq.isEmpty() && nums[maxdq.peekLast()] <= nums[r]) {
                maxdq.removeLast();
            }
            maxdq.addLast(r);
            // Maintain monotonic increasing deque for min tracking
            while (!mindq.isEmpty() && nums[mindq.peekLast()] >= nums[r]) {
                mindq.removeLast();
            }
            mindq.addLast(r);

            // Shrink window from left until the diff constraint is satisfied
            while (nums[maxdq.peekFirst()] - nums[mindq.peekFirst()] > limit) {
                if (maxdq.peekFirst() == l)
                    maxdq.removeFirst();
                if (mindq.peekFirst() == l)
                    mindq.removeFirst();
                l++;
            }
            // Removed redundant if-check: after the while loop, diff <= limit is guaranteed
            windowSize = Math.max(windowSize, r - l + 1);
        }
        return windowSize;
    }
}