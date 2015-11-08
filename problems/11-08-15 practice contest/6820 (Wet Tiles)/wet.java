/*  ACM Mid-Central Programming competition 2014 Problem I: Wet Cells
    solution by Andy Harrington

    Input:  X Y T L W
            x1 y1 ... xL yL
            ex1 ey1 ... exW eyW

    Final line after all input cases contains 0.

    ALgorithm:  At each time step a new edge set of tiles get wet.  
    They are always adjacent to tiles that got wet in the previous time step.  
    Other tiles that are adjacent to a tile in the previous time
    step include other from that same time step, and tiles 
    from the time step before that, and walls.  Hence the next time step's
    edge needs just memory of the edges from the two previous time steps
    and the fixed set of wall tiles, including the outer walls.

    For the sets we remember 2D coordinates encoded as a linear index from 
    a row by row listing of the tiles.  

    Using HashSets, the time is proportional to the total area, no matter
    how large T is.
*/

import java.util.*;
import java.io.*;
import static java.lang.Math.*;

public class wet {

   static int X, Y, T, L, W, nWet, X2; // X2 = X+2 is full grid width
   static HashSet<Integer> wall =  new HashSet<Integer>(), nextEdge, 
            edge = new HashSet<Integer>(), prevEdge = new HashSet<Integer>();
   static char[][] pic;

   public static void main(String[] args) throws Exception {
      String file = (args.length > 0) ? args[0] : "wet.in";
      Scanner in = new Scanner(new File(file));
      X = in.nextInt();
      while (X != -1) {
         Y = in.nextInt(); X2 = X + 2; 
         T = in.nextInt(); L = in.nextInt(); W = in.nextInt();
         edge.clear(); prevEdge.clear(); wall.clear();
         nWet = 0;
         for (int i = 0; i < L; i++) 
            edge.add(toCode(in.nextInt(), in.nextInt()));
         nWet = L;
         for (int i = 0; i < W; i++) 
            addWall(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt());
         addWall(1, 0, X, 0); addWall(1, Y+1, X, Y+1); // walls at edges of
         addWall(0, 1, 0, Y);  addWall(X+1, 1, X+1, Y);//[0...X+1] x [0...Y+1]
         //dprintln(show(wall)); //debug
         int[] dHV = {-1, 1, X2, -X2};
         initPic();  //judge
         for (int t=2; t<=T; t++) {
            nextEdge = new HashSet<Integer>();
            for (int e: edge)
               for (int del: dHV) {
                  int nbr = e + del;
                  if   (!wall.contains(nbr)  && !edge.contains(nbr)  && 
                        !prevEdge.contains(nbr))
                     nextEdge.add(nbr);
               }
            prevEdge = edge;
            edge = nextEdge;
            int added = edge.size();
            if (added == 0) break;
            addSet(edge, (char)('0'+t%10)); // judge
            nWet += added;
            //dprintln("t: " + t + " wet: " + nWet + "\n" + show(edge)); //debug
         }
         System.out.println(nWet);
         showPic(); //judge
         X = in.nextInt();
      }
   }
    
   static int toCode(int x, int y) {return X2*y + x;} // 2D to linear

   static void addWall(int x1, int y1, int x2, int y2) {
      int len = max(abs(x2-x1), abs(y2-y1)),
          c1 = toCode(x1, y1), c2 = toCode(x2, y2);
      wall.add(c1);
      if (len > 0) 
         for (int i = 1; i <= len; i++)
            wall.add(c1 + i*(c2-c1)/len);
   }
 
   ////////// rest for judges' testing ///////////
   static int debug = 1;
   static int MAX_PIC_SIZE = 20;

   static void dprint(String s) {
      if (debug > 0) System.err.print(s);
   }

   static void dprintln(String s) {
      dprint(s+ "\n");
   }

   static String showC(int c) { // ordered pair as string
       return String.format("(%d,%d)", c%X2, c/X2);
   }

   static String show(HashSet<Integer> s) { // show all ordered pairs
      Integer[] sa = s.toArray(new Integer[0]);
      Arrays.sort(sa);
      String ans = "";
      for (int c: sa)
         ans += showC(c) + " ";
      return ans;  // ordered by row
   }

   static void initPic() { // set up graphical display
     if (max(X, Y) > MAX_PIC_SIZE) return;
     pic = new char[Y+2][X2];
     addSet(wall, 'W');
     addSet(edge, 'L');
   }

   static void addSet(Set<Integer> s, char ch) { // add to graphical display
     if (max(X, Y) > MAX_PIC_SIZE) return;
      for( int c: s) 
        pic[c/X2][c%X2] = ch;
   }

   static void showPic() {// show pic: Y first then no outer walls, rows Y..1
     if (max(X, Y) > MAX_PIC_SIZE) return;
     dprintln("\n"+Y);
     for (int y = Y; y > 0; y--) {
        String s = new String(pic[y]).substring(1,X+1);
        dprintln(s.replace((char)0, '.'));
     }
   }
}