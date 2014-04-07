/*
 * "Roller Coaster", 2010 ICPC Mid-Atlantic Regionals problem F
 * LiveArchive problem #4870
 * Use Dynamic Programming to find the minimum amount of dizziness
 * possible for each amount of "fun" through each section of track
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.io.*;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    while(true) {
        int N = in.nextInt(), K = in.nextInt(), L = in.nextInt();
        if(N==0 && K==0 && L==0) {
          break;
        }
        int[] fun = new int[N], dizzy = new int[N];
        int maxF = 0;
        for(int i=0; i<N; i++) {
          fun[i] = in.nextInt();
          dizzy[i] = in.nextInt();
          maxF += fun[i];
        }
        // Now what?
        int[][] dp = new int[N][maxF+1];
        for(int i=0;i<N;i++) {
          for(int j=0;j<=maxF;j++) {
            dp[i][j] = Integer.MAX_VALUE;
          }
        }
        dp[0][0] = 0; //Populate with the first values
        if(dizzy[0] <= L) {
          dp[0][fun[0]] = dizzy[0];
        }
        for(int n=1; n<N; n++) {
          for(int f=0; f<=maxF; f++) {
            if(dp[n-1][f] != Integer.MAX_VALUE) {
              dp[n][f] = Math.max(dp[n-1][f] - K, 0); // Dizziness can't go below 0
            }
            if(f - fun[n] >= 0 && dp[n-1][f-fun[n]] != Integer.MAX_VALUE && dp[n-1][f-fun[n]] + dizzy[n] <= L) {
              dp[n][f] = Math.min(dp[n-1][f - fun[n]] + dizzy[n], dp[n][f]);
            }
          }
        }
        for(int i=maxF; i>=0; i--) {
          if(dp[N-1][i] != Integer.MAX_VALUE) {
            System.out.println(i);
            break;
          }
        }
    }
  }
}
