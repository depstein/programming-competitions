// shrine.cpp
// Shrine Maintenance, MCPC 2014, Problem H
// C++ version by Michael Goldwasser

#include <iostream>
#include <fstream>
#include <iomanip>
#include <cmath>
#include <set>
using namespace std;

#define MAX_N 8600
#define MAX_D 6
#define EPS 0.005

int N,D,W,S;
set<int> shrines;
double dist[MAX_N]; // dist[j] is distance from "shrines[j]" to "shrines[(j+1)%N]"

void calculateDistances() {
    int prev=0;
    int j=0;
    for (set<int>::iterator it=shrines.begin(); j < S; ++j, ++it) {
        int sep = *it - prev;
        prev = *it;
        dist[(j-1+S)%S] = 2000*sin(M_PI*sep/N);  // using chord formula
    }
}

bool feasible(double cost) {
    cost -= 2000;   // let's ignore the mandatory radii

    for (int start=0; start < S; start++) {
        // assume first worker starts at 'start'
        int workers=1;
        double subtotal = 0.0;
        for (int k=0; k < S-1; k++) {
            if (subtotal + dist[(start+k)%S] <= cost) {
                subtotal += dist[(start+k)%S];  // worker covers (start+1+k)
            } else {
                subtotal = 0;
                workers++;  // worker will start by taking (start+1+k)
                if (workers > W) break;
            }
        }
        if (workers <= W)
            return true;
    }

    return false;
}


int main(int argv, char** argc) {
    cout << fixed << setprecision(1);
    ifstream fin((argv == 1) ? "shrine.in" : argc[1]);
    while (true) {
        shrines.clear();
        fin >> W;
        if (W == 0) break;
        fin >> N >> D;
        if (N > MAX_N) cerr << "INVALID N" << endl;
        if (D > MAX_D) cerr << "INVALID D" << endl;
        for (int j=0; j<D; j++) {
            int k;
            fin >> k;
            if (N % k != 0) cerr << "INVALID DIVISOR " << k << endl;
            for (int t=k; t <= N; t += k)
                shrines.insert(t);
        }
        S = shrines.size();
        calculateDistances();
        cerr << "S: " << S << " W: " << W << endl;

        double lower = 2000.0;
        double upper = 2000.0 + 2*M_PI*2000.0; // if one workers does all
        while (upper-lower > EPS) {
            //            cout << "(" << lower << "," << upper << ")" << endl;
            double mid = (upper+lower)/2;
            if (feasible(mid)) {
                upper = mid;
            } else {
                lower = mid;
            }
        }

        cout << lower << endl;

        // may need to turn on higher EPS to do true tolerance checking
        if (int((lower+0.005)*10+0.5) != int((lower-0.005)*10+0.5)) {
            cerr << "WARNING: low tolerance: " << fixed << setprecision(4) << lower << endl;
        }
    }
}
