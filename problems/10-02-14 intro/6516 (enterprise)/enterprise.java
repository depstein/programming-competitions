/*
 * "Enterprising Escape", 2013 ICPC Pacific Northwest Regionals problem A
 * LiveArchive problem #6516
 * Find the shortest path while destroying enemy ships in your path
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.util.*;
import java.io.*;

public class enterprise {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		for(int i=0;i<cases;i++) {
			int k = in.nextInt();
			int w = in.nextInt();
			int h = in.nextInt();
			HashMap<String, Integer> ships = new HashMap<String, Integer>();
			for(int j=0;j<k;j++) {
				ships.put(in.next(), in.nextInt());
			}
			ships.put("E", 0);
			Node start = new Node(0);
			Node end = new Node(0);
			Node[][] nodes = new Node[w][h];
			for(int a=0;a<h;a++) {
				String s = in.next();
				for(int b=0;b<w;b++) {
					nodes[b][a] = new Node(ships.get(""+s.charAt(b)));
					if(s.charAt(b) == 'E') {
						start.edges.put(nodes[b][a], 0);
					}
					if(a==0 || b==0 || a==h-1 || b==w-1) {
						nodes[b][a].edges.put(end, 0);
					}
				}
			}
			for(int a=0;a<h;a++)
				for(int b=0;b<w;b++) {
					if(a>0)
						nodes[b][a].edges.put(nodes[b][a-1], nodes[b][a-1].weight);
					if(a<h-1)
						nodes[b][a].edges.put(nodes[b][a+1], nodes[b][a+1].weight);
					if(b>0)
						nodes[b][a].edges.put(nodes[b-1][a], nodes[b-1][a].weight);
					if(b<w-1)
						nodes[b][a].edges.put(nodes[b+1][a], nodes[b+1][a].weight);
				}
			System.out.println(Dijkstra(start, end));
		}
	}

	public static int Dijkstra(Node start, Node end) {
		PriorityQueue<Node> q = new PriorityQueue<Node>();
		start.path = 0;
		q.add(start);
		while(!q.isEmpty()) {
			Node n = q.poll();
			if(n==end){
				break;
			}
			n.visited = true;
			for(Node m : n.edges.keySet()) {
				if(m.visited)
					continue;
				m.path = Math.min(m.path, n.path + n.edges.get(m));
				q.remove(m); //Will only actually remove something if it's present
				q.add(m);
			}
		}
		return end.path;
	}
}

class Node implements Comparable<Node> {
	HashMap<Node, Integer> edges = new HashMap<Node, Integer>();
	int weight;
	boolean visited = false;
	int path = Integer.MAX_VALUE;
	public Node(int weight) {
		this.weight = weight;
	}

	public int compareTo(Node other) {
		return path == other.path ? 0 : path < other.path ? -1 : 1;
	}
}
