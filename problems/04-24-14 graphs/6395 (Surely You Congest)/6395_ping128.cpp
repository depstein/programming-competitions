//
// c.cpp -- C
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4406
//
// Siwakorn Sriakaokul - ping128
// Written on Sunday,  18 May 2014.
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
 
#define MAXN 25005
#define INF 2000000000
using namespace std;
 
typedef long long LL;
typedef pair<int, int> PII;
typedef pair<PII, int> PII2;
 
int N, M, C;
vector<PII>adj[MAXN];  // v, c
map<int, int> newAdj[MAXN];
int dist[MAXN];
bool seen[MAXN];
int cntCars[MAXN];
 
struct MaximumFlowFordFulkerson {
    typedef struct edge {
        int v;
        int capacity; 
        int flow; // current amount allowed to flow
        edge(int v, int c, int f) : v(v), capacity(c), flow(f) {}
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
        maxFlowEdge[from].push_back(Edge(to, cap, cap));
        maxFlowEdge[to].push_back(Edge(from, 0, 0));
    }
 
    // only works for this problem which always removes those edges that are just added
    void removeEdge(int from, int to){
        maxFlowEdge[from].pop_back();
        maxFlowEdge[to].pop_back();
    }
 
    void resetFlow(){
        for(int i = 0; i < N; i++ ){
            int sz = maxFlowEdge[i].size();
            for(int j = 0; j < sz; j++ ){
                maxFlowEdge[i][j].flow = maxFlowEdge[i][j].capacity;
            }
        }
    }
 
    int findAugmentingPath(int at, int sink, int val){
        int sol = 0;
        seen[at] = mark;
        if(at == sink) return val;
        int sz = maxFlowEdge[at].size();
        for(int i = 0; i < sz; i++ ){
            int v = maxFlowEdge[at][i].v;
            int f = maxFlowEdge[at][i].flow;
            if(seen[v] != mark && f > 0){
                sol = findAugmentingPath(v, sink, min(f, val));
                if(sol){
                    maxFlowEdge[at][i].flow -= sol;
                    for(int j = 0; j < maxFlowEdge[v].size(); j++ ){
                        if(maxFlowEdge[v][j].v == at){
                            maxFlowEdge[v][j].flow += sol;
                            break;
                        }
                    }
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
 
void reduceGraph(){
    for(int i = 1; i < N; i++ )
        dist[i] = INF;
    priority_queue<PII, vector<PII>, greater<PII> >Q;
    Q.push(PII(0, 0));
    while(!Q.empty()){
        PII top = Q.top();
        Q.pop();
        int u = top.second;
        if(seen[u]) continue;
        seen[u] = true;
        int sz = adj[u].size();
        for(int i = 0; i < sz; i++ ){
            int v = adj[u][i].first;
            int c = adj[u][i].second; 
            if(!seen[v]){
                if(dist[u] + c < dist[v]){
                    newAdj[v].clear();
                }
                if(dist[u] + c <= dist[v]){
                    dist[v] = dist[u] + c;
                    newAdj[v][u]++;
                    Q.push(PII(dist[v], v));
                }
            }
        }
    }
}
 
int main(){
    LL ans = 0;
    int u, v, c;
    while (scanf("%d %d %d", &N, &M, &C) == 3) {
        for (int i = 0; i < MAXN; i++) {
            adj[i].clear();
            cntCars[i] = 0;
            seen[i] = false;
            dist[i] = 0;
            newAdj[i].clear();
        }
        ans = 0;
        for(int i = 0; i < M; i++ ){
            scanf("%d %d %d", &u, &v, &c);
            u--, v--;
            adj[u].push_back(PII(v, c));
            adj[v].push_back(PII(u, c));
        }
        int where;
        for(int i = 0; i < C; i++ ){
            scanf("%d", &where);
            where--;
            if(where == 0){
                ans++;
                continue;
            } 
            cntCars[where]++;
        }
 
        reduceGraph();
        map<int, vector<int> >costs;
        for(int i = 0; i < N; i++ ){
            if(cntCars[i])
                costs[dist[i]].push_back(i);
        }
 
        MaximumFlowFordFulkerson solver = MaximumFlowFordFulkerson(N + 1);
        for(int i = 0; i < N; i++ ){
            for(map<int, int>::iterator it = newAdj[i].begin(); it != newAdj[i].end(); it++ ){
                solver.addEdge(i, it->first, it->second);
            }
        }
 
        for(map<int, vector<int> >::iterator it = costs.begin(); it != costs.end(); it++ ){
            for(int i = 0; i < it->second.size(); i++ ){
                solver.addEdge(N, it->second[i], cntCars[it->second[i]]);
            }
            ans += solver.getMaximumFlow(N, 0);
            solver.resetFlow();
            for(int i = 0; i < it->second.size(); i++ ){
                solver.removeEdge(N, it->second[i]);
            }
 
        }
        cout << ans << endl;
    }
    return 0;
}
