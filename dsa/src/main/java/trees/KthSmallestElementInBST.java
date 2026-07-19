/*
=========================================================
PROBLEM
=========================================================

Kth Smallest Element in a BST
(LC 230)

Given the root of a Binary Search Tree
and an integer k,

return the kth smallest value present
in the BST.


=========================================================
EXAMPLE
=========================================================

        5
       / \
      3   7
     / \
    2   4


k = 3


Sorted Order:

2, 3, 4, 5, 7


Answer:

4


=========================================================
OBSERVATION
=========================================================

BST has a very important property:

Inorder Traversal
=
Sorted Order


Example:

        5
       / \
      3   7
     / \
    2   4


Inorder:

2 → 3 → 4 → 5 → 7


Question becomes:

Find kth element in a sorted sequence.


=========================================================
INTUITION
=========================================================

Since inorder visits nodes
in increasing order,

we can simply:

1. Perform inorder traversal.

2. Count nodes as they are visited.

3. The moment count becomes k,
   current node is the answer.


No need to store entire
sorted sequence.


=========================================================
MULTIPLE APPROACHES
=========================================================

APPROACH 1

Store inorder traversal
inside an ArrayList.

Return:

list.get(k-1)

Time:

O(n)

Space:

O(n)


---------------------------------------------------------


APPROACH 2 (OPTIMAL)

Do inorder traversal.

Maintain:

1. count
2. answer

Stop traversal once answer
is found.

Time:

O(k + h) approximately

Worst:

O(n)

Space:

O(h)


=========================================================
VISUALIZATION
=========================================================

Tree:

        5
       / \
      3   7
     / \
    2   4


Inorder Order:

2 → 3 → 4 → 5 → 7


Visit Order:

Node 2

count = 1


Node 3

count = 2


Node 4

count = 3

count == k

Answer = 4


Traversal can stop here.


=========================================================
DRY RUN
=========================================================

k = 3


Visit 2

count = 1


Visit 3

count = 2


Visit 4

count = 3

answer = 4


Stop traversal.


=========================================================
CODE
=========================================================
*/

class State
{
    int count;
    Integer answer;

    State()
    {
        count = 0;
    }
}

class KthSmallestElementInBST
{
    public int kthSmallest(
            TreeNode root,
            int k)
    {
        State state = new State();

        inorder(root, k, state);

        return state.answer;
    }

    private void inorder(
            TreeNode root,
            int k,
            State state)
    {
        if(root == null
                || state.answer != null)
            return;

        inorder(root.left,
                k,
                state);

        if(state.answer != null)
            return;

        state.count++;

        if(state.count == k)
        {
            state.answer = root.val;
            return;
        }

        inorder(root.right,
                k,
                state);
    }
}


/*
=========================================================
ALTERNATIVE SOLUTION
=========================================================

ArrayList Approach


List<Integer> list = new ArrayList<>();

Perform inorder traversal
and store values.

Return:

list.get(k-1)


Pros:

✔ Easy to understand.


Cons:

❌ Extra O(n) space.


=========================================================
COMPLEXITY
=========================================================

TIME:

Worst Case:

O(n)


Average:

Can stop early after
finding kth element.


Interview Answer:

O(k + h) approximately.


---------------------------------------------------------


SPACE:

Recursion Stack

O(h)

where:

h = height of tree.


Balanced Tree:

O(log n)

Worst Case:

O(n)


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

BST Inorder Traversal
=
Sorted Order.


---------------------------------------------------------


2)

If problem asks:

kth
minimum
maximum
next
previous

Think:

Sorted Sequence.


---------------------------------------------------------


3)

Sometimes we do not need
to explicitly create the
sorted sequence.

Traversal itself can generate
elements in sorted order.


---------------------------------------------------------


4)

Traversal can often be stopped
early once answer is found.


=========================================================
COMMON MISTAKES
=========================================================

1)

Forgetting that inorder gives
sorted order only for BST,
not normal Binary Trees.


---------------------------------------------------------


2)

Traversing entire tree even after
finding the answer.


---------------------------------------------------------


3)

Using extra ArrayList when
only kth element is required.


---------------------------------------------------------


4)

Confusing:

kth smallest

with

kth node level order traversal.


=========================================================
PATTERN RECOGNITION
=========================================================

Question contains:

✔ BST
✔ kth smallest
✔ kth largest
✔ predecessor
✔ successor
✔ sorted order

Immediately think:

INORDER TRAVERSAL


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1)

Why does inorder work?

Ans:

Because inorder traversal of BST
produces nodes in sorted order.


---------------------------------------------------------


Q2)

Can we solve without extra array?

Ans:

Yes.

Count nodes during inorder traversal.


---------------------------------------------------------


Q3)

Why can traversal stop early?

Ans:

Once kth element is found,
remaining nodes cannot affect
the answer.


---------------------------------------------------------


Q4)

Can this be solved iteratively?

Ans:

Yes.

Using stack-based inorder traversal.


---------------------------------------------------------


Q5)

Follow-up:

What if tree is modified frequently
and kth smallest queries are many?

Ans:

Augment each node with:

subtree size.

This allows:

O(log n)

queries.


=========================================================
RELATED PROBLEMS
=========================================================

LC 98
Validate BST


LC 173
BST Iterator


LC 530
Minimum Absolute Difference in BST


LC 783
Minimum Distance Between BST Nodes


LC 285
Inorder Successor in BST


LC 510
Inorder Successor II


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

BST
+
Need Sorted Information

↓

Think:

INORDER


---------------------------------------------------------


BST
+
kth Smallest

↓

Inorder + Counter


---------------------------------------------------------


Mental Model:

BST

↓

Inorder Traversal

↓

Sorted Sequence

↓

Count Elements


=========================================================
*/

