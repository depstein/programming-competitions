#include <iostream>
#include <algorithm>

using namespace std;

const int MAX_NODES = 500;

bool cycle(bool G[MAX_NODES][MAX_NODES], int n, int v, bool visited[MAX_NODES],
	   int parent = -1)
{
  visited[v] = true;
  for (int w = 0; w < n; w++) {
    if (w == parent || !G[v][w]) continue;
    if (visited[w] || cycle(G, n, w, visited, v)) {
      return true;
    }
  }

  return false;
}

bool solve(int id)
{
  int n, m;
  cin >> n >> m;
  if (!n && !m) return false;

  bool G[MAX_NODES][MAX_NODES];
  for (int i = 0; i < n; i++) {
    fill(G[i], G[i]+n, false);
  }

  for (int i = 0; i < m; i++) {
    int u, v;
    cin >> u >> v;
    u--; v--;
    G[u][v] = G[v][u] = true;
  }

  int trees = 0;
  bool visited[MAX_NODES] = { false };
  for (int i = 0; i < n; i++) {
    if (visited[i]) continue;
    if (!cycle(G, n, i, visited)) {
      trees++;
    }
  }

  cout << "Case " << id << ": ";
  if (trees == 0) {
    cout << "No trees." << endl;
  } else if (trees == 1) {
    cout << "There is one tree." << endl;
  } else {
    cout << "A forest of " << trees << " trees." << endl;
  }
  return true;
}

int main(void)
{
  int id = 1;
  while (solve(id++))
    ;
  return 0;
}
