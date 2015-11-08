/*  ACM Mid-Central Programming competition 2014 Problem D: The Leprechaun Hunt
    solution by Andy Harrington

    villager bits (vBits):  bit 0..V-1 is 1 if villager at that vertex
    leprechaun vertex often called lPos
    stateBits:  toBits(lPos, vBits) = lPos + (vBits << 4) 
    willCatchV:  set of stateBits so SOME villager move => catch leprechaun
    willCatchL:  set of stateBits so ALL leprechaun moves => caught
    T:  current max number of turns until being caught for willCatchV
    nPosCaught maps vBits -> number of positions leprechaun will be caught from.
      This is updated every time willCatchL is modified. If some value is N-V,
      then we have a starting state and immediately return the solution T.
    edgeV: list of latest additions to willCatchV
    edgeL: list of latest additions to willCatchL
    
    The leprechaun wins if on next loop, edgeV or edgeL is empty.

    Central iteration idea: next new edge state must be adjacent to previous
    edge list, since a place that needs the maximum number of turns, 
    must go directly to one caught in one less turn.      

    Graph adds same vertex as neighbor: simplifies leprechaun staying put.

    State space size:   (N-V)*N!/V!/(N-V)! <= 51480 
    Each  in edge at most once for villager move and once for leprechaun.
    Loop to check out an adjacent state  <= 6*6 for lep move, less than
    6V*6V for villager ->  inner loop rep's bounded by 90810720. 
*/

import java.util.*;
import java.io.*;

public class hunt {
   static int V, N, T;  //T is turns to end for current willCatchV
   static int[][] g; //g[vertex][neighbor] use variable length int[] arrays
   static HashSet<Integer> willCatchV = new HashSet<Integer>(), //V next
            willCatchL = new HashSet<Integer>(); // lep next, any move loses
   static HashMap<Integer, Integer> nPosCaught // villagers state bits =>
        = new HashMap<Integer, Integer>();     // # places leprechaun not safe
   static ArrayList<Integer> edgeV = new ArrayList<Integer>(), // latest 
                         edgeL = new ArrayList<Integer>(); //  additions

   public static void main(String[] args) throws Exception {
      String file = (args.length > 0) ? args[0] : "hunt.in";
      Scanner in = new Scanner(new File(file));
      int nCase = 1;
      V = in.nextInt();
      while (V != 0) {
         N = in.nextInt();
         String[] letNeighbors = new String[N];
         Arrays.fill(letNeighbors, "");
         for (int E = in.nextInt(); E > 0; E--) {
            String e = in.next();
            letNeighbors[e.charAt(0)-'A'] += e.charAt(1);
            letNeighbors[e.charAt(1)-'A'] += e.charAt(0);
         }
         g = new int[N][];
         for (int i = 0; i < N; i++) {
            g[i] = new int[letNeighbors[i].length()+1];// +1 so allow stay
            g[i][0] = i;                               //   put for lep. moves
            for (int j = 1; j < g[i].length; j++)
               g[i][j] = letNeighbors[i].charAt(j-1) - 'A';
         }
         System.out.format("CASE %d: %s\n", nCase++, solve());
         V = in.nextInt();
      }
   }

   // all bit conversions - should be inlined in practice
   static int toBits(int lPos, int vBits) {return lPos + (vBits<<4);}
   static int getVBits(int bits) {return bits >> 4;}
   static int getLPos(int bits) {return bits & 15;}
   static boolean inSet(int vBits, int i) {return (vBits & (1 << i)) != 0;}
   static int inOut(int vBits, int a, int r) {return vBits + (1<<a) - (1<<r);}
   
   // adds entry to tilCatch, if not there, and nPosCaught; may give solution
   static boolean addEdgeEntry(int lPos, int vBits){ // true if solved, catch
      Integer badStartsI = nPosCaught.get(vBits);
      int badStarts = (badStartsI == null) ? 1 : badStartsI + 1;
      if (badStarts == N-V)    //   no place to start!
         return true;          //   leprechaun will be caught
      nPosCaught.put(vBits, badStarts);
      int bits = toBits(lPos, vBits);
      willCatchV.add(bits);
      dprintln(String.format("EdgeV T: %d vil: %s L: %d badStarts: %d",
                             T, showSet(vBits), lPos, badStarts)); //judge
      edgeV.add(bits);
      return false;
   }

