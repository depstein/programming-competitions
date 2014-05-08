/*
 * BFS example code
 * Daniel Epstein, depstein AT cs DOT washington DOT edu, @daepstein
 */

import java.io.*;
import java.util.*;

public class bfs {

  public static void main(String[] args) {
    Node a = new Node();
    Node b = new Node();
    Node c = new Node();
    Node d = new Node();
    Node e = new Node();
    a.edges.add(b);
    a.edges.add(c);
    b.edges.add(d);
    c.edges.add(d);
    d.edges.add(e);
    ArrayList<Node> allNodes = new ArrayList<Node>(Arrays.asList(new Node[]{e, d, c, b, a}));
    bfs(a, allNodes);
    System.out.printf("Distance to Node e is: %d\n", e.distance);
  }

  public static void bfs(Node root, ArrayList<Node> allNodes) {
    Queue<Node> q = new LinkedList<Node>();
    root.distance = 0;
    q.add(root);

    while(q.size() > 0) {
      Node u = q.poll();
      for(Node n : u.edges) {
        if(n.distance == Integer.MAX_VALUE) { // Has not been visited yet
          n.distance = u.distance + 1;
          q.add(n);
        }
      }
    }
  }
}

class Node implements Comparable<Node> {
  public ArrayList<Node> edges = new ArrayList<Node>();
  public int distance = Integer.MAX_VALUE;

  public int compareTo(Node o) {
    return (distance < o.distance) ? -1 : ((distance == o.distance) ? 0 : 1);
  }
}
