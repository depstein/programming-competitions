import java.util.Scanner;

public class cardgame_multiple {

	private int[] s, c;

	private int find(int x) {
		return x == s[x] ? x : (s[x] = find(s[x]));
	}

	private void union(int x, int y) {
		int sx = find(x);
		int sy = find(y);
		if (sx == sy) {
			c[sx]++;
		} else {
			s[sy] = sx;
			c[sx] += c[sy];
		}
	}

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			s = new int[200200];
			c = new int[200200];
			int nc = sc.nextInt();
			for (int tc = 1; tc <= nc; tc++) {
				int n = sc.nextInt();
				for (int i = 1; i <= n; i++) {
					s[i] = i;
					s[i + n] = i + n;
					c[i] = c[i + n] = 0;
				}
				for (int i = 0; i < n; i++) {
					int p = sc.nextInt();
					int q = sc.nextInt();
					union(p, q);
				}
				boolean ok = true;
				for (int i = n + n; i > 0; i--) {
					if (c[find(i)] > 1) {
						ok = false;
						break;
					}
				}
				System.out.println((ok ? "" : "im") + "possible");
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new cardgame_multiple().work();
		/*
		long t = System.currentTimeMillis() - startTime;
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
