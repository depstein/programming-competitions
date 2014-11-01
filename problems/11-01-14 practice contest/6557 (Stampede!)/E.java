import java.util.*;


public class E
{
	public static final int MAX = 25;
	
	public static char [] grid = new char[MAX*MAX];
	public static baseadj [] base = new baseadj[MAX*MAX];
	
	public static edge [] gr = new edge[MAX*MAX*(MAX*((MAX+1)/2)+MAX) + 2];
	
	public static int [] path = new int[MAX*MAX*(MAX*((MAX+1)/2)+MAX)];
	public static int npath;
	public static int sink;
	
	public static boolean printpath = false;

	public static int flow = 0;

	public static int shortest(int n)
	{
		int [][] path = new int[n][n];
		boolean done = false;
	
		for(int i=0; i<n; i++)
			for(int j=1; j<n; j++)
				path[i][j] = n*n;
		for(int i=0; i<n; i++)
			path[i][0] = 0;
		while (true) {
			for(int i=0; i<n; i++) {
				for(int j=1; j<n-1; j++)
					if (grid[i*n+j] != 'X') {
						int ans = path[i][j];
						if (path[i][j-1] + 1 < ans)
							ans = path[i][j-1]+1;
						if (path[i][j+1] + 1 < ans)
							ans = path[i][j+1]+1;
						if (i>0 && path[i-1][j] + 1 < ans)
							ans = path[i-1][j]+1;
						if (i<n-1 && path[i+1][j] + 1 < ans)
							ans = path[i+1][j]+1;
						path[i][j] = ans;
					}
			}
			for(int i=0; i<n; i++) {
				int ans = path[i][n-1];
				if (path[i][n-2] + 1 < ans)
					return path[i][n-2]+1;
				if (i>0 && path[i-1][n-1] + 1 < ans)
					return path[i-1][n-1]+1;
				if (i<n-1 && path[i+1][n-1] + 1 < ans)
					return path[i+1][n-1]+1;
			}
		}
	}
	
	
	public static boolean findPath(int len, boolean [] visited)
	{
		if (path[len] == sink) {
			npath = len;
			return true;
		}
		int v = path[len];
		for(edge p = gr[v].next; p != null; p = p.next) {
			if (!visited[p.w]) {
				visited[p.w] = true;
				path[len+1] = p.w;
				if (findPath(len+1, visited))
					return true;
			}
		}
		return false;
	}
	
	public static boolean findPath()
	{
		boolean [] visited = new boolean[sink+1];
		for(int i=0; i<=sink; i++)
			visited[i] = false;
		return findPath(0, visited);
	}
		
	public static boolean deleteEdge(int v, int w)
	{
		edge p = gr[v];
		while (p.next != null && p.next.w != w)
			p = p.next;
		if (p.next == null)
			return false;
		p.next = p.next.next;
		return true;
	}
	
	
	public static int maxflow()
	{
		while (findPath()) {
			flow++;
			for(int i=0; i<npath; i++) {
				int v = path[i];
				int w = path[i+1];
				if (!deleteEdge(v, w))
					System.out.println("ERROR: vertex " + w + " not on edge list for " + v);
				gr[w].next = new edge(v, gr[w].next);
			}
		}
		return flow;
	}
	
	public static void main(String [] args)
	{
		int n, icase=0;
		Scanner in = new Scanner(System.in);
		n = in.nextInt();
		while (n != 0) {
			icase++;
			for(int i=0; i<n; i++) {
				String s = in.next();
				for(int j=0; j<n; j++)
					grid[n*j+i] = s.charAt(j);
			}
			for(int i=0; i<n*n; i++) {
				base[i] = new baseadj();
				base[i].nadj = 0;
				int c = i/n, r = i%n;
				if (grid[i] == '.') {
					if (c>0 && grid[i-n] == '.')
						base[i].adj[base[i].nadj++] = n*n-n;
					base[i].adj[base[i].nadj++] = n*n;
					if (r>0 && grid[i-1] == '.')
						base[i].adj[base[i].nadj++] = n*n-1;
					if (r<n-1 && grid[i+1] == '.')
						base[i].adj[base[i].nadj++] = n*n+1;
					if (c<n-1 && grid[i+n] == '.')
						base[i].adj[base[i].nadj++] = n*n+n;
				}
			}
			int minlen = shortest(n);
	
			
			gr[0] = new edge();
			gr[0].next = null;
			for(int i=n; i>=1; i--) {			// source vertex
				gr[0].next = new edge(i, gr[0].next);
			}
			int index = 0;
			sink = n*n*(minlen+1) + 1;
			for(int i=0; i<minlen; i++) {
				for(int j=0; j<n*n; j++) {
					index++;
					gr[index] = new edge();
					gr[index].next = null;
					for(int k=0; k<base[j].nadj; k++) {
						if (index+base[j].adj[k] < sink)
							gr[index].next = new edge(index+base[j].adj[k], gr[index].next); 
					}
				}
			}
			for(int j=0; j<n*n; j++) {
				gr[++index] = new edge();
				gr[index].next = null;
			}
			for(int i=1; i<=n; i++)			// edges to sink
				gr[sink-i].next = new edge(sink, gr[sink-i].next);
			gr[sink] = new edge();
			gr[sink].next = null;
			flow = 0;
			while (maxflow() < n) {
				minlen++;
											// rebuild
				gr[0].next = null;
				for(int i=n; i>=1; i--) {			// source vertex
					gr[0].next = new edge(i, gr[0].next);
				}
				index = 0;
				sink = n*n*(minlen+1) + 1;
				for(int i=0; i<minlen; i++) {
					for(int j=0; j<n*n; j++) {
						index++;
						gr[index].next = null;
						for(int k=0; k<base[j].nadj; k++) {
							if (index+base[j].adj[k] < sink)
								gr[index].next = new edge(index+base[j].adj[k], gr[index].next); 
						}
					}
				}
				for(int j=0; j<n*n; j++) {
					gr[++index] = new edge();
					gr[index].next = null;
				}
				for(int i=1; i<=n; i++)			// edges to sink
					gr[sink-i].next = new edge(sink, gr[sink-i].next);
				gr[sink] = new edge();
				gr[sink].next = null;
				flow = 0;
			}
			System.out.println("Case " + icase + ": " + minlen);
	
			n = in.nextInt();
		}
	}
}
	
class baseadj{
	public int nadj;
	public int [] adj;

	baseadj() {
		adj = new int[5];
	}
}

class edge {
	public int w;
	public edge next;

	edge() {
		w = -1;
		next = null;
	}

	edge(int vert, edge nxt)
	{
		w = vert;
		next = nxt;
	}
}
