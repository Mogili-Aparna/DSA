package trees;
/**
 =========================================================
 PROBLEM
 =========================================================

 Closest Binary Search Tree Value (LC270)

 Given the root of a Binary Search Tree and a target value,
 return the value in the BST that is closest to the target.

 Examples:

 Input:

 4
 / \
 2   5
 / \
 1   3

 target = 3.714286

 Output:

 4

 =========================================================
 OBSERVATION
 =========================================================

 This is NOT an inorder problem.

 We are NOT asked for:

 - sorted order
 - kth smallest
 - rank
 - traversal of entire tree

 Instead we need:

 "Which value is closest to target?"

 This should immediately remind us of:

 Floor
 Ceil
 Predecessor
 Successor

 Pattern:

 Candidate Tracking.

 ---------------------------------------------------------

 BST Property:

 left subtree values  < root.val
 right subtree values > root.val

 This ordering allows us to eliminate
 half of the tree every step.

 =========================================================
 INTUITION
 =========================================================

 Suppose:

 target = 5

 Current node:

 8

 Difference:

 |8 - 5| = 3

 Since:

 target < root.val

 all values in right subtree are:

 >= 8

 So their difference from 5 will be:

 >= 3

 Meaning:

 Right subtree can NEVER provide
 a better answer.

 Therefore:

 Ignore right subtree completely.

 Move left.

 ---------------------------------------------------------

 Similarly:

 target = 10
 root = 7

 All values in left subtree are:

 <= 7

 Difference already:

 |10 - 7| = 3

 Moving further left only increases
 the difference.

 Therefore:

 Ignore left subtree.

 Move right.

 =========================================================
 MY INITIAL APPROACH
 =========================================================

 You correctly identified:

 1. Maintain best candidate found so far.

 2. Calculate:

 difference =
 Math.abs(root.val - target)

 3. Update answer if current node
 is closer.

 4. Use BST ordering to move
 only one direction.

 This is exactly the optimal idea.

 You implemented it using:

 State Object

 class State
 {
 Double difference;
 Integer val;
 }

 which works perfectly.

 You also considered floating point equality
 using epsilon.
 // Define a TreeNode structure with left and right children.
 // class TreeNode {
 //     int val;                  // Value stored in the node.
 //     TreeNode left;            // Reference to the left child.
 //     TreeNode right;           // Reference to the right child.

 //     // Constructor to initialize a new node with a value.
 //     TreeNode(int x) {
 //         val = x;
 //     }
 // }

 public class Solution {
 class State{
 Double difference;
 Integer val;
 }
 // This function finds the value in the BST closest to the target.
 public int closestValue(TreeNode root, double target) {
 State state = new State();
 cv(root,target,state);
 return state.val;
 }
 public void cv(TreeNode root, double target,State state){
 if(root == null) return;
 System.out.println("root.val "+root.val);
 System.out.println("State.val : "+state.val+" State.difference : "+state.difference);
 Double difference = Math.abs(root.val-target);
 System.out.println("difference : "+difference);
 if(state.val == null) {
 state.val = root.val;
 state.difference=difference;
 }
 else {
 if(state.difference>difference){
 state.difference=difference;
 state.val=root.val;
 }
 else if(state.difference == difference){
 state.val = Math.min(root.val,state.val);
 }
 }
 if(root.val <= target ){
 cv(root.right,target,state);
 }
 else{
 cv(root.left,target,state);
 }
 }
 }

 ---------------------------------------------------------

 Observation:

 epsilon is unnecessary for LeetCode because
 the problem guarantees a unique answer.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Brute Force DFS
 ---------------------------------------------------------

 Traverse entire tree.

 For every node:

 Compute:

 abs(node.val-target)

 Keep minimum.

 Time Complexity:

 O(n)

 Space Complexity:

 O(h)

 ---------------------------------------------------------
 APPROACH 2
 BST Pruning (Optimal)
 ---------------------------------------------------------

 Maintain closest candidate.

 Move exactly like BST Search.

 If:

 target < root.val

 move left.

 Else:

 move right.

 Time Complexity:

 Average:

 O(log n)

 Worst:

 O(n)

 Space Complexity:

 Recursive:

 O(h)

 Iterative:

 O(1)

 =========================================================
 VISUALIZATION
 =========================================================

 Tree:

 8
 / \
 4   10
 / \
 2   6

 target = 5

 ---------------------------------------------------------

 Start:

 closest = 8

 difference = 3

 Since:

 5 < 8

 Move LEFT.

 ---------------------------------------------------------

 Node = 4

 difference = 1

 Update:

 closest = 4

 Since:

 5 > 4

 Move RIGHT.

 ---------------------------------------------------------

 Node = 6

 difference = 1

 Tie.

 Depending on question:

 return smaller value → 4

 Move LEFT.

 NULL.

 Answer:

 4

 =========================================================
 DRY RUN
 =========================================================

 Input:

 4
 / \
 2   5
 / \
 1   3

 target = 3.714286

 ---------------------------------------------------------

 closest = 4

 difference:

 |4-3.714|

 = 0.286

 ---------------------------------------------------------

 target < 4

 Move LEFT.

 Node = 2

 difference:

 1.714

 No update.

 ---------------------------------------------------------

 target > 2

 Move RIGHT.

 Node = 3

 difference:

 0.714

 No update.

 ---------------------------------------------------------

 Move RIGHT.

 NULL.

 Answer:

 4

 =========================================================
 CODE EXPLANATION
 =========================================================

 Maintain:

 closest value found so far.

 For every node:

 if current node is closer:

 update candidate.

 Then:

 Use BST property to eliminate
 one entire subtree.

 This reduces search from:

 O(n)

 to:

 O(h)

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 ---------------------------------------------------------
 Brute Force
 ---------------------------------------------------------

 Visit every node.

 Time:

 O(n)

 ---------------------------------------------------------
 Optimal BST Search
 ---------------------------------------------------------

 Only one path is explored.

 Exactly same as BST search.

 Height:

 h

 Time:

 O(h)

 ---------------------------------------------------------

 Balanced BST:

 h = log n

 Time:

 O(log n)

 ---------------------------------------------------------

 Skewed BST:

 h = n

 Time:

 O(n)

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 Recursive Solution:

 Call stack stores one path.

 Space:

 O(h)

 Balanced BST:

 O(log n)

 Worst:

 O(n)

 ---------------------------------------------------------

 Iterative Solution:

 No recursion.

 Space:

 O(1)

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Not every BST problem requires inorder.

 ---------------------------------------------------------

 2.

 Always ask:

 "Can I eliminate an entire subtree?"

 ---------------------------------------------------------

 3.

 Closest value problems usually become:

 BST Search
 +
 Candidate Tracking

 ---------------------------------------------------------

 4.

 This pattern also appears in:

 - predecessor
 - successor
 - floor
 - ceil

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Doing inorder traversal.

 Works.

 But loses BST advantage.

 Complexity becomes:

 O(n)

 ---------------------------------------------------------

 Mistake 2:

 Exploring BOTH subtrees.

 Unnecessary.

 ---------------------------------------------------------

 Mistake 3:

 Thinking:

 difference determines movement.

 Wrong.

 Movement is determined ONLY by:

 target < root.val

 or

 target > root.val

 Difference only updates answer.

 ---------------------------------------------------------

 Mistake 4:

 Using floating point epsilon unnecessarily.

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Question asks:

 Closest value
 Floor
 Ceil
 Predecessor
 Successor

 ↓

 Candidate Tracking Pattern

 ---------------------------------------------------------

 Question asks:

 Can one subtree be eliminated?

 ↓

 BST Pruning

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Why can we ignore one subtree?

 Answer:

 Suppose:

 target = 5

 root = 8

 All values in right subtree:

 >= 8

 Their difference from 5 will be:

 >= 3

 Therefore right subtree can never
 contain a better answer.

 ---------------------------------------------------------

 Q2.

 Can inorder solve this?

 Yes.

 But complexity becomes:

 O(n)

 ---------------------------------------------------------

 Q3.

 Can we make space O(1)?

 Yes.

 Use iterative traversal.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC235 Lowest Common Ancestor in BST

 LC270 Closest BST Value

 LC530 Minimum Absolute Difference in BST

 LC230 Kth Smallest Element in BST

 Floor in BST

 Ceil in BST

 Inorder Successor

 Inorder Predecessor

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 BST

 ↓

 Need sorted order

 ↓

 INORDER

 ---------------------------------------------------------

 BST

 ↓

 Need rank / kth

 ↓

 AUGMENTATION

 ---------------------------------------------------------

 BST

 ↓

 Need closest / floor / ceil /
 predecessor / successor

 ↓

 CANDIDATE TRACKING

 ↓

 Move exactly like BST Search.

 =========================================================
 BIG IDEA
 =========================================================

 Closest Value

 =

 BST Search

 +

 Candidate Tracking

 Movement Decision:

 target < root.val

 → move LEFT

 target > root.val

 → move RIGHT

 Difference is ONLY used to update
 best answer.

 Never for deciding movement.

 =========================================================
 */

public class ClosestBSTValue {

    public int closestValue(
            TreeNode root,
            double target)
    {
        int closest = root.val;

        while(root != null)
        {
            if(Math.abs(root.val - target)
                    <
                    Math.abs(closest - target))
            {
                closest = root.val;
            }

            if(target < root.val)
                root = root.left;
            else
                root = root.right;
        }

        return closest;
    }
}
