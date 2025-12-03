package recursion.recursiveTree;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GenerateAllBalancedParentheses {
    /*
        ip : n = 2
        o/p :
                ()()
                (())
        ip : n = 3
        o/p : (()())
              ()()()
              (())()
              ()(())
              ((()))
     solved : true
     difficulty : medium
     */
    static void main() {
        System.out.println("Enter n :");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        Set<String> balancedParentheses = new HashSet<>();
        generateAllBalancedParentheses(ip,"",0,balancedParentheses);
        for (String permutation:balancedParentheses){
            System.out.println(permutation);
        }
    }

    /**
     *
     * @param ip
     * @param s
     * @param i
     * @param balancedParentheses
     */

    private static void generateAllBalancedParentheses(String ip, String s, int i, Set<String> balancedParentheses) {
    }
}
