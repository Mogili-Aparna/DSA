/*
=========================================================
PROBLEM
=========================================================

Range Sum of BST
(LC 938)

Given the root of a Binary Search Tree
and two integers L and R,

return the sum of values of all nodes
having value in the range:

L <= node.val <= R


=========================================================
EXAMPLE
=========================================================

        10
       /  \
      5    15
     / \     \
    3   7     18

L = 7
R = 15


Nodes in range:

7,10,15

Answer:

32


=========================================================
OBSERVATION
=========================================================

Brute Force Idea:

Traverse entire tree.

If current node lies inside range,
add it to answer.

This works even if tree is NOT BST.


=========================================================
IMPORTANT BST OBSERVATION
=========================================================

BST Property:

Left Subtree:

values <= root.val

Right Subtree:

values > root.val


Therefore:

If:

root.val < L

Then:

Entire LEFT subtree is useless.


Because:

left subtree values
<= root.val
< L


---------------------------------------------------------


If:

root.val > R

Then:

Entire RIGHT subtree is useless.


Because:

right subtree values
>= root.val
> R


This is called:

PRUNING


=========================================================
INTUITION
=========================================================

Normal Binary Tree:

Must visit everything.


BST:

Can eliminate entire branches.


This makes solution cleaner
and potentially faster.


=========================================================
MULTIPLE APPROACHES
=========================================================

APPROACH 1

Simple DFS Traversal

Visit every node.

If node is inside range,
add to answer.

Time : O(n)
Space: O(h)


---------------------------------------------------------


APPROACH 2 (OPTIMAL)

Use BST property.

Skip unnecessary subtrees.

Time:

O(number of visited nodes)

Worst:

O(n)

Often much better.


=========================================================
VISUALIZATION
=========================================================

Tree:

            10
           /  \
          5    15
         / \     \
        3   7     18


L = 7
R = 15


At node:

5

5 < 7

Therefore:

Entire left subtree:

3

can never contribute.


Skip it completely.


---------------------------------------------------------


At node:

18

18 > 15

Therefore:

Entire right subtree
can be skipped.


=========================================================
DRY RUN
=========================================================

Tree:

            10
           /  \
          5    15
         / \     \
        3   7     18


L = 7
R = 15


Node 10:

Inside range.

sum = 10


Node 5:

5 < 7

Skip left subtree.

Go right.


Node 7:

Inside range.

sum = 17


Node 15:

Inside range.

sum = 32


Node 18:

18 > 15

Skip right subtree.


Answer:

32


=========================================================
CODE
=========================================================
*/
package trees;
class SumOfBSTNodesInRange
{
    public int rangeSumBST(
            TreeNode root,
            int L,
            int R)
    {
        if(root == null)
            return 0;

        /*
        Entire left subtree useless.
        */
        if(root.val < L)
        {
            return rangeSumBST(
                    root.right,
                    L,
                    R);
        }

        /*
        Entire right subtree useless.
        */
        if(root.val > R)
        {
            return rangeSumBST(
                    root.left,
                    L,
                    R);
        }

        /*
        Current node lies inside range.
        */
        return root.val
                +
                rangeSumBST(
                        root.left,
                        L,
                        R)
                +
                rangeSumBST(
                        root.right,
                        L,
                        R);
    }
}


/*
=========================================================
ALTERNATIVE SOLUTION
=========================================================


Simple inorder traversal.

class State
{
    int sum;
}

Traverse every node and add
only values inside range.


Pros:

✔ Easy to think
✔ Works for any Binary Tree


Cons:

❌ Does not use BST property.
❌ Visits unnecessary nodes.


Time:

O(n)


=========================================================
COMPLEXITY
=========================================================

BRUTE:

Time:

O(n)

Space:

O(h)


---------------------------------------------------------


OPTIMAL:

Worst Case:

O(n)

Balanced BST with pruning:

Much fewer nodes visited.

Space:

O(h)


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

BST is not only useful
for inorder traversal.


---------------------------------------------------------


2)

BST allows:

SEARCH SPACE REDUCTION


---------------------------------------------------------


3)

Whenever BST is given,
always ask:

Can I skip an entire subtree?


---------------------------------------------------------


4)

Pruning often converts:

Visit Everything

↓

Visit Only Useful Nodes


=========================================================
COMMON MISTAKES
=========================================================

1)

Doing inorder traversal because
BST is mentioned.

Inorder is NOT required here.


---------------------------------------------------------


2)

Visiting both subtrees even when
they cannot contain valid values.


---------------------------------------------------------


3)

Forgetting inequalities.


If:

root.val < L

Only RIGHT subtree matters.


If:

root.val > R

Only LEFT subtree matters.


=========================================================
PATTERN RECOGNITION
=========================================================

Question contains:

✔ BST
✔ Search
✔ Range
✔ Closest Value
✔ Boundaries

Immediately think:

SUBTREE PRUNING


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1)

Why can left subtree be skipped
when root.val < L ?

Ans:

All values in left subtree
are <= root.val < L.


---------------------------------------------------------


Q2)

Why is inorder unnecessary?

Ans:

We only need sum.

Sorted order is not needed.


---------------------------------------------------------


Q3)

Can complexity become better than O(n)?

Ans:

Yes.

Because entire branches
can be skipped.


---------------------------------------------------------


Q4)

Will this optimization work
for a normal Binary Tree?

Ans:

No.

Only BST guarantees ordering.


=========================================================
RELATED PROBLEMS
=========================================================

LC 700
Search in BST


LC 669
Trim BST


LC 235
Lowest Common Ancestor in BST


LC 270
Closest Binary Search Tree Value


LC 98
Validate BST


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

Question says:

BST
+
Range
or
Search
or
Closest Value

↓

Think:

Can I eliminate an entire subtree?


Mental Model:

BST

↓

Ordering Property

↓

Pruning

↓

Visit fewer nodes


=========================================================
*/