import java.io.*;
import java.util.*;

public class lcs {
  public static int[][] memoize = new int[6][6];

  public static void main(String[] args) {
    for(int i=1;i<6;i++) {
      for(int j=1;j<6;j++) {
        memoize[i][j] = -1;
      }
    }

    String s1 = "TATGT";
    String s2 = "CATAG";
    System.out.printf("The longest common subsequence is of length %d.\n", lcs_dp(s1, s2, 5, 5));
  }

  public static int lcs(String s1, String s2, int s1index, int s2index) {
    if(s1index == s1.length() || s2index == s2.length()) {
      return 0;
    }
    if(s1.charAt(s1index) == s2.charAt(s2index)) {
      return 1 + lcs(s1, s2, s1index+1, s2index+1);
    }
    return Math.max(lcs(s1, s2, s1index+1, s2index), lcs(s1, s2, s1index, s2index+1));
  }

  public static int lcs_dp(String s1, String s2, int s1index, int s2index) {
    if(memoize[s1index][s2index] == -1) {
      if(s1.charAt(s1index-1) == s2.charAt(s2index-1)) { // the first row & column are all 0's, so charAt is off-by-1
        memoize[s1index][s2index] = 1 + lcs_dp(s1, s2, s1index-1, s2index-1);
      } else {
        memoize[s1index][s2index] = Math.max(lcs_dp(s1, s2, s1index-1, s2index), lcs_dp(s1, s2, s1index, s2index-1));
      }
    }
    return memoize[s1index][s2index];
  }
}
