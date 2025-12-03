package recursion.IBH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
* ps : sort an array of integers using only recursion
* i/p: [2,3,7,6,4,5,9]
* o/p : [2,3,4,5,6,7,9]
* 🧠 IBH Method Breakdown
Base Condition
If the list has one or zero elements, it’s already sorted.
Hypothesis
Assume that sort() correctly sorts the first n-1 elements.
Induction
🪶 Key Takeaways
Take the last element and insert it into the correct place in the sorted sub-list (using recursion again).
| Step              | Concept                                | Explanation                       |
| ----------------- | -------------------------------------- | --------------------------------- |
| **Base**          | smallest valid input                   | Single-element list is sorted     |
| **Hypothesis**    | recursive assumption                   | Assume sort() works for n−1       |
| **Induction**     | logic to extend                        | Insert nth element in right place |
| **Approach type** | IBH (Input reducing, not choice-based) | Each step shrinks list size by 1  |

💡Tip:
When doing recursion on lists:
Always create new copies (new ArrayList<>(...)) if you’re removing elements.
(SubList in Java is a view, not a separate list!)
Prefer returning new lists rather than mutating in place — makes the recursion pure and predictable.
* solved : true
* difficulty : medium
* */
public class SortAnArray {
    static void main() {
        System.out.println(sort(Arrays.asList(2,3,7,6,4,5,9),0,6));
        System.out.println(sort(Arrays.asList(2),0,0));
        System.out.println(sort(Arrays.asList(2,3),0,1));
        System.out.println(sort(Arrays.asList(2,2,2,2,2),0,4));
        System.out.println(sort(Arrays.asList(2,3,4,5,6),0,4));
        System.out.println(sort(Arrays.asList(6,5,4,3,2,1),0,5));
        System.out.println(sort(Arrays.asList(5,2,2,7,9,9,9,2,2,4,4,1,0,0,0,0),0,15));
    }

    private static List<Integer> sort(List<Integer> arr,int start,int end) {
        if(end-start == 0){ //base condition
            List<Integer> temp = new ArrayList<Integer>();
            temp.add(arr.get(start));
            return temp;
        }
        List<Integer> sortedArr = sort(arr,start,end-1);//hypothesis
        return insert(sortedArr,arr.get(end));//induction
    }
    // Insert an element into the correct position in a sorted list
    private static List<Integer> insert(List<Integer> sortedArr, Integer integer) {
        if(sortedArr.isEmpty() || sortedArr.getLast()<=integer){//base condition
            sortedArr.add(integer);
            return sortedArr;
        }
        // Hypothesis: remove last and insert recursively
        List<Integer> newList = sortedArr.subList(0,sortedArr.size()-1);
        List<Integer> insertedArr = insert(newList,integer);//hypothesis // recursive call
        insertedArr.add(sortedArr.getLast());//induction
        return insertedArr;
    }
}
/*

 */