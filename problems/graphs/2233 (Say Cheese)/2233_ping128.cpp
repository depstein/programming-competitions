//
// 2233_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=onlinejudge&page=show_problem&problem=234
// 
// Siwakorn Srisakaokul - ping128
// Written on Monday, 19 May 2014.
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

#define MAXN 105

/* Idea: This problem can be solved by Dijkstra algorithm. We can treat each sphere as a vertex in a graph.
 *       The start and end points are just spheres with radius = 0.0. If two spheres overlap, the distance between them is zero.
 *       If they don't overlap, the distance is dist(center1, center2) - r1 - r2
 */
double in[MAXN][4];
double costs[MAXN][MAXN];
double dist[MAXN];
bool seen[MAXN];

int main() {
    int tt = 0;
    int N;
    while(scanf("%d", &N) == 1) {
        if (N == -1) break;
        tt++;
        for (int i = 1; i <= N; i++) {
            scanf("%lf %lf %lf %lf", &in[i][0], &in[i][1], &in[i][2], &in[i][3]);
        }
        scanf("%lf %lf %lf", &in[0][0], &in[0][1], &in[0][2]);
        scanf("%lf %lf %lf", &in[N + 1][0], &in[N + 1][1], &in[N + 1][2]);
        in[0][3] = in[N + 1][3] = 0.0;
        for (int i = 0; i <= N + 1; i++) {
            for (int j = 0; j <= N + 1; j++) {
                double sumr = in[i][3] + in[j][3];
                double dx = in[i][0] - in[j][0];
                double dy = in[i][1] - in[j][1];
                double dz = in[i][2] - in[j][2];
                double d = sqrt(dx * dx + dy * dy + dz * dz);
                costs[i][j] = max(0.0, d - sumr);
            }
        }

        for (int i = 0; i <= N + 1; i++) {
            dist[i] = 100000000000000.0;
            seen[i] = false;
        }

        priority_queue<PII, vector<PII>, greater<PII> >Q;
        dist[0] = 0.0;
        Q.push(PII(0.0, 0));
        while (!Q.empty()) {
            PII now = Q.top();
            Q.pop();
            int u = now.second;
            double c = now.first;
            if (seen[u]) continue;
            seen[u] = true;
            if (u == N + 1) break;
            
            for (int i = 0; i <= N + 1; i++) {
                if (!seen[i] && dist[i] >= c + costs[u][i]) {
                    dist[i] = c + costs[u][i];
                    Q.push(PII(dist[i], i));
                }
            }
        }
        printf("Cheese %d: Travel time = %d sec\n", tt, (int)(round(10 * dist[N + 1]) + 0.00000001));
    }
    return 0;
}
