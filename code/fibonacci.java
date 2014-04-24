import java.io.*;
import java.util.*;

public class fibonacci {
  public static long[] memoize = new long[50];

  public static void main(String[] args) {
    memoize[0] = 1;
    memoize[1] = 1;

    for(int i=0;i<50;i++) {
      System.out.printf("The %dth Fibonacci number is %d.\n", i, fib_dp(i));
    }
  }

  public static long fib(int n) {
    if(n==0 || n==1) {
      return 1;
    }
    return fib(n-1) + fib(n-2);
  }

  public static long fib_dp(int n) {
    if(memoize[n] != 0) { //0 is the default value for the array elements
      return memoize[n];
    }

    memoize[n] = fib_dp(n-1) + fib_dp(n-2);
    return memoize[n];
  }
}
