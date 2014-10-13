import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static Node[][] nodes;
	public static int N;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		int caseNum = 0;
		while (true) {
			caseNum++;
			N = in.nextInt();
			if (N == 0)
				break;
			nodes = new Node[N][N];
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++)
					nodes[i][j] = new Node(i, j, in.nextInt());
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++)
					nodes[i][j].addNeighbors();
			PriorityQueue<Node> q = new PriorityQueue<Node>();
			nodes[0][0].pathLen = nodes[0][0].value;
			q.add(nodes[0][0]);
			while (q.size() > 0) {
				Node first = q.remove();
				first.visited = true;
				for (Node n : first.neighbors) {
					if (n.visited)
						continue;
					n.pathLen = Math.min(n.pathLen, first.pathLen + n.value);
					q.remove(n);
					q.add(n);
				}
			}

			System.out.println("Problem " + caseNum + ": "
					+ nodes[N - 1][N - 1].pathLen);
		}

	}

}

class Node implements Comparable<Node> {
	LinkedList<Node> neighbors = new LinkedList<Node>();
	int value;
	int x;
	int y;

	boolean visited = false;
	int pathLen = Integer.MAX_VALUE;

	public Node(int x, int y, int value) {
		this.value = value;
		this.x = x;
		this.y = y;
	}

	public void addNeighbors() {
		if (x > 0)
			neighbors.add(Main.nodes[x - 1][y]);
		if (x < Main.N - 1)
			neighbors.add(Main.nodes[x + 1][y]);
		if (y > 0)
			neighbors.add(Main.nodes[x][y - 1]);
		if (y < Main.N - 1)
			neighbors.add(Main.nodes[x][y + 1]);
	}

	@Override
	public int compareTo(Node o) {
		return ((Integer)pathLen).compareTo(o.pathLen);
	}
}