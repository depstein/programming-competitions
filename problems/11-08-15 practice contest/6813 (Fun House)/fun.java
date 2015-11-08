// fun.java
// Fun House, MCPC 2014, Problem B
// Java solution by Michael Goldwasser

import java.io.File;
import java.util.Scanner;

public class fun {

  final static int MIN_W = 5;
  final static int MAX_W = 20;
  final static int MIN_L = 5;
  final static int MAX_L = 20;

  static int W,L;
  static char house[][] = new char[MAX_W][MAX_L];

  public static void main(String[] args) throws Exception {
    String file = (args.length > 0) ? args[0] : "fun.in";
    Scanner in = new Scanner(new File(file));

    int num = 1;
    while (true) {
      W = in.nextInt();
      if (W == 0) break;

      L = in.nextInt();

      int curX=0, curY=0;
      int dx=0, dy=0;

      for (int y=0; y < L; y++) {
        String line = in.next();
        if (line.length() != W)
          System.err.println("Line has invalid length");
        for (int x=0; x < W; x++) {
          house[x][y] = line.charAt(x);
          if (house[x][y] == '*') {
            curX = x;
            curY = y;
            if (x == 0)
              dx = 1;
            else if (x == W-1)
              dx = -1;
            else if (y == 0)
              dy = 1;
            else if (y == L-1)
              dy = -1;
          }
        }
      }
      validate();

      do {
        curX += dx;
        curY += dy;
        if (house[curX][curY] == '/') {
          int temp = dx;
          dx = -dy;
          dy = -temp;
        } else if (house[curX][curY] == '\\') {
          int temp = dx;
          dx = dy;
          dy = temp;
        }
      } while (house[curX][curY] != 'x');
      house[curX][curY] = '&';

      System.out.println("HOUSE " + num++);
      for (int y=0; y < L; y++) {
        for (int x=0; x < W; x++)
          System.out.print(house[x][y]);
        System.out.println();
      }
    }
  }

  // used only for validating proper input
  static void validate() {
    if (W < MIN_W || W > MAX_W)
      System.err.println("ERROR: Invalid W");
    if (L < MIN_L || L > MAX_L)
      System.err.println("ERROR: Invalid L");

    boolean foundEntrance = false;
    for (int x=0; x < W; x++)
      for (int y=0; y < L; y++) {
        if (x > 0 && x < W-1 && y > 0 && y < L-1) {
          // interior
          if ("./\\".indexOf(house[x][y]) == -1)
            System.err.println("ERROR: invalid interior character");
        } else {
          // boundary
          if ("*x".indexOf(house[x][y]) == -1)
            System.err.println("ERROR: invalid boundary character");
          if (house[x][y] == '*') {
            if (foundEntrance) {
              System.err.println("ERROR: Found multiple entrances");
            } else {
              foundEntrance = true;
              if ((x == 0 && y == 0) ||
                  (x == 0 && y == L-1) ||
                  (x == W-1 && y == 0) ||
                  (x == W-1 && y == L-1))
                System.err.println("ERROR: Entrance in corner.");
            }
          }
        }
      }
  }
}
