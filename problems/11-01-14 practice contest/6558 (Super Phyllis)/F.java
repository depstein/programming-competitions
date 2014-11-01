// Solution(?) to Super Phyllis
import java.util.*;

class Pair {
    public int x,y;
    public Pair(int x, int y)
    { this.x = x; this.y = y;}
}

public class F
{
    public static Scanner in;
    public static int casenum;
    public static int m;
    public static int maxnode;
    public static boolean[][] graph;
    public static boolean[][] clos;
    public static Hashtable<String,Integer> trans;
    public static Hashtable<Integer,String> untrans;
    public static String[] names;
    public static ArrayList<Pair> removed;

    public static void main(String[] args)
    {
        in = new Scanner(System.in);
        casenum = 0;
        while (true)
        {
            m = in.nextInt();
            if (m == 0) break;
            casenum++;
            graph = new boolean[200][200];
            clos = new boolean[200][200];
            names = new String[200];
            for (int i = 0; i < 200; i++)
            {
                graph[i] = new boolean[200];
                clos[i] = new boolean[200];
                Arrays.fill(graph[i],false);
                Arrays.fill(clos[i],false);
            }

            trans = new Hashtable<String,Integer>();
            untrans = new Hashtable<Integer,String>();
            maxnode = 0;
            for (int i = 0; i < m; i++)
            {
                String n1 = in.next();
                String n2 = in.next();
                Integer p, q;
                if ((p = trans.get(n1)) == null)
                {
                    trans.put(n1,maxnode);
                    untrans.put(maxnode,n1);
                    names[maxnode] = n1;
                    p = maxnode++;
                }
                if ((q = trans.get(n2)) == null)
                {
                    q = trans.put(n2,maxnode);
                    untrans.put(maxnode,n2);
                    names[maxnode] = n2;
                    q = maxnode++;
                }
                graph[p][q] = true;
                clos[p][q] = true;
            }
            reduce();
            print();
        }
    }

    public static void reduce()
    {
        // First, find trans. closure:
        for (int k = 0; k < maxnode; k++) {
            for (int i = 0; i < maxnode; i++) {
                for (int j = 0; j < maxnode; j++) {
                        if (clos[i][k] && clos[k][j])
                            clos[i][j] = true;
                }
            }
        }

        // Now for each edge in the original graph, see if it is equivalent 
        // to a pair of edges in the trans closure; if so, remove it:
        removed = new ArrayList<Pair>();
        for (int i = 0; i < maxnode; i++) {
            for(int j = 0; j < maxnode; j++) {
                if (graph[i][j]) {
                   for (int k = 0; k < maxnode; k++) {
                       if (clos[i][k] && clos[k][j]) {
                           graph[i][j] = false;
                           removed.add(new Pair(i,j));
                           break;
                       }
                   }
                }
            }
        }
    }

    public static void print()
    {
        System.out.print("Case " + casenum + ": ");
        Pair[] pairs = new Pair[removed.size()];
        pairs = removed.toArray(pairs);
        Arrays.sort(pairs,new Comparator<Pair>() {
            public int compare(Pair p1, Pair p2) {
                String s1 = untrans.get(p1.x);
                String s2 = untrans.get(p2.x);
                if(s1.compareTo(s2) != 0)
                    return s1.compareTo(s2);
                s1 = untrans.get(p1.y);
                s2 = untrans.get(p2.y);
                return s1.compareTo(s2);
            }
        });
        System.out.print(removed.size());
        for (int i = 0; i < removed.size(); i++) {
            String s1 = untrans.get(pairs[i].x);
            String s2 = untrans.get(pairs[i].y);
            System.out.print(" "+s1+","+s2);
        }
        System.out.println();
    }

}
