/*
====================================================
REMOVE DUPLICATES FROM SORTED LIST
LC 83
====================================================

OBSERVATION:

List is SORTED.

Therefore duplicates always appear together.


Example:

1 -> 1 -> 2 -> 3 -> 3


====================================================
APPROACH 1
Two Pointers
====================================================

prev
curr

If duplicate:

prev.next = curr.next


Time  : O(n)
Space : O(1)


====================================================
APPROACH 2 (Preferred)
Single Pointer
====================================================

curr = head

while(curr != null
        &&
      curr.next != null)
{
    if(curr.val == curr.next.val)
    {
        curr.next =
            curr.next.next;
    }
    else
    {
        curr =
            curr.next;
    }
}


----------------------------------------------------
IMPORTANT
----------------------------------------------------

After removing duplicate:

DO NOT MOVE curr.

Because there may be multiple duplicates.


Example:

1 -> 1 -> 1 -> 2

Need to remove both duplicates.


====================================================
COMPLEXITY
====================================================

Time  : O(n)

Space : O(1)


====================================================
PATTERN
====================================================

Question contains:

"Sorted Array"
or
"Sorted Linked List"

↓

Duplicates are adjacent.

Think:

compare current with next.


====================================================
FOLLOW UPS
====================================================

LC 82
Remove ALL duplicate numbers.

1->1->2->3->3

↓

2
*/