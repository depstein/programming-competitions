// rot.java
// Reverse Rot, MCPC 2014, Problem G
// Java solution by Michael Goldwasser

import java.io.File;
import java.util.Scanner;

public class rot {
  static final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_.";

  public static void main(String[] args) throws Exception {
    String file = (args.length > 0) ? args[0] : "rot.in";
    Scanner in = new Scanner(new File(file));

    int N;
    while (true) {
      N = in.nextInt();
      if (N == 0) break;

      String original;
      original = in.next();
      validate(N,original);

      for (int k=original.length()-1; k >=0; --k) {
        char c = original.charAt(k);
        int index = (alpha.indexOf(c)+N) % alpha.length();
        System.out.print(alpha.charAt(index));
      }
      System.out.println();
    }
  }

  // used only for validating proper input
  static void validate(int N, String original) {
    if (N < 1 || N > 27)
      System.err.println("ERROR: Invalid N");
    if (original.length() > 40)
      System.err.println("ERROR: message too long");
    for (char c : original.toCharArray()) {
      if (alpha.indexOf(c) == -1)
        System.err.println("ERROR: Invalid character: " + c);
    }

  }
}
