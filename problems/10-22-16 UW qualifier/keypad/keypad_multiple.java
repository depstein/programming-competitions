import java.util.Scanner;

public class keypad_multiple {

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			int nc = sc.nextInt();
			for (int tc = 1; tc <= nc; tc++) {
				int m = sc.nextInt();
				int n = sc.nextInt();

				int[][] g = new int[m][n];
				for (int i = 0; i < m; i++) {
					char[] line = sc.next().toCharArray();
					for (int j = 0; j < n; j++) {
						g[i][j] = line[j] - '0';
					}
				}

				boolean ok = true;
				char[][] ans = new char[m][n];
				for (int i = 0; i < m && ok; i++) {
					for (int j = 0; j < n && ok; j++) {
						boolean h = false;
						for (int k = 0; k < n; k++) {
							if (j != k && g[i][k] != 0) {
								h = true;
								break;
							}
						}
						boolean v = false;
						for (int k = 0; k < m; k++) {
							if (k != i && g[k][j] != 0) {
								v = true;
								break;
							}
						}
						if (g[i][j] == 0) {
							ok &= !(h && v);
							ans[i][j] = 'N';
						} else {
							ans[i][j] = (h && v) ? 'I' : 'P';
						}
					}
				}

				if (ok) {
					for (int i = 0; i < m; i++) {
						for (int j = 0; j < n; j++) {
							System.out.write(ans[i][j]);
						}
						System.out.write('\n');
					}
				} else {
					System.out.println("impossible");
				}

				System.out.println("----------");
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new keypad_multiple().work();
		long t = System.currentTimeMillis() - startTime;
		/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
