package recursion.IBH;

public class Factorial {
    static void main() {
        System.out.println("factorial of 6 is "+factorial(6));
        System.out.println("factorial of 5 is "+factorial(5));
        System.out.println("factorial of 3 is "+factorial(3));
        System.out.println("factorial of 1 is "+factorial(1));
        System.out.println("factorial of 0 is "+factorial(0));
    }
    static int factorial(int n){
        if(n<=0) throw new IllegalArgumentException("n must be >= 1");
        if(n==1){ //base condition
           return n;
        }
        return factorial(n-1) * n; //hypothesis and induction
    }
}
