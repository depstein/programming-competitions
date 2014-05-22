//
// 3811_ping128.cpp
//
// Siwakorn Srisakaokul - ping128
// Written on Wednesday, 21 May 2014.
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

int N, M;

#define MAXN 55

bool removed[MAXN];

struct MaximumFlowFordFulkerson {

    // runtime: O(E F), E = #edges, F = max flow values

    typedef struct edge {
        int v;
        int capacity; 
        int flow; // current amount allowed to flow
        int rev_index; // store its reversed edge's index
        edge(int v_, int c_, int f_, int rev) : v(v_), capacity(c_), flow(f_), rev_index(rev) {}
    } Edge;
 
    int N; // node from 0 to N - 1
    vector<vector<Edge> > maxFlowEdge;
    int mark;   // global variable for checking if a node is already visited
    vector<int> seen;  // status of each node

    MaximumFlowFordFulkerson(int numNode){
        N = numNode;
        maxFlowEdge = vector<vector<Edge> >(numNode);
        seen = vector<int>(numNode);
        mark = 0;
    }

    void addEdge(int from, int to, int cap){
        maxFlowEdge[from].push_back(Edge(to, cap, cap, maxFlowEdge[to].size()));
        maxFlowEdge[to].push_back(Edge(from, 0, 0, maxFlowEdge[from].size() - 1));
    }
 
    void resetFlow(){
        for(int i = 0; i < N; i++ ){
            seen[i] = false;
            int sz = maxFlowEdge[i].size();
            for(int j = 0; j < sz; j++ ){
                maxFlowEdge[i][j].flow = maxFlowEdge[i][j].capacity;
            }
        }
    }
 
    int findAugmentingPath(int at, int sink, int val){
        int sol = 0;
        seen[at] = mark;
        if (at == sink) return val;
        int sz = maxFlowEdge[at].size();
        for (int i = 0; i < sz; i++ ) {
            int v = maxFlowEdge[at][i].v;
            int f = maxFlowEdge[at][i].flow;
            if (removed[v]) continue;
            if (seen[v] != mark && f > 0){
                sol = findAugmentingPath(v, sink, min(f, val));
                if (sol) {
                    maxFlowEdge[at][i].flow -= sol;
                    int rev = maxFlowEdge[at][i].rev_index;
                    maxFlowEdge[v][rev].flow += sol;
                    return sol;
                }
            }
        }
        return 0;
    }
 
    int getMaximumFlow(int S, int T){
        int res = 0;
        while(1){
            mark++;
            int flow = findAugmentingPath(S, T, 1000000000);
            if(flow == 0) break;
            else res += flow;
        }
        return res;
    }
};


// min_cut[i] = the mincut between node i (s) and node 0 (t)
int min_cuts[MAXN];

int main() {
    int test = 0;
    while (scanf("%d %d", &N, &M) == 2) {
        if (N == 0 && M == 0) break;
        test++;
        fill(removed, removed + MAXN, 0);

        MaximumFlowFordFulkerson solver = MaximumFlowFordFulkerson(N + 1);
        int u, v;
        for (int i = 0; i < M; i++) {
            cin >> u >> v;
            solver.addEdge(u, v, 1);
            solver.addEdge(v, u, 1);
        }

        for (int i = 1; i <= N; i++) {
            min_cuts[i] = solver.getMaximumFlow(0, i);
            solver.resetFlow();
        }

        while (true) {
            int best = 1000000000;
            for (int i = 1; i <= N; i++) {
                if (min_cuts[i] < best && !removed[i]) {
                    best = min_cuts[i];
                }
            }

            if (best == 1000000000) break;
            for (int i = 1; i <= N; i++) {
                if (min_cuts[i] <= best && !removed[i]) {
                    removed[i] = true;
                }
            }

            for (int i = 1; i <= N; i++) {
                if (!removed[i]) {
                    min_cuts[i] = min(min_cuts[i], best + (int)solver.getMaximumFlow(i, 0));
                    solver.resetFlow();
                }
            }
        }

        printf("Case %d: %d\n\n", test, min_cuts[1]);
    }
    return 0;
}
