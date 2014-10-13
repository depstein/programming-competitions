/**
* NOTE: this is too slow. Treat this as a sketch of the solution, and if you optimize it further, feel free to submit a pull request.
**/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 */
	public static point[] points;
	public static int n;
	public static int c;
	public static HashMap<Integer, Double> mask = new HashMap<Integer, Double>();
	public static double MAX = Double.MAX_VALUE;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int cases = in.nextInt();
		
		for(int aaa=0;aaa<cases;aaa++) {
			mask.clear();
			n = in.nextInt();
			c = in.nextInt();
			points = new point[n];
			for(int j=0;j<n;j++)
				points[j] = new point(in.nextDouble(), in.nextDouble());
			for(int i=0;i<1<<n;i++) {
				mask.put(i, calc(i));
			}
			MAX = Double.MAX_VALUE;
			ArrayList<Integer> masks = new ArrayList<Integer>();
			for(int i=0;i<c;i++)
				masks.add(0);
			recurse(0, 0, masks);
			System.out.printf("%.6f\n", MAX);
		}

	}
	
	private static void recurse(int index, int filled, ArrayList<Integer> masks) {
		if(index == n) {
			calcSum(masks);
			return;
		}
		for(int i=0;i<=Math.min(c-1, filled+1);i++) {
			int backup = masks.get(i);
			masks.set(i, masks.get(i) | 1<<index);
			recurse(index+1, Math.max(i, filled), masks);
			masks.set(i, backup);
		}
	}
	private static void calcSum(ArrayList<Integer> masks) {
		double myMax = Double.MIN_VALUE;
		for(int i : masks) {
			myMax = Math.max(mask.get(i), myMax);
		}
		MAX = Math.min(myMax, MAX);
	}
	private static Double calc(int i) {
		if(mask.containsKey(i)) {
			return mask.get(i);
		}
		
		ArrayList<point> myPoints = new ArrayList<point>();
		for(int j=0;j<points.length;j++) {
			if((i & 1<<j) != 0) {
				myPoints.add(points[j]);
			}
		}
		double maxDist = Double.MIN_VALUE;
		for(int j=0;j<myPoints.size();j++) {
			point p1 = myPoints.get(j);
			for(int k=j+1;k<myPoints.size();k++) {
				point p2 = myPoints.get(k);
				double dist = p1.distance(p2);
				maxDist = Math.max(dist, maxDist);
			}
		}
		maxDist = Math.sqrt(maxDist)/2;
		mask.put(i, maxDist);
		return maxDist;
	}

}

class point {
	double x;
	double y;
	public point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distance(point p2) {
		return (x-p2.x)*(x-p2.x) + (y-p2.y)*(y-p2.y);
	}
}