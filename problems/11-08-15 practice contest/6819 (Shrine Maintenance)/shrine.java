/*  ACM Mid-Central Programming competition 2014 Problem H: Shrine Maintenance
    solution by Andy Harrington

    Input:  W N D d1 d2 ...dD     all positive integers
            W workers
            N number of equal length ways circle split
            D number of divisors
            d1 d2 ... dD distinct divisors of N
    If W workers start and end in the middle of a circle or radius 1000,
    There are N equally space places marked on the circumference
    of the circle, numbered consecutively.  The points with
    a number that is divisible by one of d1 d2 ...dD  must by visited by a 
    worker.  All workers begin and end from the middle of the circle.  
    If the assignments of locations to the workers are optimal, what is the 
    maximum distance any one worker must travel, to one decimal place.
    W <= the number of places on the circle to be visited, 
    N <= 10000, D <= 10.
    Data chosen so accurate answer + or - 0.005
    rounds to one digit with the same answer.
    Final line contains 0.
*/

import java.util.*;
import java.io.*;
import static java.lang.Math.*;

public class shrine
{
   static int W, R=1000, N, D, n;  // n is final number of points
   static int[] d;  // divisor, length D
   static int[] last; //last[w] = index of last node for worker w
   static int endW0; // last vert for first worker starting from vert 0
   static double[] sum;  //distance adding one edge at a time, loop past end
   static String lastEnds;  // judge

   public static void main(String[] args) throws Exception {
      String file = (args.length > 0) ? args[0] : "shrine.in";
      Scanner in = new Scanner(new File(file));
      W = in.nextInt();
      while (W != 0) {
         N = in.nextInt(); D = in.nextInt();
         d = new int[D];
         for (int i = 0; i < D; i++)
            d[i] = in.nextInt();
         System.out.format("%.1f\n", 2*R + solve());
         W = in.nextInt();
      }
   }

   static void getSum() {
      boolean[] p = new boolean[N];
      for (int f: d )
         for (int i = 0; i < N; i++)
            if (i%f == 0)
               p[i] = true;
      ArrayList<Integer> ip = new ArrayList<Integer>();
      for (int i = 0; i < N; i++)
         if (p[i])
            ip.add(i);
      n = ip.size();
      sum = new double[3*n+1];  // allow wrap around
      for(int i =1; i < sum.length; i++)
         sum [i] = sum[i-1] +
              2*R*abs(sin(PI*(ip.get(i%n) - ip.get((i-1)%n))/N));
      dprintln(3, "vertices and cumulative distance");
      for (int i = 0; i <= n; i++)
         dprintln(3, i + " " + sum[i]);
   }

   static double solve() {  // return minimax dist on circle, NO radii
      getSum();  // set up cumulative distances
      judgeCheck(); //judge testing
      if (W == n) return 0;  // so now at least one worker does > 1
      last = new int[W];
      double best = solveAt(0, sum[n-1]);
      int endLoop = endW0;
      for (int i = 1; i < endLoop; i++)
         if (enough(best, i))
            best = solveAt(i, best);
      dprintln(1, "Final: " + lastEnds);
      if (abs(round(1000*best) % 100 - 50) < 7)
         System.err.println("Roundoff issue! " + best);
      return best;
   }

   // see if splitting workers so one starts at index istart will allow
   // max less than working maxDist.  Return the lower of the two values.
   // Stop when value clear rounded to 2 digits (2nd for check).
   static double solveAt(int istart, double maxDist) {
      double tooLow = 0, high = maxDist;
      int dig = 1000;  // more accurate than required to checkj roundoff
      dprintln(2, "solveAt range " + tooLow +" " + high);
      while (round(tooLow*dig) < round(high*dig)) {
         double mid = (tooLow + high)/2.0;
         if (enough(mid, istart)) {
            high = mid;
            endW0 = last[0];  // for first call
         }
         else
            tooLow = mid;
         dprintln(2, "next " + tooLow +" " + high);
      }
      if (high + .01 < maxDist)
         dprintln(1, "Prev: " + lastEnds);
      return high;
   }

   // True if lim is enough distance for W workers,
   // with one worker starting at index istart
   // (skip more sophisticated version returning lower max)
   static boolean enough(double lim, int istart) {
      String msg =       //judge
          "ok enough istart: " +istart+ " n: " +n+ " lim: " +lim+ " extra: ";
      int end = istart + n - 1;
      for( int w = 0; w < W; w++) {
         int low = istart, past = istart + n-1;
         dprintln(3, "enough start w: " + w + " low: " + low + " past: " + past);
         while (past > low + 1) {
            int imid = (past + low)/2;
            if (sum[imid] - sum[istart] > lim)
               past = imid;
            else
               low = imid;
            dprintln(3, "enough next " +  low + " " + past);
         }
         last[w] = low;
         if (last[w] >= end) {
            lastEnds = endstr(msg, w);
            dprintln(2, lastEnds); //judge
            return true;
         }
         istart = last[w] + 1;
      }
      return false;
   }

   static double solveDynamic() {
      return 0.0;
   }

   ////////// rest for judges' testing ///////////
   static int MAX_N = 8600, MAX_D = 6; //  judge problem limits
   static void judgeCheck() {  
      if (N > MAX_N || D > MAX_D  || W > n)
         System.err.println("bad parameters");
      for (int f: d)
         if (N % f != 0)
            System.err.format("bad factor %d for N: %d\n", f, N);      
      dprint(1, String.format("n: %d W: %d N: %d D: %d :", n, W, N, D));
      for (int f: d) dprint(1, " " + f);
      dprintln(1, "");
   }

   static int debug = 0; // set > 0 for debugging to System.err

   static void dprint(int lev, String s) {
      if (debug >= lev) System.err.print(s);
   }

   static void dprintln(int lev, String s) {
      dprint(lev, s+ "\n");
   }

   static String endstr(String label, int lastw) {
      if (debug == 0) return "";
      label += W - lastw - 1 + " ends: ";
      for (int w=0; w <= min(lastw, 30); w++)
         label += " " + last[w];
      if (lastw > 30) label += " ...";
      return label;
   }

}