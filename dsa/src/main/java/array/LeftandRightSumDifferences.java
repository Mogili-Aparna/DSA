package array;/*
*
* Problem :
Given an input array of integers nums, find an integer array, let's call it differenceArray, of the same length as an input integer array.

Each element of differenceArray, i.e., differenceArray[i], should be calculated as follows: take the sum of all elements to the left of index i in array nums (let's call it leftSumi), and subtract it from the sum of all elements to the right of index i in array nums (let's call it rightSumi), taking the absolute value of the result:

differenceArray[i] = | leftSumi - rightSumi |

If there are no elements to the left or right of i, the corresponding sum should be taken as 0.

Examples
Example 1:

Input: nums = [2, 5, 1, 6, 1]
Expected Output: [13, 6, 0, 7, 14]
Explanation:
For i=0: |(0) - (5+1+6+1)| = |0 - 13| = 13
For i=1: |(2) - (1+6+1)| = |2 - 8| = 6
For i=2: |(2+5) - (6+1)| = |7 - 7| = 0
For i=3: |(2+5+1) - (1)| = |8 - 1| = 7
For i=4: |(2+5+1+6) - (0)| = |14 - 0| = 14
Example 2:

Input: nums = [3, 3, 3]
Expected Output: [6, 0, 6]
Explanation:
For i=0: |(0) - (3+3)| = 6
For i=1: |(3) - (3)| = 0
For i=2: |(3+3) - (0)| = 6
Example 3:

Input: nums = [1, 2, 3, 4, 5]
Expected Output: [14, 11, 6, 1, 10]
Explanation:
Calculations for each index i will follow the above-mentioned logic.
Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 105

Pattern:
Prefix Sum

Key Idea:
Reuse previous left and right sums instead of recalculating.

Time:
O(n)

Space:
O(n)
*
* solved : true
* difficulty : easy
*
*
* */

import java.util.Arrays;

public class LeftandRightSumDifferences {
    public int[] findDifferenceArray(int[] nums) {
        if(nums == null || nums.length == 0) return new int[0];
        int n = nums.length;
        int[] differenceArray = new int[n];
        // TODO: Write your code here
        int leftSum = 0;
        int rightSum = 0;
        // Calculate the total sum of the array
        for(int i =1;i<nums.length;i++) rightSum +=nums[i];
        //handle 0th index
        differenceArray[0] = Math.abs(leftSum-rightSum);
        // Calculate the difference between left and right sums for each position
        for(int i =1;i<nums.length;i++){
            leftSum += nums[i-1];
            rightSum -= nums[i];
            differenceArray[i] = Math.abs(leftSum-rightSum);
        }
        return differenceArray;
    }

    static void main() {
        LeftandRightSumDifferences solution = new LeftandRightSumDifferences();

        int[] example1 = {2, 5, 1, 6, 1};
        int[] example2 = {3, 3, 3};
        int[] example3 = {1, 2, 3, 4, 5};

        System.out.println(Arrays.toString(solution.findDifferenceArray(example1))); // Output: [13, 6, 0, 7, 14]
        System.out.println(Arrays.toString(solution.findDifferenceArray(example2))); // Output: [6, 0, 6]
        System.out.println(Arrays.toString(solution.findDifferenceArray(example3))); // Output: [14, 11, 6, 1, 10]

    }
}

//Optimized solution - there is no need to handle oth index separatly
/*
public int[] findDifferenceArray(int[] nums) {
        int n = nums.length;
        int[] differenceArray = new int[n];
        int leftSum = 0, rightSum = 0;

        // Calculate the total sum of the array
        for (int i = 0; i < nums.length; i++) {
            rightSum += nums[i];
        }

        // Calculate the difference between left and right sums for each position
        for (int i = 0; i < nums.length; i++) {
            rightSum -= nums[i];
            differenceArray[i] = Math.abs(rightSum - leftSum);
            leftSum += nums[i];
        }

        return differenceArray;
    }
 */