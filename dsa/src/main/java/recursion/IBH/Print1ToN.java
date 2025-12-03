package recursion.IBH;

public class Print1ToN {
    static void main() {
        print(7);
        print(1);
        print(0);
    }
    static void print(int n){
        if(n<1){
            throw new IllegalArgumentException("n must be >= 1");
        }
        if(n==1){ //base condition
            System.out.println(n);
            return;
        }
        print(n-1); //hypothesis
        System.out.print(n+" ");//induction
    }
}