/*
=========================================================
PROBLEM (FOLLOW UP)
=========================================================

Suppose:

1. kthSmallest() queries happen
   very frequently.

2. Tree is also modified by
   insertions and deletions.

Can we answer kth smallest
faster than doing inorder
every time?


=========================================================
OBSERVATION
=========================================================

Current Solution:

For every query:

BST
↓

Inorder Traversal
↓

Count till k


Time:

O(h + k)

Worst:

O(n)


If there are thousands of queries,
this becomes expensive.


=========================================================
BIG IDEA
=========================================================

Store EXTRA information
inside every node.


This technique is called:

DATA STRUCTURE AUGMENTATION


=========================================================
AUGMENTED NODE
=========================================================
*/

class TreeNode
{
    int val;

    int size;

    TreeNode left;
    TreeNode right;

    TreeNode(int val)
    {
        this.val = val;
        this.size = 1;
    }
}

/*
=========================================================
WHAT IS size ?
=========================================================

size = number of nodes present
inside this subtree.


Example:

          5
        /   \
       3     8
      / \
     2   4


Sizes:


2 -> 1

4 -> 1

3 -> 3

8 -> 1

5 -> 5


Visual:

          5(size=5)
         /          \
  3(size=3)       8(size=1)
     /    \
 2(1)    4(1)


=========================================================
WHY IS THIS USEFUL ?
=========================================================

Suppose:

k = 4


At node:

5

Left subtree size:

3


Therefore:

1st smallest -> 2
2nd smallest -> 3
3rd smallest -> 4
4th smallest -> 5


Since:

leftSize + 1 == k

Current node itself
is the answer.


No inorder traversal required.


=========================================================
INTUITION
=========================================================

At every node:

leftSize =
size(root.left)


Case 1:

k == leftSize + 1

Current node is answer.


---------------------------------------------------------


Case 2:

k <= leftSize

Answer lies entirely
inside left subtree.


---------------------------------------------------------


Case 3:

k > leftSize + 1

Skip:

left subtree
+
current node

Go right.

New k:

k = k - leftSize - 1


=========================================================
VISUALIZATION
=========================================================

          5(size=5)
         /          \
  3(size=3)       8(size=1)
     /    \
 2(1)    4(1)


Find:

k = 3


At node 5:

leftSize = 3

k <= 3

Go LEFT.


At node 3:

leftSize = 1


k > leftSize + 1

Go RIGHT.

k = 3 - 2 = 1


At node 4:

leftSize = 0

k == 1


Answer:

4


=========================================================
KTH SMALLEST CODE
=========================================================
*/

public int kthSmallest(
        TreeNode root,
        int k)
{
    int leftSize =
            getSize(root.left);

    if(k == leftSize + 1)
        return root.val;

    if(k <= leftSize)
    {
        return kthSmallest(
                root.left,
                k);
    }

    return kthSmallest(
            root.right,
            k-leftSize-1);
}


