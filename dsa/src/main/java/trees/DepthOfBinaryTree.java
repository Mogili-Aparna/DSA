/*
=========================================================
PROBLEM : Maximum Depth of Binary Tree
LEETCODE : 104
=========================================================

Given the root of a binary tree,
return its maximum depth.

Maximum depth = number of nodes
along the longest path from root
down to the farthest leaf node.


=========================================================
EXAMPLE
=========================================================

Input:

        3
      /   \
     9     20
          /  \
         15   7


Output:

3


=========================================================
OBSERVATION
=========================================================

The depth of a node depends on
the depths of its children.

For every node:

depth(node)
=
1 +
max(depth(left),
    depth(right))

This naturally suggests RECURSION.


=========================================================
PATTERN
=========================================================

Tree DFS
+
Postorder Recursion

(Compute answers of children first,
then compute answer of current node.)


=========================================================
INTUITION
=========================================================

Ask each node:

"What should I return to my parent?"

Answer:

"I will return the height/depth
of my subtree."

Leaf node:

    1

left depth  = 0
right depth = 0

depth = 1


=========================================================
BASE CASE
=========================================================

if(root == null)
    return 0;

Null node contributes no depth.


=========================================================
RECURSION RELATION
=========================================================

depth(root)
=
1 +
max(depth(root.left),
    depth(root.right))


=========================================================
VISUALIZATION
=========================================================

            1
          /   \
         2     3
        /
       4


Node 4:

depth = 1


Node 2:

depth = 1 + max(1,0)
       = 2


Node 3:

depth = 1


Node 1:

depth = 1 + max(2,1)
       = 3


=========================================================
DRY RUN
=========================================================

maxDepth(1)

    maxDepth(2)

        maxDepth(4)

            left = 0
            right = 0

            return 1

        right = 0

        return 2


    maxDepth(3)

        return 1


return max(2,1)+1

return 3


=========================================================
CODE
=========================================================
*/
package trees;
class DepthOfBinaryTree {

    public int maxDepth(TreeNode root) {

        if(root == null)
            return 0;

        return Math.max(
                maxDepth(root.left),
                maxDepth(root.right)
        ) + 1;
    }
}


/*
=========================================================
COMPLEXITY ANALYSIS
=========================================================

TIME COMPLEXITY

Every node is visited exactly once.

Time = O(n)


---------------------------------------------------------
SPACE COMPLEXITY
---------------------------------------------------------

Recursive call stack.

Worst Case:

Skewed Tree

1
 \
  2
   \
    3
     \
      4

Space = O(n)


Balanced Tree:

        1
      /   \
     2     3
    / \   / \

Height = log n

Space = O(log n)


Interview answer:

Space = O(h)

where h = height of tree.


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

Tree problems are often easier
with recursion.

2)

Think:

"What should each node return
to its parent?"

3)

Null node usually becomes
the base case.

4)

For tree recursion:

Solve left subtree.
Solve right subtree.
Combine answers.


=========================================================
COMMON MISTAKES
=========================================================

1)

Returning:

Math.max(left,right)

instead of:

1 + Math.max(left,right)


2)

Returning 1 for null node.

Wrong:

if(root==null)
    return 1;

This overcounts depth.


3)

Confusing Height and Depth.

In interview questions:

Maximum depth of tree
and Height of tree

are usually treated the same.


=========================================================
PATTERN RECOGNITION
=========================================================

Question asks:

Maximum
Minimum
Count
Height
Path

for every subtree.

↓

Think:

Tree DFS Recursion.


=========================================================
TREE RECURSION TEMPLATE
=========================================================

int dfs(TreeNode.java root)
{
    if(root == null)
        return baseValue;

    int left =
            dfs(root.left);

    int right =
            dfs(root.right);

    return combine(left,right);
}


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1:
Why recursion?

Ans:
Because depth of a node depends
on depths of children.

This creates smaller subproblems.


---------------------------------------------------------

Q2:
Can this be solved iteratively?

Ans:
Yes.

Using BFS Level Order Traversal.


---------------------------------------------------------

Q3:
Which traversal is this?

Ans:

Postorder DFS

because children are processed
before parent.


=========================================================
ITERATIVE SOLUTION (BFS)
=========================================================

int depth = 0;

Queue<TreeNode.java> q =
        new LinkedList<>();

q.add(root);

while(!q.isEmpty())
{
    int size = q.size();

    for(int i=0;i<size;i++)
    {
        TreeNode.java node = q.poll();

        if(node.left!=null)
            q.add(node.left);

        if(node.right!=null)
            q.add(node.right);
    }

    depth++;
}


=========================================================
RELATED PROBLEMS
=========================================================

LC 111  Minimum Depth

LC 110  Balanced Binary Tree

LC 543  Diameter of Binary Tree

LC 112  Path Sum

LC 124  Binary Tree Maximum Path Sum

LC 104 and LC 543 together
build the foundation for many
tree DP problems.


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

Question says:

"Longest path"
"Maximum depth"
"Height"
"Answer depends on children"

↓

Think:

DFS Recursion
+
Postorder
+
Return answer to parent


Mental Model:

Every node asks its children:

"Tell me your answer,
I'll use it to compute mine."

=========================================================
*/