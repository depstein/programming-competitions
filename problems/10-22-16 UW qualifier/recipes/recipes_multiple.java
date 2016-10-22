import java.util.Scanner;

public class recipes_multiple {

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();

		for(int casei=0;casei<cases;casei++) {
			int nmax = 20;
			String[] rs = new String[nmax];
			double[] ws = new double[nmax];
			double[] ps = new double[nmax];
			int nc = sc.nextInt();
			for (int tc = 1; tc <= nc; tc++) {
				int r = sc.nextInt();
				int p = sc.nextInt();
				int d = sc.nextInt();
				double mainW = 0;
				for (int i = 0; i < r; i++) {
					rs[i] = sc.next();
					ws[i] = sc.nextDouble();
					ps[i] = 0.01 * sc.nextDouble();
					if (Math.abs(ps[i] - 1) < 1e-6) {
						mainW = ws[i] * d / p;
					}
				}
				System.out.printf("Recipe # %d\n", tc);
				for (int i = 0; i < r; i++) {
					System.out.printf("%s %.1f\n", rs[i], ps[i] * mainW);
				}
				System.out.println("----------------------------------------");

			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new recipes_multiple().work();
		long t = System.currentTimeMillis() - startTime;
		/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
