//
// 4280.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2281
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

#define MAXN 260

int N;

int main() {
    int test = 0;
    while (cin >> N) {
        if (N == 0) break;
        test++;
        printf("Case %d: %d pencils for %d cents\n", test, N, N);
        bool found = false;
        for (int i = 1; i <= N; i++) {  // loop on #pencils for '4 cents each'
            for (int j = 1; j <= N - i; j++) { // loop on #pencils for 'two for a penny'
                int k = N - i - j; // the total number of pencils is N
                if (k <= 0) continue;

                double cost = 4 * i + 0.5 * j + 0.25 * k;
                if (cost <= N + 0.00000001 && cost >= N - 0.00000001) {
                    printf("%d at four cents each\n%d at two for a penny\n%d at four for a penny\n\n", i, j, k);
                    found = true;
                }
            }
        }
        if (!found) printf("No solution found.\n\n");
    }


    return 0;
}
