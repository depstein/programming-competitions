import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class race_multiple {

	private Scanner sc = new Scanner(System.in);
	private int n, T, oo = 1441;
	private int[] p, time, lim;
	private int[][] d;

	private int[] ptSum;
	private int[][] bestTime;

	private void work() {
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			readCase();
			init();

			int res = 0;
			int best = 0;
			for (int used = 0; used < 1 << n; used++) {
				for (int last = 0; last < n; last++) {
					if (((1 << last) & used) != 0) {
						if (go(last, used) + d[last][n + 1] <= T) {
							if (ptSum[used] > res
									|| (ptSum[used] == res && comp(used, best) < 0)) {
								res = ptSum[used];
								best = used;
							}
						}
					}
				}
			}

			System.out.println(res);

			boolean fst = true;
			for (int i = 0; i < n; i++) {
				if (((1 << i) & best) != 0) {
					if (fst) {
						fst = false;
					} else {
						System.out.write(' ');
					}
					System.out.print(i + 1);
				}
			}
			System.out.println();
		}

		sc.close();
	}

	private int comp(int mask1, int mask2) {
		while (mask1 != 0 && mask2 != 0) {
			int b1 = mask1 & 1;
			int b2 = mask2 & 1;
			if (b1 != b2)
				return b2 - b1;
			mask1 >>= 1;
			mask2 >>= 1;
		}
		if (mask2 > 0)
			return -1;
		if (mask1 > 0)
			return 1;
		return 0;
	}

	private int go(int k, int used) {
		if (bestTime[k][used] >= 0) {
			return bestTime[k][used];
		}
		int ret = oo;
		if (used == (1 << k)) {
			int need = d[n][k] + time[k];
			if (need <= lim[k]) {
				ret = need;
			}
		} else {
			for (int i = 0; i < n; i++) {
				if (i != k && ((1 << i) & used) != 0) {
					int need = d[i][k] + time[k] + go(i, used ^ (1 << k));
					if (need <= lim[k] && need < ret)
						ret = need;
				}
			}
		}
		return bestTime[k][used] = ret;
	}

	private void init() {
		ptSum = new int[1 << n];
		for (int i = 0; i < 1 << n; i++) {
			for (int j = 0; j < n; j++) {
				if (((1 << j) & i) != 0)
					ptSum[i] += p[j];
			}
		}

		bestTime = new int[n][1 << n];
		for (int i = 0; i < n; i++) {
			Arrays.fill(bestTime[i], -1);
		}
	}

	private void readCase() {
		n = sc.nextInt();
		T = sc.nextInt();
		p = new int[n];
		time = new int[n];
		lim = new int[n];
		d = new int[n + 2][n + 2];

		for (int i = 0; i < n; i++) {
			p[i] = sc.nextInt();
			time[i] = sc.nextInt();
			lim[i] = sc.nextInt();
			if (lim[i] < 0)
				lim[i] = oo;
		}

		for (int i = 0; i < n + 2; i++) {
			for (int j = 0; j < n + 2; j++) {
				d[i][j] = sc.nextInt();
			}
		}
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new race_multiple().work();
		long t = System.currentTimeMillis() - startTime;
		/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
