//
// 4123_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=onlinejudge&page=show_problem&problem=2124
// 
// Siwakorn Srisakaokul - ping128
// Written on Tuesday, 29 April 2014.
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

/*
 * Idea:
 * A valid string s[0...N-1] needs to have these properties:
 *    1. if s[i] == 'O', then s[(i + 1) % N] == 'R'
 *    2. #R = #O + 4
 */


/*
 * dp[i][j][k] = the number of strings of length i that contains k R's more than O's
 *   j = 00 -> begins with R and ends with R
 *   j = 01 -> begins with R and ends with O
 *   j = 10 -> begins with O and ends with R
 *   j = 11 -> begins with O and ends with O (not valid for this problem)
 *
 * So the answer to the problem is dp[N][0][4] + dp[N][1][4] + dp[N][2][4].
 *
 */
LL dp[MAXN][4][5];

int main() {

    // set up base case
    // start with N = 2
    dp[2][0][2] = 1; // "RR"
    dp[2][1][0] = 1; // "RO" (good, because O is followed by R(the first R))
    dp[2][2][0] = 1; // "OR"
    // "OO" is invalid

    // bottom-up DP
    for (int i = 2; i < MAXN - 2; i++) {
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k <= 4; k++) {
                if (dp[i][j][k]) {
                    if (j == 0) { // R...R
                        if (k)
                            dp[i + 1][1][k - 1] += dp[i][j][k]; // R...R(O)
                        if (k <= 3)
                            dp[i + 1][0][k + 1] += dp[i][j][k]; // R...R(R)
                        if (k == 4)
                            dp[i + 2][1][k] += dp[i][j][k]; // R...R(RO) works too
                    } else if (j == 1) { // R...O
                        if (k <= 3)
                            dp[i + 1][0][k + 1] += dp[i][j][k]; // R...O(R)
                        if (k == 4)
                            dp[i + 2][1][k] += dp[i][j][k]; // R...O(RO)
                    } else if (j == 2) { // O...R
                        dp[i + 2][2][k] += dp[i][j][k]; // O...R(OR)
                        if (k <= 3)
                            dp[i + 1][2][k + 1] += dp[i][j][k]; // O...R(R)
                    } else { // O...O
                        // invalid
                    }
                }
            }
        }
    }

    int N;
    int test = 0;
    while (scanf("%d", &N) == 1) {
        if (N == 0) break;
        test++;
        printf("Case %d: ", test);
        cout << dp[N][0][4] + dp[N][1][4] + dp[N][2][4] << endl;
    }

    return 0;
}
