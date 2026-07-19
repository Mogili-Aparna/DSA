/*
=========================================================
PROBLEM : Swap Nodes in Pairs
LEETCODE : 24
=========================================================

Given:

1 -> 2 -> 3 -> 4

Return:

2 -> 1 -> 4 -> 3


=========================================================
OBSERVATION
=========================================================

We are NOT swapping values.

We are swapping LINKS.

Need to modify pointers carefully.

This is a classic pointer manipulation problem.


=========================================================
PATTERN
=========================================================

Dummy Node
+
Pointer Rearrangement


=========================================================
WHY DUMMY NODE ?
=========================================================

Suppose:

1 -> 2 -> 3 -> 4

After swapping:

2 becomes new head.

Without dummy node, we need:

if(first pair)
    update head

Dummy node avoids this special case.


=========================================================
POINTERS REQUIRED
=========================================================

dummy
prev
curr
next
future (tmp)


=========================================================
VISUALIZATION
=========================================================

Initially:

dummy -> 1 -> 2 -> 3 -> 4
           ↑    ↑
         curr next


future = next.next

future = 3


=========================================================
STEP 1
=========================================================

2 should point to 1


dummy -> 1 <- 2    3 -> 4


Code:

next.next = curr;


=========================================================
STEP 2
=========================================================

1 should point to future node.


dummy -> 1 <- 2    3 -> 4
                 ↑
               future


Code:

curr.next = future;


=========================================================
STEP 3
=========================================================

Previous node should connect to new pair head.


dummy -> 2 -> 1 -> 3 -> 4


Code:

prev.next = next;


=========================================================
MOVE POINTERS
=========================================================

prev = curr;
curr = future;


=========================================================
DRY RUN
=========================================================

Input:

1 -> 2 -> 3 -> 4


Iteration 1:

dummy -> 2 -> 1 -> 3 -> 4

prev = 1
curr = 3


Iteration 2:

dummy -> 2 -> 1 -> 4 -> 3

prev = 3
curr = null


Answer:

2 -> 1 -> 4 -> 3


=========================================================
CODE
=========================================================
*/
package LinkedLists;
public class SwapAdjacentNodes {

    public ListNode swapPairs(ListNode head) {

        ListNode dummy =
                new ListNode(-1);

        dummy.next = head;

        ListNode prev = dummy;
        ListNode curr = head;

        while (curr != null
                && curr.next != null) {

            ListNode next =
                    curr.next;

            ListNode future =
                    next.next;


            // swap
            next.next = curr;
            curr.next = future;
            prev.next = next;


            // move pointers
            prev = curr;
            curr = future;
        }

        return dummy.next;
    }
}


/*
=========================================================
COMPLEXITY
=========================================================

Time  : O(n)

Space : O(1)


=========================================================
IMPORTANT LEARNINGS
=========================================================

1)

Always save future nodes before
changing links.

ListNode future = next.next;


2)

Dummy node removes special cases.


3)

Linked List problems usually follow:

Save pointers
↓
Modify links
↓
Move pointers
↓
Repeat


=========================================================
PATTERN RECOGNITION
=========================================================

Questions involving:

Reverse K nodes
Swap nodes
Reorder list
Partition list
Reverse between positions

usually need:

prev
curr
next
future


=========================================================
RELATED PROBLEMS
=========================================================

LC 206  Reverse Linked List

LC 92   Reverse Between

LC 25   Reverse Nodes in K Group

LC 143  Reorder List

LC 1721 Swap Nodes in Linked List


=========================================================
INTERVIEW QUESTIONS
=========================================================

Q1:
Why use dummy node?

Ans:
To avoid handling head separately.


Q2:
Why save future node first?

Ans:
After changing links,
remaining list may become inaccessible.


Q3:
Can we swap values instead?

Ans:
Question specifically asks
to swap nodes, not values.

Also value swapping may not be allowed
in some variations.


=========================================================
RECOGNITION CHEAT SHEET
=========================================================

Question says:

"Modify node connections"

↓

Think:

Dummy Node
+
Save Future Pointer
+
Pointer Manipulation

=========================================================
*/