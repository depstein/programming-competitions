import java.io.*;
import java.util.*;

public class allpairs {
  public static int[][] dist = new int[5][5];

  public static void main(String[] args) {
    for(int i=0;i<5;i++)
      for(int j=0;j<5;j++) {
        if(i != j)
          dist[i][j] = 1000; // Not using Integer.MAX_VALUE to avoid integer overflowing
      }
    //Initialize graph as described
    dist[0][1] = 1;
    dist[0][2] = 3;
    dist[1][3] = 8;
    dist[2][3] = 2;
    dist[3][4] = 1;

    for(int k=0;k<5;k++)
      for(int i=0;i<5;i++)
        for(int j=0;j<5;j++) {
          dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
        }

    for(int i=0;i<5;i++)
      for(int j=0;j<5;j++)
        System.out.printf("The shortest path from %c to %c is %s.\n", (char)('A' + i), (char)('A' + j), (dist[i][j] == 1000) ? "Infinity": (""+dist[i][j]));
  }
}
