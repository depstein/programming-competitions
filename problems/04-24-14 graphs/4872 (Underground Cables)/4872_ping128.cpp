//
// 4872_ping128.cpp
//
// Siwakorn Srisakaokul - ping128
// Written on Tuesday, 13 May 2014.
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
typedef pair<double, int> PII;
typedef pair<PII, int> PII2;

#define MAXN 1005

pair<int, int> in[MAXN];
int N;
bool connected[MAXN];
double dist[MAXN][MAXN];

/*
 * Idea: Use Prim algorithm to find a minimum spanning tree over all the vertices
 */
double prim() {
    for (int i = 0; i < N; i++) {
        connected[i] = 0;
    }

    priority_queue<PII, vector<PII>, greater<PII> > Q;
    double ans = 0;
    connected[0] = 1;
    for (int i = 0; i < N; i++) {
        Q.push(PII(dist[0][i], i));
    }

    while (!Q.empty()) {
        PII now = Q.top();
        Q.pop();
        int u = now.second;
        if (connected[u]) continue;
        ans += now.first;
        connected[u] = true;
        for (int i = 0; i < N; i++) {
            if (i != u && !connected[i]) {
                Q.push(PII(dist[u][i], i));
            }
        }
    }
    return ans;
}

int main() {
    while (scanf("%d", &N) == 1) {
        if (N == 0) break;
        for (int i = 0; i < N; i++) scanf("%d %d", &in[i].first, &in[i].second);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int dx = in[i].first - in[j].first;
                int dy = in[i].second - in[j].second;
                dist[i][j] = sqrt(dx * dx + dy * dy);
            }
        }

        printf("%.2lf\n", prim());
    }
    return 0;
}
