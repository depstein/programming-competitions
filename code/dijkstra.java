/*
 * Dijkstra example code
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.io.*;
import java.util.*;

public class dijkstra {

  public static void main(String[] args) {
    Node a = new Node();
    Node b = new Node();
    Node c = new Node();
    Node d = new Node();
    Node e = new Node();
    a.edges.put(b, 1);
    a.edges.put(c, 3);
    b.edges.put(d, 8);
    c.edges.put(d, 2);
    d.edges.put(e, 1);
    ArrayList<Node> allNodes = new ArrayList<Node>(Arrays.asList(new Node[]{e, d, c, b, a}));
    dijkstra(a, allNodes);
    System.out.printf("Distance to Node e is: %d\n", e.distance);
  }

  public static void dijkstra(Node root, ArrayList<Node> allNodes) {
    PriorityQueue<Node> q = new PriorityQueue<Node>();
    root.distance = 0;
    q.add(root);

    while(q.size() > 0) {
      Node u = q.poll();
      for(Node n : u.edges.keySet()) {
        if(n.distance == Integer.MAX_VALUE) { // Update the distance to node n
          q.remove(n);
        }
        n.distance = Math.min(n.distance, u.distance + u.edges.get(n));
        q.add(n);
      }
    }
  }
}

class Node implements Comparable<Node> {
  public HashMap<Node, Integer> edges = new HashMap<Node, Integer>();
  public int distance = Integer.MAX_VALUE;

  public int compareTo(Node o) {
    return (distance < o.distance) ? -1 : ((distance == o.distance) ? 0 : 1);
  }
}
