//
// 2728_ping128.cpp
//
// Siwakorn Srisakaokul - ping128
// Written on Monday, 28 April 2014.
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

#define MAXN 55
#define MAXT 205

/*
 * dp[i][j] = the minimum waiting time before reaching j-th station at i-minute mark
 */
int dp[MAXT][MAXN];

int N, T;
int dist[MAXN]; // dist[i] = distance between i and i+1 station
bool f_trains_at[MAXT][MAXN]; // [i][j] = true if there's a forward train at j-th station and at i-minute mark
bool b_trains_at[MAXT][MAXN]; // [i][j] = true if there's a backward train at j-th station and at i-minute mark

int main() {
    int test = 0;
    while (scanf("%d", &N) == 1) {
        if (N == 0) break;
        scanf("%d", &T);
        for (int i = 0; i < N - 1; i++) {
            scanf("%d", &dist[i]);
        }

        for (int i = 0; i < MAXT; i++) {
            for (int j = 0; j < MAXN; j++) {
                dp[i][j] = 1000000000;
                f_trains_at[i][j] = false;
                b_trains_at[i][j] = false;
            }
        }
        int num_train, cur_time;
        scanf("%d", &num_train);
        for (int i = 0; i < num_train; i++) {
            scanf("%d", &cur_time);
            int cur_station = 0;
            for (int j = 0; j < N && cur_time <= T; j++) {
                f_trains_at[cur_time][cur_station] = true;
                cur_time += dist[cur_station];
                cur_station++;
            }
        }

        scanf("%d", &num_train);
        for (int i = 0; i < num_train; i++) {
            scanf("%d", &cur_time);
            int cur_station = N - 1;
            for (int j = 0; j < N && cur_time <= T; j++) {
                b_trains_at[cur_time][cur_station] = true;
                cur_time += dist[cur_station - 1];
                cur_station--;
            }
        }

        dp[0][0] = 0;
        for (int tt = 0; tt < T; tt++) {
            for (int cur_station = 0; cur_station < N; cur_station++) {
                if (dp[tt][cur_station] != 1000000000) {
                    for (int waiting_time = 0; waiting_time + tt <= T; waiting_time++) {
                        cur_time = tt + waiting_time;
                        dp[cur_time][cur_station] = min(dp[cur_time][cur_station],
                                                        dp[tt][cur_station] + waiting_time);
                        if (f_trains_at[cur_time][cur_station]) {
                            // take a forward train
                            int next_time = cur_time + dist[cur_station];
                            int next_station = cur_station + 1;
                            if (next_time <= T)
                                dp[next_time][next_station] = min(dp[next_time][next_station],
                                                                  dp[tt][cur_station] + waiting_time);
                        }
                        if (b_trains_at[cur_time][cur_station]) {
                            // take a backward train
                            int next_time = cur_time + dist[cur_station - 1];
                            int next_station = cur_station - 1;
                            if (next_time <= T)
                                dp[next_time][next_station] = min(dp[next_time][next_station],
                                                                  dp[tt][cur_station] + waiting_time);
                        }
                    }
                }
            }
        }

        test++;
        printf("Case Number %d: ", test);
        if (dp[T][N - 1] == 1000000000) printf("impossible\n");
        else printf("%d\n", dp[T][N - 1]);
    }
    return 0;
}
