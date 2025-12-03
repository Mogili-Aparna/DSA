package recursion.IBH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HeightOfABinaryTree {
    /*
    problem : find the height of a binary tree
    Examples :
      1)input : a
             / \
            b   c
           / \
          d   e
         /
        f
      output : 4
      2)
       input : a
            / \
           b   c
          /   / \
         d   e   f
                  \
                   g
                  / \
                 h   i
         output : 5
           height = # of layers (depth)

 💡 Notes for Your IBH Section
| Step               | What It Means Here                                |
| ------------------ | ------------------------------------------------- |
| **Base Condition** | Tree is empty (`root == null`) → height = 0       |
| **Hypothesis**     | Assume we know height of left and right subtrees  |
| **Induction**      | Combine: `1 + max(leftHeight, rightHeight)`       |
| **Visualization**  | A recursion tree (each node calling left + right) |
| **Category**       | Recursive by nature (IBH fits perfectly)          |
solved : true
difficulty : easy
*/
    static void main() {
        Node root = formTree();
        System.out.println(getHeight(root));
    }
    // IBH method:
    // Base: if node is null → height = 0
    // Hypothesis: assume getHeight() returns correct height for left and right subtree
    // Induction: height of current node = max(leftHeight, rightHeight) + 1
    private static int getHeight(Node root) {
        if(root == null)//base condition
            return 0;
        return Math.max(getHeight(root.left),getHeight(root.right))+1;//hypothesis + induction
    }
    // Builds a sample tree (modify as needed)
    static Node formTree(){
        Node n3 = new Node('d',null,null);
        Node n4 = new Node('e',null,null);
        Node n5 = new Node('f',n4,n3);
        Node n7 = new Node('h',null,null);
        Node n8 = new Node('i',null,null);
        Node n6 = new Node('g', n7,n8);
        Node n2 = new Node('c',n5,n6);
        Node n1 = new Node('b',n2,null);
        return new Node('a',n1,null);
    }
    // Node definition
    static class Node{
        char value;
        Node left;
        Node right;
        Node(char value,Node left,Node right){
            this.value= value;
            this.left = left;
            this.right = right;
        }
    }
}
/*
 */
