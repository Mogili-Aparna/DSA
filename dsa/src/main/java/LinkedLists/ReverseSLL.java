/*
====================================================
REVERSE LINKED LIST
LC 206
====================================================

PATTERN:
Pointer Manipulation

----------------------------------------------------
IDEA
----------------------------------------------------

Maintain:

prev
curr
next

Reverse one link at a time.

----------------------------------------------------
TEMPLATE
----------------------------------------------------

prev = null
curr = head

while(curr != null)
{
    next = curr.next;

    curr.next = prev;

    prev = curr;

    curr = next;
}

return prev;


----------------------------------------------------
VISUALIZATION
----------------------------------------------------

1 -> 2 -> 3 -> null

Iteration 1:

null <- 1    2 -> 3

Iteration 2:

null <- 1 <- 2    3

Iteration 3:

null <- 1 <- 2 <- 3


----------------------------------------------------
COMPLEXITY
----------------------------------------------------

Time  : O(n)
Space : O(1)


----------------------------------------------------
FOLLOW UPS
----------------------------------------------------

LC 92   Reverse Between
LC 25   Reverse Nodes in K Group
LC 143  Reorder List
LC 234  Palindrome Linked List
*/
package LinkedLists;

public class ReverseSLL {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode next = curr.next;

            curr.next = prev;

            prev = curr;
            curr = next;
        }

        return prev;
    }
}