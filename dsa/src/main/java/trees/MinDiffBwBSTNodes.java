/*
=========================================================
PROBLEM
=========================================================

Given a Binary Search Tree (BST),
find the minimum absolute difference
between values of any two different nodes.


=========================================================
OBSERVATION
=========================================================

Brute Force:

Store all nodes and compare every pair.

Time:

O(n²)

Too expensive.


Important Observation:

BST Inorder Traversal
=
Sorted Order


Example:

        4
       / \
      2   6
     / \
    1   3


Inorder:

1 2 3 4 6


Question becomes:

Find minimum difference in
a sorted sequence.


=========================================================
INTUITION
=========================================================

For:

a < b < c

We know:

c-a
=
(c-b)+(b-a)

Since both terms are non-negative:

c-a >= c-b
c-a >= b-a


Therefore:

Minimum difference can never
come from non-adjacent values.

It must occur between adjacent
elements in sorted order.


Hence:

BST
↓

Inorder Traversal
↓

Sorted Sequence
↓

Compare Adjacent Values Only


=========================================================
MULTIPLE APPROACHES
=========================================================

APPROACH 1:

Store inorder traversal
inside an ArrayList.

Compute minimum adjacent difference.

Time : O(n)
Space: O(n)


---------------------------------------------------------


APPROACH 2 (OPTIMAL):

Do inorder traversal.

Maintain:

1. Previous inorder node
2. Minimum answer till now

No extra array required.

Time : O(n)
Space: O(h)


=========================================================
VISUALIZATION
=========================================================

Tree:

        10
       /  \
      5    15
     / \     \
    2   7     18


Inorder:

2 → 5 → 7 → 10 → 15 → 18


Differences:

5-2 = 3
7-5 = 2
10-7 = 3
15-10 = 5
18-15 = 3


Minimum = 2


=========================================================
DRY RUN
=========================================================

Tree:

        4
       / \
      2   6
     / \
    1   3


Visit 1

prev = 1
min = INF


Visit 2

diff = 2-1 = 1

prev = 2
min = 1


Visit 3

diff = 3-2 = 1

prev = 3


Visit 4

diff = 4-3 = 1

prev = 4


Visit 6

diff = 6-4 = 2


Answer:

1


=========================================================
CODE
=========================================================
*/
package trees;
class State
{
    TreeNode prev;
    int minDiff;

    State()
    {
        prev = null;
        minDiff = Integer.MAX_VALUE;
    }
}

public class MinDiffBwBSTNodes
{
    public int minDiffInBST(TreeNode root)
    {
        State state = new State();

        inorder(root, state);

        return state.minDiff;
    }

    private void inorder(
            TreeNode root,
            State state)
    {
        if(root == null)
            return;

        inorder(root.left, state);

        if(state.prev != null)
        {
            state.minDiff =
                    Math.min(
                            state.minDiff,
                            root.val
                                    - state.prev.val
                    );
        }

        state.prev = root;

        inorder(root.right, state);
    }
}


/*
=========================================================
ALTERNATIVE CODE
=========================================================

Global Variables Version

TreeNode.java prev = null;
int minDiff = Integer.MAX_VALUE;

Useful for shorter interview code.

But State Object has better design.


=========================================================
COMPLEXITY
=========================================================

TIME:

Every node visited once.

O(n)


SPACE:

Recursion Stack

Balanced Tree:

O(log n)

Worst Case:

O(n)

Interview Answer:

O(h)

where h = height of tree.


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

BST + Inorder

usually means:

Sorted Order.


---------------------------------------------------------


2)

Minimum difference in sorted
data always occurs between
adjacent values.


---------------------------------------------------------


3)

Sometimes we do NOT need
all pair comparisons.

Mathematical observations
can drastically simplify problems.


---------------------------------------------------------


4)

Tree recursion often requires
state to flow across recursive calls.


=========================================================
COMMON MISTAKES
=========================================================

1)

Comparing parent and child only.

Minimum pair may not be
parent-child.


Example:

Previous node of 4 is 3,
not parent.


---------------------------------------------------------


2)

Comparing every pair.

Leads to O(n²).


---------------------------------------------------------


3)

Forgetting:

Inorder Previous Node
!= Parent Node


---------------------------------------------------------


4)

Passing prev as primitive value.

State gets lost during recursion.


=========================================================
PATTERN RECOGNITION
=========================================================

Question contains:

✔ BST
✔ Next Smaller/Larger Value
✔ Sorted Order Needed
✔ Minimum Difference

Immediately think:

INORDER TRAVERSAL


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1)

Why compare only adjacent values?

Ans:

For:

a < b < c

c-a =
(c-b)+(b-a)

Therefore larger gaps cannot
produce minimum answer.


---------------------------------------------------------


Q2)

Can we solve without extra array?

Ans:

Yes.

Maintain previous inorder node.


---------------------------------------------------------


Q3)

Why use State Object?

Ans:

Avoid global state and make
dependencies explicit.


---------------------------------------------------------


Q4)

Can globals also work?

Ans:

Yes.

They produce shorter code
but weaker design.


=========================================================
RELATED PROBLEMS
=========================================================

LC 98
Validate BST


LC 230
Kth Smallest in BST


LC 235
Lowest Common Ancestor in BST


LC 173
BST Iterator


LC 538
Convert BST to Greater Tree


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

BST
+
Need Sorted Information
+
Closest/Minimum Difference
+
Predecessor/Successor

↓

Think:

INORDER TRAVERSAL


Mental Model:

BST
↓

Inorder
↓

Sorted Sequence
↓

Adjacent Values Only


State Needed:

1. Previous Inorder Node
2. Current Answer


=========================================================
*/