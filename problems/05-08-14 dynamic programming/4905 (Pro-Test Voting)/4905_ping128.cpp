//
// 4905_ping128.cpp
//
// https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2906
//
// Siwakorn Srisakaokul - ping128
// Written on Sunday, 27 April 2014.
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

#define MAXN 105
#define MAXM 105

/*
 * dp[i][j] = the maximum number of votes Bob can get by considering precinct 1 to i and have j dollars left
 */
int dp[MAXM][MAXN]; 
int N, M;
int in[MAXM][3];
int pre[MAXM][MAXN];

void print(int i, int money_left) {
    if (i == 1) {
        printf("%d:%d\n", M - i, N - money_left);
    } else {
        int prev = pre[i][money_left];
        printf("%d:%d ", M - i, prev - money_left);
        print(i - 1, prev);

    }
}

int main() {
    int test = 0;
    while (scanf("%d %d", &N, &M) == 2) {
        test++;
        if (N + M == 0) break;

        for (int i = 0; i < M; i++)
            scanf("%d %d %d", &in[i][0], &in[i][1], &in[i][2]);

        for (int i = 0; i < MAXM; i++) {
            for (int j = 0; j < MAXN; j++) {
                dp[i][j] = -1000000000;
            }
        }

        dp[0][N] = 0; // have N dollars left, get zero votes
        for (int i = 0; i < M; i++) {
            for (int j = 0; j <= N; j++) {
                if (dp[i][j] >= 0) {
                    for (int spend = 0; spend <= j; spend++) {
                        double population = in[M - i - 1][0]; // do it from the back to satify the output format
                        double i_p = in[M - i - 1][1];
                        double max_increase = in[M - i - 1][2];
                        double f = min(100.0, i_p + max_increase * spend / (spend + 10.1));
                        double temp2 = population * f / 100;
                        int temp = round(temp2 + 0.000000001); // round
                        if (dp[i + 1][j - spend] <= dp[i][j] + temp) {
                            dp[i + 1][j - spend] = dp[i][j] + temp;
                            pre[i + 1][j - spend] = j;
                        }
                    }
                }
            }
        }

        int maxx = -1;
        int money_left = 0;
        for (int i = 0; i <= N; i++) {
            if (maxx < dp[M][i]) {
                maxx = dp[M][i];
                money_left = i;
            }
        }

        printf("Case %d: %d\n", test, maxx);
        print(M, money_left);
    }
        
    return 0;
}
