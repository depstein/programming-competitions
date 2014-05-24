//
// 5738_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3749
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

#define MAXN 1005

// shortest_dist[i][j][k] = the shortest distance from the start position to (i, j) and facing k-direction
int shortest_dist[MAXN][MAXN][4];

// num_paths[i][j][k] = the number of shortest paths from the start position to (i, j) and facing k-direction
int num_paths[MAXN][MAXN][4];

// map
char in[MAXN][MAXN];

int R, C, MOD;

int cx[] = {-1, 0, 1, 0}, cy[] = {0, 1, 0, -1}; // N E S W directions

void update(int ni, int nj, int nd, int ci, int cj, int cd, queue<PII2> &Q) {
    if (ni < 0 || ni >= R) return ;
    if (nj < 0 || nj >= C) return ;
    if (in[ni][nj] == '*') return ;
    if (shortest_dist[ni][nj][nd] > shortest_dist[ci][cj][cd] + 1) {
        shortest_dist[ni][nj][nd] = shortest_dist[ci][cj][cd] + 1;
        num_paths[ni][nj][nd] = num_paths[ci][cj][cd];
        Q.push(PII2(PII(ni, nj), nd));
    } else if (shortest_dist[ni][nj][nd] == shortest_dist[ci][cj][cd] + 1) {
        num_paths[ni][nj][nd] += num_paths[ci][cj][cd];
        num_paths[ni][nj][nd] %= MOD;
    }
}

int main() {
    
    map<char, int> direction_map;
    direction_map['N'] = 0;
    direction_map['E'] = 1;
    direction_map['S'] = 2;
    direction_map['W'] = 3;

    int test = 0;
    char start_direction;
    while (scanf("%d %d %d", &R, &C, &MOD) == 3) {
        if (MOD == 0) break;
        test++;
        int si, sj, fi, fj;
        for(int i = 0; i < R; i++) scanf("%s", in[i]);
        scanf("%d %d %d %d %c", &si, &sj, &fi, &fj, &start_direction);
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                for (int k = 0; k < 4; k++) {
                    shortest_dist[i][j][k] = 1000000000;
                    num_paths[i][j][k] = 0;
                }
            }
        }

        int di = direction_map[start_direction];
        num_paths[si][sj][di] = 1;
        shortest_dist[si][sj][di] = 0;
        // use BFS
        queue<PII2> Q;
        Q.push(PII2(PII(si, sj), di));
        int limit = 1000000000;
        while (!Q.empty()) {
            PII2 now = Q.front();
            Q.pop();

            int ci, cj, cd; // current i, j, direction
            ci = now.first.first;
            cj = now.first.second;
            cd = now.second;
            // once we reach the destination, we got the shortest distance
            // so when the current dis in the queue is greater than it, we can just exit the while loop
            if (shortest_dist[ci][cj][cd] > limit) break;  
            if (ci == fi && cj == fj) limit = min(limit, shortest_dist[ci][cj][cd]);
            int ni, nj, nd; // next i, j, direction
            // move forward
            ni = ci + cx[cd];
            nj = cj + cy[cd];
            nd = cd;
            update(ni, nj, nd, ci, cj, cd, Q);
            // turn right
            ni = ci;
            nj = cj;
            nd = (cd + 1) % 4;
            update(ni, nj, nd, ci, cj, cd, Q);
            // turn left
            ni = ci;
            nj = cj;
            nd = (cd + 3) % 4;
            update(ni, nj, nd, ci, cj, cd, Q);
        }

        int shortest_program = 1000000000;
        for (int i = 0; i < 4; i++) {
            shortest_program = min(shortest_program, shortest_dist[fi][fj][i]);
        }
        int total_num_path = 0;
        if (shortest_program == 1000000000) {
            // no path to this position
            total_num_path = -1;
        } else {
            for (int i = 0; i < 4; i++) {
                if (shortest_dist[fi][fj][i] == shortest_program) {
                    total_num_path += num_paths[fi][fj][i];
                    total_num_path %= MOD;
                }
            }
        }

        printf("Case %d: %d %d\n", test, MOD, total_num_path);
    }

    return 0;
}
