//
// 4794_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2795
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

#define MAXN 16
#define MAXR 102
/*
 * dp[i][j] = 1 if we can break up the chocolate of size i x c for 'j bitmask'
 *              (c can be easily computed because it depends on i, j, x, y)
 *          = 0 if we can't
 *          = -1 if not computed yet
 */
int dp[MAXR][1<<MAXN];

int N;
int pieces[MAXN];
int R, C;
int sum[1<<MAXN]; // sum[i] = total number of pieces for this 'i bitmask'
vector<int> all_mask_for_sum[MAXR * MAXR]; // store all possible bitmasks that sum to i

bool f(int rr, int cc, int mask) {
    
    if (rr * cc != sum[mask]) return false;

    // only one part left
    if (__builtin_popcount(mask) == 1) return true;
    
    if (dp[rr][mask] != -1) return dp[rr][mask];

    // try cutting vertically
    for (int i = 0; i < cc - 1; i++) {
        int left_cc = i + 1;
        int right_cc = cc - left_cc;
        int sz = all_mask_for_sum[rr * left_cc].size(); // try all possible subsets that sum to rr * left_cc
        for (int j = 0; j < sz; j++) {
            int mask_left = all_mask_for_sum[rr * left_cc][j];
            if ((mask_left & mask) == mask_left) { // check if this mask_left is a subset of mask
                int mask_right = mask_left ^ mask; // now set mask_right to be the rest of mask
                if (!f(rr, left_cc, mask_left))
                    continue;
                if (!f(rr, right_cc, mask_right))
                    continue;
                // if we can satisfy both left and right parts, return true
                return dp[rr][mask] = true;
            }
        } 
    }

    // try cutting horizontally - similar to the previous case
    for (int i = 0; i < rr - 1; i++) {
        int left_rr = i + 1;
        int right_rr = rr - left_rr;
        int sz = all_mask_for_sum[cc * left_rr].size();
        for (int j = 0; j < sz; j++) {
            int mask_left = all_mask_for_sum[cc * left_rr][j];
            if ((mask_left & mask) == mask_left) {
                int mask_right = mask_left ^ mask;
                if (!f(left_rr, cc, mask_left))
                    continue;
                if (!f(right_rr, cc, mask_right))
                    continue;
                return dp[rr][mask] = true;
            }
        } 
    }

    return dp[rr][mask] = false;

}

int main() {
    int test = 0;    
    while (scanf("%d", &N) == 1) {
        if (N == 0) break;
        test++;
        scanf("%d %d", &R, &C);
        for (int i = 0; i < N; i++) scanf("%d", &pieces[i]);
        
        for (int i = 0; i < MAXR; i++) {
            for (int j = 0; j < 1<<MAXN; j++) {
                dp[i][j] = -1;
            }
        }

        for (int i = 0; i < 1<<N; i++) {
            sum[i] = 0;
            for (int j = 0; j < N; j++) {
                if (i & (1<<j)) {
                    sum[i] += pieces[j];
                }
            }
        }

        for (int i = 0; i < MAXR * MAXR; i++) {
            all_mask_for_sum[i].clear();
        }

        for (int i = 0; i < 1<<N; i++) {
            all_mask_for_sum[sum[i]].push_back(i);
        }

        printf("Case %d: %s\n", test, f(R, C, (1<<N) - 1) ? "Yes" : "No");
    }
    return 0;
}
