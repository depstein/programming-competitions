//  ACM Mid-Central Programming competition 2014 Problem A: More Multiplication
//  solution by Andy Harrington

import java.io.*;
import java.util.*;

public class multiplyAndy {
   static int W;  // total width with newline, for 2D to linear calc
   static StringBuffer all; // could use a String, but this is neater

   public static void main(String[] args) throws Exception {
      String file = (args.length > 0) ? args[0] : "multiply.in";
      Scanner in = new Scanner(new File(file));
      int A = in.nextInt();
      while (A != 0) {
         int B = in.nextInt(), C = A*B;
         int alen = (""+A).length(), blen = (""+B).length();
         String gSep = "| " + rep("+---", alen) + "+ |\n";
         W = gSep.length();
         String end = "+" + rep("-", W-3) + "+\n",
                gap = "|" + rep(" ", W-3) + "|\n", 
                g = gSep;
         String[] gridLine = {"|  /", "| / ", "|/  "};
         for (String line: gridLine)  // accumulate 4 grid rows
            g += "| " + rep(line, alen) + "| |\n";
         all = new StringBuffer(end + gap + rep(g, blen) + gSep + gap + end);
         for (int row = 4*blen, n = B; row > 0; row -= 4, n /= 10)      
            sub(n%10+"", row, W-3);                      //   B digits
         for (int col=4*alen, m=A; col > 0; col-=4, m /= 10) { 
            sub(m%10+"", 1, col);                        //   A digits
            for (int row = 4*blen, n=B; row > 0; row-=4, n /= 10) { 
               int p = (n%10)*(m%10);
               sub(p/10+"", row-1, col-1);             // product digits
               sub(p%10+"", row+1, col+1);
            }
         }
         for (int col = 4*alen-1; col > 0; col-=4, C /= 10) {
            sub(C%10+"", 3+4*blen, col);  // bottom answer
            if (col < 3*alen)
               sub("/", 3+4*blen, col+2);
         }
         for (int row = 1+4*blen; C > 0; row -= 4, C /= 10) {
            sub(C%10+"", row, 1);       // left column answer
            sub("/", row+2, 1);
         }
         System.out.print(all);
         A = in.nextInt();
      }
   }

   static String rep(String s, int n) {  // repeat s n times
      String ans = "";
      for (int i = 0; i < n; i++)
         ans += s;
      return ans;
   }

   static void sub(String ch, int row, int col) { // substitute at row, column
      int i = row*W + col;
      all.replace(i, i+1, ch); // could have longer, slower string assignment
   }
}