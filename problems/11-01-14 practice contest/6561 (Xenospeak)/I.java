/* Xenospeak -- find strings in the sequence s1, s2, ... where
 si is the i-th string (in lexicographic order) that can be formed
 from a, ab, bb.

 KEY OBSERVATION: Every string of as and bs appears in this sequence
 except those that begin with an odd number of bs.
*/
import java.util.*;

public class I {
  public static void main(String[] args) {
    long wpp, pn;        // words per page, page number (input)
    long first,last; // first and last sequence numbers on page
    String ans1, ans2; // the strings labeling the page (output)
    Scanner in = new Scanner(System.in);
    int caseNum = 0;

    // Loop over all inputs:
    while(true) {
      wpp = in.nextLong();
      if (wpp == 0) break;
      pn = in.nextLong();
      first = (pn-1)*wpp+1;
      last = pn*wpp;
      caseNum++;

      ans1 = findans(first);
      ans2 = findans(last);
      System.out.println("Case " + caseNum + ": " + ans1 + " " + ans2);

    }
  }


  public static long mypow(int b) {
    long result = 1L;
    for (int i = 0; i < b; i++)
      result = 2*result;
    return result;
  }



  public static String findans(long n)
  {
      String ans = "";
      if (n == 1) return "a";
      else {
        long count = 1; // number of valid strings skipped over so far
        int exponent = 1; // start running through blocks of strings of
			// larger and larger powers of 2

        while (count < n) { // Each iteration looks at a bigger starting block
          boolean parity = true; // parity switches when exponent decreases by 1
          int base = exponent+1; // current string length
          int leading = 0; // number of leading b's in current block


          long blocksize = mypow(exponent);
          while (true) { // Count down by powers of 2 in current block
            if (parity && (count + blocksize >= n)) {
              long diff = n-count-1;
              // string consists of "leading" b's followed by
              // binary representation of "diff", padded with
              // enough a's to give total length "base"
              for (int k = 0; k < leading; k++)
                ans += "b";
              long val = diff;
              String res = "";
              for (int j = 0; j < base-leading; j++) {
                if (val%2==0) res = "a"+res;
                else res = "b"+res;
                val = val/2;
              }
              if (res.equals("")) res = "a";
              ans += res;
              return ans;
            }
            if (parity)
              count += blocksize;
            leading++;
            blocksize = blocksize/2;
            parity = !parity;
            if (parity && blocksize == 0) {
              count++;
              if (count == n) {
               // string consists of "leading" bs.
                for (int k = 0; k < leading; k++)
                   ans += "b";
                return ans;
              }
            }
            if (blocksize == 0)
              break;
          }
          exponent++;
        }
      }
      System.out.println("This shouldn't happen!");
      return "";
  }
}
