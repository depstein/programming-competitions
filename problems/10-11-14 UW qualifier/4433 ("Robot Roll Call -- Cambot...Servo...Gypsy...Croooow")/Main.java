import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner fin = new Scanner(System.in);
		
		int T = Integer.parseInt(fin.nextLine());
		
		for (int t = 0; t < T; t++) {
			int D = Integer.parseInt(fin.nextLine());
			ArrayList<String> roboNames = new ArrayList<String>();
			
			for (int d = 0; d < D; d++) {
				roboNames.add(fin.nextLine());
			}
			
			HashSet<String> strs = new HashSet<String>();
			
			int L = Integer.parseInt(fin.nextLine());
			
			for (int l = 0; l < L; l++) {
				String tmp = fin.nextLine();
				String[] tmps = tmp.split(" ");
				
				for (String t2 : tmps) strs.add(t2);
			}
			
			System.out.println("Test set " + (t+1) + ":");
			for (String t2 : roboNames) {
				System.out.println(t2 + " is " + ((strs.contains(t2)) ? "present" : "absent"));
			}
			System.out.println();
		}

	}

}
