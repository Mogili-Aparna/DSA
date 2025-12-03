package recursion.recursiveTree;

import java.util.ArrayList;
import java.util.List;

public class ListAllSubSetsOfAString {
    public static void main(String[] args){
        String input = "abc";
        IO.println("iterative response : ");
        List<String> substrings = getSubSets(input);
        int counter =1;
        for (String subString:substrings)
        {
            IO.println(" subString "+(counter++)+" "+subString);
        }
        IO.println("recursive response : ");
        substrings = new ArrayList<>();
        getSubSetsRecursive(input,"",0,substrings);
        counter =1;
        for (String subString:substrings)
        {
            IO.println(" subString "+(counter++)+" "+subString);
        }
        IO.println("recursive response : ");
        substrings = new ArrayList<>();
        getSubSetsRecursive("aab","",0,substrings);
        counter =1;
        for (String subString:substrings)
        {
            IO.println(" subString "+(counter++)+" "+subString);
        }
    }
/*                          "abc","",0,[]
          "abc","",1,[]                  "abc","a",1,[]
    "abc","",2,[] "abc","b",2,[]  "abc","a",2,[] "abc","ab",2,[]
    "abc","",3,[] "abc","c",3,[] "abc","b",3,[] "abc","bc",3,[] "abc","a",3,[] "abc","ac",3,[]  "abc","ab",3,[] "abc","abc",3,[]
    ["","c","b","bc","a","ac","ab","abc"]*/
    private static void getSubSetsRecursive(String input, String output,int currentEleIndex, List<String> substrings) {
        if(currentEleIndex >= input.length()) {
            if(!substrings.contains(output)) //add this stmt only if question asks to print unique subsets.
                substrings.add(output);
            return;
        }
        char currentChar = input.charAt(currentEleIndex);
        currentEleIndex++;
        getSubSetsRecursive(input,output,currentEleIndex,substrings);
        getSubSetsRecursive(input,output+currentChar,currentEleIndex,substrings);
    }


    private static List<String> getSubSets(String input) {
        List<String> substrings = new ArrayList<>();
        substrings.add("");
        for(char ch : input.toCharArray()){
            List<String> newSubStrings = new ArrayList<>();
            for (String subString : substrings) {
                newSubStrings.add(subString + ch);
            }
            substrings.addAll(newSubStrings);
        }
        return substrings;
    }
}
