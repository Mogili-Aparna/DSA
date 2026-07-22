# Weekly Revision Test - Week __

Date: 22nd Jul 2026

Topics Covered:
- Arrays
- Stack
- Queue
- Linked List
- Trees/BST
- HashMap
- HashSet
- Sliding Window
- Java Concepts

---

# Q1.

## Q
When should you use HashMap and HashSet?

## A

HashMap:
Used when extra information is needed.

Examples:
- Frequency counting
- Last seen index
- Relationships between elements

HashSet:
Used when only uniqueness/existence checking is needed.

Examples:
- Contains Duplicate
- Counting Elements
- Jewels and Stones

## Why?

HashMap stores:

```text
key → value
```

HashSet stores:

```text
key
```

Recognition:

```text
Need extra information?
↓

HashMap

Need only existence?
↓

HashSet
```

---

# Q2.

## Q
Why is this O(n)?

```java
while(!set.add(s.charAt(r)))
{
    set.remove(s.charAt(l++));
}
```

## A

Time Complexity:

```text
O(n)
```

## Why?

Each character:

1. Enters window once.
2. Leaves window once.

Total operations:

```text
≤ 2n
```

This is called:

```text
Amortized Analysis
```

---

# Q3.

## Q
When do we use these BST patterns?

1. Inorder Traversal
2. Candidate Tracking
3. Pruning
4. Augmentation
5. Simultaneous DFS

---

## A

### 1. Inorder Traversal

Need sorted order.

Examples:
- Kth Smallest
- Minimum Difference BST

### Why?

BST inorder gives:

```text
Sorted Sequence
```

---

### 2. Candidate Tracking

Need:

- closest
- predecessor
- successor
- floor
- ceil

Examples:
- Closest Value BST

### Why?

Keep updating best answer seen so far.

---

### 3. Pruning

Need to eliminate entire subtree.

Examples:
- Range Sum BST
- Search BST

### Why?

BST ordering guarantees some subtrees cannot contain answer.

---

### 4. Augmentation

Need frequent:

- kth smallest
- rank
- count queries

### Why?

Store extra information.

Example:

```java
class Node
{
    int val;
    int size;
}
```

---

### 5. Simultaneous DFS

Given:

```text
(root1, root2)
```

Examples:

- Same Tree
- Symmetric Tree
- Merge Trees
- Subtree

### Why?

Question becomes:

```text
What should this PAIR of nodes return?
```

---

# Q4.

## Q
Why can we ignore right subtree if:

```text
target < root.val
```

## A

All values in right subtree are larger than root.

Difference can only increase.

## Why?

BST property:

```text
left < root < right
```

---

# Q5.

## Q

Difference:

```java
y = x--;
y = --x;
```

## A

Postfix:

```java
y = x--;
```

Assign first.

Decrement later.

Prefix:

```java
y = --x;
```

Decrement first.

Assign later.

## Why?

Prefix updates before evaluation.

Postfix updates after evaluation.

---

# Q6.

## Q

```text
arr = [1,1,2]
```

Answer?

## A

Answer:

```text
2
```

## Why?

Both 1's contribute because:

```text
1 + 1 = 2
```

exists.

Duplicates are counted separately.

---

# Q7.

## Q

```text
jewels = "aA"
stones = "aAAbbbb"
```

## A

Answer:

```text
3
```

## Why?

Matching stones:

```text
a
A
A
```

---

# Q8.

## Q

```text
s = "abba"
```

Longest substring without repeating characters?

## A

Answer:

```text
2
```

Substrings:

```text
ab
ba
```

## Why?

Duplicate 'b' forces shrinking of window.

---

# Q9.

## Q

Tree:

```text
        8
       / \
      4   10
     / \
    2   6
```

Target:

```text
5
```

Visited nodes?

Answer?

## A

Visited:

```text
8 → 4 → 6
```

Answer:

```text
4
```

## Why?

4 and 6 both have difference 1.

Problem returns smaller value.

---

# Q10.

## Q

Complexities:

- HashSet.add()
- HashSet.contains()
- HashMap.get()
- HashMap.put()

## A

Average:

```text
O(1)
```

Worst:

```text
O(n)
```

## Why?

Worst case:

All elements collide into same bucket.

---

# Q11.

## Q

Complexities:

1. Merge Trees
2. Closest Value BST
3. Longest Substring

## A

Merge Trees:

```text
O(m+n)
```

Closest Value:

```text
Average → O(log n)

Worst → O(n)
```

Longest Substring:

```text
O(n)
```

---

# Q12.

## Q

Why is this O(n)?

```java
for(int freq : map.values())
{
    if(!set.add(freq))
        return false;
}
```

## A

Loop runs once.

HashSet operations are O(1).

## Why?

Every frequency is processed only once.

---

# Mistakes Log

## Week 1

### ❌ Sliding Window
- Fixed vs Variable confusion.
- HashSet vs HashMap version confusion.

### ❌ HashMap Complexity
- Forgot worst case O(n).

### ❌ BST Patterns
- Mixed Pruning and Augmentation.

### ❌ Implementation
- Forgot:

```java
l =
Math.max(
        l,
        map.get(ch)+1
);
```

---

# Confidence Tracker

| Topic | Rating (1-5) |
|--------|--------------|
| Arrays | |
| Stack | |
| Queue | |
| Linked List | |
| Trees | |
| BST | |
| HashMap | |
| HashSet | |
| Sliding Window | |
| Java Collections | |

---

# Next Revision Date

Date:
___________________

Topics to revise:

- __________________
- __________________
- __________________