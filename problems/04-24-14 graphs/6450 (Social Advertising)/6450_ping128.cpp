//
// 6450_ping128.cpp
//
// Siwakorn Srisakaokul - ping128
// Written on Thursday,  8 May 2014.
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

#define MAXN 21

int N;
vector<int> adj[MAXN];
int sz[MAXN];

/*
 * Use Dynamic Programming to use this though (it's easier for me to think of it in this way)
 *
 * dp[i] = the minimum number of posted ads so that 'bitmask' i can see
 *
 * ex if i == 5, N = 7 then i represents 1010000 (user 0 and user 2 can see the ads)
 *
 * so the answer is dp[127] (dp[(1111111)_2] - binary representation of 127)
 */
int dp[1<<MAXN];

void solve() {
    cin >> N;
    int u;
    for (int i = 0; i < N; i++) {
        adj[i].clear();
        scanf("%d", &sz[i]);
        for (int j = 0; j < sz[i]; j++) {
            scanf("%d", &u);
            u--;
            adj[i].push_back(u);
        }
    }

    for (int i = 0; i < 1<<N; i++) {
        dp[i] = 1000000000;
    }
    dp[0] = 0;

    for (int i = 0; i < 1<<N; i++) {
        if (dp[i] != 1000000000) {
            for (int j = 0; j < N; j++) { // post ads at user j
                int next_state = i | (1<<j); // now j can see the ads
                for (int k = 0; k < sz[j]; k++) {
                    int v = adj[j][k];
                    next_state |= (1<<v); // all friends of j can also see the ads from j's wall
                }
                dp[next_state] = min(dp[next_state], dp[i] + 1);
            }
        }
    }
    printf("%d\n", dp[(1<<N) - 1]);
}

int main() {
    int test;
    cin >> test;
    for (int tt = 0; tt < test; tt++) {
        solve();
    }

    return 0;
}
