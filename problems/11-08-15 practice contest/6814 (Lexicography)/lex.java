// lex.java
// Lexicography, MCPC 2014, Problem C
// Java solution by Andy Harrington

import java.util.*;
import java.io.*;

public class lex
{
   static int[] rep; //repetitions of each letter with unique letters sorted
   
   public static void main(String[] args) throws Exception {
      String file = (args.length > 0) ? args[0] : "lex.in";
      Scanner in = new Scanner(new File(file));
      String s= in.next();
      while (!s.startsWith("#")) {
         long K = in.nextLong() - 1;  // to index - since input counts from 1
         char[] sa = s.toCharArray();
         Arrays.sort(sa);
         rep = new int[sa.length];  // easiest - max size is length of string
         String uniqueStr = "";
         char prev = ' '; // does not match first character
         for(char ch : sa) // both find unique char and count repetitions
            if (ch != prev) {
               rep[uniqueStr.length()] = 1;
               uniqueStr += ch;
               prev = ch;
            }
            else rep[uniqueStr.length()-1]++;
         System.out.println(solve(K, uniqueStr.toCharArray()));
         s = in.next();
      }
   }

   static long fac(long i) { // return i!
      long tot = 1;
      for ( ; i>1; i--)
         tot *= i;
      return tot;
   }

   static long perm(int tot) { //permutations of tot items with repeats rep
      long x = fac(tot);
      for (int r: rep)
         x /= fac(r);  // extra 0's ok: 0! = 1
      return x;
   }

   static String solve(long K, char[] let) { // let is unique char sorted 
      int n = rep.length, d = let.length; // repetitions of let[i] is rep[i]
      char[] sol = new char[n];
      for (int i = 0; i < n; i++)  //next position to choose
          for (int j = 0; j < d; j++) // which letter to choose
             if (rep[j] > 0) {
                rep[j]--; // attempt to use let[j]
                long dk = perm(n-i-1); // #  starting with sol[0..i-1], let[j] 
                if (dk > K) {  // can't skip current letter    
                    sol[i] = let[j];  
                    break;
                }
                rep[j]++; // no, did not use let[j], 
                K -= dk;  // past let[j] and dK permutations of further chars
             }
      if (K > 0) System.err.println("Residual K is " + K);  // judge check
      return new String(sol);
   }
}