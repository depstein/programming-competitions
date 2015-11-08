// maze.cpp
// The Maze Makers, MCPC 2014, Problem F
// C++ solution by Michael Goldwasser

#include <iostream>
#include <fstream>
#include <vector>
using namespace std;

#define MIN_W 2
#define MIN_H 1
#define MAX_W 50
#define MAX_H 50
#define MAX_AREA MAX_W * MAX_H

// actual runtime parameters
int H,W;
int grid[MAX_H][MAX_W];

// underlying graph nodes are integers computed as (row * W + col)
vector<int> neigh[MAX_AREA];   // neigh[k] is vector of k's neighbors
int exits[2];                  // represent start and end locations

int discovery[MAX_AREA];  // dfs discovery times ala CLRS
int finish[MAX_AREA];     // dfs finish times ala CLRS
int timestamp;

void reset() {
    timestamp = 0;
    for (int k=0; k < W*H; k++) {
        discovery[k] = finish[k] = -1;
    }
}

void dfs(int u) {
    discovery[u] = timestamp++;
    for (int j=0; j < neigh[u].size(); ++j) {
        int v = neigh[u][j];
        if (discovery[v] == -1)
            dfs(v);
    }
    finish[u] = timestamp++;
}

// record exit at (r,c), unless we've already seen two
void recordExit(int &numTerm, int r, int c) {
    numTerm++;
    if (numTerm <= 2)
        exits[numTerm-1] = r*W + c;
}

int main(int argv, char** argc) {
    ifstream fin((argv == 1) ? "maze.in" : argc[1]);

    while (true) {
        fin >> H >> W;
        if (W == 0) break;
        if (W < MIN_W || H < MIN_H)
            cerr << "ERROR: maze too small" << endl;
        if (W > MAX_W || H > MAX_H)
            cerr << "ERROR: maze too large" << endl;

        for (int r=0; r < H; r++) {
            string temp;
            fin >> temp;
            if (temp.size() != W)
                cerr << "ERROR: maze width does not match W" << endl;

            for (int c=0; c < W; c++) {
                if (temp[c] >= '0' && temp[c] <= '9')
                    grid[r][c] = temp[c] - '0';
                else if (temp[c] >= 'A' && temp[c] <= 'F')
                    grid[r][c] = 10 + temp[c] - 'A';
                else
                    cerr << "ERROR: Illegal digit " << grid[r][c] << endl;
            }
        }

        // check for consistency
        bool consistent = true;
        for (int r=0; r < H; r++) {
            for (int c=0; c < W; c++) {
                if (r > 0)
                    if (((grid[r][c] & 8) == 0) != ((grid[r-1][c] & 2) == 0))
                        consistent = false;
                if (c > 0)
                    if (((grid[r][c] & 1) == 0) != ((grid[r][c-1] & 4) == 0))
                        consistent = false;
            }
        }
        if (!consistent) {
            cerr << "ERROR: Inconsistent hex encoding" << endl;
            continue;
        }

        // look for precisely two terminals
        int numTerm = 0;
        for (int r=0; r < H; r++) {
            if ((grid[r][0] & 1) == 0)
                recordExit(numTerm,r,0);
            if ((grid[r][W-1] & 4) == 0)
                recordExit(numTerm,r,W-1);
        }
        for (int c=0; c < W; c++) {
            if ((grid[0][c] & 8) == 0)
                recordExit(numTerm,0,c);
            if ((grid[H-1][c] & 2) == 0)
                recordExit(numTerm,H-1,c);
        }
        if (numTerm != 2 || exits[0] == exits[1]) {
            cerr << "ERROR: invalid terminals" << endl;
            continue;
        }

        // next let's build up an explicit graph of neighbors
        for (int k=0; k < W*H; k++)
            neigh[k].clear();         // reset graph

        for (int r=0; r < H; r++) {
            for (int c=0; c < W; c++) {
                int k = r*W + c;
                if (r > 0 && ((grid[r][c] & 8) == 0))
                    neigh[k].push_back( (r-1)*W + c);
                if (c < W-1 && ((grid[r][c] & 4) == 0))
                    neigh[k].push_back( r*W + c+1 );
                if (r < H-1 && ((grid[r][c] & 2) == 0))
                    neigh[k].push_back( (r+1)*W + c);
                if (c > 0 && ((grid[r][c] & 1) == 0))
                    neigh[k].push_back( r*W + c-1 );
            }
        }

        // compute DFS on graph starting at exits[0]
        reset();
        dfs(exits[0]);

        if (discovery[exits[1]] == -1) {
            cout << "NO SOLUTION" << endl;
            continue;
        }

        bool connected = true;
        for (int k=0; k < W*H; k++)
            if (discovery[k] == -1) {
                connected = false;
                break;
            }
        if (!connected) {
            cout << "UNREACHABLE CELL" << endl;
            continue;
        }

        // given that graphs is now known to be connected,
        // we simply need to know if its a tree, and thus has
        // n-1 edges for n=H*W
        int numEdges = 0;
        for (int k=0; k < W*H; k++)
            numEdges += neigh[k].size();
        numEdges /= 2;                   // double-counted each edge
        if (numEdges != W*H - 1) {
            cout << "MULTIPLE PATHS" << endl;
            continue;
        }

        cout << "MAZE OK" << endl;
    }
}
