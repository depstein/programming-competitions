import java.io.*;
import java.util.*;

public class practice {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int c = in.nextInt();
		for(int a=0;a<c;a++) {
			String t1 = in.next();
			String t2 = in.next();
			int s1 = in.nextInt();
			int s2 = in.nextInt();
			System.out.printf("%s Win!\n", s1>s2?t1:t2);
		}
	}
}