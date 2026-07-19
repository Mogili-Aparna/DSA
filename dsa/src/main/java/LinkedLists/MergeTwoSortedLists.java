/*
====================================================
MERGE TWO SORTED LISTS
LC 21
====================================================

PATTERN:
Dummy Node + Two Pointers

----------------------------------------------------
OBSERVATION
----------------------------------------------------

Both lists are sorted.

Always pick smaller node.


----------------------------------------------------
WHY DUMMY NODE?
----------------------------------------------------

Avoid handling:

if(head == null)

special cases.


----------------------------------------------------
TEMPLATE
----------------------------------------------------

dummy = new ListNode(-1)

tail = dummy

while(l1 != null && l2 != null)
{
    choose smaller node

    tail.next = node

    move chosen list

    tail = tail.next
}

tail.next =
    remaining list

return dummy.next


----------------------------------------------------
COMPLEXITY
----------------------------------------------------

Time  : O(m+n)

Space : O(1)

----------------------------------------------------
PATTERN RECOGNITION
----------------------------------------------------

Questions involving:

Create new list
Merge lists
Partition lists

↓

Think Dummy Node.
*/


package LinkedLists;
public class MergeTwoSortedLists {
    // Function to merge two sorted linked lists
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode l3 = new ListNode(0),l3head =l3;
        while(l1!=null && l2!=null){
            if(l1.val <=l2.val){
                l3.next = l1;
                l3 =l1;
                l1 = l1.next;
            }
            else{
                l3.next= l2;
                l3=l2;
                l2 = l2.next;
            }
        }
        if(l1!=null){
            l3.next=l1;
        }
        else{
            l3.next=l2;
        }
        return l3head.next;
    }
}
