import java.util.Scanner;


public class Main {
	public static void main(String[] args) {
		Scanner fin = new Scanner(System.in);
		
		int n = 1;
		while (true) {
			int N = fin.nextInt();
			if (N == 0) break;
			
			System.out.println("Case " + n + ":");
			System.out.println(N + " pencils for " + N + " cents");
			
			int solutions = 0;
			
			for (int fourCP = 1; fourCP <= N; fourCP++) {
				for (int halfCP = 1; halfCP <= N-fourCP; halfCP++) {
					int quarterCP = N - halfCP - fourCP;
					if (quarterCP < 1) continue;
					
					int cost = quarterCP * 1 + halfCP * 2 + fourCP * 16;
					if (cost == N*4) {
						System.out.println(fourCP + " at four cents each");
						System.out.println(halfCP + " at two for a penny");
						System.out.println(quarterCP + " at four for a penny");
						System.out.println();
						solutions++;
					}
				}
			}
			
			if (solutions == 0) System.out.println("No solution found.\n");
			
			n++;
		}
	}
}
