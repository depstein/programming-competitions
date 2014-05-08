//
// 6091_ping128.cpp
//
// Siwakorn Srisakaokul - ping128
// Written on Thursday,  8 May 2014.
//

#include <cstdio>
#include <iostream>
#include <sstream>
#include <cstdlib>
#include <string>
#include <vector>
#include <set>
#include <queue>
#include <stack>
#include <list>
#include <cmath>
#include <algorithm>
#include <map>
#include <ctype.h>
#include <string.h>

#include <assert.h>

using namespace std;

typedef long long LL;
typedef pair<int, int> PII;
typedef pair<PII, int> PII2;

#define MAXN 505

int N, M;
vector<int> adj[MAXN];
int visited[MAXN];

void dfs(int at, int pre, int &numNode, int &numEdge) {
    visited[at] = 1; // on the recursive path
    numNode++;
    visited[at] = true;
    int sz = adj[at].size();
    for (int i = 0; i < sz; i++) {
        int v = adj[at][i];
        if (v == pre) continue;
        if (visited[v] == 0) {
            numEdge++;
            dfs(v, at, numNode, numEdge);
        } else if (visited[v] == 1) {
            // cycle
            numEdge = 10000000;
        }
    }
    visited[at] = 2;
}

void solve() {
    int u, v;
    for (int i = 0; i < N; i++) {
        adj[i].clear();
        visited[i] = 0;
    }

    for (int i = 0; i < M; i++) {
        scanf("%d %d", &u, &v);
        u--, v--;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    int cnt_tree = 0;

    for (int i = 0; i < N; i++) {
        if (!visited[i]) {
            int numNode = 0;
            int numEdge = 0;
            dfs(i, -1, numNode, numEdge);
            if (numNode == numEdge + 1) {
                cnt_tree++;
            }
        }
    }

    if (cnt_tree == 0) {
        printf("No trees.\n");
    } else if (cnt_tree == 1) {
        printf("There is one tree.\n");
    } else {
        printf("A forest of %d trees.\n", cnt_tree);
    }
}

int main() {
    int test = 0;
    while (scanf("%d %d", &N, &M) == 2) {
        if (N == 0 && M == 0) break;
        test++;
        printf("Case %d: ", test);
        solve();
    }
    return 0;
}
