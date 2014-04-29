//
// 4131_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2132
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

double in[MAXN][MAXN];
int N;
string currencies[MAXN];

void solve() {
    string from, to;
    scanf("%d", &N);
    for (int i = 0; i < N; i++) cin >> currencies[i];
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            cin >> in[i][j];
        }
    }
    cin >> from >> to;
    for (int k = 0; k < N; k++) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (in[i][j] < in[i][k] * in[k][j]) {
                    in[i][j] = in[i][k] * in[k][j];
                }
            }
        }
    }

    for (int i = 0; i < N; i++) {
        if (in[i][i] > 1.000000000) {
            printf("Break the bank!\n");
            return ;
        }
    }

    int a, b;
    for (int i = 0; i < N; i++) {
        if (from == currencies[i]) a = i;
        if (to == currencies[i]) b = i;
    }

    printf("%.4lf\n", in[a][b]);
}

int main() {
    int test;
    scanf("%d", &test);
    for (int tt = 0; tt < test; tt++) {
        solve();
    }    
    return 0;
}
