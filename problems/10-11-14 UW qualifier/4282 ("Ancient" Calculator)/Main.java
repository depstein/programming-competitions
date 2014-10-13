import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;


public class Main {
	public static HashMap<Integer, HashSet<Integer>> mAmpLookup = new HashMap<Integer, HashSet<Integer>>();
	
	public static int calcAmps(char c) {
		switch(c) {
		case '0': return 6;
		case '1': return 2;
		case '2': return 5;
		case '3': return 5;
		case '4': return 4;
		case '5': return 5;
		case '6': return 6;
		case '7': return 3;
		case '8': return 7;
		case '9': return 6;
		case '-': return 1;
		}
		return 0;
	}
	
	public static int calcAmps(int num) {
		String s = ""+ num;
		char[] sAra = s.toCharArray();
		int ret = 0;
		for (char c : sAra) ret+=calcAmps(c);
		return ret*5;
	}
	
	public static void main(String[] args) {
		Scanner fin = new Scanner(System.in);
		
		// Build lookup
		for (int i = -999; i <= 999; i++) {
			int amps = calcAmps(i);
			HashSet<Integer> list = mAmpLookup.get(amps);
			
			if (list == null) {
				list = new HashSet<Integer>();
				list.add(i);
				mAmpLookup.put(amps, list);
			}
			
			list.add(i);
		}
		
		
		while (true) {
			int a = fin.nextInt(); if (a == 0) break;
			int b = fin.nextInt();
			int c = fin.nextInt();
			
			HashSet<Integer> aList = mAmpLookup.get(a);
			HashSet<Integer> bList = mAmpLookup.get(b);
			HashSet<Integer> cList = mAmpLookup.get(c);
			
			//for (int cSel : cList) System.out.println("cPossible: " + cSel);
			
			int solutions = 0;
			
			if (aList != null && bList != null && cList != null) {
				//System.out.println("Found Lists");
				
				for (int aSel : aList) {
					for (int bSel : bList) {
						//System.out.println("Selecting " + aSel + " " + bSel);
						int tmp;
						
						if (bSel != 0) {
							tmp = aSel / bSel;
							if (cList.contains(tmp)) solutions++;
						}
						
						tmp = aSel * bSel;
						if (cList.contains(tmp)) solutions++;
						
						tmp = aSel + bSel;
						if (cList.contains(tmp)) solutions++;
						
						tmp = aSel - bSel;
						if (cList.contains(tmp)) solutions++;
					}
				}
			}
			
			if (solutions == 1)
				System.out.println(solutions + " solution for " + a + " " + b + " " + c);
			else
				System.out.println(solutions + " solutions for " + a + " " + b + " " + c);
		}
	}
}
