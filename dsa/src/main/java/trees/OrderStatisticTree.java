package trees;
/*
* =========================================================
ORDER STATISTIC TREE
=========================================================

PROBLEM
---------------------------------------------------------
Design a BST that efficiently supports:

1. insert(x)
2. delete(x)
3. kthSmallest(k)
4. rank(x)
5. predecessor(x)
6. successor(x)

Normal BST supports search operations efficiently,
but order-statistic queries like kth smallest and rank
require O(n) traversal.

Goal:

Augment BST nodes with extra metadata so that order
queries can also become efficient.


=========================================================
OBSERVATION
---------------------------------------------------------

BST already provides:

✔ Search
✔ Min/Max
✔ Floor/Ceil
✔ Predecessor/Successor

efficiently using ordering.

However:

kthSmallest()
rank()

need information about how many nodes exist in
subtrees.

Repeatedly counting subtree nodes is expensive.

Example:

          5
        /   \
       3     8
      / \
     2   4

For kthSmallest(4):

Need to know:

How many nodes are smaller than 5?

Without extra information:

count(left subtree)
→ O(n)

With augmentation:

store subtree size
→ O(1)


=========================================================
INTUITION
---------------------------------------------------------

Store in every node:

class TreeNode
{
    int val;

    int size;

    TreeNode left;
    TreeNode right;
}

Meaning:

size =
number of nodes in subtree rooted at current node.

Formula:

size =
1
+ size(left)
+ size(right)

Whenever insertion/deletion changes tree structure,
recompute size while recursion unwinds.


=========================================================
NODE STRUCTURE
---------------------------------------------------------

class TreeNode
{
    int val;

    int size = 1;

    TreeNode left;
    TreeNode right;
}


=========================================================
METHOD : getSize()
=========================================================

PROBLEM
---------------------------------------------------------
Return subtree size.


OBSERVATION
---------------------------------------------------------
Since metadata is already stored,
no traversal is required.


CODE
---------------------------------------------------------

int getSize(TreeNode root)
{
    if(root == null)
        return 0;

    return root.size;
}


COMPLEXITY
---------------------------------------------------------
Time  : O(1)
Space : O(1)


IMPORTANT LEARNING
---------------------------------------------------------
Augmentation is useful only if metadata can be
obtained quickly.


=========================================================
METHOD : insert()
=========================================================

PROBLEM
---------------------------------------------------------
Insert node while maintaining subtree sizes.


OBSERVATION
---------------------------------------------------------
Insertion changes sizes of all ancestors.


INTUITION
---------------------------------------------------------
Perform normal BST insertion.

While recursion returns:

recompute size.


CODE
---------------------------------------------------------

TreeNode insert(TreeNode root,int val)
{
    if(root == null)
        return new TreeNode(val);

    if(val < root.val)
        root.left =
            insert(root.left,val);

    else if(val > root.val)
        root.right =
            insert(root.right,val);

    root.size =
        1
        + getSize(root.left)
        + getSize(root.right);

    return root;
}


VISUALIZATION
---------------------------------------------------------

Insert:

3,2,5

        3(size=3)
       / \
  2(1)   5(1)


COMPLEXITY
---------------------------------------------------------
Average : O(log n)
Worst   : O(n)


COMMON MISTAKES
---------------------------------------------------------
1. Forget to update size.
2. Ignore duplicate handling policy.


IMPORTANT LEARNING
---------------------------------------------------------
Structural modifications happen first.

Metadata maintenance happens later.


=========================================================
METHOD : delete()
=========================================================

PROBLEM
---------------------------------------------------------
Delete node while maintaining subtree sizes.


OBSERVATION
---------------------------------------------------------
Deletion may change subtree sizes of all ancestors.


INTUITION
---------------------------------------------------------
Perform normal BST deletion.

After recursive call:

recompute size.


CASES
---------------------------------------------------------

1. Leaf

2. One Child

3. Two Children

    Replace with inorder successor.


CODE PATTERN
---------------------------------------------------------

root.size =
1
+ getSize(root.left)
+ getSize(root.right);


COMPLEXITY
---------------------------------------------------------
Average : O(log n)
Worst   : O(n)


COMMON MISTAKES
---------------------------------------------------------

tree.delete(root,3);

instead of:

root = tree.delete(root,3);

because root itself may change.


IMPORTANT LEARNING
---------------------------------------------------------
Whenever tree structure changes:

metadata must also change.


=========================================================
METHOD : kthSmallest()
=========================================================

PROBLEM
---------------------------------------------------------
Return kth smallest element.


BRUTE FORCE
---------------------------------------------------------
Inorder traversal.

Time : O(n)


OBSERVATION
---------------------------------------------------------
Inorder gives sorted order.

Current node rank:

leftSize + 1


INTUITION
---------------------------------------------------------

left subtree contains:

leftSize elements.

Therefore:

k == leftSize+1
→ answer

k <= leftSize
→ go left

otherwise
→ go right


CODE
---------------------------------------------------------

int kthSmallest(TreeNode root,int k)
{
    int leftSize =
        getSize(root.left);

    if(k == leftSize+1)
        return root.val;

    if(k <= leftSize)
        return kthSmallest(
                root.left,
                k);

    return kthSmallest(
            root.right,
            k-leftSize-1);
}


VISUALIZATION
---------------------------------------------------------

          5(size=7)
        /            \
   3(size=3)         8

leftSize = 3

rank(5)=4


COMPLEXITY
---------------------------------------------------------
Average : O(log n)
Worst   : O(n)


IMPORTANT LEARNING
---------------------------------------------------------
Trade extra memory
for faster queries.


PATTERN RECOGNITION
---------------------------------------------------------
Need kth element
→ subtree size


RELATED PROBLEMS
---------------------------------------------------------
LC230 Kth Smallest in BST


=========================================================
METHOD : rank()
=========================================================

PROBLEM
---------------------------------------------------------
Return:

number of nodes <= x


BRUTE FORCE
---------------------------------------------------------
Inorder traversal and count.

Time : O(n)


OBSERVATION
---------------------------------------------------------

If:

root.val < x

then:

entire left subtree
+
current root

are guaranteed answers.


INTUITION
---------------------------------------------------------

Add:

1 + leftSize

and continue searching right.


CODE
---------------------------------------------------------

int rank(TreeNode root,int x)
{
    if(root == null)
        return 0;

    if(root.val < x)
    {
        return
            1
            + getSize(root.left)
            + rank(root.right,x);
    }

    else if(root.val > x)
    {
        return rank(
                    root.left,
                    x);
    }

    return
        1
        + getSize(root.left);
}


VISUALIZATION
---------------------------------------------------------

1 2 3 4 5

rank(4)=4

rank(100)=5


COMPLEXITY
---------------------------------------------------------
Average : O(log n)
Worst   : O(n)


IMPORTANT LEARNING
---------------------------------------------------------
rank()
and
kthSmallest()

are inverse operations.


PATTERN RECOGNITION
---------------------------------------------------------
Need count of smaller elements
→ subtree size


RELATED PROBLEMS
---------------------------------------------------------
LC315 Count Smaller Numbers After Self


=========================================================
METHOD : predecessor()
=========================================================

PROBLEM
---------------------------------------------------------
Return largest value smaller than x.


OBSERVATION
---------------------------------------------------------
This does NOT require augmentation.

BST ordering itself is sufficient.


INTUITION
---------------------------------------------------------

Maintain:

best candidate.

If:

root.val < x

then root itself may be predecessor.

Store it.

Move RIGHT to search for a larger candidate.


If:

root.val >= x

then root cannot be predecessor.

Move LEFT.


VISUALIZATION
---------------------------------------------------------

Find predecessor(28)

        20
       /  \
     10    30
          /
        25


20 < 28

candidate = 20

move right


30 >= 28

move left


25 < 28

candidate = 25

Answer = 25


COMPLEXITY
---------------------------------------------------------
Average : O(log n)
Worst   : O(n)


IMPORTANT LEARNING
---------------------------------------------------------
Not every problem needs augmentation.


PATTERN RECOGNITION
---------------------------------------------------------
Largest smaller element
→ Candidate Tracking


RELATED PROBLEMS
---------------------------------------------------------
Floor in BST


=========================================================
METHOD : successor()
=========================================================

PROBLEM
---------------------------------------------------------
Return smallest value greater than x.


OBSERVATION
---------------------------------------------------------
Mirror image of predecessor.


INTUITION
---------------------------------------------------------

If:

root.val > x

root may be answer.

Store candidate.

Move LEFT.


If:

root.val <= x

move RIGHT.


VISUALIZATION
---------------------------------------------------------

Find successor(17)

        20
       /  \
      10   30
        \
         15


20 > 17

candidate = 20

move left


10 <= 17

move right


15 <= 17

move right

NULL


Answer = 20


COMPLEXITY
---------------------------------------------------------
Average : O(log n)
Worst   : O(n)


PATTERN RECOGNITION
---------------------------------------------------------
Smallest larger element
→ Candidate Tracking


RELATED PROBLEMS
---------------------------------------------------------
LC285 Inorder Successor in BST


=========================================================
MULTIPLE APPROACHES SUMMARY
=========================================================

Search Problems
---------------------------------------------------------
search()
predecessor()
successor()
floor()
ceil()

Use:

BST Property
+
Candidate Tracking


Order Problems
---------------------------------------------------------
kthSmallest()
rank()
median()
percentile()

Use:

Subtree Size Augmentation


Range Problems
---------------------------------------------------------
countRange(low,high)

Use:

rank(high)
-
rank(low-1)


=========================================================
IMPORTANT LEARNINGS
=========================================================

1.
Augmentation should answer:

"What expensive information
am I repeatedly recomputing?"

2.
Not every query needs augmentation.

3.
Metadata maintenance is part of insert/delete.

4.
Subtree size enables:

kth smallest
rank
median
range count

5.
Candidate tracking solves:

predecessor
successor
floor
ceil


=========================================================
COMMON MISTAKES
=========================================================

1.
Forgetting:

root =
delete(root,val)

2.
Forgetting size updates.

3.
Incorrect kth condition:

k <= leftSize+1

instead of:

k <= leftSize

4.
Not defining duplicate policy.

5.
Returning Integer.MIN_VALUE
when predecessor doesn't exist.


=========================================================
INTERVIEW QUESTIONS
=========================================================

1.
Why use augmentation?

2.
Can predecessor use subtree size?

3.
How would you support median queries?

4.
How would duplicates be handled?

5.
Can this be balanced?

Answer:

AVL + subtree size

or

Red Black Tree + subtree size


=========================================================
RELATED PROBLEMS
=========================================================

LC230  Kth Smallest in BST
LC285  Inorder Successor in BST
LC315  Count Smaller Numbers After Self
LC938  Range Sum of BST
LC700  Search in BST


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

Need kth element?
→ subtree size

Need rank?
→ subtree size

Need count in range?
→ rank()

Need predecessor?
→ candidate tracking

Need successor?
→ candidate tracking

Need floor/ceil?
→ candidate tracking

Need closest value?
→ candidate tracking

Need median?
→ subtree size


=========================================================
FINAL BIG IDEA
=========================================================

Order Statistic Tree

=

BST
+
Subtree Size Metadata

Some queries use BST ordering.

Some queries use augmentation.

Choosing the correct metadata
is the essence of data structure design.
=========================================================
* */
public class OrderStatisticTree {
    TreeNode insert(TreeNode root, int val)
    {
        if(root == null)
            return new TreeNode(val);
        if(root.val > val)
            root.left=insert(root.left, val);
        if(root.val < val)
            root.right = insert(root.right, val);
        root.size = 1 + getSize(root.left) + getSize(root.right);
        return root;
    }
    int getSize(TreeNode root)
    {
        if(root == null)
            return 0;
        return root.size;
    }
    TreeNode delete(TreeNode root, int val)
    {
        if(root == null)
            return null;
        if(root.val > val)
            root.left =delete(root.left, val);
        else if(root.val < val)
            root.right =delete(root.right, val);
        else{
            if(root.left == null)
               return root.right;
            else if(root.right == null)
                return root.left;
            else {
                TreeNode successor = getMin(root.right);
                root.val = successor.val;
                root.right = delete(root.right, successor.val);
            }
        }
        root.size = 1 + getSize(root.left) + getSize(root.right);
        return root;
    }
    TreeNode getMin(TreeNode root)
    {
        if(root == null) return null;
        while(root.left!=null){
            root=root.left;
        }
        return root;
    }
    private void inorder(TreeNode root,String path){
        if(root == null) return;
        inorder(root.left,"left");
        System.out.print(root.val + "("+ root.size+") - path : "+path+" ");
        inorder(root.right,"right");
    }
    private int kthSmallest(TreeNode root,int k){
        if(root == null) return 0;
        int leftSize = getSize(root.left);
        if(k == leftSize + 1)return root.val;
        if(k <= leftSize)return kthSmallest(root.left,k);
        return kthSmallest(root.right,k-leftSize-1);
    }
    private int rank(TreeNode root,int x){
        if(root == null) return 0;
        if(root.val < x){
            return 1+getSize(root.left)+rank(root.right,x);
        }
        else if(root.val > x){
            return rank(root.left,x);
        }
        else{
            return 1+getSize(root.left);
        }
    }
    static class predecessorState{
        Integer val;
    }
    private void predecessor(TreeNode root,int x,predecessorState state){
        if(root == null) return;
        if(root.val< x){
            if(state.val == null) state.val =root.val;
            else state.val = Math.max(root.val,state.val);//possible candidate if we can't find anything larger than this and smaller than x
            predecessor(root.right,x, state);
        }
        else {
            predecessor(root.left,x,state);
        }
    }
    static class successorState{
        Integer val;
    }
    private void successor(TreeNode root,int x,successorState state){
        if(root == null) return;
        if(root.val> x){
            if(state.val ==null) state.val=root.val;
            else state.val = Math.min(root.val,state.val);//possible candidate if we can't find anything smaller than this and larger than x
            successor(root.left,x, state);
        }
        else {
            successor(root.right,x,state);
        }
    }
    static void main(String[] args) {
        OrderStatisticTree tree = new OrderStatisticTree();
        TreeNode root = tree.insert(null,3);
        tree.insert(root,2);
        tree.insert(root,5);
        tree.insert(root,4);
        tree.insert(root,1);

        tree.inorder(root,"root");
        System.out.println();
        System.out.println("2nd smallest element is :"+ tree.kthSmallest(root,2));
        System.out.println("4th smallest element is :"+ tree.kthSmallest(root,4));
        System.out.println("rank of 2 :"+ tree.rank(root,2));
        System.out.println("rank of 4 :"+ tree.rank(root,4));
        System.out.println("rank of 3 :"+ tree.rank(root,3));
        root =tree.delete(root,1);
        tree.inorder(root,"root");
        root = tree.delete(root,5);
        System.out.println();
        tree.inorder(root,"root");
        root = tree.delete(root,3);
        System.out.println();
        tree.inorder(root,"root");
        System.out.println();
        System.out.println("2nd smallest element is :"+ tree.kthSmallest(root,2));
        OrderStatisticTree tree1 = new OrderStatisticTree();
        TreeNode root1 = tree.insert(null,20);
        tree.insert(root1,10);
        tree.insert(root1,30);
        tree.insert(root1,5);
        tree.insert(root1,15);
        tree.insert(root1,20);
        tree.insert(root1,25);
        tree.insert(root1,40);
        tree.inorder(root1,"root");
        System.out.println();
        predecessorState state =new predecessorState();
        tree.predecessor(root1,20,state);
        System.out.println("predecessor of 20 :"+ state.val);
        state = new predecessorState();
        tree.predecessor(root1,28,state);
        System.out.println("predecessor of 28 :"+ state.val);
        successorState sstate =new successorState();
        tree.successor(root1,20,sstate);
        System.out.println("successor of 20 :"+ sstate.val);
        sstate = new successorState();
        tree.successor(root1,28,sstate);
        System.out.println("successor of 17 :"+ sstate.val);
        sstate = new successorState();
        tree.successor(root1,40,sstate);
        System.out.println("successor of 40 :"+ sstate.val);

    }
}