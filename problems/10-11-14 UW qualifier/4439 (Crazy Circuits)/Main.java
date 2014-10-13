/**
* NOTE: this is too slow. Treat this as a sketch of the solution, and if you optimize it further, feel free to submit a pull request.
**/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		while (true) {
			String[] line = in.nextLine().split("\\s+");
			int N = Integer.parseInt(line[0]);
			if (N == 0)
				break;
			int M = Integer.parseInt(line[1]);
			Node plus = new Node();
			plus.isSourceOrSink = true;
			plus.height = N + 2;
			plus.excess = Integer.MAX_VALUE;
			Node minus = new Node();
			minus.isSourceOrSink = true;
			minus.height = 1;
			minus.index = -5;
			Node[] junctions = new Node[N];
			for (int i = 0; i < N; i++) {
				junctions[i] = new Node();
				junctions[i].index = i+1;
			}
			HashSet<Edge> toCheck = new HashSet<Edge>();
			for (int i = 0; i < M; i++) {
				line = in.nextLine().split("\\s+");
				Node start, end;
				int capacity = Integer.parseInt(line[2]);
				if (line[0].equals("+"))
					start = plus;
				else if (line[0].equals("-"))
					start = minus;
				else
					start = junctions[Integer.parseInt(line[0]) - 1];
				if (line[1].equals("+"))
					end = plus;
				else if (line[1].equals("-"))
					end = minus;
				else
					end = junctions[Integer.parseInt(line[1]) - 1];
				toCheck.add(new Edge(end, start, capacity, false));
			}
			while (true) {
				PreflowPush(plus, minus);
				boolean good = true;
				for (Edge e : toCheck) {
					if (e.flow < e.initCapacity) {
						good = false;
						break;
					}
				}
				if (good)
					break;
				//do something to increment edge capacity
				
			}
			int sum = 0;
			for (Edge e : plus.neighbors) {
				sum += e.flow;
			}
			System.out.println(sum);
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
	public int index = 0;
	public int excess = 0;
	public boolean isSourceOrSink = false;
}

class Edge {
	public Node dest;
	public Edge back;
	public int capacity;
	public int flow = 0;
	public int initCapacity;
	public boolean visited;

	public Edge(Node dest, Node source, int capacity, boolean isBack) {
		this.dest = dest;
		this.capacity = capacity;
		this.initCapacity = capacity;
		this.visited = false;
		source.neighbors.add(this);
		if (!isBack) {
			Edge backEdge = new Edge(source, this.dest, 0, true);
			visited = true;
			// Change 0 to capacity for bidirectional graph
			this.back = backEdge;
			backEdge.back = this;
		}
	}

	public int remaining() {
		return capacity - flow;
	}
}