/*
* Maximum flow problem, implemented using the Preflow Push Algorithm
* Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
*/

import java.util.*;
import java.io.*;

public class maxflow {

	static HashMap<String, Node> nodes = new HashMap<String, Node>();

	public static void main(String[] args) {
			Node source = new Node();
			Node sink = new Node();
			source.isSourceOrSink = true;
			sink.isSourceOrSink = true;
			source.height = 7;
			source.excess = Integer.MAX_VALUE;
			Node a = new Node();
			Node b = new Node();
			Node c = new Node();
			Node d = new Node();
			Node e = new Node();

			new Edge(a, source, 5, false);
			new Edge(b, source, 3, false);
			new Edge(c, source, 12, false);
			new Edge(c, a, 2, false);
			new Edge(d, a, 6, false);
			new Edge(d, c, 1, false);
			new Edge(e, c, 2, false);
			new Edge(e, b, 4, false);
			new Edge(sink, c, 2, false);
			new Edge(sink, d, 8, false);
			new Edge(sink, e, 11, false);

			PreflowPush(source, sink);
			System.out.printf("Flow pushed through the graph: %d\n", sink.excess);

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
