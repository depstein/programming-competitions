/*
 * Dijkstra example code
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.io.*;
import java.util.*;

public class prims {

  public static void main(String[] args) {
    Node a = new Node();
    Node b = new Node();
    Node c = new Node();
    Node d = new Node();
    Node e = new Node();
    Node f = new Node();
    Node g = new Node();
    a.edges.put(b, 1);
    b.edges.put(a, 1);
    b.edges.put(d, 3);
    d.edges.put(b, 3);
    a.edges.put(d, 5);
    d.edges.put(a, 5);
    c.edges.put(d, 8);
    d.edges.put(c, 8);
    c.edges.put(e, 6);
    e.edges.put(c, 6);
    d.edges.put(e, 4);
    e.edges.put(d, 4);
    d.edges.put(g, 9);
    g.edges.put(d, 9);
    e.edges.put(g, 2);
    g.edges.put(e, 2);
    d.edges.put(f, 3);
    f.edges.put(d, 3);
    f.edges.put(g, 2);
    g.edges.put(f, 2);
    ArrayList<Node> allNodes = new ArrayList<Node>(Arrays.asList(new Node[]{g, f, e, d, c, b, a}));
    prims(allNodes);
    int size = 0;
    for(Node n : allNodes) {
      size += n.distance;
    }
    System.out.printf("Size of MST is: %d\n", size);
  }

  public static void prims(ArrayList<Node> allNodes) {
    PriorityQueue<Node> q = new PriorityQueue<Node>();
    Node root = allNodes.get(0);
    root.distance = 0;
    q.add(root);

    while(!q.isEmpty()) {
      Node cur = q.poll();
      cur.visited = true;
      for(Node n : cur.edges.keySet()) {
        if(!n.visited && n.distance > cur.edges.get(n)) {
          n.distance = cur.edges.get(n);
          q.remove(n);
          q.add(n);
        }
      }
    }

  }
}

class Node implements Comparable<Node> {
  public HashMap<Node, Integer> edges = new HashMap<Node, Integer>();
  public int distance = Integer.MAX_VALUE;
  public boolean visited = false;

  public int compareTo(Node o) {
    return (distance < o.distance) ? -1 : ((distance == o.distance) ? 0 : 1);
  }
}
