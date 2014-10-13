import java.util.Scanner;


public class Main {

	/**
	 * @param args
	 */
	static double MIN = Double.MAX_VALUE;
	static double xPos;
	static double yPos;
	static double[] directionsX = new double[8];
	static double[] directionsY = new double[8];
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		int numCases = in.nextInt();
		
		double degrees = 0;
		for(int i=0;i<8;i++) {
			directionsX[i] = 10*Math.cos(Math.toRadians(degrees));
			directionsY[i] = 10*Math.sin(Math.toRadians(degrees));
			degrees -= 360/8.0;
			//System.out.println(directionsX[i] + " " + directionsY[i]);
		}
		
		for(int jjj=0;jjj<numCases;jjj++) {
			int t = in.nextInt();
			xPos = in.nextDouble();
			yPos = in.nextDouble();
			MIN = Double.MAX_VALUE;
			recurse(t, 0, 0, 0);
			System.out.printf("%.6f\n", Math.sqrt(MIN));
		}

	}
	private static void recurse(int timeLeft, double x, double y, int rotation) {
		MIN = Math.min(MIN, dist(x, y));
		if(timeLeft == 0)
			return;
		if(rotation<7)
			recurse(timeLeft-1, x, y, rotation+1);
		recurse(timeLeft-1, x+directionsX[rotation], y+directionsY[rotation], rotation);
	}
	private static double dist(double x, double y) {
		return (x-xPos)*(x-xPos) + (y-yPos)*(y-yPos);
	}

}
