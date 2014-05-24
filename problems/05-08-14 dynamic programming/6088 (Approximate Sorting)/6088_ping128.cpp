//
// 6088_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=onlinejudge&Itemid=8&page=show_problem&problem=4099
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

#define MAXN 18
#define INF 1000000000

int N;
char my_less[MAXN][MAXN + 5];

// dp[i] = the minimum number of inversion of a permutation that used number j
// if (1<<j) & i > 0
//
// ex i = 5 -> 101 (binary) -> we're using only number 0 and 2 to form the permutation
// 
int dp[(1<<MAXN) + 5];

// the previous state
int pre[(1<<MAXN) + 5];

void print(int cur_state) {
    for (int i = 0; i < N; i++) {
        if (1<<i == cur_state) {
            printf("%d", i);
            return ;
        }
    }

    int pre_state = pre[cur_state];
    for (int i = 0; i < N; i++) {
        if (((1<<i) & cur_state) && !((1<<i) & pre_state)) {
            printf("%d ", i);
            print(pre_state);
            return ;
        }
    }
    assert(false); // can't reach here
}

int main() {
    while (cin >> N) {
        if (N == 0) break;
        for (int i = 0; i < N; i++) scanf("%s", my_less[i]);
        
        for (int i = 0; i < 1<<N; i++)
            dp[i] = INF;

        // not using any number -> inversion = 0
        dp[0] = 0;

        for (int i = 0; i < 1<<N; i++) {
            // try to use a higher number first to achieve lexicographically smallest goal
            for (int j = N - 1; j >= 0; j--) {
                if (((1<<j) & i) == 0) {
                    // pick the next unused number
                    // and add it to the front
                    // calculate the number of inversion with this number
                    int inverse = 0;
                    for (int k = 0; k < N; k++) {
                        if (((1<<k) & i) && my_less[k][j] == '1') {
                            inverse++;
                        }
                    }
                    if (dp[i | (1<<j)] >= dp[i] + inverse) {
                        dp[i | (1<<j)] = dp[i] + inverse;
                        pre[i | (1<<j)] = i;
                    }
                }
            }
        }
        print((1<<N) - 1);
        printf("\n%d\n", dp[(1<<N) - 1]);
    }

    return 0;
}
