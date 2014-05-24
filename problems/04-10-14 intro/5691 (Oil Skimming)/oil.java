/*
* "Oil Skimming", 2011 ICPC South Pacific Regionals problem J
* LiveArchive problem #5691
* Turn into a bipartite graph connecting neighboring nodes,
* solve using a maximum flow algorithm
* Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
*/

import java.util.*;
import java.io.*;

public class oil {

	static HashMap<String, Node> nodes = new HashMap<String, Node>();

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		int numCases = in.nextInt();
		for (int c = 1; c <= numCases; c++) {
			nodes.clear();

			int N = in.nextInt();
			boolean[][] grid = new boolean[N][N];
			for (int i = 0; i < N; i++) {
				String s = in.next();
				for (int j = 0; j < N; j++)
					grid[i][j] = s.charAt(j)=='#';
			}

			Node source = new Node();
			Node sink = new Node();
			source.isSourceOrSink = true;
			sink.isSourceOrSink = true;
			int numNodes = 2;

      Node[][] aNodes = new Node[N][N];
      Node[][] bNodes = new Node[N][N];
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++) {
					if (grid[i][j]) {
						Node n = new Node();
						new Edge(n, source, 1, false);
            aNodes[i][j] = n;
						n = new Node();
						new Edge(sink, n, 1, false);
						bNodes[i][j] = n;
						numNodes += 2;
					}
				}
			source.height = numNodes;
			source.excess = Integer.MAX_VALUE;
			for (int i = 0; i < N; i++)
				for (int j = 0; j < N; j++) {
					if (grid[i][j]) {
						if (i - 1 >= 0 && grid[i - 1][j])
							new Edge(bNodes[i-1][j], aNodes[i][j], 1, false);
						if (j - 1 >= 0 && grid[i][j - 1])
							new Edge(bNodes[i][j-1], aNodes[i][j], 1, false);
            if (i + 1 < N && grid[i + 1][j])
							new Edge(bNodes[i+1][j], aNodes[i][j], 1, false);
						if (j + 1 < N && grid[i][j + 1])
							new Edge(bNodes[i][j+1], aNodes[i][j], 1, false);
					}
				}
			PreflowPush(source, sink);
			System.out.printf("Case %d: %d\n", c, sink.excess/2);
		}

	}

	public static void PreflowPush(Node source, Node sink) {
		// source.height should be the number of nodes, sink.height should be 1
		LinkedList<Node> q = new LinkedList<Node>();
		q.add(source);
		while (!q.isEmpty()) {
			Node node = q.remove();
			int minHeight = Integer.MAX_VALUE;
			for (int i = 0; i < node.neighbors.size(); i++) {
				if (node.neighbors.get(i).dest.height < minHeight
						&& node.neighbors.get(i).remaining() > 0)
					minHeight = node.neighbors.get(i).dest.height;
			}
			if (minHeight != Integer.MAX_VALUE && minHeight >= node.height)
				node.height = minHeight + 1;
			for (int i = 0; i < node.neighbors.size(); i++) {
				if (node.neighbors.get(i).dest.height < node.height) {
					int pushedFlow = node.neighbors.get(i).remaining();
					if (pushedFlow > node.excess)
						pushedFlow = node.excess;
					if (pushedFlow > 0) {
						node.neighbors.get(i).flow += pushedFlow;
						node.neighbors.get(i).back.flow -= pushedFlow;
						node.neighbors.get(i).dest.excess += pushedFlow;
						node.excess -= pushedFlow;
						if (!node.neighbors.get(i).dest.isSourceOrSink)
							q.add(node.neighbors.get(i).dest);
						if (node.excess <= 0)
							break;
					}
				}
			}
			if (node.excess > 0 && !node.isSourceOrSink)
				q.add(node);
		}
	}
}

class Node {
	public ArrayList<Edge> neighbors = new ArrayList<Edge>();
	public int height = 1;
	public int excess = 0;
	public boolean isSourceOrSink = false;
}

class Edge {
	public Node dest;
	public Edge back;
	public int capacity;
	public int flow = 0;

	public Edge(Node dest, Node source, int capacity, boolean isBack) {
		this.dest = dest;
		this.capacity = capacity;
		source.neighbors.add(this);
		if (!isBack) {
			Edge backEdge = new Edge(source, this.dest, 0, true);
			this.back = backEdge;
			backEdge.back = this;
		}
	}

	public int remaining() {
		return capacity - flow;
	}
}
