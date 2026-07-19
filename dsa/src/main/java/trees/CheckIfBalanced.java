/*
=========================================================
PROBLEM : Balanced Binary Tree
LEETCODE : 110
=========================================================

Given a binary tree, determine whether
it is height-balanced.

A tree is balanced if:

For every node,

|height(left)-height(right)| <= 1


=========================================================
EXAMPLE
=========================================================

Balanced:

        3
      /   \
     9     20
          /  \
         15   7


Unbalanced:

            1
           /
          2
         /
        3
       /
      4


=========================================================
BRUTE FORCE IDEA
=========================================================

For every node:

1. Compute left height
2. Compute right height
3. Check difference
4. Recursively check children


Code:

isBalanced(root)
{
    return
        isBalanced(root.left)
        &&
        isBalanced(root.right)
        &&
        abs(height(left)
            -height(right))<=1;
}


=========================================================
PROBLEM WITH BRUTE FORCE
=========================================================

Height gets calculated repeatedly.

Example:

        1
       /
      2
     /
    3
   /
  4


At node 1:

height(2)

At node 2:

height(3)

At node 3:

height(4)

Same subtrees are traversed again.

Time Complexity:

O(n²)


=========================================================
OPTIMIZATION
=========================================================

Can we calculate:

1. Height
2. Balance Status

in ONE DFS?


YES.


=========================================================
KEY OBSERVATION
=========================================================

A node should return:

Either:

1. Height of subtree

OR

2. A special value indicating
   subtree is unbalanced.


Use:

-1


=========================================================
WHY -1 ?
=========================================================

Valid heights are:

0,1,2,3...

So:

-1

can safely represent:

"Subtree is unbalanced."


=========================================================
PATTERN
=========================================================

Bottom-Up DFS

Children return information
to parent.


=========================================================
INTUITION
=========================================================

Ask every node:

"What information should I return
to my parent?"

Answer:

Height OR Unbalanced.


=========================================================
VISUALIZATION
=========================================================

            1
           /
          2
         /
        3
       /
      4


Node 4:

height = 1


Node 3:

height = 2


Node 2:

height = 3


Node 1:

left = 3
right = 0

difference = 3

return -1


Unbalanced information now
propagates upward.


=========================================================
ALGORITHM
=========================================================

1.

Compute left height.

If left subtree already
unbalanced:

return -1


2.

Compute right height.

If right subtree already
unbalanced:

return -1


3.

Check current node balance.

If difference > 1:

return -1


4.

Otherwise return height.


=========================================================
CODE
=========================================================
*/
package trees;
class CheckIfBalanced {

    public boolean isBalanced(TreeNode root) {

        return getHeight(root) != -1;
    }

    private int getHeight(TreeNode root)
    {
        if(root == null)
            return 0;

        int leftH =
                getHeight(root.left);

        if(leftH == -1)
            return -1;

        int rightH =
                getHeight(root.right);

        if(rightH == -1)
            return -1;

        if(Math.abs(leftH-rightH) > 1)
            return -1;

        return Math.max(
                leftH,
                rightH
        ) + 1;
    }
}


/*
=========================================================
DRY RUN
=========================================================

Tree:

            1
           /
          2
         /
        3
       /
      4


Node 4:

returns 1


Node 3:

returns 2


Node 2:

returns 3


Node 1:

left = 3
right = 0

difference > 1

returns -1


Final Answer:

false


=========================================================
COMPLEXITY ANALYSIS
=========================================================

TIME:

Every node visited once.

O(n)


SPACE:

Recursive stack.

Worst Case:

Skewed Tree

O(n)

Balanced Tree:

O(log n)

Interview answer:

O(h)

where h = height.


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

Tree problems often become optimal
by returning extra information.

2)

Avoid repeated traversals.

3)

Bottom-Up DFS is often better
than Top-Down DFS.

4)

Sentinel values can simplify code.


=========================================================
COMMON MISTAKES
=========================================================

1)

Recomputing heights repeatedly.

Leads to O(n²).


2)

Ignoring early exit.

If subtree already unbalanced,
immediately return.


3)

Returning boolean from DFS.

Then height must be computed again.


=========================================================
PATTERN RECOGNITION
=========================================================

Question involves:

Subtree property
+
Repeated calculations

↓

Think:

Can I return more information
from DFS?


=========================================================
TREE DP TEMPLATE
=========================================================

int dfs(node)
{
    left = dfs(leftChild);

    right = dfs(rightChild);

    if(invalid)
        return specialValue;

    return usefulInformation;
}


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1:

Why is brute force O(n²)?

Ans:

Height of same subtree gets
computed repeatedly.


---------------------------------------------------------

Q2:

Why use -1?

Ans:

Valid heights are non-negative.

-1 can represent failure state.


---------------------------------------------------------

Q3:

Can SRP be violated here?

Ans:

In interview code, helper DFS
functions often return multiple
pieces of information for efficiency.

Production code may use:

class TreeInfo
{
    int height;
    boolean balanced;
}


=========================================================
RELATED PROBLEMS
=========================================================

LC 104 Maximum Depth

LC 543 Diameter of Binary Tree

LC 124 Binary Tree Maximum Path Sum

LC 333 Largest BST Subtree

All use:

"What should this node return
to its parent?"


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

Question says:

Check property for every node.

Current solution repeats traversals.

↓

Think:

Bottom-Up DFS

Can node return extra information?

Use sentinel value if helpful.


Mental Model:

Each node says:

"I will tell my parent everything
it needs in one DFS."

=========================================================
*/