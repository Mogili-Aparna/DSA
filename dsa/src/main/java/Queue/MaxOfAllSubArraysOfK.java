/**
 * ============================================================
 * Date :
 * Topic : Sliding Window Maximum (Monotonic Queue)
 * Pattern : Sliding Window + Monotonic Decreasing Deque
 * Leetcode : 239
 * ============================================================
 *
 * Problem:
 *
 * Given an array and window size k,
 * return maximum element in every window.
 *
 * Example:
 *
 * nums = [1,3,-1,-3,5,3,6,7]
 * k = 3
 *
 * Answer:
 *
 * [3,3,5,5,6,7]
 *
 * ------------------------------------------------------------
 * Observation 1:
 *
 * Brute Force:
 *
 * For every window:
 *     iterate over k elements.
 *
 * Time:
 *
 * O(n*k)
 *
 * ------------------------------------------------------------
 * Observation 2:
 *
 * Since repeatedly finding maximum,
 * Heap comes naturally.
 *
 * Max Heap:
 *
 * Insert current element.
 * Remove stale elements.
 *
 * Time:
 *
 * O(n log k)
 *
 * ------------------------------------------------------------
 * Observation 3 (Optimization):
 *
 * Since window moves only forward:
 *
 * elements leave in FIFO order.
 *
 * Some elements become permanently useless.
 *
 * Example:
 *
 * [5,2,4]
 *
 * Once 4 arrives:
 *
 * 2 can never become maximum.
 *
 * Because:
 *
 * 5 leaves before 2.
 * 4 leaves after 2.
 *
 * Therefore:
 *
 * remove 2 immediately.
 *
 * ------------------------------------------------------------
 * Pattern Recognition:
 *
 * Maximum / Minimum
 * +
 * Sliding Window
 * +
 * Window moves only forward
 *
 * ==> Think Monotonic Queue
 *
 * ------------------------------------------------------------
 * Monotonic Decreasing Queue:
 *
 * Front ---> Back
 *
 * Largest ----> Smallest candidate
 *
 * Example:
 *
 * [5,4,2]
 *
 * Invariant:
 *
 * nums[dq[0]]
 * >= nums[dq[1]]
 * >= nums[dq[2]]
 *
 * ------------------------------------------------------------
 * Why store indices?
 *
 * Need to know:
 *
 * Has element left the window?
 *
 * Window:
 *
 * [i-k+1 .... i]
 *
 * Remove:
 *
 * dq.peekFirst() <= i-k
 *
 * ------------------------------------------------------------
 * Algorithm:
 *
 * for each index:
 *
 * 1.
 * Remove expired indices.
 *
 * 2.
 * Remove smaller elements from back.
 *
 * 3.
 * Insert current index.
 *
 * 4.
 * If first window formed:
 *
 * i >= k-1
 *
 * add answer.
 *
 * ------------------------------------------------------------
 * Heap Solution:
 *
 * Time:
 * O(n log k)
 *
 * Space:
 * O(k)
 *
 * Easier to think.
 *
 * ------------------------------------------------------------
 * Monotonic Queue:
 *
 * Time:
 * O(n)
 *
 * Space:
 * O(k)
 *
 * Harder to recognize,
 * but optimal.
 *
 * ------------------------------------------------------------
 * Similar Problems:
 *
 * LC 239 - Sliding Window Maximum
 * LC 862 - Shortest Subarray >= K
 * LC 1438 - Longest Continuous Subarray
 * LC 1696 - Jump Game VI
 *
 * Stack cousins:
 *
 * LC 739 - Daily Temperatures
 * LC 901 - Stock Span
 * LC 84  - Largest Rectangle
 *
 * ------------------------------------------------------------
 */
/**
 * ============================================================
 * SOLUTION 1 : Max Heap
 * ============================================================
 *
 * Pattern:
 * Maximum in every window.
 *
 * Idea:
 * Keep maximum element in heap.
 * Remove stale elements.
 *
 * Store:
 * [value,index]
 *
 * Time:
 * O(n log k)
 *
 * Space:
 * O(k)
 *

    PriorityQueue<int[]> pq =
            new PriorityQueue<>(
                    (a, b) -> b[0] - a[0]);

    for (int i = 0; i < n; i++) {
        pq.offer(new int[]{nums[i], i});

        while (!pq.isEmpty()
                && pq.peek()[1] <= i - k) {
            pq.poll();
        }

        if (i >= k - 1) {
            ans.add(pq.peek()[0]);
        }
    }


 * ============================================================
 * SOLUTION 2 : Monotonic Decreasing Deque
 * ============================================================
 *
 * Store indices.
 *
 * Invariant:
 *
 * nums[dq[0]]
 * >= nums[dq[1]]
 * >= nums[dq[2]]
 *
 * Front always stores maximum.
 *
 * Time:
 * O(n)
 *
 * Space:
 * O(k)
 *


Deque<Integer> dq =
        new LinkedList<>();

for(int i=0;i<n;i++)
        {
        // 1. Remove expired indices
        while(!dq.isEmpty()
            && dq.peekFirst() <= i-k)
        {
        dq.removeFirst();
    }

            // 2. Remove smaller elements
            while(!dq.isEmpty()
            && nums[dq.peekLast()] <= nums[i])
        {
        dq.removeLast();
    }

            // 3. Insert current index
            dq.addLast(i);

// 4. First window formed
    if(i >= k-1)
        {
        ans.add(nums[dq.peekFirst()]);
        }
        }

 * ============================================================
 * Heap vs Monotonic Queue
 * ============================================================
 *
 * Heap:
 *
 * Easier to recognize.
 * Easier to code.
 *
 * Time:
 * O(n log k)
 *
 * Useful when:
 * - Window movement is irregular
 * - Elements are inserted/deleted arbitrarily
 *
 * ------------------------------------------------------------
 *
 * Monotonic Queue:
 *
 * Harder to recognize.
 *
 * Time:
 * O(n)
 *
 * Works only because:
 *
 * 1. Window moves only forward.
 * 2. Elements expire in FIFO order.
 * 3. Smaller elements can become
 *    permanently useless.
 *
 * Recognition Pattern:
 *
 * Sliding Window
 * +
 * Repeated Max/Min
 * +
 * Window moves only forward
 *
 * ==> Think Monotonic Queue
 *
 * ============================================================
 */

/**
 * Interview Flow:
 *
 * Brute Force
 *      ↓
 * Heap Solution
 *      ↓
 * Observe permanently useless elements
 *      ↓
 * Monotonic Queue Optimization
 *
 */