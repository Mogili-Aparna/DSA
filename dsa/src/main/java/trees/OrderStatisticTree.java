package trees;
public class OrderStatisticTree {
    TreeNode insert(TreeNode root, int val)
    {
        if(root == null)
            return new TreeNode(val);
        if(root.val > val)
            root.left=insert(root.left, val);
        if(root.val < val)
            root.right = insert(root.right, val);
        root.size = 1 + getSize(root.left) + getSize(root.right);
        return root;
    }
    int getSize(TreeNode root)
    {
        if(root == null)
            return 0;
        return root.size;
    }
    TreeNode delete(TreeNode root, int val)
    {
        if(root == null)
            return null;
        if(root.val > val)
            root.left =delete(root.left, val);
        else if(root.val < val)
            root.right =delete(root.right, val);
        else{
            if(root.left == null)
               return root.right;
            else if(root.right == null)
                return root.left;
            else {
                TreeNode successor = getMin(root.right);
                root.val = successor.val;
                root.right = delete(root.right, successor.val);
            }
        }
        root.size = 1 + getSize(root.left) + getSize(root.right);
        return root;
    }
    TreeNode getMin(TreeNode root)
    {
        if(root == null) return null;
        while(root.left!=null){
            root=root.left;
        }
        return root;
    }
    private void inorder(TreeNode root,String path){
        if(root == null) return;
        inorder(root.left,"left");
        System.out.print(root.val + "("+ root.size+") - path : "+path+" ");
        inorder(root.right,"right");
    }
    private int kthSmallest(TreeNode root,int k){
        if(root == null) return 0;
        int leftSize = getSize(root.left);
        if(k == leftSize + 1)return root.val;
        if(k <= leftSize)return kthSmallest(root.left,k);
        return kthSmallest(root.right,k-leftSize-1);
    }
    private int rank(TreeNode root,int x){
        if(root == null) return 0;
        if(root.val < x){
            return 1+getSize(root.left)+rank(root.right,x);
        }
        else if(root.val > x){
            return rank(root.left,x);
        }
        else{
            return 1+getSize(root.left);
        }
    }
    static void main(String[] args) {
        OrderStatisticTree tree = new OrderStatisticTree();
        TreeNode root = tree.insert(null,3);
        tree.insert(root,2);
        tree.insert(root,5);
        tree.insert(root,4);
        tree.insert(root,1);

        tree.inorder(root,"root");
        System.out.println();
        System.out.println("2nd smallest element is :"+ tree.kthSmallest(root,2));
        System.out.println("4th smallest element is :"+ tree.kthSmallest(root,4));
        System.out.println("rank of 2 :"+ tree.rank(root,2));
        System.out.println("rank of 4 :"+ tree.rank(root,4));
        System.out.println("rank of 3 :"+ tree.rank(root,3));
        tree.delete(root,1);
        tree.inorder(root,"root");
        tree.delete(root,5);
        System.out.println();
        tree.inorder(root,"root");
        tree.delete(root,3);
        System.out.println();
        tree.inorder(root,"root");
        System.out.println();
        System.out.println("2nd smallest element is :"+ tree.kthSmallest(root,2));

    }
}