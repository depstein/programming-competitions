/*  ACM Mid-Central Programming competition 2014 Problem F: Maze Makers
    solution by Andy Harrington
    
    A single depth first search is needed, avoiding reversing, counting cells,
    and noting the terminus and when there is a repeat (and hence a loop).
*/

import java.util.*;
import java.io.*;

public class maze
{
   static int[][] cell;
   static boolean[][] visited;
   static boolean hasLoop, hasSolution;
   static int H, W, cellsLeft;// latter to detect unreachable cells
   static int[] termC = new int[2], termR = new int[2], // dir low bit
                dr = {0, 1, 0, -1}, dc = {-1, 0, 1, 0}; //     to high

   public static void main(String[] args) throws Exception {
      String file = (args.length > 0) ? args[0] : "maze.in";
      String hex = "0123456789ABCDEF";
      Scanner in = new Scanner(new File(file));
      H = in.nextInt();
      while (H != 0) {
         int termi = 0;
         W = in.nextInt();
         cell = new int[H][W];
         for (int r=0; r < H; r++) {
            String line = in.next();
            for (int c=0; c < W; c++) {
               cell[r][c] = hex.indexOf(line.charAt(c));
               boolean isTerminal = true;  // note openings and remove
               if (r == 0 && (cell[r][c] & 8) == 0)
                  cell[r][c] |= 8;
               else if (c == W-1 && (cell[r][c] & 4) == 0)
                  cell[r][c] |= 4;
               else if (r == H-1 && (cell[r][c] & 2) == 0)
                  cell[r][c] |= 2;
               else if (c == 0 && (cell[r][c] & 1) == 0) 
                  cell[r][c] |= 1;
               else
                  isTerminal = false;
               if (isTerminal) {
                  termR[termi] = r; termC[termi] = c;
                  termi++;
               }
            }
         }
//         System.err.format("r%d c%d r%d c%d\n", 
//                           termR[0], termC[0], termR[1], termC[1]);
         visited = new boolean[H][W];
         hasLoop = hasSolution = false;
         cellsLeft = H*W;
         dfs(termR[0], termC[0], -1, -1);
         if (!hasSolution)
            System.out.println("NO SOLUTION");
         else if (cellsLeft > 0)
            System.out.println("UNREACHABLE CELL");
         else if (hasLoop)
            System.out.println("MULTIPLE PATHS");
         else 
            System.out.println("MAZE OK");
         H = in.nextInt();
      }
   }

   static void dfs(int r, int c, int parentR, int parentC) {
      if (visited[r][c]) {
         hasLoop = true;
//         System.err.format("again r%d c%d r%d c%d\n", r, c, parentR, parentC);
         return;
      }
//      System.err.format("dfs r%d c%d r%d c%d\n", r, c, parentR, parentC);
      visited[r][c] = true;
      cellsLeft--;
      if (r == termR[1] && c == termC[1])
         hasSolution = true;
      for (int i = 0, mask = cell[r][c]; i < 4; i++, mask >>= 1) 
         if ((mask & 1) == 0) {
            int nr = r + dr[i], nc = c + dc[i];
            if (nr != parentR || nc != parentC)
               dfs(nr, nc, r, c);
         }
   }
}