//
// 5945_ping128.cpp
//
// https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3956
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

#define MAXL 85
#define MAXN 505
int L;
vector<string> words;

// dp[i][j] = the minimum total raggedness after putting word 1-i and the last line has j spaces left
vector<int> dp[MAXN], pre[MAXN];

void print(int cur_word, int space_left) {
    if (cur_word == 1) {
        cout << words[cur_word - 1];
    } else {
        int prev = pre[cur_word][space_left];
        print(cur_word - 1, abs(prev) - 2000000);
        if (prev < 0)
            printf("\n");
        else
            printf(" ");
        cout << words[cur_word - 1];
    }
}

int main() {
    while (cin >> L) {
        if (L == 0) break;
        string s;
        getline(cin, s);

        words.clear();
        while(1) {
            getline(cin, s);
            if (s.empty()) break;
            istringstream iss(s);
            string token;
            while (iss >> token) {
                words.push_back(token);
            }
        }

        int N = words.size();
        

        for (int i = 0; i < MAXN; i++) {
            dp[i] = vector<int> (MAXL);
            pre[i] = vector<int> (MAXL);
        }

        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < MAXL; j++) {
                dp[i][j] = 1000000000; // dp[i][j] is not computed yet
            }
        }

        // base case: put the first word on the first line
        int len = words[0].size();
        dp[1][L - len] = (L - len) * (L - len);
        for (int i = 1; i < N; i++) {
            for (int j = 0; j < L; j++) {
                if (dp[i][j] != 1000000000) {
                    // case 1: put the next word on the same line
                    len = words[i].size();
                    int needed_space = len + 1;

                    if (needed_space <= j) {
                        // enough space
                        if (dp[i + 1][j - needed_space] > dp[i][j] - j * j + (j - needed_space) * (j - needed_space)) {
                            dp[i + 1][j - needed_space] = dp[i][j] - j * j + (j - needed_space) * (j - needed_space);
                            pre[i + 1][j - needed_space] = j + 2000000;
                        }
                    }

                    // case 2: put the next word on the next line
                    needed_space = len;
                    if (dp[i + 1][L - needed_space] > dp[i][j] + (L - needed_space) * (L - needed_space)) {
                        dp[i + 1][L - needed_space] = dp[i][j] + (L - needed_space) * (L - needed_space);
                        pre[i + 1][L - needed_space] = - (j + 2000000); // sign to indicate that this step contains '\n'
                    }
                }
            }
        }


        int minn = 1000000000;
        int space_left;
        for (int j = 0; j <= L; j++) {
            if (minn > dp[N][j] - j * j) { // the last line's raggedness is ignored
                minn = dp[N][j] - j * j;
                space_left = j;
            }
        }
        print(N, space_left);
        cout << endl << "===" << endl;
    }
    return 0;
}
