import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class puzzle_multiple {

	private static final int N = 4;
	private static final int SHIFT = 2;
	private static final int MASK = 3;
	private static final int MOVE_LIM = 6;

	private static final String colors = "RGBY";

	private int[][] g;

	private int solvedCode;

	public puzzle_multiple() {
		g = new int[N][N];

		solvedCode = 0;
		for (int row = 1; row < N; row++) {
			for (int col = 0; col < N; col++) {
				solvedCode <<= SHIFT;
				solvedCode |= row;
			}
		}
	}

	private void print(int code) {
		decode(code);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.err.write(colors.charAt(g[i][j]));
			}
			System.err.println();
		}
		System.err.println();
	}

	private int encode() {
		int code = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				code <<= SHIFT;
				code |= g[i][j];
			}
		}
		return code;
	}

	private void decode(int code) {
		for (int i = N - 1; i >= 0; i--) {
			for (int j = N - 1; j >= 0; j--) {
				g[i][j] = code & MASK;
				code >>= SHIFT;
			}
		}
	}

	/**
	 * Make a move in-place
	 * 
	 * @param idx
	 *            Index of the row or column
	 * @param type
	 *            row(0) or column(1)
	 * @param dir
	 *            right/down (0) or left/up(1)
	 */
	private void move(int idx, int type, int dir) {
		if (type == 0) {
			if (dir == 0) {
				int saved = g[idx][N - 1];
				for (int col = N - 1; col > 0; col--) {
					g[idx][col] = g[idx][col - 1];
				}
				g[idx][0] = saved;
			} else {
				int saved = g[idx][0];
				for (int col = 0; col < N - 1; col++) {
					g[idx][col] = g[idx][col + 1];
				}
				g[idx][N - 1] = saved;
			}
		} else {
			if (dir == 0) {
				int saved = g[N - 1][idx];
				for (int row = N - 1; row > 0; row--) {
					g[row][idx] = g[row - 1][idx];
				}
				g[0][idx] = saved;
			} else {
				int saved = g[0][idx];
				for (int row = 0; row < N - 1; row++) {
					g[row][idx] = g[row + 1][idx];
				}
				g[N - 1][idx] = saved;
			}
		}
	}

	private int solve(int startingCode, Set<Integer> targetCodes) {

		Map<Integer, Integer> seen = new HashMap<Integer, Integer>();
		Queue<Integer> q = new LinkedList<Integer>();

		seen.put(startingCode, 0);
		q.add(startingCode);
		while (!q.isEmpty()) {
			int code = q.poll();
			int moves = seen.get(code);
			if (targetCodes.contains(code)) {
				return moves;
			}
			if (moves < MOVE_LIM) {
				decode(code);
				for (int idx = 0; idx < N; idx++) {
					for (int type = 0; type < 2; type++) {
						for (int dir = 0; dir < 2; dir++) {
							move(idx, type, dir);
							int nextCode = encode();
							move(idx, type, 1 - dir);
							if (!seen.containsKey(nextCode)) {
								seen.put(nextCode, moves + 1);
								q.add(nextCode);
							}
						}
					}
				}
			}
		}

		Set<Integer> otherTargetCodes = new HashSet<Integer>();
		for (Map.Entry<Integer, Integer> e : seen.entrySet()) {
			if (e.getValue() == MOVE_LIM) {
				otherTargetCodes.add(e.getKey());
			}
		}
		// System.out.println(otherTargetCodes.size() + " " + seen.size());
		return MOVE_LIM
				+ solve(targetCodes.iterator().next(), otherTargetCodes);

	}

	private void printErrorAndExit(Scanner sc, String msg) {
		sc.close();
		System.err.println(msg);
		System.exit(-1);
	}

	private void work() {
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for(int casei=0;casei<cases;casei++) {
			int code = 0;
			for (int i = 0; i < N; i++) {
				char[] ca = sc.next().toCharArray();
				if (ca.length != N) {
					printErrorAndExit(sc, String.format(
							"Expected %d characters but got %d", N, ca.length));
				}
				for (int j = 0; j < N; j++) {
					code <<= SHIFT;
					int color = colors.indexOf(ca[j]);
					if (color < 0) {
						printErrorAndExit(sc,
								String.format("Unexpected color |%c|", ca[j]));
					}
					code |= color;
				}
			}
			Set<Integer> targetCodes = new HashSet<Integer>();
			targetCodes.add(code);
			System.out.println(solve(solvedCode, targetCodes));
		}
		sc.close();
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		new puzzle_multiple().work();
		/*
		long t = System.currentTimeMillis() - startTime;
		System.err.printf("Elapsed time: %02d:%02d:%02d.%03d\n",
				t / 1000 / 3600, ((t / 1000) % 3600) / 60, (t / 1000) % 60,
				t % 1000);
		*/
	}

}
