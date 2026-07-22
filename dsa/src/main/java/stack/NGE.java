package stack;
/*Problem:
Next Greater Element
problem :
Given an array, print the Next Greater Element (NGE) for every element.

The Next Greater Element for an element x is the first greater element on the right side of x in the array.

Elements for which no greater element exist, consider the next greater element as -1.

Examples
Example 1:

 Input: [4, 5, 2, 25]
 Output: [5, 25, 25, -1]
Example 1:

 Input: [13, 7, 6, 12]
 Output: [-1, 12, 12, -1]
Example 1:

 Input: [1, 2, 3, 4, 5]
 Output: [2, 3, 4, 5, -1]
Constraints:

1 <= arr.length <= 104
-109 <= arr[i] <= 109

Pattern:
Monotonic Stack

Key Idea:
Store indices whose next greater element is not found yet.

When current element is greater than stack top:
    Resolve answers for waiting indices.

Stack Stores:
Indices

Why Indices?
Needed to update result array directly.

Complexity:
Time: O(n)
Space: O(n)

Recognition Clues:
- Next Greater
- Previous Greater
- Next Smaller
- Previous Smaller
- Nearest larger/smaller element

Monotonic Property:
Stack maintains elements in decreasing order.
Time Complexity Analysis:

Most people see:

```java
for(...) {
    while(...) {
        ...
    }
}
```

and immediately think:

```text
O(n²)
```

because:

```text
for = n
while = n
therefore n*n
```

But that's not always true.

---

Let's use a example:

```text
[4, 5, 2, 25]
```

### Step 1

Push 0

```text
stack = [0]
```

Operations:

```text
push(0)
```

Count = 1

---

### Step 2

Current element = 5

```text
5 > 4
```

Pop 0

```text
stack = []
```

Push 1

```text
stack = [1]
```

Operations:

```text
pop(0)
push(1)
```

Count = 2

---

### Step 3

Current element = 2

```text
2 > 5 ? No
```

Push 2

```text
stack = [1,2]
```

Operations:

```text
push(2)
```

Count = 1

---

### Step 4

Current element = 25

```text
25 > 2
```

Pop 2

```text
25 > 5
```

Pop 1

Push 3

Operations:

```text
pop(2)
pop(1)
push(3)
```

Count = 3

---

### Total Operations

Index 0:

```text
push once
pop once
```

Index 1:

```text
push once
pop once
```

Index 2:

```text
push once
pop once
```

Index 3:

```text
push once
```

Total:

```text
4 pushes
3 pops
```

which is:

```text
2n - 1
```

≈

```text
O(n)
```

---

## The Key Observation

Ask yourself:

Can an index be popped twice?

```text
No
```

Once popped, it's gone forever.

Can an index be pushed twice?

```text
No
```

Each index enters the stack exactly once.

---

For an array of size n:

Worst case:

```text
n pushes
n pops
```

Total stack operations:

```text
2n
```

which is:

```text
O(n)
```

---

### A Bigger Example

Suppose:

```text
[1,2,3,4,5]
```

When processing 5:

```text
pop 4
pop 3
pop 2
pop 1
```

Looks scary.

Looks like:

```text
while while while while
```

But notice:

Those elements will NEVER be popped again.

The work was already paid for.

---

### The Interview Trick

Whenever you see a stack problem, ask:

> How many times can an element enter the stack?

and

> How many times can an element leave the stack?

If the answer is:

```text
at most once
at most once
```

then the complexity is usually:

```text
O(n)
```

This technique is called **amortized analysis**.

You don't count iterations of the while loop in isolation.

You count the total number of pushes and pops across the entire algorithm.

---

### Tracker Note

```text
Time Complexity Analysis:

Although there is a nested while loop, each index is:
1. Pushed into the stack once.
2. Popped from the stack at most once.

Total operations:
n pushes + n pops

Therefore:
Time = O(n)

This is an example of amortized analysis.
```

similar problems for time complexity analysis :

* Next Smaller Element
* Daily Temperatures
* Stock Span
* Largest Rectangle in Histogram

They all use the same "push once, pop once" reasoning.

Brute Force :
For each element,
scan right until a greater element is found.

Time: O(n²)
Space: O(1)

O(n) Solution #1 (My Solution)

Direction: Left → Right

Stack Stores: Indices waiting for answers

Idea:

Current element resolves pending elements.

Example:

4 5 2 25

25 resolves:
2 -> 25
5 -> 25

Time: O(n)

Space: O(n)

O(n) Solution #2 (Monotonic Stack Template)

Direction: Right → Left

Stack Stores: Candidate next greater values

Idea:

Current element asks:
Who is my next greater element?

Pop all smaller/equal elements.

Top of stack becomes answer.

Time: O(n)

Space: O(n)

--- Solution 1 vs solution 2 :
prefer 2 as it can be used as template  for other problems where monotonic stack pattern can be applied.

## Solution 1

You traversed:

```text
Left → Right
```

and maintained a stack of:

```text
indices waiting for their next greater element
```

When you found a bigger element:

```java
while(!st.isEmpty() && arr.get(st.peek()) < arr.get(i))
```

you resolved all those pending answers.

Think of it as:

```text
Current element helps previous elements.
```

Example:

```text
4 5 2 25
```

When you reach 25:

```text
25 resolves:
2 -> 25
5 -> 25
```

---

## Solution 2 :

They traverse:

```text
Right → Left
```

and maintain a stack of:

```text
potential next greater elements
```

Notice they store:

```java
s.push(arr.get(i));
```

Not indices.

Actual values.

---

Let's dry run.

### Array

```text
[4, 5, 2, 25]
```

Start from right.

---

### i = 3

```text
25
```

Stack:

```text
[]
```

No greater element.

```text
res[3] = -1
```

Push:

```text
25
```

Stack:

```text
[25]
```

---

### i = 2

```text
2
```

Top:

```text
25
```

Since:

```text
25 > 2
```

Next greater is immediately available:

```text
res[2] = 25
```

Push:

```text
2
```

Stack:

```text
[25, 2]
```

---

### i = 1

```text
5
```

Top:

```text
2
```

But:

```text
2 <= 5
```

Can 2 ever be the next greater element for anybody to the left of 5?

```text
No.
```

Because 5 blocks it.

So pop 2.

Stack:

```text
[25]
```

Now:

```text
25 > 5
```

So:

```text
res[1] = 25
```

Push 5.

Stack:

```text
[25, 5]
```

---

### i = 0

```text
4
```

Top:

```text
5
```

Next greater:

```text
res[0] = 5
```

Done.

---

## Why Reverse Traversal?

Because the problem asks:

> Find the next greater element **to the right**.

When standing at index `i`, if we traverse from right to left:

```text
everything to the right
is already processed
```

So the answer may already be sitting in the stack.

For index:

```text
5
```

we've already examined:

```text
2
25
```

and built useful information.

---

## Compare

### Solution 1

Stack stores:

```text
indices waiting for answers
```

Question:

```text
Who can the current element help?
```

---

### Solution 2

Stack stores:

```text
candidate answers
```

Question:

```text
Who can help the current element?
```

---

### One More Insight

Look at this line:

```java
while (!s.empty() && s.peek() <= arr.get(i))
```

Why do we pop?

Because if:

```text
Current = 10
Stack Top = 7
```

then 7 can never be useful again.

Any element further left that could use 7 as a next greater element could use 10 instead, and 10 is closer and larger.

This is the heart of a **monotonic stack**.

*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class NGE {
    public List<Integer> nextLargerElementSol1(List<Integer> arr) {
        List<Integer> res = new ArrayList<>();
        if(arr == null || arr.size() ==0 )return res;
        for(int anum:arr){
            res.add(-1);
        }
        System.out.println("arr: "+arr);
        Stack<Integer> st = new Stack<Integer>();
        st.push(0);
        System.out.println(st);
        for(int i=1;i<arr.size();i++){
            System.out.println("i: "+i);
            System.out.println(arr.get(st.peek()));
            System.out.println(arr.get(i));
            System.out.println(arr.get(st.peek())<arr.get(i));
            while(!st.isEmpty() && arr.get(st.peek())<arr.get(i)){
                res.set(st.pop(),arr.get(i));
                System.out.println(res);
            }
            st.push(i);
            System.out.println(st);
        }
        return res;
    }

    // Method to find the Next Greater Element (NGE) for each element in the list
    public List<Integer> nextLargerElementSol2(List<Integer> arr) {
        int n = arr.size();
        Stack<Integer> s = new Stack<>();
        Integer[] res = new Integer[n]; // Initialize an array to store results

        // Iterate through the list in reverse order
        for (int i = n - 1; i >= 0; i--) {
            // Pop elements from the stack while they are smaller than or equal to the current element
            while (!s.empty() && s.peek() <= arr.get(i)) {
                s.pop();
            }

            // If the stack is empty, there is no greater element on the right, so set it to -1
            // Otherwise, set it to the top element of the stack
            res[i] = s.empty() ? -1 : s.peek();

            // Push the current element onto the stack
            s.push(arr.get(i));
        }

        // Convert the array to a list and return
        return Arrays.asList(res);
    }

    public static void main(String[] args) {
        List<Integer> arr1 = Arrays.asList(11, 13, 21, 3);
        List<Integer> arr2 = Arrays.asList(4, 5, 2, 25);
        List<Integer> arr3 = Arrays.asList(13, 7, 6, 12);
        NGE nge = new NGE();


        // Find and print the Next Greater Element (NGE) for each list
        System.out.println(nge.nextLargerElementSol1(arr1)); // Output: [13, 21, -1, -1]
        System.out.println(nge.nextLargerElementSol1(arr2)); // Output: [5, 25, 25, -1]
        System.out.println(nge.nextLargerElementSol1(arr3)); // Output: [-1, 12, 12, -1]

        // Find and print the Next Greater Element (NGE) for each list
        System.out.println(nge.nextLargerElementSol2(arr1)); // Output: [13, 21, -1, -1]
        System.out.println(nge.nextLargerElementSol2(arr2)); // Output: [5, 25, 25, -1]
        System.out.println(nge.nextLargerElementSol2(arr3)); // Output: [-1, 12, 12, -1]
    }
}