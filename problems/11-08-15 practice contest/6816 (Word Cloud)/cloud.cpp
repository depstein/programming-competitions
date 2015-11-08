// cloud.cpp
// Word Cloud, MCPC 2014, Problem E
// C++ version by Michael Goldwasser

#include <iostream>
#include <fstream>
using namespace std;

#define PAD 10           // interspace padding
#define MIN_C 5
#define MAX_C 1000
#define MAX_W 5000
#define MAX_N 1000

bool DEBUG = false;

struct item {
    int wordlength, count, width, height;
};

item box[MAX_N];

int ceil(int num, int denom) {
    return (num/denom + (num % denom > 0 ? 1 : 0));
}

int main(int argv, char** argc) {
    ifstream fin((argv == 1) ? "cloud.in" : argc[1]);
    DEBUG = (argv > 2);

    int trial=0;
    while (true) {
        int W,N;
        fin >> W >> N;
        if (W == 0) break;

        if (W > MAX_W) cerr << "ILLEGAL W" << endl;
        if (N > MAX_N) cerr << "ILLEGAL N" << endl;

        int maxC = 0;
        string prev = "";
        for (int j=0; j<N; j++) {
            string S;
            int C;
            fin >> S >> C;
            if (C < MIN_C || C > MAX_C) cerr << "ILLEGAL C" << endl;
            if (S < prev) cerr << "WARNING: NOT ALPHABETIZED" << endl;
            prev = S;  // for style only
            box[j].wordlength = S.size();
            box[j].count = C;
            maxC = max(maxC, C);
        }

        for (int j=0; j<N; j++) {
            box[j].height = 8 + ceil(40*(box[j].count-4), maxC-4);
            box[j].width = ceil(9*box[j].wordlength*box[j].height, 16);
            if (box[j].width > W) cerr << "WORD TOO LONG" << endl;
            if (DEBUG) cout << box[j].height << " x " << box[j].width << endl;
        }

        int total = 0;
        int lineWidth = box[0].width;
        int lineHeight = box[0].height;
        for (int j=1; j<N; j++) {
            if (lineWidth + PAD + box[j].width <= W) {
                lineWidth += PAD + box[j].width;
                lineHeight = max(lineHeight, box[j].height);
            } else {
                total += lineHeight;
                lineHeight = box[j].height;
                lineWidth = box[j].width;
            }
        }
        total += lineHeight;  // don't forget the last line

        cout << "CLOUD " << ++trial << ": " << total << endl;
    }
}
