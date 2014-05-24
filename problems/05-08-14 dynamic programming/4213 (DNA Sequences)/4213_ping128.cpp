//
// 4213_ping128.cpp
//
// https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2214
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

#define MAXK 105
#define MAXN 1005

/*
 * dp[i][j] = max length of the longest subsequece between s1[0..i - 1], s2[0...j-1]
 * with the extra condition (each segment has length at least K)
 */
int dp[MAXN][MAXN];
int K;
char s1[MAXN];
char s2[MAXN];

int main() {
    
    while (cin >> K) {
        if (K == 0) break;

        // reset states
        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < MAXN; j++) {
                dp[i][j] = 0;
            }
        }

        scanf("%s", s1);
        scanf("%s", s2);
        int len1 = strlen(s1);
        int len2 = strlen(s2);

        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                dp[i + 1][j + 1] = max(dp[i][j + 1], dp[i + 1][j]);
                for (int k = 0; i >= k && j >= k; k++) {
                    if (s1[i - k] != s2[j - k]) {
                        break;
                    } else if (k + 1 >= K) { // only update when we have at least K matching chars
                        dp[i + 1][j + 1] = max(dp[i + 1][j + 1], dp[i - k][j - k] + k + 1);
                    }
                }
            }
        }
        printf("%d\n", dp[len1][len2]);
    }

    return 0;
}
