package trees;
/**
 =========================================================
 PROBLEM
 =========================================================

 Merge Two Binary Trees (LC617)

 Given two binary trees:

 t1 and t2

 Merge them into a new binary tree.

 Rules:

 1. If both nodes exist:

 merged value =
 t1.val + t2.val

 2. If only one node exists:

 use that node directly.

 ---------------------------------------------------------

 Example:

 Tree1:

 1
 / \
 3   2
 /
 5

 Tree2:

 2
 / \
 1   3
 \    \
 4    7

 Output:

 3
 / \
 4   5
 / \   \
 5   4   7

 =========================================================
 OBSERVATION
 =========================================================

 This is NOT a BST problem.

 BST ordering is completely irrelevant.

 This is simply:

 Traverse TWO trees simultaneously.

 ---------------------------------------------------------

 Notice function signature:

 mergeTrees(TreeNode t1,
 TreeNode t2)

 Whenever a tree problem gives:

 (TreeNode root1,
 TreeNode root2)

 it usually indicates:

 Simultaneous DFS.

 =========================================================
 INTUITION
 =========================================================

 Think recursively.

 Question:

 "What should this pair of nodes return?"

 ---------------------------------------------------------

 Case 1:

 Both nodes are null.

 Return:

 null

 ---------------------------------------------------------

 Case 2:

 Only one node exists.

 Return that node.

 ---------------------------------------------------------

 Case 3:

 Both nodes exist.

 Create new node:

 value =

 t1.val + t2.val

 Then recursively merge:

 left children

 right children

 =========================================================
 MY INITIAL APPROACH
 =========================================================

 You correctly identified all three cases.

 Code:

 if(t1 == null && t2 == null)
 return null;

 if(t1 != null && t2 == null)
 return t1;

 if(t1 == null && t2 != null)
 return t2;

 Create:

 new TreeNode(
 t1.val+t2.val
 );

 Then recursively merge children.

 This is the standard solution.

 ---------------------------------------------------------

 Minor simplification:

 First condition can be removed.

 Because:

 if(t1 == null)
 return t2;

 automatically handles:

 both null.

 =========================================================
 MULTIPLE APPROACHES
 =========================================================

 ---------------------------------------------------------
 APPROACH 1
 Create New Tree (MY Solution)
 ---------------------------------------------------------

 Create a completely new tree.

 Time Complexity:

 O(n+m)

 Space Complexity:

 O(max(h1,h2))

 ---------------------------------------------------------
 APPROACH 2
 Modify Existing Tree In Place
 ---------------------------------------------------------

 Instead of creating new nodes:

 modify t1 itself.

 t1.val += t2.val

 Return t1.

 Advantages:

 No extra node allocations.

 Time Complexity:

 O(n+m)

 Space Complexity:

 O(max(h1,h2))

 =========================================================
 VISUALIZATION
 =========================================================

 Tree1:

 1
 / \
 3   2
 /
 5

 Tree2:

 2
 / \
 1   3
 \    \
 4    7

 ---------------------------------------------------------

 Merge roots:

 1 + 2 = 3

 Create:

 3

 ---------------------------------------------------------

 Merge left children:

 3 + 1 = 4

 Create:

 4

 ---------------------------------------------------------

 Merge:

 5 + null

 Return:

 5

 ---------------------------------------------------------

 Merge:

 null + 4

 Return:

 4

 ---------------------------------------------------------

 Merge right children:

 2 + 3 = 5

 Create:

 5

 ---------------------------------------------------------

 Merge:

 null + 7

 Return:

 7

 =========================================================
 DRY RUN
 =========================================================

 merge(1,2)

 ↓

 Create:

 3

 ---------------------------------------------------------

 Left:

 merge(3,1)

 ↓

 Create:

 4

 ---------------------------------------------------------

 Left:

 merge(5,null)

 ↓

 return 5

 ---------------------------------------------------------

 Right:

 merge(null,4)

 ↓

 return 4

 ---------------------------------------------------------

 Right:

 merge(2,3)

 ↓

 Create:

 5

 ---------------------------------------------------------

 Right:

 merge(null,7)

 ↓

 return 7

 =========================================================
 CODE EXPLANATION
 =========================================================

 Step 1:

 Handle base cases.

 ---------------------------------------------------------

 if(t1 == null)
 return t2;

 if(t2 == null)
 return t1;

 ---------------------------------------------------------

 Step 2:

 Create merged node.

 TreeNode root =
 new TreeNode(
 t1.val+t2.val
 );

 ---------------------------------------------------------

 Step 3:

 Merge left subtrees.

 root.left =
 mergeTrees(
 t1.left,
 t2.left
 );

 ---------------------------------------------------------

 Step 4:

 Merge right subtrees.

 root.right =
 mergeTrees(
 t1.right,
 t2.right
 );

 Return root.

 =========================================================
 TIME COMPLEXITY ANALYSIS
 =========================================================

 Let:

 n = number of nodes in tree1

 m = number of nodes in tree2

 ---------------------------------------------------------

 Every existing node position
 is visited once.

 No node is processed twice.

 ---------------------------------------------------------

 Total Time:

 O(n+m)

 ---------------------------------------------------------

 Interview Explanation:

 Each recursive call processes
 one pair of nodes.

 Thus total work is proportional
 to the total number of nodes.

 =========================================================
 SPACE COMPLEXITY ANALYSIS
 =========================================================

 Extra space comes from:

 Recursion stack.

 Maximum depth:

 max(h1,h2)

 ---------------------------------------------------------

 Balanced Trees:

 h ≈ log n

 Space:

 O(log n)

 ---------------------------------------------------------

 Worst Case:

 Skewed trees.

 h = n

 Space:

 O(n+m)

 ---------------------------------------------------------

 Additional Space:

 My solution creates new nodes.

 Result tree itself contains:

 O(n+m) nodes.

 Usually output tree space is not counted
 in auxiliary complexity.

 =========================================================
 IMPORTANT LEARNINGS
 =========================================================

 1.

 Not all tree problems are BST problems.

 ---------------------------------------------------------

 2.

 Whenever function takes:

 (TreeNode t1,
 TreeNode t2)

 think:

 Simultaneous DFS.

 ---------------------------------------------------------

 3.

 Recursive question becomes:

 "What should this pair of nodes return?"

 ---------------------------------------------------------

 4.

 Returning existing node directly
 can avoid unnecessary work.

 =========================================================
 COMMON MISTAKES
 =========================================================

 Mistake 1:

 Forgetting:

 one node exists
 other is null.

 ---------------------------------------------------------

 Mistake 2:

 Returning:

 new TreeNode(node.val)

 instead of returning existing node.

 This causes unnecessary copying.

 ---------------------------------------------------------

 Mistake 3:

 Trying inorder traversal.

 Ordering is irrelevant.

 ---------------------------------------------------------

 Mistake 4:

 Confusing this with BST merge.

 This problem has nothing to do
 with BST ordering.

 =========================================================
 PATTERN RECOGNITION
 =========================================================

 Function signature:

 (TreeNode root1,
 TreeNode root2)

 ↓

 Usually means:

 Simultaneous DFS.

 ---------------------------------------------------------

 Need to compare two trees?

 ↓

 DFS on both trees together.

 =========================================================
 INTERVIEW QUESTIONS
 =========================================================

 Q1.

 Can we do it iteratively?

 Yes.

 Use stack/queue.

 ---------------------------------------------------------

 Q2.

 Can we avoid creating new nodes?

 Yes.

 Modify t1 directly.

 ---------------------------------------------------------

 Q3.

 What if trees are extremely deep?

 Recursion may cause stack overflow.

 Iterative approach may be preferred.

 =========================================================
 RELATED PROBLEMS
 =========================================================

 LC100 Same Tree

 LC101 Symmetric Tree

 LC572 Subtree of Another Tree

 LC617 Merge Two Binary Trees

 LC951 Flip Equivalent Binary Trees

 =========================================================
 RECOGNITION CHEAT SHEET
 =========================================================

 Tree Problem

 ↓

 Two roots given

 ↓

 Compare / Merge / Validate

 ↓

 Simultaneous DFS

 ---------------------------------------------------------

 Recursive Question:

 "What should this pair of nodes return?"

 =========================================================
 BIG IDEA
 =========================================================

 Merge Two Trees

 =

 DFS on Two Trees Simultaneously

 ---------------------------------------------------------

 Single Tree Problems:

 "What should this node return?"

 ---------------------------------------------------------

 Two Tree Problems:

 "What should this PAIR of nodes return?"

 This mindset solves many tree problems.

 =========================================================
 */

public class Merge2Trees {

    public static TreeNode mergeTrees(
            TreeNode t1,
            TreeNode t2)
    {
        if(t1 == null)
            return t2;

        if(t2 == null)
            return t1;

        TreeNode root =
                new TreeNode(
                        t1.val + t2.val
                );

        root.left =
                mergeTrees(
                        t1.left,
                        t2.left
                );

        root.right =
                mergeTrees(
                        t1.right,
                        t2.right
                );

        return root;
    }
}