//
// 4153_ping128.cpp
//
// Link: https://icpcarchive.ecs.baylor.edu/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2154
//
// Siwakorn Srisakaokul - ping128
// Written on Sunday, 18 May 2014.
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
typedef pair<double, int> PII;
typedef pair<PII, int> PII2;

#define MAXN 105

double D, T;
int N;
double latitude[MAXN], longitude[MAXN];
double dist[MAXN][MAXN];

bool connected[MAXN];

void latitude_longitude_to_xyz(double R, double lat, double lon, double &x, double &y, double &z) {
    x = -R * cos(lat) * cos(lon);
    y = R * sin(lat);
    z = R * cos(lat) * sin(lon);
}

double cal_dist(int ii, int jj) {
    /*
      This formula works as well
    double R = D / 2;
    double phi1 = latitude[ii] / 180.0 * M_PI;
    double phi2 = latitude[jj] / 180.0 * M_PI;
    double delta = (latitude[jj] - latitude[ii]) / 180.0 * M_PI;
    double delta2 = (longitude[jj] - longitude[ii]) / 180.0 * M_PI;
    double a = sin(delta / 2) * sin(delta / 2) +
        cos(phi1) * cos(phi2) *
        sin(delta2 / 2) * sin(delta2 / 2);
    double c = 2 * atan2(sqrt(a), sqrt(1 - a));
    return R * c;
    */

    double R = D / 2;
    double xi, yi, zi;
    latitude_longitude_to_xyz(R, latitude[ii] * M_PI / 180.0, longitude[ii] * M_PI / 180.0, xi, yi, zi);

    double xj, yj, zj;
    latitude_longitude_to_xyz(R, latitude[jj] * M_PI / 180.0, longitude[jj] * M_PI / 180.0, xj, yj, zj);

    double dot = xi * xj + yi * yj + zi * zj;
    double dist_i = sqrt (xi * xi + yi * yi + zi * zi);
    double dist_j = sqrt (xj * xj + yj * yj + zj * zj);
    double angle = acos(dot / dist_i / dist_j);
    return min(R * angle, R * (2 * M_PI - angle));

}

void solve() {
    cin >> D >> T;
    cin >> N;
    for (int i = 0; i < N; i++) {
        cin >> latitude[i] >> longitude[i];
    }
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            dist[i][j] = cal_dist(i, j);
        }
    }
    fill(connected, connected + MAXN, 0);
    connected[0] = true;
    priority_queue<PII, vector<PII>, greater<PII> >Q;
    for (int i = 0; i < N; i++) {
        Q.push(PII(dist[0][i], i));
    }
    
    // Prim's algorithm
    double ans = 0.0;
    while (!Q.empty()) {
        PII now = Q.top();
        Q.pop();
        if (connected[now.second]) continue;
        connected[now.second] = true;
        ans += now.first;
        for (int i = 0; i < N; i++) {
            if (!connected[i]) {
                Q.push(PII(dist[now.second][i], i));
            }
        }
    }

    if (ans <= T) cout << "IS POSSIBLE" << endl;
    else cout << "IS NOT POSSIBLE" << endl;
}


int main() {
    int test;
    scanf("%d", &test);
    for (int tt = 0; tt < test; tt++) {
        solve();
    }
    
    return 0;
}
