/**
 *    Name:  worst-case-scenario.cpp
 * Purpose:  Solves Jeremy's worst-case-scenario problem
 *
 *  Author:  Andre Wiggins
 *    Date:  April 21, 2013
 */

#include <iostream>
#include <iomanip>
#include <string>
#include <map>
using namespace std;

#define DEBUG if (0)
#define X -1
#define A 0
#define B 1
#define C 2
#define D 3

int** stagemap;

void dump_stagemap(int w, int h) {
    DEBUG cout << " ";
    DEBUG for (int wi = 0; wi < w; wi++) {cout << wi;}
    DEBUG cout << endl;

    for (int hi = 0; hi < h; hi++) {
        DEBUG cout << hi;
        for (int wi = 0; wi < w; wi++) {
            int i = stagemap[wi][hi];
            char c;
            if (i == X)
                c = 'X';
            else if (i == A)
                c = 'A';
            else if (i == B)
                c = 'B';
            else if (i == C)
                c = 'C';
            else if (i == D)
                c = 'D';
            cout << c;
        }
        cout << endl;
    }
}

void deallocate_stagemap(int w, int h) {
    for (int wi = 0; wi < w; wi++) {
        delete[] stagemap[wi];
    }
    delete[] stagemap;
}

void allocate_stagemap(int w, int h) {
    stagemap = new int*[w];
    for (int wi = 0; wi < w; wi++) {
        stagemap[wi] = new int[h];
    }
}

void read_stagemap(int w, int h) {
    string line;
    for (int hi = 0; hi < h; hi++) {
        cin >> line;
        for (int wi = 0; wi < w; wi++) {
            int c;

            if (line[wi] == 'A')
                c = A;
            else if (line[wi] == 'B')
                c = B;
            else if (line[wi] == 'C')
                c = C;
            else if (line[wi] == 'D')
                c = D;
            else if (line[wi] == 'X')
                c = X;
            else
                cout << "oh shit" << endl;

            stagemap[wi][hi] = c;
        }
    }
}

map<int, bool> visited;
int neighbors[4][2] = {{1,0}, {0,1}, {-1,0}, {0,-1}};

void add_infection(int x, int y, int w, int h) {
    if (x < 0 || x >= w || y < 0 || y >= h) {
        return;
    } else if (stagemap[x][y] == X || visited[x*w +y]) {
        return;
    } else if (stagemap[x][y] > X && stagemap[x][y] < D) {
        stagemap[x][y] = stagemap[x][y] + 1;
    } else if (stagemap[x][y] == D) {
        visited[x*w + y] = true;
        for (int i = 0; i < 4; i++) {
            int next_x = x + neighbors[i][0];
            int next_y = y + neighbors[i][1];
            add_infection(next_x, next_y, w, h);
        }
    }
}

int main() {
    int n, w, h, i, x, y;
    cin >> n;
    for (int ni = 0; ni < n; ni++) {
        cin >> w >> h;

        allocate_stagemap(w, h);
        read_stagemap(w, h);
        DEBUG dump_stagemap(w, h);

        cin >> i;
        DEBUG cout << "i: " << i << endl;
        for (int ii = 0; ii < i; ii++) {
            cin >> x >> y;
            DEBUG cout << "(" << x << ", " << y << ")" << endl;

            visited.clear();
            add_infection(x, y, w, h);

            DEBUG dump_stagemap(w, h);
            DEBUG cout << endl;
        }

        dump_stagemap(w, h);

        deallocate_stagemap(w, h);
        visited.clear();
        DEBUG cout << endl;
    }
}
