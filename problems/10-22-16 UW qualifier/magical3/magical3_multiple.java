import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class magical3_multiple {

	private Long[] ps;

	private String solve(long n) {
		if (n < 7) {
			return n == 3 ? Long.toString(4) : "No such base";
		}
		List<Long> divs = getSortedDivisorsFromFirstThreePrimes(n - 3);
		for (long i : divs) {
			if (i > 3)
				return Long.toString(i);
		}
		return "No such base";
	}

	private List<Long> getSortedDivisorsFromFirstThreePrimes(long n) {
		Map<Long, Integer> firstThreePrimes = getFirstThreePrimes(n);
		List<Long> ret = new ArrayList<Long>();
		for (long i : firstThreePrimes.keySet()) {
			ret.add(i);
			if (firstThreePrimes.get(i) > 1)
				ret.add(i * i);
			for (long j : firstThreePrimes.keySet()) {
				if (i < j)
					ret.add(i * j);
			}
		}
		Collections.sort(ret);
		return ret;
	}

	private Map<Long, Integer> getFirstThreePrimes(long n) {
		if (ps == null)
			init();
		Map<Long, Integer> ret = new TreeMap<Long, Integer>();
		for (int i = 0; i < ps.length && ps[i] * ps[i] <= n && ret.size() < 3; i++) {
			if (n % ps[i] == 0) {
				int k = 0;
				while (n % ps[i] == 0) {
					n /= ps[i];
					k++;
				}
				ret.put(ps[i], k);
			}
		}
		if (ret.size() < 3 && n > 1) {
			ret.put(n, 1);
		}
		return ret;
	}

	private void work() {

		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			init();
			int count = 0;
			while (true) {
				long n = sc.nextLong();
				if (n < 0 || n > Integer.MAX_VALUE) {
					printErrorAndExit(sc, String.format("N out of range: %d", n));
				}
				if (n == 0)
					break;
				if (++count > 5000) {
					printErrorAndExit(sc, "Too many cases");
				}

				System.out.println(solve(n));
			}
		}
		sc.close();
	}

	private void init() {
		boolean[] s = new boolean[100000];
		Arrays.fill(s, true);
		for (int i = 2; i * i < s.length; i++) {
			if (s[i]) {
				for (int j = i * i; j < s.length; j += i) {
					s[j] = false;
				}
			}
		}

		Set<Long> p = new TreeSet<Long>();
		for (int i = 2; (long) i * i < Integer.MAX_VALUE; i++) {
			if (s[i]) {
				p.add((long) i);
			}
		}
		ps = p.toArray(new Long[p.size()]);
	}

	private void printErrorAndExit(Scanner sc, String msg) {
		sc.close();
		System.err.println(msg);
		System.exit(-1);
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new magical3_multiple().work();
		/*
		long t = System.currentTimeMillis() - startTime;
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
