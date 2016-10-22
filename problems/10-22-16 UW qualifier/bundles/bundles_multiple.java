import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class bundles_multiple {

	class Bundle implements Comparable<Bundle> {
		int k, cost, bestCost, count, parent;
		long[] s;
		List<Bundle> children;

		Bundle(int k, int cost, int count, long[] s) {
			this.k = k;
			this.cost = cost;
			this.count = count;
			this.s = s;
			parent = -1;
			children = new ArrayList<Bundle>();
		}

		public boolean isSubsetOf(Bundle b) {
			return (s[0] & b.s[0]) == s[0] && (s[1] & b.s[1]) == s[1];
		}

		public int getBestCost(long[] ss) {
			long[] cs = new long[2];
			for (Bundle child : children) {
				bestCost += child.getBestCost(cs);
			}
			if (cs[0] != s[0] || cs[1] != s[1] || cost < bestCost)
				bestCost = cost;
			ss[0] |= s[0];
			ss[1] |= s[1];
			return bestCost;
		}

		public int compareTo(Bundle b) {
			if (s[0] < b.s[0])
				return -1;
			if (s[0] > b.s[0])
				return 1;
			if (s[1] < b.s[1])
				return -1;
			if (s[1] > b.s[1])
				return 1;
			if (cost < b.cost)
				return -1;
			if (cost > b.cost)
				return 1;
			return 0;
		}
	}

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			int nc = sc.nextInt();
			for (int tc = 1; tc <= nc; tc++) {
				int n = sc.nextInt();
				int m = sc.nextInt();
				Bundle root = null;
				Bundle[] tree = new Bundle[m + 1];
				for (int i = 0; i < m; i++) {
					int cost = sc.nextInt();
					int count = sc.nextInt();
					long[] s = new long[2];
					for (int j = 0; j < count; j++) {
						int u = sc.nextInt() - 1;
						if (u < 50) {
							s[0] |= 1L << u;
						} else {
							s[1] |= 1L << (u - 50);
						}
					}
					tree[i] = new Bundle(i, cost, count, s);
				}
				Arrays.sort(tree, 0, m);
				for (int i = 0; i < m - 1; i++) {
					if (tree[i].s[0] == tree[i + 1].s[0]
							&& tree[i].s[1] == tree[i + 1].s[1]) {
						for (int j = i + 1; j < m - 1; j++) {
							tree[j] = tree[j + 1];
						}
						m--;
						i--;
					}
				}

				for (int i = 0; i < m; i++) {
					if (tree[i].count == n)
						root = tree[i];
					for (int j = i + 1; j < m; j++) {
						if (tree[i].isSubsetOf(tree[j])) {
							tree[i].parent = j;
							tree[j].children.add(tree[i]);
							break;
						}
					}
				}

				if (root == null) {
					long[] s = new long[2];
					for (int i = 0; i < Math.min(n, 50); i++) {
						s[0] |= 1L << i;
					}
					for (int i = 50; i < n; i++) {
						s[1] |= 1L << (i - 50);
					}
					root = (tree[m] = new Bundle(m, Integer.MAX_VALUE, n, s));
					for (int i = 0; i < m; i++) {
						if (tree[i].parent < 0) {
							tree[i].parent = m;
							root.children.add(tree[i]);
						}
					}
				}

				System.out.println(root.getBestCost(new long[2]));
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new bundles_multiple().work();
		long t = System.currentTimeMillis() - startTime;
/*
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
*/
	}

}
