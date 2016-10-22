import java.util.Scanner;

public class junk_multiple {

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			double oo = 1e30;
			int n = sc.nextInt();
			while (n-- > 0) {
				double[] j1 = new double[7];
				for (int i = 0; i < j1.length; i++) {
					j1[i] = sc.nextInt();
				}
				double[] j2 = new double[7];
				for (int i = 0; i < j2.length; i++) {
					j2[i] = sc.nextInt();
				}

				double lo = 0;
				double hi = oo;
				for (int i = 0; i < 500; i++) {
					double a = (2 * lo + hi) / 3;
					double b = (lo + 2 * hi) / 3;
					if (f(a, j1, j2) < f(b, j1, j2))
						hi = b;
					else
						lo = a;
				}

				if (hi == oo || f(lo, j1, j2) > j1[3] + j2[3] + 1e-15) {
					System.out.println("No collision");
				} else {
					lo = 0;
					double d = j1[3] + j2[3];
					for (int i = 0; i < 300; i++) {
						double mid = 0.5 * (lo + hi);
						if (f(mid, j1, j2) < d)
							hi = mid;
						else
							lo = mid;
					}
					System.out.printf("%.3f\n", lo);
				}
			}
		}
		sc.close();
	}

	private double f(double t, double[] j1, double[] j2) {
		double dx = j1[0] + t * j1[4] - j2[0] - t * j2[4];
		double dy = j1[1] + t * j1[5] - j2[1] - t * j2[5];
		double dz = j1[2] + t * j1[6] - j2[2] - t * j2[6];
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new junk_multiple().work();
		/*
		long t = System.currentTimeMillis() - startTime;
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
