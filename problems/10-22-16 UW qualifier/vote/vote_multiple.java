import java.util.Scanner;

public class vote_multiple {

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			int[] k = new int[10];
			int[] v = new int[10];
			int nc = sc.nextInt();
			for (int tc = 1; tc <= nc; tc++) {
				int n = sc.nextInt();
				int s = 0;
				for (int i = 0; i < n; i++) {
					k[i] = i + 1;
					s += v[i] = sc.nextInt();
				}
				for (int i = 0; i < n; i++) {
					for (int j = n - 1; j > i; j--) {
						if (v[j] > v[j - 1]) {
							int t = v[j];
							v[j] = v[j - 1];
							v[j - 1] = t;
							t = k[j];
							k[j] = k[j - 1];
							k[j - 1] = t;
						}
					}
				}
				if (2 * v[0] > s) {
					System.out.println("majority winner " + k[0]);
				} else if (v[0] == v[1]) {
					System.out.println("no winner");
				} else {
					System.out.println("minority winner " + k[0]);
				}
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new vote_multiple().work();
		long t = System.currentTimeMillis() - startTime;
		/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