private int getSize(TreeNode root)
{
    return root == null
            ? 0
            : root.size;
}


/*
=========================================================
INSERT OPERATION
=========================================================

Important:

After insertion,
subtree sizes change.


Example:

Before:

3(size=3)

Insert one node.

After:

3(size=4)


Therefore while returning from
recursion we must recalculate size.


=========================================================
INSERT CODE
=========================================================
*/

public TreeNode insert(
        TreeNode root,
        int val)
{
    if(root == null)
        return new TreeNode(val);

    if(val < root.val)
    {
        root.left =
                insert(
                        root.left,
                        val);
    }
    else
    {
        root.right =
                insert(
                        root.right,
                        val);
    }

    root.size =
            1
                    + getSize(root.left)
                    + getSize(root.right);

    return root;
}


/*
=========================================================
DELETE OPERATION
=========================================================

Deletion cases:

1. Leaf

2. One Child

3. Two Children


After deletion:

size must again
be recalculated.


=========================================================
DELETE CODE
=========================================================
*/

public TreeNode delete(
        TreeNode root,
        int val)
{
    if(root == null)
        return null;

    if(val < root.val)
    {
        root.left =
                delete(
                        root.left,
                        val);
    }
    else if(val > root.val)
    {
        root.right =
                delete(
                        root.right,
                        val);
    }
    else
    {
        /*
        Node found.
        */

        if(root.left == null)
            return root.right;

        if(root.right == null)
            return root.left;

        /*
        Two children.
        */

        TreeNode successor =
                getMin(root.right);

        root.val =
                successor.val;

        root.right =
                delete(
                        root.right,
                        successor.val);
    }

    root.size =
            1
                    + getSize(root.left)
                    + getSize(root.right);

    return root;
}


private TreeNode getMin(
        TreeNode root)
{
    while(root.left != null)
        root = root.left;

    return root;
}

void main() {
}


/*
=========================================================
DRY RUN
=========================================================

Tree:

          5(size=5)
         /          \
  3(size=3)       8(size=1)
     /    \
 2(1)    4(1)


Find:

k = 4


At node 5:

leftSize = 3


leftSize + 1 = 4


Therefore:

Answer = 5


Only ONE node inspection.


=========================================================
COMPLEXITY
=========================================================

kthSmallest:

Balanced BST:

O(log n)

Worst:

O(n)


---------------------------------------------------------


Insert:

Balanced:

O(log n)

Worst:

O(n)


---------------------------------------------------------


Delete:

Balanced:

O(log n)

Worst:

O(n)


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

Data Structures can be
AUGMENTED.


---------------------------------------------------------


2)

Store extra metadata to answer
queries faster.


---------------------------------------------------------


3)

Trade:

Extra Memory

for

Faster Queries.


---------------------------------------------------------


4)

This is a very common
senior interview idea.


=========================================================
COMMON MISTAKES
=========================================================

1)

Forgetting to update size
after insert.


---------------------------------------------------------


2)

Forgetting to update size
after delete.


---------------------------------------------------------


3)

Updating size before recursive
calls complete.


=========================================================
PATTERN RECOGNITION
=========================================================

Question contains:

✔ Frequent Queries
✔ Repeated Computation
✔ Dynamic Updates

Ask:

Can I store extra information?


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1)

What is this technique called?

Ans:

Data Structure Augmentation.


---------------------------------------------------------


Q2)

Why store subtree size?

Ans:

To know immediately how many
elements are smaller than
current node.


---------------------------------------------------------


Q3)

Can this become O(log n)?

Ans:

Yes.

If tree remains balanced.


---------------------------------------------------------


Q4)

Real data structures using this idea?

Ans:

Order Statistic Tree,
AVL Tree,
Red Black Tree,
Segment Tree,
Interval Tree.


=========================================================
RELATED PROBLEMS
=========================================================

LC 230
Kth Smallest Element in BST


LC 703
Kth Largest in Stream


Order Statistic Tree


Rank Queries


Dynamic Median Problems


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

Repeated Queries
+
Tree Updates

↓

Think:

Store Metadata


---------------------------------------------------------


Metadata Examples:

subtree size
subtree sum
height
min
max
frequency


---------------------------------------------------------


Mental Model:

Normal BST

Stores:

value


Augmented BST

Stores:

value
+
extra information


Extra Information

↓

Less Computation Later.


=========================================================
*/
