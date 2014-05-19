//
// 4435_ping128.cpp
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
typedef pair<int, int> PII;
typedef pair<int, PII> PII2;

#define MAXN 130

/*
 * Idea: Use Dijkstra algorithm to find the shortest path from (0, 0) to (N - 1, N - 1).
 */

int min_dist[MAXN][MAXN];
int N;
int cost[MAXN][MAXN];
bool seen[MAXN][MAXN];

int cx[] = {0, 0, 1, -1}, cy[] = {1, -1, 0, 0};

int main() {
    int problem = 0;
    while (scanf("%d", &N) == 1) {
        if (N == 0) break;
        problem++;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                scanf("%d", &cost[i][j]);
                min_dist[i][j] = 1000000000;
                seen[i][j] = false;
            }
        }
        min_dist[0][0] = cost[0][0];
        priority_queue<PII2, vector<PII2>, greater<PII2> > Q;
        Q.push(PII2(cost[0][0], PII(0, 0)));
        while (!Q.empty()) {
            PII2 now = Q.top();
            Q.pop();
            int ii = now.second.first;
            int jj = now.second.second;
            int dd = now.first;
            if (seen[ii][jj]) continue;
            if (ii == N - 1 && jj == N - 1) {
                printf("Problem %d: %d\n", problem, dd);
                break;
            }
            seen[ii][jj] = true;
            for (int i = 0; i < 4; i++) {
                int iii = ii + cx[i];
                int jjj = jj + cy[i];
                if (iii >= 0 && jjj >= 0 && iii < N && jjj < N && min_dist[iii][jjj] > dd + cost[iii][jjj]) {
                    min_dist[iii][jjj] = dd + cost[iii][jjj];
                    Q.push(PII2(min_dist[iii][jjj], PII(iii, jjj)));
                }
            }
        }
    }
    return 0;
}
