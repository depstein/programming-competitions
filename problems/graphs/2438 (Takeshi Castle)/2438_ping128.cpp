//
// 2438_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=onlinejudge&page=show_problem&problem=439
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
typedef pair<PII, int> PII2;

/*
 * Idea: 1. Use Binary search to find the minimum jump distance X each member needs to have in order to pass the challenge
 *       2. If the majority has the maximum jump distance < X, the challenge is valid
 */

#define MAXN 30005
#define MAXM 55

int N, M;
PII discs[MAXM];
int jump_distances[MAXN];
bool can_reach[MAXN];

int dist2(int a, int b) {
    int dx = discs[a].first - discs[b].first;
    int dy = discs[a].second - discs[b].second;
    return dx * dx + dy * dy;
}

void dfs(int at, int limit) {
    can_reach[at] = true;
    for (int i = 0; i < M; i++) {
        if (can_reach[i] || dist2(at, i) > limit * limit) continue;
        dfs(i, limit);
    }
}

// Return true, if it's possible to go from the start disc to the destination disc with all the jumps' distance <= limit
bool good(int limit) {
    memset(can_reach, 0, sizeof(can_reach));
    dfs(0, limit);
    return can_reach[1];
}

int main() {
    while (scanf("%d %d", &N, &M) == 2) {
        if (N == -1 && M == -1) break;
        for (int i = 0; i < N; i++) scanf("%d", &jump_distances[i]);
        for (int i = 0; i < M; i++) {
            scanf("%d %d", &discs[i].first, &discs[i].second);
        }
        
        int left = 0, right = 10000;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (good(mid)) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        int minimum_jump_distance = left;
        int cnt_pass = 0;
        for (int i = 0; i < N; i++) {
            if (jump_distances[i] >= minimum_jump_distance) {
                cnt_pass++;
            }
        }
        if (cnt_pass < N - cnt_pass) {
            printf("valid\n");
        } else {
            printf("NOT valid\n");
        }
    }
    return 0;
}
