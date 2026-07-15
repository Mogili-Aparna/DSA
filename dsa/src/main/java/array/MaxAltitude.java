/*Problem: Find Highest Altitude

Pattern:
Running Sum / Prefix Sum

Key Idea:
Maintain current altitude while traversing.
Track maximum altitude encountered.

Time:
O(n)

Space:
O(1)

Recognition:
Current state depends on cumulative previous values.

Mistake:
None.

* solved : true
* difficulty : easy

Problem Statement
A bike rider is going on a ride. The road contains n + 1 points at different altitudes. The rider starts from point 0 at an altitude of 0.

Given an array of integers gain of length n, where gain[i] represents the net gain in altitude between points i and i + 1 for all (0 <= i < n), return the highest altitude of a point.

Examples
Example 1
Input: gain = [-5, 1, 5, 0, -7]
Expected Output: 1
Justification: The altitude changes are [-5, -4, 1, 1, -6], where 1 is the highest altitude reached.
Example 2
Input: gain = [4, -3, 2, -1, -2]
Expected Output: 4
Justification: The altitude changes are [4, 1, 3, 2, 0], where 4 is the highest altitude reached.
Example 3
Input: gain = [2, 2, -3, -1, 2, 1, -5]
Expected Output: 4
Justification: The altitude changes are [2, 4, 1, 0, 2, 3, -2], where 4 is the highest altitude reached.
Constraints:

n == gain.length
1 <= n <= 100
-100 <= gain[i] <= 100

*/
public class MaxAltitude {
    public int largestAltitude(int[] gain) {
        int maxAltitude = 0,altitude=0; // To store the maximum altitude encountered
        // TODO: Write your code here
        if(gain == null || gain.length == 0) return maxAltitude;
        for(int num : gain) {
            altitude=altitude+num;
            maxAltitude = Math.max(maxAltitude,altitude);
        }
        return maxAltitude;
    }
    public static void main(String[] args) {
        MaxAltitude solution = new MaxAltitude();

        // Example 1
        System.out.println(solution.largestAltitude(new int[]{-5, 1, 5, 0, -7})); // Expected: 1

        // Example 2
        System.out.println(solution.largestAltitude(new int[]{4, -3, 2, -1, -2})); // Expected: 4

        // Example 3
        System.out.println(solution.largestAltitude(new int[]{2, 2, -3, -1, 2, 1, -5})); // Expected: 4
    }
}

