/*
 * "Assignments?", 2013 ICPC Pacific Northwest Regionals problem A
 * LiveArchive problem #6512
 * Count how many ships can reach a certain distance
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		for(int i=0;i<cases;i++) {
			int n = in.nextInt();
			int d = in.nextInt();
			int count = 0;
			for(int j=0;j<n;j++) {
				int v = in.nextInt();
				int f = in.nextInt();
				int c = in.nextInt();
				if((v*f)/c >= d) {
					count++;
				}
			}
			System.out.println(count);
		}
	}
}