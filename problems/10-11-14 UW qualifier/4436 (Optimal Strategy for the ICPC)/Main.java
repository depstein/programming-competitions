/**
* NOTE: this is unoptimized, the optimized version is buried somewhere on my harddrive and I will attempt to find it later.
* For now, treat this as a sketch of the solution, and feel free to submit a pull request.
**/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
	public static Problem[] times;
	
	public static int DIOPDF = 50;
	
	public static Solution addShit(Solution start, char prob, int time, int aTime, int bTime, int cTime, int stackInd) {
		Solution s = new Solution(start.probs+1, start.time+time, start.solutions + prob + " ", start.aTime+aTime, start.bTime+bTime, start.cTime+cTime);
		
		s.stacks = new ArrayList<String>();
		for(String aaa : start.stacks)
			s.stacks.add(aaa);
		
		s.stacks.set(stackInd, s.stacks.get(stackInd)+" " +prob);
		return s;
	}
	
	public static Solution optTime(int aTime, int bTime, int cTime, int j) {
		if (j == 0) return Solution.NOPROBS;
		if (aTime < 0) return Solution.WORST;
		if (bTime < 0) return Solution.WORST;
		if (cTime < 0) return Solution.WORST;
		
		//System.out.println(j);
		
		Solution max = optTime(aTime, bTime, cTime, j-1);
		Solution tmp;
		
		tmp = optTime(aTime-times[j-1].time, bTime, cTime, j-1);
		tmp = addShit(tmp, times[j-1].letter, tmp.aTime+times[j-1].time, times[j-1].time, 0, 0, 0);
		if (j == DIOPDF) {
			System.out.println(300-aTime);
			System.out.println("If alloc to A then: " + tmp);
		}
		if (tmp.compareTo(max) > 0) max = tmp;
		
		//System.out.println(tmp + "!" + (300-aTime+times[j-1]));
		
		tmp = optTime(aTime, bTime-times[j-1].time, cTime, j-1);
		tmp = addShit(tmp, times[j-1].letter, tmp.bTime+times[j-1].time, 0, times[j-1].time, 0, 1);
		if (j == DIOPDF) System.out.println("If alloc to B then: " + tmp);
		if (tmp.compareTo(max) > 0) max = tmp;
		
		tmp = optTime(aTime, bTime, cTime-times[j-1].time, j-1);
		tmp = addShit(tmp, times[j-1].letter, tmp.cTime+times[j-1].time, 0, 0, times[j-1].time, 2);
		if (j == DIOPDF) System.out.println("If alloc to C then: " + tmp);
		if (tmp.compareTo(max) > 0) max = tmp;
		
		return max;
	}
	
	public static void main(String[] args) {
		Scanner fin = new Scanner(System.in);
		
		int N = fin.nextInt();
		
		for (int n = 0; n < N; n++) {
			times = new Problem[fin.nextInt()];
			
			for(int i = 0; i<times.length; i++) {
				times[i] = new Problem(fin.nextInt(), (char) (i+'A'));
			}
			
			Arrays.sort(times);
			
			//System.out.println(optTime(300, 300, 300, 1));
			//System.out.println(optTime(300, 300, 300, DIOPDF));
			//System.out.println(optTime(300, 300, 300, 3));
			//System.out.println(optTime(300, 300, 300, 4));
			//System.out.println(optTime(300, 300, 300, 5));
			//System.out.println(optTime(300, 300, 300, 6));
			
			Solution s = optTime(300, 300, 300, times.length);
			System.out.println("DONE: " + s);
			System.out.println("A: " + s.stacks.get(0));
			System.out.println("B: " + s.stacks.get(1));
			System.out.println("C: " + s.stacks.get(2));
		}
	}
}

class Solution implements Comparable<Solution> {
	public static Solution NOPROBS = new Solution(0, 0, "", 0, 0, 0);
	public static Solution WORST = new Solution(0, Integer.MAX_VALUE, "", 0, 0, 0);
	
	public int probs, time;
	public String solutions;
	public ArrayList<String> stacks;
	public int aTime, bTime, cTime;
	
	public Solution(int probs, int time, String solutions, int aTime, int bTime, int cTime) {
		this.probs = probs;
		this.time = time;
		this.solutions = solutions;
		this.aTime = aTime;
		this.bTime = bTime;
		this.cTime = cTime;
		this.stacks = new ArrayList<String>(3);
		this.stacks.add(""); this.stacks.add(""); this.stacks.add("");
	}
	
	public int compareTo(Solution s) {
		if (probs == s.probs) {
			if(s.time == this.time) {
				return this.solutions.compareTo(s.solutions);
			}
			return s.time - this.time;
		}
		return this.probs - s.probs;
	}
	
	public String toString() {
		return "(" + probs + ", " + time + ", " + solutions + ")";
	}
}

class Problem implements Comparable<Problem> {
	public int time;
	public char letter;
	
	public Problem(int time, char letter) {
		this.time = time;
		this.letter = letter;
	}
	
	public int compareTo(Problem o) {
		if(this.time == o.time)
			return letter - o.letter;
		return this.time - o.time;
	}
}
