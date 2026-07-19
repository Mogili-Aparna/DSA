/*
====================================================
PALINDROME IN DOUBLY LINKED LIST
====================================================

PATTERN:
Two Pointers

----------------------------------------------------
IDEA
----------------------------------------------------

left  = head

right = tail

Compare both ends.

If equal:

left  = left.next

right = right.prev


----------------------------------------------------
STOP CONDITIONS
----------------------------------------------------

Odd Length:

left == right


Even Length:

left.next == right


----------------------------------------------------
COMPLEXITY
----------------------------------------------------

Time  : O(n)

Space : O(1)


----------------------------------------------------
ADVANTAGE OF DLL
----------------------------------------------------

Array:
left++, right--

DLL:
left = left.next
right = right.prev

Almost identical.
*/

package LinkedLists;
class PalidronDll {
    public boolean isPalindrome(DLNode head) {
        if(head == null)return true;
        DLNode right=head,left=head;
        while(right.next!=null){
            right=right.next;
        }
        while(left!=right && left.next !=right ){
            if(left.val == right.val){
                left = left.next;
                right = right.prev;
            }
            else
                return false;
        }
        if(left!=right){
            return left.val == right.val;
        }
        return true;
    }
}