   // Set first edgeL on end states, with 1 move to end. In recursion
   // vBits has bits for villagers so far, for bit n, n < nStart.
   // Add bits at nStart or later. vLeft is number of bit to add still.
   // In case no place for lep. that is safe for some state, return true.
   static boolean setEndStates(int vBits, int vLeft, int nStart) {
      if (vLeft ==  0) { // have all villagers, place lep.
         for (int i = 0; i < N; i++)    // for each vertex
            if (inSet(vBits,i))         //     with a villager
               for (int nb: g[i])                // if neighbor
                  if (!inSet(vBits,nb)           //    is not villager
                       && !willCatchV.contains(toBits(nb, vBits)) //not repeat
                       && addEdgeEntry(nb, vBits)) // && new entry -> solution
                     return true;                // done
         return false;                  // no immediate final solution
      }
      if (vLeft + nStart < N  && setEndStates(vBits, vLeft, nStart+1))
         return true;           // when can afford to skip nStart and solve or
      return setEndStates(vBits + (1<<nStart), vLeft-1, nStart+1);//use nStart
   }

   static void checkAllLCatch(int lp, int vBits) {// add if lep. always caught
     int bits = toBits(lp,vBits);
     if (willCatchL.contains(bits)) return;  // made entry before
     for (int nl: g[lp])  // check all lep moves
         if (!inSet(vBits,nl) && !willCatchV.contains(toBits(nl, vBits)))
            return;   // can escape still
     willCatchL.add(bits);  // all routes caught
     edgeL.add(bits);
   }

   // if some v move from vBits means leprechun at lp is caught, remember
   static boolean checkSomeVCatch(int lp, int vBits) { // true if now solved
       int bits = toBits(lp, vBits);
       if (willCatchV.contains(bits)) return false;  // no change
       for (int i = 0; i < N; i++)   // for each vertex
           if (inSet(vBits,i))       //   that has a villager
              for (int vMove: g[i])  { // each neighbor of villager
                 if (inSet(vBits, vMove)) continue; // skip used spot
                 int nextVBits = inOut(vBits, vMove, i); // change i -> vMove
                 if (willCatchL.contains(toBits(lp, nextVBits)))
                    return addEdgeEntry(lp, vBits); // a move will catch lep.
              }
       return false;  // no route yet
   }

   static String solve() {
      nPosCaught.clear();
      willCatchV.clear(); willCatchL.clear();
      edgeV.clear(); edgeL.clear();
      T=1;
      if (setEndStates(0, V, 0)) return "1"; // sets initial edgeV
      while (edgeV.size() > 0) {
         for (int bits: edgeV) { //add to willCatchL
            int vBits = getVBits(bits), lPos = getLPos(bits);
            for (int nl: g[lPos])
               if (!inSet(vBits, nl))
                  checkAllLCatch(nl, vBits);
         }
         if (edgeL.size() == 0) break;
         T++;  // next check for previous villager move
         edgeV.clear();
         for (int bits: edgeL)  {
            int vBits = getVBits(bits), lPos = getLPos(bits);
            for (int i = 0; i < N; i++)   // for each vertex
               if (inSet(vBits, i))       //   that has a villager
                  for (int vMove: g[i])  { // each neighbor of villager
                     if (inSet(vBits, vMove) || vMove == lPos) 
                        continue; // skip used spot
                     int prevBits = inOut(vBits, vMove, i); //move i -> vMove
                     if (checkSomeVCatch(lPos, prevBits)) 
                        return "" + T;              // may record solution     
                  }
         }
      }
      return "NEVER";
   }

   ////////// rest for judges' testing ///////////
   static int debug = 0;

   static void dprint(String s) {
      if (debug > 0) System.err.print(s);
   }

   static void dprintln(String s) {
      dprint(s+ "\n");
   }

   static int[] getSet(int vBits) { 
        int[] loc = new int[V];
        for (int i = 0, si = 0; i < N; i++)   // for each vertex
           if ((vBits & (1<<i)) != 0) 
             loc[si++] = i;                       
        return loc;
     }

   static String showSet(int vBits) {
      return Arrays.toString(getSet(vBits));
   }
}