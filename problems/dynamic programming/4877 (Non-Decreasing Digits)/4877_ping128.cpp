//
// 4877_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2878
// 
// Siwakorn Srisakaokul - ping128
// Written on Thursday, 24 April 2014.
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

#define MAXN 65

/* 
 * dp[i][j] = the number of i-digit values which end with digit j
 * dp[i][j] = dp[i - 1][0] + dp[i - 1][1] + ... + dp[i - 1][j] * 
 * So if N = 10, the answer will be dp[10][0] + dp[10][1] + ... + dp[10][9]
 *
 */
LL dp[MAXN][10];

void solve() {
    int test_id, N;
    cin >> test_id >> N;
    LL ans = 0;
    for (int i = 0; i < 10; i++) ans += dp[N][i];
    cout << test_id << " " << ans << endl;
}

int main() {

    for (int i = 0; i < 10; i++) dp[1][i] = 1;
    for (int i = 1; i < MAXN - 1; i++) {
        for (int j = 0; j < 10; j++) {
            for (int k = j; k < 10; k++) {
                dp[i + 1][k] += dp[i][j];
            }
        }
    }

    int test;
    cin >> test;
    for (int tt = 0; tt < test; tt++) {
        solve();
    }
    return 0;
}
