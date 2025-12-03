package recursion.IBH;

public class PrintNto1 {
    static void main() {
        print(7);
        print(6);
        print(1);
        print(0);
    }
    static void print(int n){
        if(n<1) {//invalid cases
            throw new IllegalArgumentException("n must be >= 1");
        }
        if(n==1){//base condition
            System.out.println(n);
            return;
        }
        System.out.print(n+" ");//induction
        print(n-1);//hypothesis
    }
}
