// wet.cpp
// Wet Cell, MCPC 2014, Problem I
// C++ version by Michael Goldwasser

#include <iostream>
#include <fstream>
#include <cmath>
#include <set>
using namespace std;

#define MAX_X 1000
#define MAX_Y 1000
#define MAX_T 200000
#define MAX_L 100
#define MAX_W 100

struct Cell {
    int x,y;
    bool operator<(const Cell& other) const {
        return x < other.x || (x == other.x && y < other.y);
    }
};

int main(int argv, char** argc) {
    ifstream fin((argv == 1) ? "wet.in" : argc[1]);

    while (true) {
        int X,Y,T,L,W;
        fin >> X;
        if (X == -1) break;
        fin >> Y >> T >> L >> W;
        if (X < 1 || X > MAX_X) cerr << "ERROR: BAD X" << endl;
        if (Y < 1 || Y > MAX_Y) cerr << "ERROR: BAD Y" << endl;
        if (T < 1 || T > MAX_T) cerr << "ERROR: BAD T" << endl;
        if (L < 1 || L > MAX_L) cerr << "ERROR: BAD L" << endl;
        if (W < 0 || W > MAX_W) cerr << "ERROR: BAD W" << endl;

        set<Cell> walls;
        set<Cell> wet;
        set<Cell> fringe;
        for (int j=0; j < L; j++) {
            Cell leak;
            fin >> leak.x >> leak.y;
            if (leak.x < 1 || leak.x > X) cerr << "ERROR: BAD LEAK x" << endl;
            if (leak.y < 1 || leak.y > Y) cerr << "ERROR: BAD LEAK y" << endl;
            if (!wet.insert(leak).second)
                cerr << "ERROR: DUPLICATE LEAK " << leak.x << "," << leak.y << endl;
            fringe.insert(leak);
        }

        for (int j=0; j < W; j++) {
            int x1,y1,x2,y2;
            fin >> x1 >> y1 >> x2 >> y2;
            if (x1 < 1 || x1 > X) cerr << "ERROR: BAD WALL ENDPOINT" << endl;
            if (x2 < 1 || x2 > X) cerr << "ERROR: BAD WALL ENDPOINT" << endl;
            if (y1 < 1 || y1 > Y) cerr << "ERROR: BAD WALL ENDPOINT" << endl;
            if (y2 < 1 || y2 > Y) cerr << "ERROR: BAD WALL ENDPOINT" << endl;
            int dx = x2-x1;
            int dy = y2-y1;
            if (dx != 0 && dy != 0 && abs(dx) != abs(dy))
                cerr << "ERROR: INVALID WALL SLOPE" << endl;
            if (dx != 0) dx = dx / abs(dx);
            if (dy != 0) dy = dy / abs(dy);
            Cell wall = {x1, y1};
            walls.insert(wall);
            while (wall.x != x2 || wall.y != y2) {
                wall.x += dx;
                wall.y += dy;
                walls.insert(wall);
            }
        }

        for (set<Cell>::const_iterator it = fringe.begin(); it != fringe.end(); ++it) {
            if (walls.count(*it) > 0)
                cerr << "ERROR: LEAK ABOVE A WALL" << endl;
        }

        for (int step=1; step < T; step++) {  // note we start at T=1
            set<Cell> newest;
            for (set<Cell>::const_iterator it = fringe.begin();
                 it != fringe.end(); ++it) {
                Cell neighbor[4] = {
                    { it->x + 1, it->y },
                    { it->x - 1, it->y },
                    { it->x, it->y + 1 },
                    { it->x, it->y - 1 },
                };
                for (int d=0; d < 4; d++) {
                    if (neighbor[d].x > 0 && neighbor[d].x <= X &&
                        neighbor[d].y > 0 && neighbor[d].y <= Y &&
                        walls.count(neighbor[d]) == 0 &&
                        wet.count(neighbor[d]) == 0) {
                        wet.insert(neighbor[d]);
                        newest.insert(neighbor[d]);
                    }
                }
            }
            fringe.swap(newest);
            if (fringe.empty()) break;  // no more water to spread
        }
        cout << wet.size() << endl;
    }
}
